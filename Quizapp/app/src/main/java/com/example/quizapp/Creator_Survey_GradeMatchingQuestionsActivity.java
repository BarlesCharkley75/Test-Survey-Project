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

public class Creator_Survey_GradeMatchingQuestionsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question, q_count;



    private TextView L1_1_p, L1_2_p, L1_3_p, L1_4_p;
    private TextView L2_1_p, L2_2_p, L2_3_p, L2_4_p;
    private TextView L3_1_p, L3_2_p, L3_3_p, L3_4_p;
    private TextView L4_1_p, L4_2_p, L4_3_p, L4_4_p;




    private Button option1_L, option2_L, option3_L, option4_L;
    private Button option1_R, option2_R, option3_R, option4_R;
    private Button GoNext, GoPrev;

    private ArrayList<Survey_MatchingQuestion> questionList = new ArrayList<>();



    private int current_question;
    private int info = 0;




    private FirebaseFirestore firestore;

    public static ArrayList<String> survey_grader_MATCHING_IDs = new ArrayList<>();
    private Dialog loading;

    private int count;



    private double L1_1_count, L1_2_count,L1_3_count,L1_4_count;
    private double L2_1_count, L2_2_count,L2_3_count,L2_4_count;
    private double L3_1_count, L3_2_count,L3_3_count,L3_4_count;
    private double L4_1_count, L4_2_count,L4_3_count,L4_4_count;


    private double L1_1_percentage,L1_2_percentage,L1_3_percentage,L1_4_percentage;
    private double L2_1_percentage,L2_2_percentage,L2_3_percentage,L2_4_percentage;
    private double L3_1_percentage,L3_2_percentage,L3_3_percentage,L3_4_percentage;
    private double L4_1_percentage,L4_2_percentage,L4_3_percentage,L4_4_percentage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__survey__grade_matching_questions);


        Intent intent = getIntent();
        info = intent.getIntExtra("info",0);

        question = findViewById(R.id.question);
        q_count = findViewById(R.id.question_num);

        option1_L = findViewById(R.id.option_L1);
        option2_L = findViewById(R.id.option_L2);
        option3_L = findViewById(R.id.option_L3);
        option4_L = findViewById(R.id.option_L4);

        option1_R = findViewById(R.id.option_R1);
        option2_R = findViewById(R.id.option_R2);
        option3_R = findViewById(R.id.option_R3);
        option4_R = findViewById(R.id.option_R4);

        GoNext = findViewById(R.id.GoNext);
        GoPrev = findViewById(R.id.GoPrev);

        L1_1_p = findViewById(R.id.L1_1_percentage);
        L1_2_p = findViewById(R.id.L1_2_percentage);
        L1_3_p = findViewById(R.id.L1_3_percentage);
        L1_4_p = findViewById(R.id.L1_4_percentage);

        L2_1_p = findViewById(R.id.L2_1_percentage);
        L2_2_p = findViewById(R.id.L2_2_percentage);
        L2_3_p = findViewById(R.id.L2_3_percentage);
        L2_4_p = findViewById(R.id.L2_4_percentage);

        L3_1_p = findViewById(R.id.L3_1_percentage);
        L3_2_p = findViewById(R.id.L3_2_percentage);
        L3_3_p = findViewById(R.id.L3_3_percentage);
        L3_4_p = findViewById(R.id.L3_4_percentage);

        L4_1_p = findViewById(R.id.L4_1_percentage);
        L4_2_p = findViewById(R.id.L4_2_percentage);
        L4_3_p = findViewById(R.id.L4_3_percentage);
        L4_4_p = findViewById(R.id.L4_4_percentage);





        firestore = FirebaseFirestore.getInstance();


        GoNext.setOnClickListener(this);
        GoPrev.setOnClickListener(this);


        loading = new Dialog(Creator_Survey_GradeMatchingQuestionsActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();



        getQuestionList();
    }


    private void getQuestionList(){

//        questionList.clear();

        survey_grader_MATCHING_IDs.clear();

        firestore.collection("surveys").document(SURVEY_IDs.get(grader_NumOfSurvey)).collection("MatchingQuestions")
                .document("questionList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        count = Integer.valueOf(doc.getString("count"));



                        for(int i = 1; i <= count; i++){
                            survey_grader_MATCHING_IDs.add(doc.getString("question"+String.valueOf(i)+"_id"));
                        }

                        if(count == 0){
                            //no matching questions, go to ranking questions

                            Intent intent = new Intent (Creator_Survey_GradeMatchingQuestionsActivity.this, Creator_Survey_GradeRankingQuestionsActivity.class);
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
        firestore.collection("surveys").document(SURVEY_IDs.get(grader_NumOfSurvey)).collection("MatchingQuestions")
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

                    QueryDocumentSnapshot quesDoc = docList.get(survey_grader_MATCHING_IDs.get(i));

                    questionList.add(new Survey_MatchingQuestion(
                            quesDoc.getString("question"),
                            quesDoc.getString("optionL1"),
                            quesDoc.getString("optionL2"),
                            quesDoc.getString("optionL3"),
                            quesDoc.getString("optionL4"),
                            quesDoc.getString("optionR1"),
                            quesDoc.getString("optionR2"),
                            quesDoc.getString("optionR3"),
                            quesDoc.getString("optionR4")));
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
        q_count.setText(String.valueOf(i + 1) + "/" + String.valueOf(questionList.size()));

        option1_L.setText(questionList.get(i).getL1());
        option2_L.setText(questionList.get(i).getL2());
        option3_L.setText(questionList.get(i).getL3());
        option4_L.setText(questionList.get(i).getL4());

        option1_R.setText(questionList.get(i).getR1());
        option2_R.setText(questionList.get(i).getR2());
        option3_R.setText(questionList.get(i).getR3());
        option4_R.setText(questionList.get(i).getR4());

        option1_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option2_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option3_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option4_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));

        option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));




        firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(grader_NumOfSurvey)).collection("MatchingQuestions")
                .document("question" + String.valueOf(current_question + 1)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                L1_1_count = Integer.valueOf(documentSnapshot.getString("L1_selected1_count"));
                L1_2_count = Integer.valueOf(documentSnapshot.getString("L1_selected2_count"));
                L1_3_count = Integer.valueOf(documentSnapshot.getString("L1_selected3_count"));
                L1_4_count = Integer.valueOf(documentSnapshot.getString("L1_selected4_count"));

                L2_1_count = Integer.valueOf(documentSnapshot.getString("L2_selected1_count"));
                L2_2_count = Integer.valueOf(documentSnapshot.getString("L2_selected2_count"));
                L2_3_count = Integer.valueOf(documentSnapshot.getString("L2_selected3_count"));
                L2_4_count = Integer.valueOf(documentSnapshot.getString("L2_selected4_count"));

                L3_1_count = Integer.valueOf(documentSnapshot.getString("L3_selected1_count"));
                L3_2_count = Integer.valueOf(documentSnapshot.getString("L3_selected2_count"));
                L3_3_count = Integer.valueOf(documentSnapshot.getString("L3_selected3_count"));
                L3_4_count = Integer.valueOf(documentSnapshot.getString("L3_selected4_count"));

                L4_1_count = Integer.valueOf(documentSnapshot.getString("L4_selected1_count"));
                L4_2_count = Integer.valueOf(documentSnapshot.getString("L4_selected2_count"));
                L4_3_count = Integer.valueOf(documentSnapshot.getString("L4_selected3_count"));
                L4_4_count = Integer.valueOf(documentSnapshot.getString("L4_selected4_count"));


                double sum1 = L1_1_count + L1_2_count + L1_3_count + L1_4_count;
                double sum2 = L2_1_count + L2_2_count + L2_3_count + L2_4_count;
                double sum3 = L3_1_count + L3_2_count + L3_3_count + L3_4_count;
                double sum4 = L4_1_count + L4_2_count + L4_3_count + L4_4_count;


                L1_1_percentage = L1_1_count/sum1;
                L1_2_percentage = L1_2_count/sum1;
                L1_3_percentage = L1_3_count/sum1;
                L1_4_percentage = L1_4_count/sum1;

                L2_1_percentage = L2_1_count/sum2;
                L2_2_percentage = L2_2_count/sum2;
                L2_3_percentage = L2_3_count/sum2;
                L2_4_percentage = L2_4_count/sum2;

                L3_1_percentage = L3_1_count/sum3;
                L3_2_percentage = L3_2_count/sum3;
                L3_3_percentage = L3_3_count/sum3;
                L3_4_percentage = L3_4_count/sum3;

                L4_1_percentage = L4_1_count/sum4;
                L4_2_percentage = L4_2_count/sum4;
                L4_3_percentage = L4_3_count/sum4;
                L4_4_percentage = L4_4_count/sum4;


                L1_1_p.setText(String.format("1-1 percentage: %.2f"  ,L1_1_percentage));
                L1_2_p.setText(String.format("1-2 percentage: %.2f"  ,L1_2_percentage));
                L1_3_p.setText(String.format("1-3 percentage: %.2f"  ,L1_3_percentage));
                L1_4_p.setText(String.format("1-4 percentage: %.2f"  ,L1_4_percentage));

                L2_1_p.setText(String.format("2-1 percentage: %.2f"  ,L2_1_percentage));
                L2_2_p.setText(String.format("2-2 percentage: %.2f"  ,L2_2_percentage));
                L2_3_p.setText(String.format("2-3 percentage: %.2f"  ,L2_3_percentage));
                L2_4_p.setText(String.format("2-4 percentage: %.2f"  ,L2_4_percentage));

                L3_1_p.setText(String.format("3-1 percentage: %.2f"  ,L3_1_percentage));
                L3_2_p.setText(String.format("3-2 percentage: %.2f"  ,L3_2_percentage));
                L3_3_p.setText(String.format("3-3 percentage: %.2f"  ,L3_3_percentage));
                L3_4_p.setText(String.format("3-4 percentage: %.2f"  ,L3_4_percentage));

                L4_1_p.setText(String.format("4-1 percentage: %.2f"  ,L4_1_percentage));
                L4_2_p.setText(String.format("4-2 percentage: %.2f"  ,L4_2_percentage));
                L4_3_p.setText(String.format("4-3 percentage: %.2f"  ,L4_3_percentage));
                L4_4_p.setText(String.format("4-4 percentage: %.2f"  ,L4_4_percentage));




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
            Intent intent = new Intent(Creator_Survey_GradeMatchingQuestionsActivity.this, Creator_Survey_GradeRankingQuestionsActivity.class);
            intent.putExtra("info",1);
            startActivity(intent);
        }
    }

    private void changeQuestionBackward(){

        if(current_question == 0){
            Intent intent = new Intent(Creator_Survey_GradeMatchingQuestionsActivity.this, Creator_Survey_GradeFRQuestionsActivity.class);
            intent.putExtra("info",2);

            startActivity(intent);
        }

        else{
            current_question --;

            setQuestion(current_question);
        }
    }
}