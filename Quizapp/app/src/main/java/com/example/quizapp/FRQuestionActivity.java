package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import static com.example.quizapp.MCQuestionActivity2.NumOfTest;


public class FRQuestionActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView question;
    private TextView q_count;

    private EditText user_input;

    private ArrayList<FRQuestion> questionList;

    private int current_question;

    private Button GoNext,GoPrev;

    private int info = 0;

    private FirebaseFirestore firestore;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_r_question);

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

        getQuestionList();
    }


    private void getQuestionList(){
        questionList = new ArrayList<FRQuestion>();

        firestore.collection("tests").document("test" + String.valueOf(NumOfTest)).collection("FRQuestions")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    docList.put(doc.getId(),doc);
                }
                QueryDocumentSnapshot quesListDoc = docList.get("questionList");
                String count = quesListDoc.getString("count");

                for(int i = 1 ; i<= Integer.valueOf(count); i++){
//                    String quesName = "question" + String.valueOf(i);

                    QueryDocumentSnapshot quesDoc = docList.get("question" + String.valueOf(i));

                    questionList.add(new FRQuestion(quesDoc.getString("question"),
                            quesDoc.getString("UserAnswer"),
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

        if(!questionList.get(i).getUser_answer().equals("null")){
            user_input.setText(questionList.get(i).getUser_answer());
        }
        else{
            user_input.getText().clear();
        }

    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.GoNext){
            questionList.get(current_question).setUser_answer(user_input.getText().toString());
            changeQuestionForward();
        }


        if(v.getId() == R.id.GoPrev){
            questionList.get(current_question).setUser_answer(user_input.getText().toString());
            changeQuestionBackward();
        }

    }

    private void changeQuestionForward(){
        if(current_question >= questionList.size()-1){
            Intent intent = new Intent (FRQuestionActivity.this, MatchingQurestionActivity.class);
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
            Intent intent = new Intent(FRQuestionActivity.this, MCQuestionActivity2.class);
            intent.putExtra("info",2);
            startActivity(intent);
        }

        else{
            current_question --;

            setQuestion(current_question);
        }
    }

}