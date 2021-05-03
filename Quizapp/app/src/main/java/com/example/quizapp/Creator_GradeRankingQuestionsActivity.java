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

import static com.example.quizapp.Test_WorksheetListActivity.grader_NumOfTest;
import static com.example.quizapp.UserProfileActivity.TEST_IDs;

public class Creator_GradeRankingQuestionsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question, q_count;

    private TextView input1,input2,input3,input4;
    private TextView correct_answer1,correct_answer2,correct_answer3,correct_answer4;

    private TextView points_total;

    private TextInputLayout points_get;


    private Button option1, option2, option3, option4, GoNext, GoPrev;
    private ArrayList<RankingQuestion> questionList = new ArrayList<>();

    private ArrayList<RankingAnswers> AnswerList = new ArrayList<>();

    private int current_question;
    private int info = 0;

    private FirebaseFirestore firestore;

    public static ArrayList<String> grader_RANKING_IDs = new ArrayList<>();
    private Dialog loading;

    private int count;

    public static int RANKING_points_total;
    public static ArrayList<Integer> RANKING_points_get = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__grade_ranking_questions);

        Intent intent = getIntent();
        info = intent.getIntExtra("info",0);

        question = findViewById(R.id.question);
        q_count = findViewById(R.id.question_num);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        GoNext = findViewById(R.id.GoNext);
        GoPrev = findViewById(R.id.GoPrev);

        correct_answer1 = findViewById(R.id.correct_answer1);
        correct_answer2 = findViewById(R.id.correct_answer2);
        correct_answer3 = findViewById(R.id.correct_answer3);
        correct_answer4 = findViewById(R.id.correct_answer4);

        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        input3 = findViewById(R.id.input3);
        input4 = findViewById(R.id.input4);

        points_total = findViewById(R.id.points_total);

        points_get = findViewById(R.id.points_get);


        GoNext.setOnClickListener(this);
        GoPrev.setOnClickListener(this);

        firestore = FirebaseFirestore.getInstance();

        loading = new Dialog(Creator_GradeRankingQuestionsActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();





        getQuestionList();
    }

    private void getQuestionList(){

//        questionList.clear();

        grader_RANKING_IDs.clear();

        firestore.collection("tests").document(TEST_IDs.get(grader_NumOfTest)).collection("RankingQuestions")
                .document("questionList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        count = Integer.valueOf(doc.getString("count"));



                        for(int i = 1; i <= count; i++){
                            grader_RANKING_IDs.add(doc.getString("question"+String.valueOf(i)+"_id"));
                        }

                        if(count == 0){
                            //no ranking question, go to submission page.

                            Intent intent = new Intent(Creator_GradeRankingQuestionsActivity.this, FinalScoreActivity.class);
                            intent.putExtra("info",1);

                            startActivity(intent);

                        }
                        else{
                            getAnswerList();

                        }


                    }
                }
            }
        });





    }


    private void getAnswerList(){

        AnswerList.clear();

        firestore.collection("TestWorksheet").document(TEST_IDs.get(grader_NumOfTest)).collection("userWorksheets")
                .document("worksheet" + String.valueOf(grader_NumOfWorksheet + 1)).collection("RankingQuestions")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Map<String, QueryDocumentSnapshot> docList2 = new ArrayMap<>();

                for(QueryDocumentSnapshot doc2 : queryDocumentSnapshots){
                    docList2.put(doc2.getId(),doc2);
                }

                for(int i = 0; i < count; i ++){
                    QueryDocumentSnapshot quesDoc2 = docList2.get("question"+String.valueOf(i + 1));
                    AnswerList.add(new RankingAnswers(quesDoc2.getString("user_input1"),
                            quesDoc2.getString("user_input2"),
                            quesDoc2.getString("user_input3"),
                            quesDoc2.getString("user_input4")));
                }

                setQuestionList();

            }
        });

    }

    private void setQuestionList(){
        firestore.collection("tests").document(TEST_IDs.get(grader_NumOfTest)).collection("RankingQuestions")
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

                    QueryDocumentSnapshot quesDoc = docList.get(grader_RANKING_IDs.get(i));

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

        correct_answer1.setText("Correct answer 1:" + questionList.get(current_question).getAns_1());
        correct_answer2.setText("Correct answer 2:" + questionList.get(current_question).getAns_2());
        correct_answer3.setText("Correct answer 3:" + questionList.get(current_question).getAns_3());
        correct_answer4.setText("Correct answer 4:" + questionList.get(current_question).getAns_4());


        points_total.setText("/" + String.valueOf(questionList.get(current_question).getPoints()));

        option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));





        RANKING_points_get.add(-1);

        if(RANKING_points_get.get(current_question) != -1){
            points_get.getEditText().setText(String.valueOf(RANKING_points_get.get(current_question)));

        }
        else{
            points_get.getEditText().getText().clear();
        }


        if(!AnswerList.get(current_question).getUser_input1().equals("null")){
            input1.setText(AnswerList.get(current_question).getUser_input1());
        }
        if(!AnswerList.get(current_question).getUser_input2().equals("null")){
            input2.setText(AnswerList.get(current_question).getUser_input2());
        }
        if(!AnswerList.get(current_question).getUser_input3().equals("null")){
            input3.setText(AnswerList.get(current_question).getUser_input3());
        }
        if(!AnswerList.get(current_question).getUser_input4().equals("null")){
            input4.setText(AnswerList.get(current_question).getUser_input4());
        }


        RANKING_points_total = 0;

        for(int j = 0; j<questionList.size();j++){
            RANKING_points_total += questionList.get(j).getPoints();
        }

        loading.cancel();

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.GoNext){


            if(points_get.getEditText().getText().toString().equals("")){
                RANKING_points_get.set(current_question,0);
            }
            else{
                RANKING_points_get.set(current_question,Integer.valueOf(points_get.getEditText().getText().toString()));
            }


            changeQuestionForward();
        }
        else if (v.getId() == R.id.GoPrev){

            if(points_get.getEditText().getText().toString().equals("")){
                RANKING_points_get.set(current_question,0);
            }
            else{
                RANKING_points_get.set(current_question,Integer.valueOf(points_get.getEditText().getText().toString()));
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

//             go to free response activity
            Intent intent = new Intent(Creator_GradeRankingQuestionsActivity.this, FinalScoreActivity.class);
            intent.putExtra("info",1);
            startActivity(intent);
        }
    }

    private void changeQuestionBackward(){

        if(current_question == 0){
            Intent intent = new Intent(Creator_GradeRankingQuestionsActivity.this, Creator_GradeMatchingQuestionsActivity.class);
            intent.putExtra("info",2);

            startActivity(intent);
        }

        else{
            current_question --;

            setQuestion(current_question);
        }
    }
}