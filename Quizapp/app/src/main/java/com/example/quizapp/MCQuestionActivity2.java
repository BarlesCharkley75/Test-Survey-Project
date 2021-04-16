package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
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
import java.util.jar.Attributes;

//MC Questions is used as a base reference for all types of questions.

public class MCQuestionActivity2 extends AppCompatActivity implements View.OnClickListener{

    private TextView question;
    private TextView q_count;

    private Button option1, option2, option3, option4, GoNext, GoPrev;

    private ArrayList<MCQuestion> questionList;

    private int current_question;

    private int info = 0;

    private FirebaseFirestore firestore;

    public static int NumOfTest;

    private int questions_needed = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_c_question2);


        Intent intent = getIntent();
        info = intent.getIntExtra("info",0);

        NumOfTest = intent.getIntExtra("NAME",0) + 1;

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

        questionList = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();


        getQuestionList();
    }

    private void getQuestionList(){
        questionList.clear();

        firestore.collection("tests").document("test" + String.valueOf(NumOfTest)).collection("MCQuestions")
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

                    questionList.add(new MCQuestion(quesDoc.getString("question"),
                            quesDoc.getString("option1"),
                            quesDoc.getString("option2"),
                            quesDoc.getString("option3"),
                            quesDoc.getString("option4"),
                            Integer.valueOf(quesDoc.getString("CorrectAnswer")),
                            Integer.valueOf(quesDoc.getString("SelectedAnswer"))));
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

        if(questionList.get(current_question).getSelectedAnswer() ==1){
            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
        }
        else if(questionList.get(current_question).getSelectedAnswer() ==2){
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
        }
        else if(questionList.get(current_question).getSelectedAnswer() ==3){
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
        }
        else if (questionList.get(current_question).getSelectedAnswer() ==4){
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
        }


    }

    //Change the color of the clicked option
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.option1){
            questionList.get(current_question).setSelectedAnswer(1);

            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        }
        else if (v.getId() == R.id.option2){
            questionList.get(current_question).setSelectedAnswer(2);

            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        }
        else if(v.getId() == R.id.option3){
            questionList.get(current_question).setSelectedAnswer(3);

            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        }
        else if(v.getId() == R.id.option4){
            questionList.get(current_question).setSelectedAnswer(4);

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
            // go to free response activity
            Intent intent = new Intent(MCQuestionActivity2.this, FRQuestionActivity.class);
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
}