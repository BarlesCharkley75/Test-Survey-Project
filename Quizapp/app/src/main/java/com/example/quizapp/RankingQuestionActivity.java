package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.quizapp.MCQuestionActivity2.NumOfTest;

public class RankingQuestionActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView question, q_count;

    private Button option1, option2, option3, option4, GoNext, GoPrev;

    private EditText input1, input2, input3, input4;

    private ArrayList<RankingQuestion> questionList;

    private int current_question;
    private int info = 0;

    private FirebaseFirestore firestore;


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

        getQuestionList();
    }

    private void getQuestionList(){
        questionList = new ArrayList<RankingQuestion>();

//        questionList.add(new RankingQuestion("biggest to smallest", "football","basketball","tennis ball","ping pong ball",
//                "null","null","null","null","2","1","3","4"));
//
//        questionList.add(new RankingQuestion("Most NBA championship", "Bill Russell","Michael Jordan","LeBron James","Kobe Bryant",
//                "null","null","null","null","1","2","4","3"));

        firestore.collection("tests").document("test" + String.valueOf(NumOfTest)).collection("RankingQuestions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot questions = task.getResult();

                            for(QueryDocumentSnapshot doc : questions){
                                questionList.add(new RankingQuestion(
                                        doc.getString("question"),
                                        doc.getString("option1"),
                                        doc.getString("option2"),
                                        doc.getString("option3"),
                                        doc.getString("option4"),
                                        doc.getString("user_input1"),
                                        doc.getString("user_input2"),
                                        doc.getString("user_input3"),
                                        doc.getString("user_input4"),
                                        doc.getString("Answer1"),
                                        doc.getString("Answer2"),
                                        doc.getString("Answer3"),
                                        doc.getString("Answer4")));
                            }

                            pass();
//                            Toast.makeText(FRQuestionActivity.this, "Finished fetching data",Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(RankingQuestionActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
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

        if(!questionList.get(current_question).getUser_input1().equals("null")){
            input1.setText(questionList.get(current_question).getUser_input1());
        }
        if(!questionList.get(current_question).getUser_input2().equals("null")){
            input2.setText(questionList.get(current_question).getUser_input2());
        }
        if(!questionList.get(current_question).getUser_input3().equals("null")){
            input3.setText(questionList.get(current_question).getUser_input3());
        }
        if(!questionList.get(current_question).getUser_input4().equals("null")){
            input4.setText(questionList.get(current_question).getUser_input4());
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.GoNext){
            questionList.get(current_question).setUser_input1(input1.getText().toString());
            questionList.get(current_question).setUser_input2(input2.getText().toString());
            questionList.get(current_question).setUser_input3(input3.getText().toString());
            questionList.get(current_question).setUser_input4(input4.getText().toString());
            changeQuestionForward();
        }
        else if (v.getId() == R.id.GoPrev){
            questionList.get(current_question).setUser_input1(input1.getText().toString());
            questionList.get(current_question).setUser_input2(input2.getText().toString());
            questionList.get(current_question).setUser_input3(input3.getText().toString());
            questionList.get(current_question).setUser_input4(input4.getText().toString());
            changeQuestionBackward();
        }

    }

    private void changeQuestionForward(){


        if(current_question < questionList.size() - 1){
            current_question ++;
            setQuestion(current_question);

        }

        else{
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
}