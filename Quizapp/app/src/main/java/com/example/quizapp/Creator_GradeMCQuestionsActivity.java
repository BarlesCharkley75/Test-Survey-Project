package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import static com.example.quizapp.Test_WorksheetListActivity.grader_NumOfTest;
import static com.example.quizapp.Test_WorksheetListActivity.grader_worksheet_names;
import static com.example.quizapp.UserProfileActivity.Current_user_name;
import static com.example.quizapp.UserProfileActivity.TEST_IDs;

public class Creator_GradeMCQuestionsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question;
    private TextView q_count;

    private TextView correct_answer, points_total;

    private TextInputLayout points_get;


    private Button option1, option2, option3, option4, GoNext, GoPrev;

    private ArrayList<MCQuestion> questionList;

    private ArrayList<String> AnswerList;

    private int current_question;

    private int info = 0;

    private FirebaseFirestore firestore;

    private Dialog loading;

    private int count;

    public static ArrayList<String> grader_MC_IDs = new ArrayList<>();

    public static int grader_NumOfWorksheet;

    public static int MC_points_total;

    public static ArrayList<Integer> MC_points_get = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__grade_m_c_questions);



        Intent intent = getIntent();

        grader_NumOfWorksheet = intent.getIntExtra("WORKSHEET_NAME",-1);

        info = intent.getIntExtra("info",0);


        question = findViewById(R.id.question);

        q_count = findViewById(R.id.question_num);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        correct_answer = findViewById(R.id.correct_answer);
        points_total = findViewById(R.id.points_total);

        points_get = findViewById(R.id.points_get);

        GoNext = findViewById(R.id.GoNext);
        GoPrev = findViewById(R.id.GoPrev);


        GoNext.setOnClickListener(this);
        GoPrev.setOnClickListener(this);

        questionList = new ArrayList<>();
        AnswerList = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();

        loading = new Dialog(Creator_GradeMCQuestionsActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();





        getQuestionList();



    }

    private void getQuestionList(){
//        questionList.clear();


        grader_MC_IDs.clear();


        firestore.collection("tests").document(TEST_IDs.get(grader_NumOfTest)).collection("MCQuestions")
                .document("questionList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        count = Integer.valueOf(doc.getString("count"));

                        for(int i = 1; i <= count; i++){
                            grader_MC_IDs.add(doc.getString("question"+String.valueOf(i)+"_id"));
                        }

                        if(count == 0){
                            //if there is no MC questions, go to FR questions activity.
                            Intent intent = new Intent(Creator_GradeMCQuestionsActivity.this, Creator_GradeFRQuestionsActivity.class);
                            intent.putExtra("info",1);
                            startActivity(intent);
                        }
                        else {
                            getAnswerList();

                        }

                    }

                }
            }
        });




    }

    private void getAnswerList(){

        AnswerList.clear();

        firestore.collection("TestWorksheet").document(TEST_IDs.get(grader_NumOfTest)).collection("userWorksheets")
                .document("worksheet" + String.valueOf(grader_NumOfWorksheet + 1)).collection("MCQuestions")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Map<String, QueryDocumentSnapshot> docList2 = new ArrayMap<>();

                for(QueryDocumentSnapshot doc2 : queryDocumentSnapshots){
                    docList2.put(doc2.getId(),doc2);
                }

                for(int i = 0; i < count; i ++){
                    QueryDocumentSnapshot quesDoc2 = docList2.get("question"+String.valueOf(i + 1));
                    AnswerList.add(quesDoc2.getString("selected"));
                }

                setQuestionList();

            }
        });

    }



    private void setQuestionList(){
        firestore.collection("tests").document(TEST_IDs.get(grader_NumOfTest)).collection("MCQuestions")
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

                    QueryDocumentSnapshot quesDoc = docList.get(grader_MC_IDs.get(i));

                    questionList.add(new MCQuestion(quesDoc.getString("question"),
                            quesDoc.getString("option1"),
                            quesDoc.getString("option2"),
                            quesDoc.getString("option3"),
                            quesDoc.getString("option4"),
                            Integer.valueOf(quesDoc.getString("CorrectAnswer")),
                            Integer.valueOf(quesDoc.getString("Points"))));
                }

                pass();

            }
        });
    }

    //If info == 1, that means the question is passed from the front, so set the questions from the beginning.
    //If info == 2, that means the question is passed from the back (user clicked "GoPrev"), so set the questions from the end.

    private void pass(){
        if(info == 1){
            current_question = 0;
            setQuestion(current_question);
        }
        else if (info == 2){
            current_question = questionList.size()-1;
            setQuestion(current_question);

        }
    }

    private void setQuestion(int i){
        question.setText(questionList.get(i).getQuestion()+"\n"+" \n"+"(points: " + questionList.get(i).getPoints() + ")");

        option1.setText(questionList.get(i).getOption1());
        option2.setText(questionList.get(i).getOption2());
        option3.setText(questionList.get(i).getOption3());
        option4.setText(questionList.get(i).getOption4());


        correct_answer.setText("correct answer: " + String.valueOf(questionList.get(current_question).getCorrectAnswer()));

        points_total.setText("/" + String.valueOf(questionList.get(current_question).getPoints()));




        q_count.setText(String.valueOf(i + 1) + "/" + String.valueOf(questionList.size()));

        option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));

        MC_points_get.add(-1);

        if(MC_points_get.get(current_question) != -1){
            points_get.getEditText().setText(String.valueOf(MC_points_get.get(current_question)));

        }
        else{
            points_get.getEditText().getText().clear();
        }




        if(Integer.valueOf(AnswerList.get(current_question)) == 1){
            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
        }
        else if(Integer.valueOf(AnswerList.get(current_question)) == 2){
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
        }
        else if(Integer.valueOf(AnswerList.get(current_question)) == 3){
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
        }
        else if (Integer.valueOf(AnswerList.get(current_question)) == 4){
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
        }



        MC_points_total = 0;

        for(int j = 0; j<questionList.size();j++){
            MC_points_total += questionList.get(j).getPoints();
        }





        loading.cancel();


    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.GoNext){


            if(points_get.getEditText().getText().toString().equals("")){
                MC_points_get.set(current_question,0);
            }
            else{
                MC_points_get.set(current_question,Integer.valueOf(points_get.getEditText().getText().toString()));
            }


            changeQuestionForward();
        }
        else if (v.getId() == R.id.GoPrev){

            if(points_get.getEditText().getText().toString().equals("")){
                MC_points_get.set(current_question,0);
            }
            else{
                MC_points_get.set(current_question,Integer.valueOf(points_get.getEditText().getText().toString()));
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



            for (int i = questionList.size(); i < MC_points_get.size();i++){
                MC_points_get.set(i,0);
            }

//             go to free response activity
            Intent intent = new Intent(Creator_GradeMCQuestionsActivity.this, Creator_GradeFRQuestionsActivity.class);
            intent.putExtra("info",1);
            startActivity(intent);
        }
    }

    private void changeQuestionBackward(){
        if(current_question > 0){

            current_question --;

            setQuestion(current_question);

        }
    }

}