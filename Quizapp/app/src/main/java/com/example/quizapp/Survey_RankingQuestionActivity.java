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
import android.widget.EditText;
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
import static com.example.quizapp.MCQuestionActivity2.newWorksheet;
import static com.example.quizapp.MCQuestionActivity2.worksheet_count;
import static com.example.quizapp.MCQuestionActivity2.worksheet_names;
import static com.example.quizapp.Survey_MCQuestionActivity.NumOfSurvey;
import static com.example.quizapp.UserProfileActivity.Current_user_name;
import static com.example.quizapp.UserProfileActivity.SURVEY_IDs;
import static com.example.quizapp.UserProfileActivity.TEST_IDs;

public class Survey_RankingQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question, q_count;

    private Button option1, option2, option3, option4, GoNext, GoPrev;

    private EditText input1, input2, input3, input4;

    private ArrayList<Survey_RankingQuestion> questionList = new ArrayList<>();

    private int current_question;
    private int info = 0;

    private FirebaseFirestore firestore;

    public static ArrayList<String> survey_RANKING_IDs = new ArrayList<>();
    private Dialog loading;

    private int count;

    public static ArrayList<RankingAnswers> survey_RANKING_Answers = new ArrayList<>();

    private int num_worksheet_questions;


    private int input1_1_count,input1_2_count,input1_3_count,input1_4_count;
    private int input2_1_count,input2_2_count,input2_3_count,input2_4_count;
    private int input3_1_count,input3_2_count,input3_3_count,input3_4_count;
    private int input4_1_count,input4_2_count,input4_3_count,input4_4_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey__ranking_question);

        Intent intent = getIntent();
        info = intent.getIntExtra("info",0);

        question = findViewById(R.id.question);
        q_count = findViewById(R.id.question_num);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        input3 = findViewById(R.id.input3);
        input4 = findViewById(R.id.input4);


        GoNext = findViewById(R.id.GoNext);
        GoPrev = findViewById(R.id.GoPrev);


        GoNext.setOnClickListener(this);
        GoPrev.setOnClickListener(this);

        firestore = FirebaseFirestore.getInstance();

        loading = new Dialog(Survey_RankingQuestionActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();

        getQuestionList();
    }

    private void getQuestionList(){

//        questionList.clear();

        survey_RANKING_IDs.clear();

        firestore.collection("surveys").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                .document("questionList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        count = Integer.valueOf(doc.getString("count"));



                        for(int i = 1; i <= count; i++){
                            survey_RANKING_IDs.add(doc.getString("question"+String.valueOf(i)+"_id"));
                        }

                        if(count == 0){
                            //no ranking question, go to submission page.

                            Intent intent = new Intent(Survey_RankingQuestionActivity.this, Survey_SubmissionActivity.class);
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
        firestore.collection("surveys").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
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

                    QueryDocumentSnapshot quesDoc = docList.get(survey_RANKING_IDs.get(i));

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

        input1.getText().clear();
        input2.getText().clear();
        input3.getText().clear();
        input4.getText().clear();

        survey_RANKING_Answers.add(new RankingAnswers("null","null","null","null"));

        if(survey_RANKING_Answers.size()>0){//that means we have done the problems before

            if(!survey_RANKING_Answers.get(current_question).getUser_input1().equals("null")){
                input1.setText(survey_RANKING_Answers.get(current_question).getUser_input1());
            }
            if(!survey_RANKING_Answers.get(current_question).getUser_input2().equals("null")){
                input2.setText(survey_RANKING_Answers.get(current_question).getUser_input2());
            }
            if(!survey_RANKING_Answers.get(current_question).getUser_input3().equals("null")){
                input3.setText(survey_RANKING_Answers.get(current_question).getUser_input3());
            }
            if(!survey_RANKING_Answers.get(current_question).getUser_input4().equals("null")){
                input4.setText(survey_RANKING_Answers.get(current_question).getUser_input4());
            }
        }



        loading.cancel();

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.GoNext){
            survey_RANKING_Answers.get(current_question).setUser_input1(input1.getText().toString());
            survey_RANKING_Answers.get(current_question).setUser_input2(input2.getText().toString());
            survey_RANKING_Answers.get(current_question).setUser_input3(input3.getText().toString());
            survey_RANKING_Answers.get(current_question).setUser_input4(input4.getText().toString());


            changeQuestionForward();
        }
        else if (v.getId() == R.id.GoPrev){
            survey_RANKING_Answers.get(current_question).setUser_input1(input1.getText().toString());
            survey_RANKING_Answers.get(current_question).setUser_input2(input2.getText().toString());
            survey_RANKING_Answers.get(current_question).setUser_input3(input3.getText().toString());
            survey_RANKING_Answers.get(current_question).setUser_input4(input4.getText().toString());

            changeQuestionBackward();
        }
    }


    private void changeQuestionForward(){


        if(current_question < questionList.size() - 1){
            current_question ++;
            setQuestion(current_question);

        }

        else{


//            firestore.collection("TestWorksheet").document(TEST_IDs.get(NumOfTest))
//                    .collection("userWorksheets").document("worksheetList")
//                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if(task.isSuccessful()){
//                        DocumentSnapshot doc = task.getResult();
//                        if(doc.exists()){
//                            worksheet_count = Integer.valueOf(doc.getString("count"));
//
//                            for(int i = 1; i <= worksheet_count; i++){
//                                worksheet_names.add(doc.getString("worksheet"+String.valueOf(i)+"_name"));
//                            }
//
//                            for (int i = 0; i < worksheet_names.size(); i++){
//                                if(Current_user_name.equals(worksheet_names.get(i))){
//                                    newWorksheet = false;
//                                }
//                            }
//
//                            if(worksheet_names.size() == 0 || newWorksheet == true){
//                                CreateNewWorksheet();
//                            }
//                            else if(newWorksheet == false && worksheet_names.size()>0){
//                                update();
//                            }
//
//
//                        }
//                    }
//                }
//            });

            firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
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



            //go to submission
            Intent intent = new Intent(Survey_RankingQuestionActivity.this, Survey_SubmissionActivity.class);
            intent.putExtra("info",1);

            startActivity(intent);
        }
    }

    private void changeQuestionBackward(){
        if(current_question==0){
            Intent intent = new Intent(Survey_RankingQuestionActivity.this, Survey_MatchingQuestionActivity.class);
            intent.putExtra("info",2);
            startActivity(intent);
        }

        else{
            current_question --;

            setQuestion(current_question);
        }
    }


    private void update(int i){

        firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                .document("question"+String.valueOf(i + 1)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                input1_1_count = Integer.valueOf(documentSnapshot.getString("input1_1_count"));
                input1_2_count = Integer.valueOf(documentSnapshot.getString("input1_2_count"));
                input1_3_count = Integer.valueOf(documentSnapshot.getString("input1_3_count"));
                input1_4_count = Integer.valueOf(documentSnapshot.getString("input1_4_count"));

                input2_1_count = Integer.valueOf(documentSnapshot.getString("input2_1_count"));
                input2_2_count = Integer.valueOf(documentSnapshot.getString("input2_2_count"));
                input2_3_count = Integer.valueOf(documentSnapshot.getString("input2_3_count"));
                input2_4_count = Integer.valueOf(documentSnapshot.getString("input2_4_count"));

                input3_1_count = Integer.valueOf(documentSnapshot.getString("input3_1_count"));
                input3_2_count = Integer.valueOf(documentSnapshot.getString("input3_2_count"));
                input3_3_count = Integer.valueOf(documentSnapshot.getString("input3_3_count"));
                input3_4_count = Integer.valueOf(documentSnapshot.getString("input3_4_count"));

                input4_1_count = Integer.valueOf(documentSnapshot.getString("input4_1_count"));
                input4_2_count = Integer.valueOf(documentSnapshot.getString("input4_2_count"));
                input4_3_count = Integer.valueOf(documentSnapshot.getString("input4_3_count"));
                input4_4_count = Integer.valueOf(documentSnapshot.getString("input4_4_count"));


                if(survey_RANKING_Answers.get(i).getUser_input1().equals("1")){
                    input1_1_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input1_1_count", String.valueOf(input1_1_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_RANKING_Answers.get(i).getUser_input1().equals("2")){
                    input1_2_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input1_2_count", String.valueOf(input1_2_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_RANKING_Answers.get(i).getUser_input1().equals("3")){
                    input1_3_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input1_3_count", String.valueOf(input1_3_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_RANKING_Answers.get(i).getUser_input1().equals("4")){
                    input1_4_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input1_4_count", String.valueOf(input1_4_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }



                if(survey_RANKING_Answers.get(i).getUser_input2().equals("1")){
                    input2_1_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input2_1_count", String.valueOf(input2_1_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_RANKING_Answers.get(i).getUser_input2().equals("2")){
                    input2_2_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input2_2_count", String.valueOf(input2_2_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_RANKING_Answers.get(i).getUser_input2().equals("3")){
                    input2_3_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input2_3_count", String.valueOf(input2_3_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_RANKING_Answers.get(i).getUser_input2().equals("4")){
                    input2_4_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input2_4_count", String.valueOf(input2_4_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }







                if(survey_RANKING_Answers.get(i).getUser_input3().equals("1")){
                    input3_1_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input3_1_count", String.valueOf(input3_1_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_RANKING_Answers.get(i).getUser_input3().equals("2")){
                    input3_2_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input3_2_count", String.valueOf(input3_2_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_RANKING_Answers.get(i).getUser_input3().equals("3")){
                    input3_3_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input3_3_count", String.valueOf(input3_3_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_RANKING_Answers.get(i).getUser_input3().equals("4")){
                    input3_4_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input3_4_count", String.valueOf(input3_4_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }







                if(survey_RANKING_Answers.get(i).getUser_input4().equals("1")){
                    input4_1_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input4_1_count", String.valueOf(input4_1_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_RANKING_Answers.get(i).getUser_input4().equals("2")){
                    input4_2_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input4_2_count", String.valueOf(input4_2_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_RANKING_Answers.get(i).getUser_input4().equals("3")){
                    input4_3_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input4_3_count", String.valueOf(input4_3_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }
                if(survey_RANKING_Answers.get(i).getUser_input4().equals("4")){
                    input4_4_count ++;

                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("input4_4_count", String.valueOf(input4_4_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                            .document("question"+String.valueOf(i + 1)).update(count_update);
                }

            }
        });

    }

    private void setup(int i){

        Map<String, Object> Data = new ArrayMap<>();

        Data.put("input1_1_count","0");
        Data.put("input1_2_count","0");
        Data.put("input1_3_count","0");
        Data.put("input1_4_count","0");

        Data.put("input2_1_count","0");
        Data.put("input2_2_count","0");
        Data.put("input2_3_count","0");
        Data.put("input2_4_count","0");

        Data.put("input3_1_count","0");
        Data.put("input3_2_count","0");
        Data.put("input3_3_count","0");
        Data.put("input3_4_count","0");

        Data.put("input4_1_count","0");
        Data.put("input4_2_count","0");
        Data.put("input4_3_count","0");
        Data.put("input4_4_count","0");




        firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                .document("question"+String.valueOf(i + 1)).set(Data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                num_worksheet_questions ++;

                Map<String,Object> new_list_count = new ArrayMap<>();

                new_list_count.put("count", String.valueOf(num_worksheet_questions));

                firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("RankingQuestions")
                        .document("questionList").update(new_list_count);

                update(i);
            }
        });



    }



}