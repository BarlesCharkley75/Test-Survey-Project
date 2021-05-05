package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static com.example.quizapp.Creator_GradeMCQuestionsActivity.grader_NumOfWorksheet;
import static com.example.quizapp.Creator_Survey_GradeMCQuestionsActivity.grader_NumOfSurvey;
import static com.example.quizapp.Test_WorksheetListActivity.grader_NumOfTest;
import static com.example.quizapp.UserProfileActivity.SURVEY_IDs;
import static com.example.quizapp.UserProfileActivity.TEST_IDs;

public class Creator_Survey_GradeFRQuestionsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question;
    private TextView q_count;


    private GridView answer_grid;

    private ArrayList<Survey_FRQuestion> questionList = new ArrayList<>();


    private ArrayList<String> answers = new ArrayList<>();

    public static int survey_grade_fr_current_question;

    private Button GoNext,GoPrev;

    private int info = 0;

    private FirebaseFirestore firestore;

    public static ArrayList<String> survey_grader_FR_IDs = new ArrayList<>();

    private Dialog loading;

    private int count;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__survey__grade_f_r_questions);


        Intent intent = getIntent();
        info = intent.getIntExtra("info",1);

        question = findViewById(R.id.question);

        q_count = findViewById(R.id.question_num);

        answer_grid = findViewById(R.id.grade_survey_fr_gridview);

        GoNext = findViewById(R.id.GoNext);
        GoPrev = findViewById(R.id.GoPrev);

        GoNext.setOnClickListener(this);
        GoPrev.setOnClickListener(this);

        firestore = FirebaseFirestore.getInstance();

        loading = new Dialog(Creator_Survey_GradeFRQuestionsActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();



        getQuestionList();
    }

    private void getQuestionList(){

//        questionList.clear();

        survey_grader_FR_IDs.clear();

        firestore.collection("surveys").document(SURVEY_IDs.get(grader_NumOfSurvey)).collection("FRQuestions")
                .document("questionList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        count = Integer.valueOf(doc.getString("count"));


                        for(int i = 1; i <= count; i++){
                            survey_grader_FR_IDs.add(doc.getString("question"+String.valueOf(i)+"_id"));
                        }

                        if (count == 0){
                            //if there is no FR questions, go to matching questions.
                            Intent intent = new Intent (Creator_Survey_GradeFRQuestionsActivity.this, Creator_Survey_GradeMatchingQuestionsActivity.class);
                            intent.putExtra("info",1);
                            startActivity(intent);
                        }
                        else{
                            setQuestionList();

                        }



                    }


                }
            }
        });


    }



    private void setQuestionList(){
        firestore.collection("surveys").document(SURVEY_IDs.get(grader_NumOfSurvey)).collection("FRQuestions")
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

                    QueryDocumentSnapshot quesDoc = docList.get(survey_grader_FR_IDs.get(i));

                    questionList.add(new Survey_FRQuestion(quesDoc.getString("question"),
                            Integer.valueOf(quesDoc.getString("WordLimit"))));
                }



                pass();
            }
        });
    }

    private void pass(){
        if(info == 1){
            survey_grade_fr_current_question = 0;
            setQuestion(survey_grade_fr_current_question);
        }
        else if(info == 2){
            survey_grade_fr_current_question = questionList.size() - 1;
            setQuestion(survey_grade_fr_current_question);
        }
    }

    private void setQuestion(int i){
        int limit = questionList.get(i).getWord_limit();


        if(limit > 0){
            question.setText(questionList.get(i).getQuestion() + " (Length limit: " + String.valueOf(limit)+")");
        }
        else{
            question.setText(questionList.get(i).getQuestion() + " (No length limit)");
        }

        q_count.setText(String.valueOf(i + 1) + "/" + String.valueOf(questionList.size()));



        firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(grader_NumOfSurvey)).collection("FRQuestions")
                .document("question"+String.valueOf(survey_grade_fr_current_question + 1)).collection("userAnswers")
                .document("answerList").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int answer_count = Integer.valueOf(documentSnapshot.getString("count"));

                for (int i = 0; i< answer_count;i++){
                    answers.add("answer " + String.valueOf(i + 1));
                }

                Survey_FRAnswerAdapter adapter = new Survey_FRAnswerAdapter(answers);
                answer_grid.setAdapter(adapter);

            }
        });



        loading.cancel();

    }



    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.GoNext){
//            questionList.get(current_question).setUser_answer(user_input.getText().toString());

            changeQuestionForward();
        }


        if(v.getId() == R.id.GoPrev){
//            questionList.get(current_question).setUser_answer(user_input.getText().toString());

            changeQuestionBackward();
        }
    }


    private void changeQuestionForward(){
        if(survey_grade_fr_current_question < questionList.size() - 1){



            survey_grade_fr_current_question ++;
            setQuestion(survey_grade_fr_current_question);

        }

        else{


//            question.setText(String.valueOf(MC_points_get.get(0))+" "+String.valueOf(MC_points_get.get(1))+ " "
//            + String.valueOf(MC_points_total));

//             go to free response activity
            Intent intent = new Intent(Creator_Survey_GradeFRQuestionsActivity.this, Creator_Survey_GradeMatchingQuestionsActivity.class);
            intent.putExtra("info",1);
            startActivity(intent);
        }
    }

    private void changeQuestionBackward(){

        if(survey_grade_fr_current_question == 0){
            Intent intent = new Intent(Creator_Survey_GradeFRQuestionsActivity.this, Creator_Survey_GradeMCQuestionsActivity.class);
            intent.putExtra("info",2);
            intent.putExtra("NAME", grader_NumOfSurvey);

            startActivity(intent);
        }

        else{
            survey_grade_fr_current_question --;

            setQuestion(survey_grade_fr_current_question);
        }
    }
}