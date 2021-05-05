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
import static com.example.quizapp.UserProfileActivity.SURVEY_IDs;
import static com.example.quizapp.UserProfileActivity.TEST_IDs;

public class Creator_Survey_GradeMCQuestionsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question;
    private TextView q_count;

    private TextView option1_percentage,option2_percentage,option3_percentage,option4_percentage;




    private Button option1, option2, option3, option4, GoNext, GoPrev;

    private ArrayList<Survey_MCQuestion> questionList = new ArrayList<>();


    private int current_question;

    private int info = 0;

    private FirebaseFirestore firestore;

    private Dialog loading;

    private int count;

    public static ArrayList<String> survey_grader_MC_IDs = new ArrayList<>();

    public static int grader_NumOfWorksheet;

    public static int grader_NumOfSurvey;


    private double option1_count, option2_count, option3_count, option4_count;

    private double option1_p,option2_p,option3_p,option4_p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__survey__grade_m_c_questions);

        Intent intent = getIntent();

        int temp = intent.getIntExtra("NAME",-1);

        if(temp != -1){
           grader_NumOfSurvey = temp;
        }

        info = intent.getIntExtra("info",0);


        question = findViewById(R.id.question);

        q_count = findViewById(R.id.question_num);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        option1_percentage = findViewById(R.id.option1_percentage);
        option2_percentage = findViewById(R.id.option2_percentage);
        option3_percentage = findViewById(R.id.option3_percentage);
        option4_percentage = findViewById(R.id.option4_percentage);

        GoNext = findViewById(R.id.GoNext);
        GoPrev = findViewById(R.id.GoPrev);


        GoNext.setOnClickListener(this);
        GoPrev.setOnClickListener(this);




        firestore = FirebaseFirestore.getInstance();

        loading = new Dialog(Creator_Survey_GradeMCQuestionsActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();

        getQuestionList();
    }

    private void getQuestionList(){
//        questionList.clear();


        survey_grader_MC_IDs.clear();


        firestore.collection("surveys").document(SURVEY_IDs.get(grader_NumOfSurvey)).collection("MCQuestions")
                .document("questionList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        count = Integer.valueOf(doc.getString("count"));

                        for(int i = 1; i <= count; i++){
                            survey_grader_MC_IDs.add(doc.getString("question"+String.valueOf(i)+"_id"));
                        }

                        if(count == 0){
                            //if there is no MC questions, go to FR questions activity.
                            Intent intent = new Intent(Creator_Survey_GradeMCQuestionsActivity.this, Creator_Survey_GradeFRQuestionsActivity.class);
                            intent.putExtra("info",1);
                            startActivity(intent);
                        }
                        else {
                            setQuestionList();

                        }

                    }

                }
            }
        });




    }



    private void setQuestionList(){
        firestore.collection("surveys").document(SURVEY_IDs.get(grader_NumOfSurvey)).collection("MCQuestions")
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

                    QueryDocumentSnapshot quesDoc = docList.get(survey_grader_MC_IDs.get(i));

                    questionList.add(new Survey_MCQuestion(quesDoc.getString("question"),
                            quesDoc.getString("option1"),
                            quesDoc.getString("option2"),
                            quesDoc.getString("option3"),
                            quesDoc.getString("option4")));
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
        else if (info == 2){
            current_question = questionList.size()-1;
            setQuestion(current_question);

        }
    }

    private void setQuestion(int i){
        question.setText(questionList.get(i).getQuestion());

        option1.setText(questionList.get(i).getOption1());
        option2.setText(questionList.get(i).getOption2());
        option3.setText(questionList.get(i).getOption3());
        option4.setText(questionList.get(i).getOption4());



        q_count.setText(String.valueOf(i + 1) + "/" + String.valueOf(questionList.size()));

        option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));


        firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(grader_NumOfSurvey)).collection("MCQuestions")
                .document("question" + String.valueOf(current_question + 1)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                option1_count = Integer.valueOf(documentSnapshot.getString("option1_count"));
                option2_count = Integer.valueOf(documentSnapshot.getString("option2_count"));
                option3_count = Integer.valueOf(documentSnapshot.getString("option3_count"));
                option4_count = Integer.valueOf(documentSnapshot.getString("option4_count"));

                double sum = option1_count + option2_count + option3_count + option4_count;

                option1_p = option1_count/sum;
                option2_p = option2_count/sum;
                option3_p = option3_count/sum;
                option4_p = option4_count/sum;


                option1_percentage.setText(String.format("1 percentage: %.2f"  ,option1_p));
                option2_percentage.setText(String.format("2 percentage: %.2f"  ,option2_p));
                option3_percentage.setText(String.format("3 percentage: %.2f"  ,option3_p));
                option4_percentage.setText(String.format("4 percentage: %.2f"  ,option4_p));



            }
        });








        loading.cancel();


    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.GoNext){

            changeQuestionForward();
        }
        else if (v.getId() == R.id.GoPrev){

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
            Intent intent = new Intent(Creator_Survey_GradeMCQuestionsActivity.this, Creator_Survey_GradeFRQuestionsActivity.class);
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