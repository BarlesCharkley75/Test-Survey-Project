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

public class Survey_FRQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question;
    private TextView q_count;

    private EditText user_input;

    private ArrayList<Survey_FRQuestion> questionList = new ArrayList<>();

    private int current_question;

    private Button GoNext,GoPrev;

    private int info = 0;

    private FirebaseFirestore firestore;

    public static ArrayList<String> survey_FR_IDs = new ArrayList<>();

    private Dialog loading;

    private int count;

    public static ArrayList<String> survey_FR_Answers = new ArrayList<>();
    private int num_worksheet_questions;

    private int answer_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey__f_r_question);

        Intent intent = getIntent();
        info = intent.getIntExtra("info",0);



        question = findViewById(R.id.question);
        q_count =  findViewById(R.id.question_num);

        user_input = findViewById(R.id.input);

        GoNext = findViewById(R.id.GoNext);
        GoPrev = findViewById(R.id.GoPrev);

        GoNext.setOnClickListener(this);
        GoPrev.setOnClickListener(this);

        firestore = FirebaseFirestore.getInstance();

        loading = new Dialog(Survey_FRQuestionActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();



        getQuestionList();

    }

    private void getQuestionList(){

//        questionList.clear();

        survey_FR_IDs.clear();

        firestore.collection("surveys").document(SURVEY_IDs.get(NumOfSurvey)).collection("FRQuestions")
                .document("questionList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        count = Integer.valueOf(doc.getString("count"));



                        for(int i = 1; i <= count; i++){
                            survey_FR_IDs.add(doc.getString("question"+String.valueOf(i)+"_id"));
                        }

                        if (count == 0){
                            //if there is no FR questions, go to matching questions.
                            Intent intent = new Intent (Survey_FRQuestionActivity.this, Survey_MatchingQuestionActivity.class);
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
        firestore.collection("surveys").document(SURVEY_IDs.get(NumOfSurvey)).collection("FRQuestions")
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

                    QueryDocumentSnapshot quesDoc = docList.get(survey_FR_IDs.get(i));

                    questionList.add(new Survey_FRQuestion(quesDoc.getString("question"),
                            Integer.valueOf(quesDoc.getString("WordLimit"))));
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
            question.setText(questionList.get(i).getQuestion() + " (Length limit: " + String.valueOf(limit)+")");
        }
        else{
            question.setText(questionList.get(i).getQuestion() + " (No length limit)");
        }

        q_count.setText(String.valueOf(i + 1) + "/" + String.valueOf(questionList.size()));


        if(limit > 0){
            user_input.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(limit)
            });
        }
        else{
            user_input.setFilters(new InputFilter[]{});
        }


        survey_FR_Answers.add("null");

        if(survey_FR_Answers.size()>0){//means we have dones this question before
            if(!survey_FR_Answers.get((current_question)).equals("null")){
                user_input.setText(survey_FR_Answers.get(current_question));
            }
            else{
                user_input.getText().clear();
            }
        }



        loading.cancel();

    }



    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.GoNext){
//            questionList.get(current_question).setUser_answer(user_input.getText().toString());
            survey_FR_Answers.set(current_question,user_input.getText().toString());
            changeQuestionForward();
        }


        if(v.getId() == R.id.GoPrev){
//            questionList.get(current_question).setUser_answer(user_input.getText().toString());
            survey_FR_Answers.set(current_question,user_input.getText().toString());
            changeQuestionBackward();
        }

    }


    private void changeQuestionForward(){
        if(current_question >= questionList.size()-1){


//            //upload answers
//            Map<String , Object> Answers = new ArrayMap<>();
//
//            for(int i = 0; i < count; i++){
//                Answers.clear();
//                Answers.put("UserAnswer",String.valueOf(questionList.get(i).getUser_answer()));
//
//                firestore2.collection("tests").document(TEST_IDs.get(NumOfTest)).collection("FRQuestions")
//                        .document(FR_IDs.get(i)).update(Answers);
//
//            }


//            firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey))
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



            firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("FRQuestions")
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

                            //go to next activity
                            Intent intent = new Intent (Survey_FRQuestionActivity.this, Survey_MatchingQuestionActivity.class);
                            intent.putExtra("info",1);
                            startActivity(intent);


                        }
                    }
                }
            });



        }

        else{
            current_question ++;

            setQuestion(current_question);


        }
    }

    private void changeQuestionBackward(){

        if(current_question == 0){
            Intent intent = new Intent(Survey_FRQuestionActivity.this, Survey_MCQuestionActivity.class);
            intent.putExtra("info",2);
            intent.putExtra("NAME",NumOfSurvey);
            startActivity(intent);
        }

        else{
            current_question --;

            setQuestion(current_question);
        }
    }






    private void update(int i){




        firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("FRQuestions")
                .document("question" + String.valueOf(i + 1)).collection("userAnswers").document("answerList")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                answer_count = Integer.valueOf(documentSnapshot.getString("count"));


                Map<String, Object> new_answer = new ArrayMap<>();

                new_answer.put("input", survey_FR_Answers.get(i));

                firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("FRQuestions")
                        .document("question" + String.valueOf(i+1)).collection("userAnswers")
                        .document("answer"+String.valueOf(answer_count + 1)).set(new_answer).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        Map<String, Object> new_answer_count = new ArrayMap<>();
                        new_answer_count.put("count",String.valueOf(answer_count + 1));

                        firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("FRQuestions")
                                .document("question" + String.valueOf( i + 1)).collection("userAnswers").document("answerList")
                                .update(new_answer_count);

                    }
                });



            }
        });

    }

    private void setup(int i){

//        Map<String, Object> Data = new ArrayMap<>();
//
//        Data.put("option1_count", "0");
//        Data.put("option2_count", "0");
//        Data.put("option3_count", "0");
//        Data.put("option4_count", "0");

        Map<String, Object> question_field = new ArrayMap<>();

        question_field.put("name", "question");

        firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("FRQuestions")
                .document("question"+String.valueOf(i + 1)).set(question_field).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                num_worksheet_questions ++;
                Map<String,Object> new_list_count = new ArrayMap<>();

                new_list_count.put("count", String.valueOf(num_worksheet_questions));

                firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("FRQuestions")
                        .document("questionList").update(new_list_count);


                Map<String, Object> answerList = new ArrayMap<>();

                answerList.put("count","0");


                firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("FRQuestions")
                        .document("question"+String.valueOf(i + 1)).collection("userAnswers").document("answerList")
                        .set(answerList).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        update(i);
                    }
                });
            }
        });




    }

}