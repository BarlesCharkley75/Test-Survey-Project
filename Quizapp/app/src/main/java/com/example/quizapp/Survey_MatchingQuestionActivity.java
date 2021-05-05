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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static com.example.quizapp.MCQuestionActivity2.NumOfTest;
import static com.example.quizapp.MCQuestionActivity2.NumOfWorksheet;
import static com.example.quizapp.MCQuestionActivity2.newWorksheet;
import static com.example.quizapp.MCQuestionActivity2.worksheet_count;
import static com.example.quizapp.MCQuestionActivity2.worksheet_names;
import static com.example.quizapp.MCQuestionActivity2.worksheet_next;
import static com.example.quizapp.Survey_MCQuestionActivity.NumOfSurvey;
import static com.example.quizapp.UserProfileActivity.Current_user_name;
import static com.example.quizapp.UserProfileActivity.SURVEY_IDs;
import static com.example.quizapp.UserProfileActivity.TEST_IDs;

public class Survey_MatchingQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question, q_count;

    private Button option1_L, option2_L, option3_L, option4_L;
    private Button option1_R, option2_R, option3_R, option4_R;
    private Button GoNext, GoPrev, clear;

    private ArrayList<Survey_MatchingQuestion> questionList = new ArrayList<>();

    private int current_question;
    private int info = 0;

    private int pair_order = 0;


    private FirebaseFirestore firestore;

    public static ArrayList<String> survey_MATCHING_IDs = new ArrayList<>();

    public static ArrayList<MatchingAnswers> survey_MATCHING_Answers = new ArrayList<>();
    private Dialog loading;

    private int count;

    private int num_worksheet_questions;


    private int L1_selected1_count,L1_selected2_count,L1_selected3_count,L1_selected4_count;
    private int L2_selected1_count,L2_selected2_count,L2_selected3_count,L2_selected4_count;
    private int L3_selected1_count,L3_selected2_count,L3_selected3_count,L3_selected4_count;
    private int L4_selected1_count,L4_selected2_count,L4_selected3_count,L4_selected4_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey__matching_question);

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
        clear = findViewById(R.id.Clear);


        option1_L.setOnClickListener(this);
        option2_L.setOnClickListener(this);
        option3_L.setOnClickListener(this);
        option4_L.setOnClickListener(this);

        option1_R.setOnClickListener(this);
        option2_R.setOnClickListener(this);
        option3_R.setOnClickListener(this);
        option4_R.setOnClickListener(this);

        GoNext.setOnClickListener(this);
        GoPrev.setOnClickListener(this);
        clear.setOnClickListener(this);

        firestore = FirebaseFirestore.getInstance();

        loading = new Dialog(Survey_MatchingQuestionActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();



        getQuestionList();
    }

    private void getQuestionList(){

//        questionList.clear();

        survey_MATCHING_IDs.clear();

        firestore.collection("surveys").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                .document("questionList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        count = Integer.valueOf(doc.getString("count"));



                        for(int i = 1; i <= count; i++){
                            survey_MATCHING_IDs.add(doc.getString("question"+String.valueOf(i)+"_id"));
                        }

                        if(count == 0){
                            //no matching questions, go to ranking questions

                            Intent intent = new Intent (Survey_MatchingQuestionActivity.this, Survey_RankingQuestionActivity.class);
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
        firestore.collection("surveys").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
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

                    QueryDocumentSnapshot quesDoc = docList.get(survey_MATCHING_IDs.get(i));

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


        survey_MATCHING_Answers.add(new MatchingAnswers(0,0,0,0));

        if(survey_MATCHING_Answers.size()>0){//means we have already done the problems before
            if(survey_MATCHING_Answers.get(current_question).getL1_selected() != 0){
                option1_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                if(survey_MATCHING_Answers.get(current_question).getL1_selected() == 1){
                    option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                }
                else if(survey_MATCHING_Answers.get(current_question).getL1_selected() == 2){
                    option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                }
                else if(survey_MATCHING_Answers.get(current_question).getL1_selected() == 3){
                    option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                }
                else if(survey_MATCHING_Answers.get(current_question).getL1_selected() == 4){
                    option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                }
            }

            if(survey_MATCHING_Answers.get(current_question).getL2_selected() != 0){
                option2_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                if(survey_MATCHING_Answers.get(current_question).getL2_selected() == 1){
                    option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                }
                else if(survey_MATCHING_Answers.get(current_question).getL2_selected() == 2){
                    option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                }
                else if(survey_MATCHING_Answers.get(current_question).getL2_selected() == 3){
                    option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                }
                else if(survey_MATCHING_Answers.get(current_question).getL2_selected() == 4){
                    option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                }
            }

            if(survey_MATCHING_Answers.get(current_question).getL3_selected() != 0){
                option3_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                if(survey_MATCHING_Answers.get(current_question).getL3_selected() == 1){
                    option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                }
                else if(survey_MATCHING_Answers.get(current_question).getL3_selected() == 2){
                    option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                }
                else if(survey_MATCHING_Answers.get(current_question).getL3_selected() == 3){
                    option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                }
                else if(survey_MATCHING_Answers.get(current_question).getL3_selected() == 4){
                    option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                }
            }

            if(survey_MATCHING_Answers.get(current_question).getL4_selected() != 0){
                option4_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                if(survey_MATCHING_Answers.get(current_question).getL4_selected() == 1){
                    option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                }
                else if(survey_MATCHING_Answers.get(current_question).getL4_selected() == 2){
                    option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                }
                else if(survey_MATCHING_Answers.get(current_question).getL4_selected() == 3){
                    option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                }
                else if(survey_MATCHING_Answers.get(current_question).getL4_selected() == 4){
                    option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                }
            }

        }

        loading.cancel();

    }




    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.option_L1){
            option1_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            pair_order = 1;
        }

        if(v.getId() == R.id.option_L2){
            option2_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
            pair_order = 2;
        }

        if(v.getId() == R.id.option_L3){
            option3_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
            pair_order = 3;
        }

        if (v.getId() == R.id.option_L4){
            option4_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
            pair_order = 4;
        }


        if(pair_order == 1){
            if(v.getId() == R.id.option_R1 && survey_MATCHING_Answers.get(current_question).getL1_selected() == 0){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                survey_MATCHING_Answers.get(current_question).setL1_selected(1);
            }
            if(v.getId() == R.id.option_R2 && survey_MATCHING_Answers.get(current_question).getL1_selected() == 0){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                survey_MATCHING_Answers.get(current_question).setL1_selected(2);
            }
            if(v.getId() == R.id.option_R3 && survey_MATCHING_Answers.get(current_question).getL1_selected() == 0){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                survey_MATCHING_Answers.get(current_question).setL1_selected(3);
            }

            if (v.getId() == R.id.option_R4 && survey_MATCHING_Answers.get(current_question).getL1_selected() == 0){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                survey_MATCHING_Answers.get(current_question).setL1_selected(4);
            }

        }

        if(pair_order == 2){
            if(v.getId() == R.id.option_R1 && survey_MATCHING_Answers.get(current_question).getL2_selected() == 0){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                survey_MATCHING_Answers.get(current_question).setL2_selected(1);
            }
            if(v.getId() == R.id.option_R2 && survey_MATCHING_Answers.get(current_question).getL2_selected() == 0){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                survey_MATCHING_Answers.get(current_question).setL2_selected(2);
            }
            if(v.getId() == R.id.option_R3 && survey_MATCHING_Answers.get(current_question).getL2_selected() == 0 ){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                survey_MATCHING_Answers.get(current_question).setL2_selected(3);
            }

            if (v.getId() == R.id.option_R4 && survey_MATCHING_Answers.get(current_question).getL2_selected() == 0){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                survey_MATCHING_Answers.get(current_question).setL2_selected(4);

            }

        }

        if(pair_order == 3){
            if(v.getId() == R.id.option_R1 && survey_MATCHING_Answers.get(current_question).getL3_selected() == 0){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                survey_MATCHING_Answers.get(current_question).setL3_selected(1);
            }
            if(v.getId() == R.id.option_R2 && survey_MATCHING_Answers.get(current_question).getL3_selected() == 0){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                survey_MATCHING_Answers.get(current_question).setL3_selected(2);
            }
            if(v.getId() == R.id.option_R3 && survey_MATCHING_Answers.get(current_question).getL3_selected() == 0){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                survey_MATCHING_Answers.get(current_question).setL3_selected(3);
            }

            if (v.getId() == R.id.option_R4 && survey_MATCHING_Answers.get(current_question).getL3_selected() == 0){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                survey_MATCHING_Answers.get(current_question).setL3_selected(4);
            }

        }

        if(pair_order == 4){
            if(v.getId() == R.id.option_R1 && survey_MATCHING_Answers.get(current_question).getL4_selected() == 0){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                survey_MATCHING_Answers.get(current_question).setL4_selected(1);
            }
            if(v.getId() == R.id.option_R2 && survey_MATCHING_Answers.get(current_question).getL4_selected() == 0){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                survey_MATCHING_Answers.get(current_question).setL4_selected(2);
            }
            if(v.getId() == R.id.option_R3 && survey_MATCHING_Answers.get(current_question).getL4_selected() == 0){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                survey_MATCHING_Answers.get(current_question).setL4_selected(3);
            }

            if (v.getId() == R.id.option_R4 && survey_MATCHING_Answers.get(current_question).getL4_selected() == 0){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                survey_MATCHING_Answers.get(current_question).setL4_selected(4);
            }

        }

        if(v.getId() == R.id.Clear){
            survey_MATCHING_Answers.get(current_question).setL1_selected(0);
            survey_MATCHING_Answers.get(current_question).setL2_selected(0);
            survey_MATCHING_Answers.get(current_question).setL3_selected(0);
            survey_MATCHING_Answers.get(current_question).setL4_selected(0);

            option1_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option2_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option3_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option4_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));

            option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));


        }

        if(v.getId() == R.id.GoNext){
            changeQuestionForward();
        }
        else if (v.getId() == R.id.GoPrev){
            changeQuestionBackward();
        }

    }


    private void changeQuestionForward(){
        if(current_question >= questionList.size()-1){

            firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                    .document("questionList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        if(doc.exists()){

                            num_worksheet_questions = Integer.valueOf(doc.getString("count"));

                            if(num_worksheet_questions != questionList.size()){
                                for (int i = 0; i < questionList.size();i++){
                                    setup(i);
                                }
                            }
                            else{
                                for (int i = 0; i < questionList.size();i++){
                                    update(i);
                                }
                            }

                        }
                    }
                }
            });


            //go to next activity
            Intent intent = new Intent (Survey_MatchingQuestionActivity.this, Survey_RankingQuestionActivity.class);
            intent.putExtra("info",1);
            startActivity(intent);
        }

        else{
            current_question ++;

            setQuestion(current_question);


        }
    }

    private void changeQuestionBackward(){

        if(current_question==0){
            Intent intent = new Intent(Survey_MatchingQuestionActivity.this, Survey_FRQuestionActivity.class);
            intent.putExtra("info",2);
            startActivity(intent);
        }
        else{
            current_question --;

            setQuestion(current_question);
        }
    }






    private void update(int i){


        firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                .document("question"+String.valueOf(i + 1)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                L1_selected1_count = Integer.valueOf(documentSnapshot.getString("L1_selected1_count"));
                L1_selected2_count = Integer.valueOf(documentSnapshot.getString("L1_selected2_count"));
                L1_selected3_count = Integer.valueOf(documentSnapshot.getString("L1_selected3_count"));
                L1_selected4_count = Integer.valueOf(documentSnapshot.getString("L1_selected4_count"));

                L2_selected1_count = Integer.valueOf(documentSnapshot.getString("L2_selected1_count"));
                L2_selected2_count = Integer.valueOf(documentSnapshot.getString("L2_selected2_count"));
                L2_selected3_count = Integer.valueOf(documentSnapshot.getString("L2_selected3_count"));
                L2_selected4_count = Integer.valueOf(documentSnapshot.getString("L2_selected4_count"));

                L3_selected1_count = Integer.valueOf(documentSnapshot.getString("L3_selected1_count"));
                L3_selected2_count = Integer.valueOf(documentSnapshot.getString("L3_selected2_count"));
                L3_selected3_count = Integer.valueOf(documentSnapshot.getString("L3_selected3_count"));
                L3_selected4_count = Integer.valueOf(documentSnapshot.getString("L3_selected4_count"));

                L4_selected1_count = Integer.valueOf(documentSnapshot.getString("L4_selected1_count"));
                L4_selected2_count = Integer.valueOf(documentSnapshot.getString("L4_selected2_count"));
                L4_selected3_count = Integer.valueOf(documentSnapshot.getString("L4_selected3_count"));
                L4_selected4_count = Integer.valueOf(documentSnapshot.getString("L4_selected4_count"));


                if(survey_MATCHING_Answers.get(i).getL1_selected() == 1){
                    L1_selected1_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L1_selected1_count", String.valueOf(L1_selected1_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_MATCHING_Answers.get(i).getL1_selected() == 2){
                    L1_selected2_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L1_selected2_count", String.valueOf(L1_selected2_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_MATCHING_Answers.get(i).getL1_selected() == 3){
                    L1_selected3_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L1_selected3_count", String.valueOf(L1_selected3_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_MATCHING_Answers.get(i).getL1_selected() == 4){
                    L1_selected4_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L1_selected4_count", String.valueOf(L1_selected4_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }




                if(survey_MATCHING_Answers.get(i).getL2_selected() == 1){
                    L2_selected1_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L2_selected1_count", String.valueOf(L2_selected1_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_MATCHING_Answers.get(i).getL2_selected() == 2){
                    L2_selected2_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L2_selected2_count", String.valueOf(L2_selected2_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_MATCHING_Answers.get(i).getL2_selected() == 3){
                    L2_selected3_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L2_selected3_count", String.valueOf(L2_selected3_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_MATCHING_Answers.get(i).getL2_selected() == 4){
                    L2_selected4_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L2_selected4_count", String.valueOf(L2_selected4_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }




                if(survey_MATCHING_Answers.get(i).getL3_selected() == 1){
                    L3_selected1_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L3_selected1_count", String.valueOf(L3_selected1_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_MATCHING_Answers.get(i).getL3_selected() == 2){
                    L3_selected2_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L3_selected2_count", String.valueOf(L3_selected2_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_MATCHING_Answers.get(i).getL3_selected() == 3){
                    L3_selected3_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L3_selected3_count", String.valueOf(L3_selected3_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_MATCHING_Answers.get(i).getL3_selected() == 4){
                    L3_selected4_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L3_selected4_count", String.valueOf(L3_selected4_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }




                if(survey_MATCHING_Answers.get(i).getL4_selected() == 1){
                    L4_selected1_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L4_selected1_count", String.valueOf(L4_selected1_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_MATCHING_Answers.get(i).getL4_selected() == 2){
                    L4_selected2_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L4_selected2_count", String.valueOf(L4_selected2_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_MATCHING_Answers.get(i).getL4_selected() == 3){
                    L4_selected3_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L4_selected3_count", String.valueOf(L4_selected3_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_MATCHING_Answers.get(i).getL4_selected() == 4){
                    L4_selected4_count++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("L4_selected4_count", String.valueOf(L4_selected4_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }





            }
        });

    }

    private void setup(int i){

        Map<String, Object> Data = new ArrayMap<>();

        Data.put("L1_selected1_count", "0");
        Data.put("L1_selected2_count", "0");
        Data.put("L1_selected3_count", "0");
        Data.put("L1_selected4_count", "0");

        Data.put("L2_selected1_count", "0");
        Data.put("L2_selected2_count", "0");
        Data.put("L2_selected3_count", "0");
        Data.put("L2_selected4_count", "0");

        Data.put("L3_selected1_count", "0");
        Data.put("L3_selected2_count", "0");
        Data.put("L3_selected3_count", "0");
        Data.put("L3_selected4_count", "0");

        Data.put("L4_selected1_count", "0");
        Data.put("L4_selected2_count", "0");
        Data.put("L4_selected3_count", "0");
        Data.put("L4_selected4_count", "0");




        firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                .document("question"+String.valueOf(i + 1)).set(Data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                num_worksheet_questions ++;

                Map<String,Object> new_list_count = new ArrayMap<>();

                new_list_count.put("count", String.valueOf(num_worksheet_questions));

                firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MatchingQuestions")
                        .document("questionList").update(new_list_count);

                update(i);
            }
        });



    }


}