package com.example.quizapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import static com.example.quizapp.Creator_CreateQuestionsActivity.CurrentNumOfTest;
import static com.example.quizapp.Creator_MCQuestionsListActivity.MC_id_list;
import static com.example.quizapp.Creator_TestListActivity.test_id_list;

public class Creator_TestListAdapter extends BaseAdapter {


    private ArrayList<String> testList;

    private String id;
    private int count, next;
    private FirebaseFirestore firestore;

    public Creator_TestListAdapter(ArrayList<String> testList) {
        this.testList = testList;
    }


    @Override
    public int getCount() {
        return testList.size();
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
                Intent intent = new Intent( parent.getContext(), Creator_CreateQuestionsActivity.class);
//                intent.putExtra("info",1);
                intent.putExtra("NAME", position);
                parent.getContext().startActivity(intent);


            }
        });


        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(parent.getContext())
                        .setTitle("Delete test")
                        .setMessage("Are you sure you want to delete test " +String.valueOf(position + 1) + "?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                firestore.collection("tests").document("testList")
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot doc = task.getResult();
                                            if(doc.exists()){
                                                id = doc.getString("test" + String.valueOf(position + 1)+"_id");
                                                count = Integer.valueOf(doc.getString("count"));
                                                next = Integer.valueOf(doc.getString("NEXT"));

                                                firestore.collection("tests").document(id)
                                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Map<String,Object> quesDoc = new ArrayMap<>();
                                                        int index = 1;
                                                        for( int i = 0; i < count; i++){
                                                            if(i != position ){
                                                                quesDoc.put("test"+String.valueOf(index)+"_id",test_id_list.get(i));
                                                                index ++;
                                                            }
                                                        }

                                                        quesDoc.put("count",String.valueOf(count - 1));
                                                        quesDoc.put("NEXT",String.valueOf(next));


                                                        firestore.collection("tests").document("testList")
                                                                .set(quesDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Intent intent = new Intent (parent.getContext(), Creator_TestListActivity.class);
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

        ((TextView)view.findViewById(R.id.list_num_textview)).setText(testList.get(position));
        return view;
    }
}
