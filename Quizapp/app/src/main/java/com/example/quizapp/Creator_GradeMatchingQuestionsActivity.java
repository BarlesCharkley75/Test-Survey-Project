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
import static com.example.quizapp.MCQuestionActivity2.NumOfTest;
import static com.example.quizapp.Test_WorksheetListActivity.grader_NumOfTest;
import static com.example.quizapp.UserProfileActivity.TEST_IDs;

public class Creator_GradeMatchingQuestionsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question, q_count;

    private TextView correct_answerL1, correct_answerL2,correct_answerL3,correct_answerL4;

    private TextView points_total;

    private TextInputLayout points_get;


    private Button option1_L, option2_L, option3_L, option4_L;
    private Button option1_R, option2_R, option3_R, option4_R;
    private Button GoNext, GoPrev;

    private ArrayList<MatchingQuestion> questionList = new ArrayList<>();

    private ArrayList<MatchingAnswers> AnswerList = new ArrayList<>();

    private int current_question;
    private int info = 0;

    


    private FirebaseFirestore firestore;

    public static ArrayList<String> grader_MATCHING_IDs = new ArrayList<>();
    private Dialog loading;

    private int count;

    public static int MATCHING_points_total;

    public static ArrayList<Integer> MATCHING_points_get = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__grade_matching_questions);


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


        correct_answerL1 = findViewById(R.id.correct_answerL1);
        correct_answerL2 = findViewById(R.id.correct_answerL2);
        correct_answerL3 = findViewById(R.id.correct_answerL3);
        correct_answerL4 = findViewById(R.id.correct_answerL4);

        points_total = findViewById(R.id.points_total);
        points_get = findViewById(R.id.points_get);

        firestore = FirebaseFirestore.getInstance();


        GoNext.setOnClickListener(this);
        GoPrev.setOnClickListener(this);


        loading = new Dialog(Creator_GradeMatchingQuestionsActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();



        getQuestionList();
    }


    private void getQuestionList(){

//        questionList.clear();

        grader_MATCHING_IDs.clear();

        firestore.collection("tests").document(TEST_IDs.get(grader_NumOfTest)).collection("MatchingQuestions")
                .document("questionList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        count = Integer.valueOf(doc.getString("count"));



                        for(int i = 1; i <= count; i++){
                            grader_MATCHING_IDs.add(doc.getString("question"+String.valueOf(i)+"_id"));
                        }

                        if(count == 0){
                            //no matching questions, go to ranking questions

                            Intent intent = new Intent (Creator_GradeMatchingQuestionsActivity.this, Creator_GradeRankingQuestionsActivity.class);
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
        firestore.collection("TestWorksheet").document(TEST_IDs.get(grader_NumOfTest)).collection("userWorksheets")
                .document("worksheet" + String.valueOf(grader_NumOfWorksheet + 1)).collection("MatchingQuestions")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    docList.put(doc.getId(),doc);
                }

                for(int i = 0; i < count; i ++){
                    QueryDocumentSnapshot quesDoc = docList.get("question" + String.valueOf(i + 1));

                    AnswerList.add(new MatchingAnswers(Integer.valueOf(quesDoc.getString("L1_selected")),
                            Integer.valueOf(quesDoc.getString("L2_selected")),
                            Integer.valueOf(quesDoc.getString("L3_selected")),
                            Integer.valueOf(quesDoc.getString("L4_selected"))));
                }

                setQuestionList();
            }
        });


    }

    private void setQuestionList(){
        firestore.collection("tests").document(TEST_IDs.get(grader_NumOfTest)).collection("MatchingQuestions")
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

                    QueryDocumentSnapshot quesDoc = docList.get(grader_MATCHING_IDs.get(i));

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

        correct_answerL1.setText("correct answer L1: " + String.valueOf(questionList.get(current_question).getL1_ANS()));
        correct_answerL2.setText("correct answer L2: " + String.valueOf(questionList.get(current_question).getL2_ANS()));
        correct_answerL3.setText("correct answer L3: " + String.valueOf(questionList.get(current_question).getL3_ANS()));
        correct_answerL4.setText("correct answer L4: " + String.valueOf(questionList.get(current_question).getL4_ANS()));

        points_total.setText("/" + String.valueOf(questionList.get(current_question).getPoints()));


        MATCHING_points_get.add(-1);

        if(MATCHING_points_get.get(current_question) != -1){
            points_get.getEditText().setText(String.valueOf(MATCHING_points_get.get(current_question)));
        }
        else{
            points_get.getEditText().getText().clear();
        }


        if(AnswerList.get(current_question).getL1_selected() != 0){
            option1_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            if(AnswerList.get(current_question).getL1_selected() == 1){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            }
            else if(AnswerList.get(current_question).getL1_selected() == 2){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            }
            else if(AnswerList.get(current_question).getL1_selected() == 3){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            }
            else if(AnswerList.get(current_question).getL1_selected() == 4){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            }
        }

        if(AnswerList.get(current_question).getL2_selected() != 0){
            option2_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
            if(AnswerList.get(current_question).getL2_selected() == 1){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
            }
            else if(AnswerList.get(current_question).getL2_selected() == 2){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
            }
            else if(AnswerList.get(current_question).getL2_selected() == 3){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
            }
            else if(AnswerList.get(current_question).getL2_selected() == 4){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
            }
        }

        if(AnswerList.get(current_question).getL3_selected() != 0){
            option3_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
            if(AnswerList.get(current_question).getL3_selected() == 1){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
            }
            else if(AnswerList.get(current_question).getL3_selected() == 2){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
            }
            else if(AnswerList.get(current_question).getL3_selected() == 3){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
            }
            else if(AnswerList.get(current_question).getL3_selected() == 4){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
            }
        }

        if(AnswerList.get(current_question).getL4_selected() != 0){
            option4_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
            if(AnswerList.get(current_question).getL4_selected() == 1){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
            }
            else if(AnswerList.get(current_question).getL4_selected() == 2){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
            }
            else if(AnswerList.get(current_question).getL4_selected() == 3){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
            }
            else if(AnswerList.get(current_question).getL4_selected() == 4){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
            }
        }


        MATCHING_points_total = 0;

        for(int j = 0; j<questionList.size();j++){
            MATCHING_points_total += questionList.get(j).getPoints();
        }


        loading.cancel();

    }



    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.GoNext){
            if(points_get.getEditText().getText().toString().equals("")){
                MATCHING_points_get.set(current_question,0);
            }
            else{
                MATCHING_points_get.set(current_question,Integer.valueOf(points_get.getEditText().getText().toString()));
            }
            changeQuestionForward();
        }
        else if (v.getId() == R.id.GoPrev){
            if(points_get.getEditText().getText().toString().equals("")){
                MATCHING_points_get.set(current_question,0);
            }
            else{
                MATCHING_points_get.set(current_question,Integer.valueOf(points_get.getEditText().getText().toString()));
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


            for (int i = questionList.size(); i < MATCHING_points_get.size();i++){
                MATCHING_points_get.set(i,0);
            }

//             go to free response activity
            Intent intent = new Intent(Creator_GradeMatchingQuestionsActivity.this, Creator_GradeRankingQuestionsActivity.class);
            intent.putExtra("info",1);
            startActivity(intent);
        }
    }

    private void changeQuestionBackward(){

        if(current_question == 0){
            Intent intent = new Intent(Creator_GradeMatchingQuestionsActivity.this, Creator_GradeFRQuestionsActivity.class);
            intent.putExtra("info",2);

            startActivity(intent);
        }

        else{
            current_question --;

            setQuestion(current_question);
        }
    }
}