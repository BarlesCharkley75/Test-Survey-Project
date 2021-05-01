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

public class RankingQuestionActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView question, q_count;

    private Button option1, option2, option3, option4, GoNext, GoPrev;

    private EditText input1, input2, input3, input4;

    private ArrayList<RankingQuestion> questionList;

    private int current_question;
    private int info = 0;

    private FirebaseFirestore firestore;

    public static ArrayList<String> RANKING_IDs;
    private Dialog loading;

    private int count;

    public static ArrayList<RankingAnswers> RANKING_Answers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_question);

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

        RANKING_IDs = new ArrayList<>();
        questionList = new ArrayList<>();


        loading = new Dialog(RankingQuestionActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();

        getQuestionList();
    }

    private void getQuestionList(){

//        questionList.clear();

        RANKING_IDs.clear();

        firestore.collection("tests").document(TEST_IDs.get(NumOfTest)).collection("RankingQuestions")
                .document("questionList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        count = Integer.valueOf(doc.getString("count"));



                        for(int i = 1; i <= count; i++){
                            RANKING_IDs.add(doc.getString("question"+String.valueOf(i)+"_id"));
                        }

                        if(count == 0){
                            //no ranking question, go to submission page.

                            Intent intent = new Intent(RankingQuestionActivity.this, SubmissionActivity.class);
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
        firestore.collection("tests").document(TEST_IDs.get(NumOfTest)).collection("RankingQuestions")
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

                    QueryDocumentSnapshot quesDoc = docList.get(RANKING_IDs.get(i));

                    questionList.add(new RankingQuestion(
                            quesDoc.getString("question"),
                            quesDoc.getString("option1"),
                            quesDoc.getString("option2"),
                            quesDoc.getString("option3"),
                            quesDoc.getString("option4"),
                            quesDoc.getString("Answer1"),
                            quesDoc.getString("Answer2"),
                            quesDoc.getString("Answer3"),
                            quesDoc.getString("Answer4"),
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

        RANKING_Answers.add(new RankingAnswers("null","null","null","null"));

        if(RANKING_Answers.size()>0){//that means we have done the problems before

            if(!RANKING_Answers.get(current_question).getUser_input1().equals("null")){
                input1.setText(RANKING_Answers.get(current_question).getUser_input1());
            }
            if(!RANKING_Answers.get(current_question).getUser_input2().equals("null")){
                input2.setText(RANKING_Answers.get(current_question).getUser_input2());
            }
            if(!RANKING_Answers.get(current_question).getUser_input3().equals("null")){
                input3.setText(RANKING_Answers.get(current_question).getUser_input3());
            }
            if(!RANKING_Answers.get(current_question).getUser_input4().equals("null")){
                input4.setText(RANKING_Answers.get(current_question).getUser_input4());
            }
        }



        loading.cancel();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.GoNext){
            RANKING_Answers.get(current_question).setUser_input1(input1.getText().toString());
            RANKING_Answers.get(current_question).setUser_input2(input2.getText().toString());
            RANKING_Answers.get(current_question).setUser_input3(input3.getText().toString());
            RANKING_Answers.get(current_question).setUser_input4(input4.getText().toString());


            changeQuestionForward();
        }
        else if (v.getId() == R.id.GoPrev){
            RANKING_Answers.get(current_question).setUser_input1(input1.getText().toString());
            RANKING_Answers.get(current_question).setUser_input2(input2.getText().toString());
            RANKING_Answers.get(current_question).setUser_input3(input3.getText().toString());
            RANKING_Answers.get(current_question).setUser_input4(input4.getText().toString());

            changeQuestionBackward();
        }

    }

    private void changeQuestionForward(){


        if(current_question < questionList.size() - 1){
            current_question ++;
            setQuestion(current_question);

        }

        else{
            //upload answers
//            Map<String , Object> Answers = new ArrayMap<>();
//
//            for(int i = 0; i < count; i++){
//                Answers.clear();
//                Answers.put("user_input1",String.valueOf(questionList.get(i).getUser_input1()));
//                Answers.put("user_input2",String.valueOf(questionList.get(i).getUser_input2()));
//                Answers.put("user_input3",String.valueOf(questionList.get(i).getUser_input3()));
//                Answers.put("user_input4",String.valueOf(questionList.get(i).getUser_input4()));
//
//                firestore.collection("tests").document(TEST_IDs.get(NumOfTest)).collection("RankingQuestions")
//                        .document(RANKING_IDs.get(i)).update(Answers);
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



            //go to submission
            Intent intent = new Intent(RankingQuestionActivity.this, SubmissionActivity.class);
            intent.putExtra("info",1);

            startActivity(intent);
        }
    }

    private void changeQuestionBackward(){
        if(current_question==0){
            Intent intent = new Intent(RankingQuestionActivity.this, MatchingQurestionActivity.class);
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
            Answers.put("user_input1",RANKING_Answers.get(current_question).getUser_input1());
            Answers.put("user_input2",RANKING_Answers.get(current_question).getUser_input2());
            Answers.put("user_input3",RANKING_Answers.get(current_question).getUser_input3());
            Answers.put("user_input4",RANKING_Answers.get(current_question).getUser_input4());

//                firestore.collection("tests").document(TEST_IDs.get(NumOfTest)).collection("MCQuestions")
//                        .document(MC_IDs.get(i)).update(Answers);

            firestore.collection("TestWorksheet").document(TEST_IDs.get(NumOfTest))
                    .collection("userWorksheets").document("worksheet"+String.valueOf(worksheet_next))
                    .collection("RankingQuestions").document("question"+String.valueOf(i + 1))
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
                    Answers.put("user_input1",RANKING_Answers.get(current_question).getUser_input1());
                    Answers.put("user_input2",RANKING_Answers.get(current_question).getUser_input2());
                    Answers.put("user_input3",RANKING_Answers.get(current_question).getUser_input3());
                    Answers.put("user_input4",RANKING_Answers.get(current_question).getUser_input4());

//                firestore.collection("tests").document(TEST_IDs.get(NumOfTest)).collection("MCQuestions")
//                        .document(MC_IDs.get(i)).update(Answers);

                    firestore.collection("TestWorksheet").document(TEST_IDs.get(NumOfTest))
                            .collection("userWorksheets").document("worksheet"+String.valueOf(NumOfWorksheet + 1))
                            .collection("RankingQuestions").document("question"+String.valueOf(j + 1))
                            .set(Answers);

                }
            }


        }

    }
}