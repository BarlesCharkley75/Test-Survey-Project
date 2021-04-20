package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button start;//define buttons first

    public static ArrayList<String> testList = new ArrayList<>();

    private FirebaseFirestore firestore;

    private Dialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connect to the interface
        start = findViewById(R.id.StartButton);
        firestore = FirebaseFirestore.getInstance();

        //a loading interface when fetching data from database
        loading = new Dialog(MainActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();


        loadData();


//        Toast.makeText(MainActivity.this, "Accessing database",Toast.LENGTH_SHORT).show();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//this function helps us click a button and go to next page
                Intent intent = new Intent(MainActivity.this, UserTypeActivity.class);
                startActivity(intent);
            }
        });
    }

    //Firebase stuff, trying to get data from firestore.
    private void loadData(){
        testList.clear();
        firestore.collection("tests").document("testList").
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        long count = (long)doc.get("count");

                        for(int i = 1; i <= count; i ++){
                            String testName = doc.getString("test" + String.valueOf(i)+"_name");
                            testList.add(testName);

                        }
                        loading.cancel();
                        Toast.makeText(MainActivity.this, "Finished fetching data",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        loading.cancel();
                        Toast.makeText(MainActivity.this, "No tests yet",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else{
                    loading.cancel();
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}

