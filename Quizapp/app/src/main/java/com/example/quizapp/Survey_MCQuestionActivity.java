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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static com.example.quizapp.UserProfileActivity.Current_user_name;
import static com.example.quizapp.UserProfileActivity.SURVEY_IDs;
import static com.example.quizapp.UserProfileActivity.TEST_IDs;

public class Survey_MCQuestionActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView question;
    private TextView q_count;

    private Button option1, option2, option3, option4, GoNext, GoPrev;

    private ArrayList<Survey_MCQuestion> questionList = new ArrayList<>();

    private int current_question;

    private int info = 0;

    private FirebaseFirestore firestore;

    public static int NumOfSurvey;

//    private int questions_needed = 0;

    public static ArrayList<String> survey_MC_IDs = new ArrayList<>();

    private Dialog loading;

    private int count;

    public static ArrayList<Integer> survey_MC_selected = new ArrayList<>();



    private int num_worksheet_questions;

    private int option1_count,option2_count,option3_count,option4_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey__m_c_question);

        Intent intent = getIntent();
        info = intent.getIntExtra("info",0);

        int temp = intent.getIntExtra("NAME",-1);

        if(temp != -1){
            NumOfSurvey = temp;
        }

        question = findViewById(R.id.question);
        q_count = findViewById(R.id.question_num);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        GoNext = findViewById(R.id.GoNext);
        GoPrev = findViewById(R.id.GoPrev);


        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);
        GoNext.setOnClickListener(this);
        GoPrev.setOnClickListener(this);

        firestore = FirebaseFirestore.getInstance();

        loading = new Dialog(Survey_MCQuestionActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();

        getQuestionList();


    }

    private void getQuestionList(){
//        questionList.clear();


        survey_MC_IDs.clear();


        firestore.collection("surveys").document(SURVEY_IDs.get(NumOfSurvey)).collection("MCQuestions")
                .document("questionList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        count = Integer.valueOf(doc.getString("count"));

                        for(int i = 1; i <= count; i++){
                            survey_MC_IDs.add(doc.getString("question"+String.valueOf(i)+"_id"));
                        }

                        if(count == 0){
                            //if there is no MC questions, go to FR questions activity.
                            Intent intent = new Intent(Survey_MCQuestionActivity.this, Survey_FRQuestionActivity.class);
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
        firestore.collection("surveys").document(SURVEY_IDs.get(NumOfSurvey)).collection("MCQuestions")
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

                    QueryDocumentSnapshot quesDoc = docList.get(survey_MC_IDs.get(i));

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

    //At the beginning, set up all the options with light grey color, but if user has already selected a answer, remember that answer
    // and set it to green.

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



        survey_MC_selected.add(0);


        if(survey_MC_selected.size() > 0){//means we have done this question before.
            if(survey_MC_selected.get(current_question) == 1){
                option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            }
            else if(survey_MC_selected.get(current_question)== 2){
                option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            }
            else if(survey_MC_selected.get(current_question) == 3){
                option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            }
            else if (survey_MC_selected.get(current_question) == 4){
                option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            }
        }



        loading.cancel();


    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.option1){
//            questionList.get(current_question).setSelectedAnswer(1);
            survey_MC_selected.set(current_question,1);

            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        }
        else if (v.getId() == R.id.option2){
//            questionList.get(current_question).setSelectedAnswer(2);
            survey_MC_selected.set(current_question,2);

            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        }
        else if(v.getId() == R.id.option3){
//            questionList.get(current_question).setSelectedAnswer(3);
            survey_MC_selected.set(current_question,3);

            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        }
        else if(v.getId() == R.id.option4){
//            questionList.get(current_question).setSelectedAnswer(4);
            survey_MC_selected.set(current_question,4);

            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
        }


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


            firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MCQuestions")
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






            // go to free response activity
            Intent intent = new Intent(Survey_MCQuestionActivity.this, Survey_FRQuestionActivity.class);
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




    private void update(int i){

        int finalI = i;
        firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MCQuestions")
                .document("question"+String.valueOf(i + 1)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                option1_count = Integer.valueOf(documentSnapshot.getString("option1_count"));
                option2_count = Integer.valueOf(documentSnapshot.getString("option2_count"));
                option3_count = Integer.valueOf(documentSnapshot.getString("option3_count"));
                option4_count = Integer.valueOf(documentSnapshot.getString("option4_count"));

                if(survey_MC_selected.get(finalI) == 1){
                    option1_count ++ ;
                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("option1_count", String.valueOf(option1_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MCQuestions")
                            .document("question"+String.valueOf(finalI + 1)).update(count_update);
                }

                else if(survey_MC_selected.get(finalI) == 2){
                    option2_count ++ ;
                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("option2_count", String.valueOf(option2_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MCQuestions")
                            .document("question"+String.valueOf(finalI + 1)).update(count_update);

                }
                else if(survey_MC_selected.get(finalI) == 3){
                    option3_count ++ ;
                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("option3_count", String.valueOf(option3_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MCQuestions")
                            .document("question"+String.valueOf(finalI + 1)).update(count_update);

                }

                else if(survey_MC_selected.get(finalI) == 4){
                    option4_count ++ ;
                    Map<String, Object> count_update = new ArrayMap<>();

                    count_update.put("option4_count", String.valueOf(option4_count));

                    firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MCQuestions")
                            .document("question"+String.valueOf(finalI + 1)).update(count_update);

                }

            }
        });

    }

    private void setup(int i){

        Map<String, Object> Data = new ArrayMap<>();

        Data.put("option1_count", "0");
        Data.put("option2_count", "0");
        Data.put("option3_count", "0");
        Data.put("option4_count", "0");


        firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MCQuestions")
                .document("question"+String.valueOf(i + 1)).set(Data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                num_worksheet_questions ++;

                Map<String,Object> new_list_count = new ArrayMap<>();

                new_list_count.put("count", String.valueOf(num_worksheet_questions));

                firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(NumOfSurvey)).collection("MCQuestions")
                        .document("questionList").update(new_list_count);

                update(i);
            }
        });



    }



}