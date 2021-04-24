package com.example.quizapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import static com.example.quizapp.Creator_CreateQuestionsActivity.CurrentNumOfTest;
import static com.example.quizapp.Creator_MCQuestionsListActivity.MC_id_list;
import static com.example.quizapp.Creator_TestListActivity.test_id_list;

public class Creator_MCQuestionsListAdapter extends BaseAdapter {

    private ArrayList<String> questionList;

    private int NumOfTest;

    private FirebaseFirestore firestore;

    private String id;

    private int count ,next;



    public Creator_MCQuestionsListAdapter(ArrayList<String> questionList) {
        this.questionList = questionList;

    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        firestore = FirebaseFirestore.getInstance();

        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent,false);
        }
        else{
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( parent.getContext(), Creator_CreateMCQuestionActivity.class);
//                intent.putExtra("info",1);
                intent.putExtra("QuestionNAME", position + 1);
//                intent.putExtra("TestNAME", NumOfTest);

                parent.getContext().startActivity(intent);

            }
        });




        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(parent.getContext())
                        .setTitle("Delete question")
                        .setMessage("Are you sure you want to delete question " +String.valueOf(position + 1) + "?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                firestore.collection("tests").document(test_id_list.get(CurrentNumOfTest))
                                        .collection("MCQuestions").document("questionList")
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot doc = task.getResult();
                                            if(doc.exists()){
                                                id = doc.getString("question" + String.valueOf(position + 1)+"_id");
                                                count = Integer.valueOf(doc.getString("count"));
                                                next = Integer.valueOf(doc.getString("NEXT"));

                                                firestore.collection("tests").document(test_id_list.get(CurrentNumOfTest))
                                                        .collection("MCQuestions").document(id)
                                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Map<String,Object> quesDoc = new ArrayMap<>();
                                                        int index = 1;
                                                        for( int i = 0; i < count; i++){
                                                            if(i != position ){
                                                                quesDoc.put("question"+String.valueOf(index)+"_id",MC_id_list.get(i));
                                                                index ++;
                                                            }
                                                        }

                                                        quesDoc.put("count",String.valueOf(count - 1));
                                                        quesDoc.put("NEXT",String.valueOf(next));





                                                        firestore.collection("tests").document(test_id_list.get(CurrentNumOfTest))
                                                                .collection("MCQuestions").document("questionList")
                                                                .set(quesDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Intent intent = new Intent (parent.getContext(), Creator_MCQuestionsListActivity.class);
                                                                parent.getContext().startActivity(intent);
                                                            }
                                                        });




                                                    }
                                                });

                                            }
                                        }
                                    }
                                });


                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();



                alertDialog.show();
                return true;
            }
        });




        ((TextView)view.findViewById(R.id.list_num_textview)).setText(questionList.get(position));
        return view;
    }


}
