package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


import static com.example.quizapp.Creator_GradeMCQuestionsActivity.grader_NumOfWorksheet;
import static com.example.quizapp.MCQuestionActivity2.NumOfTest;
import static com.example.quizapp.Test_WorksheetListActivity.grader_NumOfTest;
import static com.example.quizapp.UserProfileActivity.TEST_IDs;

public class Creator_GradeFRQuestionsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question;
    private TextView q_count;

    private TextView user_answer;
    private TextView points_total;
    private TextInputLayout points_get;


    private ArrayList<FRQuestion> questionList = new ArrayList<>();

    private ArrayList<String> AnswerList = new ArrayList<>();

    private int current_question;

    private Button GoNext,GoPrev;

    private int info = 0;

    private FirebaseFirestore firestore;

    public static ArrayList<String> grader_FR_IDs = new ArrayList<>();

    private Dialog loading;

    private int count;

    public static int FR_points_total;

    public static ArrayList<Integer> FR_points_get = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__grade_f_r_questions);


        Intent intent = getIntent();
        info = intent.getIntExtra("info",0);

        question = findViewById(R.id.question);

        q_count = findViewById(R.id.question_num);

        user_answer = findViewById(R.id.user_answer);

        points_total = findViewById(R.id.points_total);

        points_get = findViewById(R.id.points_get);

        GoNext = findViewById(R.id.GoNext);
        GoPrev = findViewById(R.id.GoPrev);

        GoNext.setOnClickListener(this);
        GoPrev.setOnClickListener(this);

        firestore = FirebaseFirestore.getInstance();

        loading = new Dialog(Creator_GradeFRQuestionsActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();



        getQuestionList();



    }

    private void getQuestionList(){

//        questionList.clear();

        grader_FR_IDs.clear();

        firestore.collection("tests").document(TEST_IDs.get(grader_NumOfTest)).collection("FRQuestions")
                .document("questionList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        count = Integer.valueOf(doc.getString("count"));


                        for(int i = 1; i <= count; i++){
                            grader_FR_IDs.add(doc.getString("question"+String.valueOf(i)+"_id"));
                        }

                        if (count == 0){
                            //if there is no FR questions, go to matching questions.
                            Intent intent = new Intent (Creator_GradeFRQuestionsActivity.this, Creator_GradeMatchingQuestionsActivity.class);
                            intent.putExtra("info",1);
                            startActivity(intent);
                        }
                        else{
                            getAnswerList();

                        }



                    }


                }
            }
        });


    }
    private void getAnswerList(){
        firestore.collection("TestWorksheet").document(TEST_IDs.get(grader_NumOfTest)).collection("userWorksheets")
                .document("worksheet" + String.valueOf(grader_NumOfWorksheet + 1)).collection("FRQuestions")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    docList.put(doc.getId(),doc);
                }

                for(int i = 0; i < count; i ++){
                    QueryDocumentSnapshot quesDoc = docList.get("question" + String.valueOf(i + 1));

                    AnswerList.add(quesDoc.getString("User_input"));
                }
                setQuestionList();
            }
        });


    }

    private void setQuestionList(){
        firestore.collection("tests").document(TEST_IDs.get(grader_NumOfTest)).collection("FRQuestions")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    docList.put(doc.getId(),doc);
                }
//                QueryDocumentSnapshot quesListDoc = docList.get("questionList");
//                String count = quesListDoc.getString("count");

                for(int i = 0 ; i< count; i++){
//                    String quesName = "question" + String.valueOf(i);

                    QueryDocumentSnapshot quesDoc = docList.get(grader_FR_IDs.get(i));

                    questionList.add(new FRQuestion(quesDoc.getString("question"),
                            Integer.valueOf(quesDoc.getString("WordLimit")),
                            Integer.valueOf(quesDoc.getString("Points"))));
                }



                pass();
            }
        });
    }

    private void pass(){
        if(info == 1){
            current_question = 0;
            setQuestion(current_question);
        }
        else if(info == 2){
            current_question = questionList.size() - 1;
            setQuestion(current_question);
        }
    }

    private void setQuestion(int i){
        int limit = questionList.get(i).getWord_limit();


        if(limit > 0){
            question.setText(questionList.get(i).getQuestion() + " (Length limit: " + String.valueOf(limit)+")"
                    +"\n"+" \n"+"(points: " + questionList.get(i).getPoints() + ")");
        }
        else{
            question.setText(questionList.get(i).getQuestion() + " (No length limit)"
                    +"\n"+" \n"+"(points: " + questionList.get(i).getPoints() + ")");
        }

        q_count.setText(String.valueOf(i + 1) + "/" + String.valueOf(questionList.size()));


        points_total.setText("/" + String.valueOf(questionList.get(current_question).getPoints()));





        FR_points_get.add(-1);

        if(FR_points_get.get(current_question) != -1){
            points_get.getEditText().setText(String.valueOf(FR_points_get.get(current_question)));

        }
        else{
            points_get.getEditText().getText().clear();
        }

        user_answer.setText(AnswerList.get(current_question));

        FR_points_total = 0;

        for(int j = 0; j<questionList.size();j++){
            FR_points_total += questionList.get(j).getPoints();
        }

        loading.cancel();

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.GoNext){
//            questionList.get(current_question).setUser_answer(user_input.getText().toString());
            if(points_get.getEditText().getText().toString().equals("")){
                FR_points_get.set(current_question,0);
            }
            else{
                FR_points_get.set(current_question,Integer.valueOf(points_get.getEditText().getText().toString()));
            }
            changeQuestionForward();
        }


        if(v.getId() == R.id.GoPrev){
//            questionList.get(current_question).setUser_answer(user_input.getText().toString());
            if(points_get.getEditText().getText().toString().equals("")){
                FR_points_get.set(current_question,0);
            }
            else{
                FR_points_get.set(current_question,Integer.valueOf(points_get.getEditText().getText().toString()));
            }
            changeQuestionBackward();
        }

    }

    private void changeQuestionForward(){
        if(current_question < questionList.size() - 1){



            current_question ++;
            setQuestion(current_question);

        }

        else{


//            question.setText(String.valueOf(MC_points_get.get(0))+" "+String.valueOf(MC_points_get.get(1))+ " "
//            + String.valueOf(MC_points_total));

//             go to free response activity
            Intent intent = new Intent(Creator_GradeFRQuestionsActivity.this, Creator_GradeMatchingQuestionsActivity.class);
            intent.putExtra("info",1);
            startActivity(intent);
        }
    }

    private void changeQuestionBackward(){

        if(current_question == 0){
            Intent intent = new Intent(Creator_GradeFRQuestionsActivity.this, Creator_GradeMCQuestionsActivity.class);
            intent.putExtra("info",2);
            intent.putExtra("WORKSHEET_NAME", grader_NumOfWorksheet);

            startActivity(intent);
        }

        else{
            current_question --;

            setQuestion(current_question);
        }
    }
}