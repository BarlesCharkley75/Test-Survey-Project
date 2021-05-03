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
import android.widget.Toast;

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
import static com.example.quizapp.UserProfileActivity.Current_user_name;
import static com.example.quizapp.UserProfileActivity.TEST_IDs;

public class MatchingQurestionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question, q_count;

    private Button option1_L, option2_L, option3_L, option4_L;
    private Button option1_R, option2_R, option3_R, option4_R;
    private Button GoNext, GoPrev, clear;

    private ArrayList<MatchingQuestion> questionList;

    private int current_question;
    private int info = 0;

    private int pair_order = 0;


    private FirebaseFirestore firestore;
    
    public static ArrayList<String> MATCHING_IDs;

    public static ArrayList<MatchingAnswers> MATCHING_Answers = new ArrayList<>();
    private Dialog loading;

    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_qurestion);

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

        questionList = new ArrayList<>();
        MATCHING_IDs = new ArrayList<>();

        loading = new Dialog(MatchingQurestionActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();



        getQuestionList();

    }

    private void getQuestionList(){

//        questionList.clear();

        MATCHING_IDs.clear();

        firestore.collection("tests").document(TEST_IDs.get(NumOfTest)).collection("MatchingQuestions")
                .document("questionList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        count = Integer.valueOf(doc.getString("count"));



                        for(int i = 1; i <= count; i++){
                            MATCHING_IDs.add(doc.getString("question"+String.valueOf(i)+"_id"));
                        }

                        if(count == 0){
                            //no matching questions, go to ranking questions

                            Intent intent = new Intent (MatchingQurestionActivity.this, RankingQuestionActivity.class);
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
        firestore.collection("tests").document(TEST_IDs.get(NumOfTest)).collection("MatchingQuestions")
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

                    QueryDocumentSnapshot quesDoc = docList.get(MATCHING_IDs.get(i));

                    questionList.add(new MatchingQuestion(
                            quesDoc.getString("question"),
                            quesDoc.getString("optionL1"),
                            quesDoc.getString("optionL2"),
                            quesDoc.getString("optionL3"),
                            quesDoc.getString("optionL4"),
                            quesDoc.getString("optionR1"),
                            quesDoc.getString("optionR2"),
                            quesDoc.getString("optionR3"),
                            quesDoc.getString("optionR4"),
                            Integer.valueOf(quesDoc.getString("L1_Answer")),
                            Integer.valueOf(quesDoc.getString("L2_Answer")),
                            Integer.valueOf(quesDoc.getString("L3_Answer")),
                            Integer.valueOf(quesDoc.getString("L4_Answer")),
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
        else if (info == 2){
            current_question = questionList.size()-1;
            setQuestion(current_question);

        }
    }

    private void setQuestion(int i){
        question.setText(questionList.get(i).getQuestion()+"\n"+" \n"+"(points: " + questionList.get(i).getPoints() + ")");
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


        MATCHING_Answers.add(new MatchingAnswers(0,0,0,0));

        if(MATCHING_Answers.size()>0){//means we have already done the problems before
            if(MATCHING_Answers.get(current_question).getL1_selected() != 0){
                option1_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                if(MATCHING_Answers.get(current_question).getL1_selected() == 1){
                    option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                }
                else if(MATCHING_Answers.get(current_question).getL1_selected() == 2){
                    option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                }
                else if(MATCHING_Answers.get(current_question).getL1_selected() == 3){
                    option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                }
                else if(MATCHING_Answers.get(current_question).getL1_selected() == 4){
                    option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                }
            }

            if(MATCHING_Answers.get(current_question).getL2_selected() != 0){
                option2_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                if(MATCHING_Answers.get(current_question).getL2_selected() == 1){
                    option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                }
                else if(MATCHING_Answers.get(current_question).getL2_selected() == 2){
                    option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                }
                else if(MATCHING_Answers.get(current_question).getL2_selected() == 3){
                    option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                }
                else if(MATCHING_Answers.get(current_question).getL2_selected() == 4){
                    option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                }
            }

            if(MATCHING_Answers.get(current_question).getL3_selected() != 0){
                option3_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                if(MATCHING_Answers.get(current_question).getL3_selected() == 1){
                    option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                }
                else if(MATCHING_Answers.get(current_question).getL3_selected() == 2){
                    option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                }
                else if(MATCHING_Answers.get(current_question).getL3_selected() == 3){
                    option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                }
                else if(MATCHING_Answers.get(current_question).getL3_selected() == 4){
                    option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                }
            }

            if(MATCHING_Answers.get(current_question).getL4_selected() != 0){
                option4_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                if(MATCHING_Answers.get(current_question).getL4_selected() == 1){
                    option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                }
                else if(MATCHING_Answers.get(current_question).getL4_selected() == 2){
                    option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                }
                else if(MATCHING_Answers.get(current_question).getL4_selected() == 3){
                    option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                }
                else if(MATCHING_Answers.get(current_question).getL4_selected() == 4){
                    option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                }
            }

        }

        loading.cancel();

    }

    //This is the most complicated part about the matching questions. The idea is, each pair order represent a pair
    //options in the same pair order will have the same color. Other than that, just set up refer to MC questions.

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
            if(v.getId() == R.id.option_R1 && MATCHING_Answers.get(current_question).getL1_selected() == 0){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                MATCHING_Answers.get(current_question).setL1_selected(1);
            }
            if(v.getId() == R.id.option_R2 && MATCHING_Answers.get(current_question).getL1_selected() == 0){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                MATCHING_Answers.get(current_question).setL1_selected(2);
            }
            if(v.getId() == R.id.option_R3 && MATCHING_Answers.get(current_question).getL1_selected() == 0){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                MATCHING_Answers.get(current_question).setL1_selected(3);
            }

            if (v.getId() == R.id.option_R4 && MATCHING_Answers.get(current_question).getL1_selected() == 0){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                MATCHING_Answers.get(current_question).setL1_selected(4);
            }

        }

        if(pair_order == 2){
            if(v.getId() == R.id.option_R1 && MATCHING_Answers.get(current_question).getL2_selected() == 0){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                MATCHING_Answers.get(current_question).setL2_selected(1);
            }
            if(v.getId() == R.id.option_R2 && MATCHING_Answers.get(current_question).getL2_selected() == 0){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                MATCHING_Answers.get(current_question).setL2_selected(2);
            }
            if(v.getId() == R.id.option_R3 && MATCHING_Answers.get(current_question).getL2_selected() == 0 ){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                MATCHING_Answers.get(current_question).setL2_selected(3);
            }

            if (v.getId() == R.id.option_R4 && MATCHING_Answers.get(current_question).getL2_selected() == 0){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                MATCHING_Answers.get(current_question).setL2_selected(4);

            }

        }

        if(pair_order == 3){
            if(v.getId() == R.id.option_R1 && MATCHING_Answers.get(current_question).getL3_selected() == 0){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                MATCHING_Answers.get(current_question).setL3_selected(1);
            }
            if(v.getId() == R.id.option_R2 && MATCHING_Answers.get(current_question).getL3_selected() == 0){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                MATCHING_Answers.get(current_question).setL3_selected(2);
            }
            if(v.getId() == R.id.option_R3 && MATCHING_Answers.get(current_question).getL3_selected() == 0){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                MATCHING_Answers.get(current_question).setL3_selected(3);
            }

            if (v.getId() == R.id.option_R4 && MATCHING_Answers.get(current_question).getL3_selected() == 0){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                MATCHING_Answers.get(current_question).setL3_selected(4);
            }

        }

        if(pair_order == 4){
            if(v.getId() == R.id.option_R1 && MATCHING_Answers.get(current_question).getL4_selected() == 0){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                MATCHING_Answers.get(current_question).setL4_selected(1);
            }
            if(v.getId() == R.id.option_R2 && MATCHING_Answers.get(current_question).getL4_selected() == 0){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                MATCHING_Answers.get(current_question).setL4_selected(2);
            }
            if(v.getId() == R.id.option_R3 && MATCHING_Answers.get(current_question).getL4_selected() == 0){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                MATCHING_Answers.get(current_question).setL4_selected(3);
            }

            if (v.getId() == R.id.option_R4 && MATCHING_Answers.get(current_question).getL4_selected() == 0){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                MATCHING_Answers.get(current_question).setL4_selected(4);
            }

        }

        if(v.getId() == R.id.Clear){
            MATCHING_Answers.get(current_question).setL1_selected(0);
            MATCHING_Answers.get(current_question).setL2_selected(0);
            MATCHING_Answers.get(current_question).setL3_selected(0);
            MATCHING_Answers.get(current_question).setL4_selected(0);

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
            //upload answers
//            Map<String , Object> Answers = new ArrayMap<>();
//
//            for(int i = 0; i < count; i++){
//                Answers.clear();
//                Answers.put("L1_selected",String.valueOf(questionList.get(i).getL1_selected()));
//                Answers.put("L2_selected",String.valueOf(questionList.get(i).getL2_selected()));
//                Answers.put("L3_selected",String.valueOf(questionList.get(i).getL3_selected()));
//                Answers.put("L4_selected",String.valueOf(questionList.get(i).getL4_selected()));
//
//                firestore.collection("tests").document(TEST_IDs.get(NumOfTest)).collection("MatchingQuestions")
//                        .document(MATCHING_IDs.get(i)).update(Answers);
//
//            }


            firestore.collection("TestWorksheet").document(TEST_IDs.get(NumOfTest))
                    .collection("userWorksheets").document("worksheetList")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        if(doc.exists()){
                            worksheet_count = Integer.valueOf(doc.getString("count"));

                            for(int i = 1; i <= worksheet_count; i++){
                                worksheet_names.add(doc.getString("worksheet"+String.valueOf(i)+"_name"));
                            }

                            for (int i = 0; i < worksheet_names.size(); i++){
                                if(Current_user_name.equals(worksheet_names.get(i))){
                                    newWorksheet = false;
                                }
                            }

                            if(worksheet_names.size() == 0 || newWorksheet == true){
                                CreateNewWorksheet();
                            }
                            else if(newWorksheet == false && worksheet_names.size()>0){
                                update();
                            }


                        }
                    }
                }
            });


            //go to next activity
            Intent intent = new Intent (MatchingQurestionActivity.this, RankingQuestionActivity.class);
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
            Intent intent = new Intent(MatchingQurestionActivity.this, FRQuestionActivity.class);
            intent.putExtra("info",2);
            startActivity(intent);
        }
        else{
            current_question --;

            setQuestion(current_question);
        }
    }


    private void CreateNewWorksheet(){

        firestore.collection("TestWorksheet").document(TEST_IDs.get(NumOfTest))
                .collection("userWorksheets").document("worksheetList")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        worksheet_count = Integer.valueOf(doc.getString("count"));
                        worksheet_next = Integer.valueOf(doc.getString("NEXT"));

                    }


                    Map<String, Object> worksheetData = new ArrayMap<>();

                    worksheetData.put("NAME",Current_user_name);

                    firestore.collection("TestWorksheet").document(TEST_IDs.get(NumOfTest))
                            .collection("userWorksheets").document("worksheet"+String.valueOf(worksheet_next))
                            .set(worksheetData);


                    Map<String, Object> newWorksheetData = new ArrayMap<>();
                    newWorksheetData.put("count", String.valueOf(worksheet_count + 1));
                    newWorksheetData.put("NEXT", String.valueOf(worksheet_next + 1));
                    newWorksheetData.put("worksheet"+String.valueOf(worksheet_next)+"_name",Current_user_name);

                    firestore.collection("TestWorksheet").document(TEST_IDs.get(NumOfTest))
                            .collection("userWorksheets").document("worksheetList").update(newWorksheetData);


                    upload();

                }
            }
        });


    }

    private void upload(){
        Map<String , Object> Answers = new ArrayMap<>();

        for(int i = 0; i < count; i++){
            Answers.clear();
            Answers.put("L1_selected",String.valueOf(MATCHING_Answers.get(current_question).getL1_selected()));
            Answers.put("L2_selected",String.valueOf(MATCHING_Answers.get(current_question).getL2_selected()));
            Answers.put("L3_selected",String.valueOf(MATCHING_Answers.get(current_question).getL3_selected()));
            Answers.put("L4_selected",String.valueOf(MATCHING_Answers.get(current_question).getL4_selected()));


//                firestore.collection("tests").document(TEST_IDs.get(NumOfTest)).collection("MCQuestions")
//                        .document(MC_IDs.get(i)).update(Answers);

            firestore.collection("TestWorksheet").document(TEST_IDs.get(NumOfTest))
                    .collection("userWorksheets").document("worksheet"+String.valueOf(worksheet_next))
                    .collection("MatchingQuestions").document("question"+String.valueOf(i + 1))
                    .set(Answers);

        }
    }

    private void update(){


        for(int i = 0; i< worksheet_names.size(); i++){
            if(Current_user_name.equals(worksheet_names.get(i))){
                NumOfWorksheet = i;

                Map<String , Object> Answers = new ArrayMap<>();

                for(int j = 0; j < count; j++){
                    Answers.clear();
                    Answers.put("L1_selected",String.valueOf(MATCHING_Answers.get(current_question).getL1_selected()));
                    Answers.put("L2_selected",String.valueOf(MATCHING_Answers.get(current_question).getL2_selected()));
                    Answers.put("L3_selected",String.valueOf(MATCHING_Answers.get(current_question).getL3_selected()));
                    Answers.put("L4_selected",String.valueOf(MATCHING_Answers.get(current_question).getL4_selected()));

//                firestore.collection("tests").document(TEST_IDs.get(NumOfTest)).collection("MCQuestions")
//                        .document(MC_IDs.get(i)).update(Answers);

                    firestore.collection("TestWorksheet").document(TEST_IDs.get(NumOfTest))
                            .collection("userWorksheets").document("worksheet"+String.valueOf(NumOfWorksheet + 1))
                            .collection("MatchingQuestions").document("question"+String.valueOf(j + 1))
                            .set(Answers);

                }
            }


        }

    }

}