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

import static com.example.quizapp.Creator_GradeMCQuestionsActivity.grader_NumOfWorksheet;
import static com.example.quizapp.Creator_Survey_GradeMCQuestionsActivity.grader_NumOfSurvey;
import static com.example.quizapp.Test_WorksheetListActivity.grader_NumOfTest;
import static com.example.quizapp.UserProfileActivity.SURVEY_IDs;
import static com.example.quizapp.UserProfileActivity.TEST_IDs;

public class Creator_Survey_GradeRankingQuestionsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question, q_count;

    private TextView input1,input2,input3,input4;


    private TextView o1_1_p, o1_2_p, o1_3_p, o1_4_p;
    private TextView o2_1_p, o2_2_p, o2_3_p, o2_4_p;
    private TextView o3_1_p, o3_2_p, o3_3_p, o3_4_p;
    private TextView o4_1_p, o4_2_p, o4_3_p, o4_4_p;



    private Button option1, option2, option3, option4, GoNext, GoPrev;
    private ArrayList<Survey_RankingQuestion> questionList = new ArrayList<>();



    private int current_question;
    private int info = 0;

    private FirebaseFirestore firestore;

    public static ArrayList<String> survey_grader_RANKING_IDs = new ArrayList<>();
    private Dialog loading;

    private int count;



    private double op1_1_count,op1_2_count,op1_3_count,op1_4_count;
    private double op2_1_count,op2_2_count,op2_3_count,op2_4_count;
    private double op3_1_count,op3_2_count,op3_3_count,op3_4_count;
    private double op4_1_count,op4_2_count,op4_3_count,op4_4_count;


    private double op1_1_percentage, op1_2_percentage,op1_3_percentage,op1_4_percentage;
    private double op2_1_percentage, op2_2_percentage,op2_3_percentage,op2_4_percentage;
    private double op3_1_percentage, op3_2_percentage,op3_3_percentage,op3_4_percentage;
    private double op4_1_percentage, op4_2_percentage,op4_3_percentage,op4_4_percentage;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__survey__grade_ranking_questions);

        Intent intent = getIntent();
        info = intent.getIntExtra("info",0);

        question = findViewById(R.id.question);
        q_count = findViewById(R.id.question_num);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        GoNext = findViewById(R.id.GoNext);
        GoPrev = findViewById(R.id.GoPrev);



        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        input3 = findViewById(R.id.input3);
        input4 = findViewById(R.id.input4);


        o1_1_p = findViewById(R.id.option1_1_percentage);
        o1_2_p = findViewById(R.id.option1_2_percentage);
        o1_3_p = findViewById(R.id.option1_3_percentage);
        o1_4_p = findViewById(R.id.option1_4_percentage);

        o2_1_p = findViewById(R.id.option2_1_percentage);
        o2_2_p = findViewById(R.id.option2_2_percentage);
        o2_3_p = findViewById(R.id.option2_3_percentage);
        o2_4_p = findViewById(R.id.option2_4_percentage);

        o3_1_p = findViewById(R.id.option3_1_percentage);
        o3_2_p = findViewById(R.id.option3_2_percentage);
        o3_3_p = findViewById(R.id.option3_3_percentage);
        o3_4_p = findViewById(R.id.option3_4_percentage);

        o4_1_p = findViewById(R.id.option4_1_percentage);
        o4_2_p = findViewById(R.id.option4_2_percentage);
        o4_3_p = findViewById(R.id.option4_3_percentage);
        o4_4_p = findViewById(R.id.option4_4_percentage);




        GoNext.setOnClickListener(this);
        GoPrev.setOnClickListener(this);

        firestore = FirebaseFirestore.getInstance();

        loading = new Dialog(Creator_Survey_GradeRankingQuestionsActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();





        getQuestionList();
    }


    private void getQuestionList(){

//        questionList.clear();

        survey_grader_RANKING_IDs.clear();

        firestore.collection("surveys").document(SURVEY_IDs.get(grader_NumOfSurvey)).collection("RankingQuestions")
                .document("questionList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        count = Integer.valueOf(doc.getString("count"));



                        for(int i = 1; i <= count; i++){
                            survey_grader_RANKING_IDs.add(doc.getString("question"+String.valueOf(i)+"_id"));
                        }

                        if(count == 0){
                            //no ranking question, go to submission page.

                            Intent intent = new Intent(Creator_Survey_GradeRankingQuestionsActivity.this, Survey_SubmissionActivity.class);
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
        firestore.collection("surveys").document(SURVEY_IDs.get(grader_NumOfSurvey)).collection("RankingQuestions")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    docList.put(doc.getId(),doc);
                }
//                QueryDocumentSnapshot quesListDoc = docList.get("questionList");
//                String count = quesListDoc.getString("count");

                for(int i = 0 ; i < count; i++){
//                    String quesName = "question" + String.valueOf(i);

                    QueryDocumentSnapshot quesDoc = docList.get(survey_grader_RANKING_IDs.get(i));

                    questionList.add(new Survey_RankingQuestion(
                            quesDoc.getString("question"),
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





        firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(grader_NumOfSurvey)).collection("RankingQuestions")
                .document("question" + String.valueOf(current_question + 1)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                op1_1_count = Integer.valueOf(documentSnapshot.getString("input1_1_count"));
                op1_2_count = Integer.valueOf(documentSnapshot.getString("input1_2_count"));
                op1_3_count = Integer.valueOf(documentSnapshot.getString("input1_3_count"));
                op1_4_count = Integer.valueOf(documentSnapshot.getString("input1_4_count"));

                op2_1_count = Integer.valueOf(documentSnapshot.getString("input2_1_count"));
                op2_2_count = Integer.valueOf(documentSnapshot.getString("input2_2_count"));
                op2_3_count = Integer.valueOf(documentSnapshot.getString("input2_3_count"));
                op2_4_count = Integer.valueOf(documentSnapshot.getString("input2_4_count"));

                op3_1_count = Integer.valueOf(documentSnapshot.getString("input3_1_count"));
                op3_2_count = Integer.valueOf(documentSnapshot.getString("input3_2_count"));
                op3_3_count = Integer.valueOf(documentSnapshot.getString("input3_3_count"));
                op3_4_count = Integer.valueOf(documentSnapshot.getString("input3_4_count"));

                op4_1_count = Integer.valueOf(documentSnapshot.getString("input4_1_count"));
                op4_2_count = Integer.valueOf(documentSnapshot.getString("input4_2_count"));
                op4_3_count = Integer.valueOf(documentSnapshot.getString("input4_3_count"));
                op4_4_count = Integer.valueOf(documentSnapshot.getString("input4_4_count"));


                double sum1 = op1_1_count + op1_2_count +op1_3_count +op1_4_count;
                double sum2 = op2_1_count + op2_2_count +op2_3_count +op2_4_count;
                double sum3 = op3_1_count + op3_2_count +op3_3_count +op3_4_count;
                double sum4 = op4_1_count + op4_2_count +op4_3_count +op4_4_count;


                op1_1_percentage = op1_1_count/sum1;
                op1_2_percentage = op1_2_count/sum1;
                op1_3_percentage = op1_3_count/sum1;
                op1_4_percentage = op1_4_count/sum1;

                op2_1_percentage = op2_1_count/sum2;
                op2_2_percentage = op2_2_count/sum2;
                op2_3_percentage = op2_3_count/sum2;
                op2_4_percentage = op2_4_count/sum2;

                op3_1_percentage = op3_1_count/sum3;
                op3_2_percentage = op3_2_count/sum3;
                op3_3_percentage = op3_3_count/sum3;
                op3_4_percentage = op3_4_count/sum3;

                op4_1_percentage = op4_1_count/sum4;
                op4_2_percentage = op4_2_count/sum4;
                op4_3_percentage = op4_3_count/sum4;
                op4_4_percentage = op4_4_count/sum4;


                o1_1_p.setText(String.format("1-1 percentage: %.2f"  ,op1_1_percentage));
                o1_2_p.setText(String.format("1-2 percentage: %.2f"  ,op1_2_percentage));
                o1_3_p.setText(String.format("1-3 percentage: %.2f"  ,op1_3_percentage));
                o1_4_p.setText(String.format("1-4 percentage: %.2f"  ,op1_4_percentage));

                o2_1_p.setText(String.format("2-1 percentage: %.2f"  ,op2_1_percentage));
                o2_2_p.setText(String.format("2-2 percentage: %.2f"  ,op2_2_percentage));
                o2_3_p.setText(String.format("2-3 percentage: %.2f"  ,op2_3_percentage));
                o2_4_p.setText(String.format("2-4 percentage: %.2f"  ,op2_4_percentage));

                o3_1_p.setText(String.format("3-1 percentage: %.2f"  ,op3_1_percentage));
                o3_2_p.setText(String.format("3-2 percentage: %.2f"  ,op3_2_percentage));
                o3_3_p.setText(String.format("3-3 percentage: %.2f"  ,op3_3_percentage));
                o3_4_p.setText(String.format("3-4 percentage: %.2f"  ,op3_4_percentage));

                o4_1_p.setText(String.format("4-1 percentage: %.2f"  ,op4_1_percentage));
                o4_2_p.setText(String.format("4-2 percentage: %.2f"  ,op4_2_percentage));
                o4_3_p.setText(String.format("4-3 percentage: %.2f"  ,op4_3_percentage));
                o4_4_p.setText(String.format("4-4 percentage: %.2f"  ,op4_4_percentage));
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
            Intent intent = new Intent(Creator_Survey_GradeRankingQuestionsActivity.this, Survey_SubmissionActivity.class);
            intent.putExtra("info",1);
            startActivity(intent);
        }
    }

    private void changeQuestionBackward(){

        if(current_question == 0){
            Intent intent = new Intent(Creator_Survey_GradeRankingQuestionsActivity.this, Creator_GradeMatchingQuestionsActivity.class);
            intent.putExtra("info",2);

            startActivity(intent);
        }

        else{
            current_question --;

            setQuestion(current_question);
        }
    }
}