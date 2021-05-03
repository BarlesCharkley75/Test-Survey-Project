package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView image;
    private TextView user_name;
    private Button log_out, keep_going;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    public static ArrayList<String> testList = new ArrayList<>();
    public static ArrayList<String> TEST_IDs;

    public static ArrayList<String> surveyList = new ArrayList<>();
    public static ArrayList<String> SURVEY_IDs = new ArrayList<>();

    private FirebaseFirestore firestore;

    private Dialog loading;

    public static String Current_user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        image = findViewById(R.id.image);
        user_name = findViewById(R.id.user_name);
        log_out = findViewById(R.id.log_out_button);
        keep_going = findViewById(R.id.keep_going);

        firestore = FirebaseFirestore.getInstance();




        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Current_user_name = firebaseUser.getDisplayName();


        if(firebaseUser != null){
            //set image on image view
            Glide.with(UserProfileActivity.this).load(firebaseUser.getPhotoUrl())
                    .into(image);

            //Set name on text view
            user_name.setText(firebaseUser.getDisplayName());
        }

        //Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(UserProfileActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);


        //a loading interface when fetching data from database
        loading = new Dialog(UserProfileActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();

        loadData();

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sign out
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //sign out from firebase
                            firebaseAuth.signOut();
                            Toast.makeText(getApplicationContext(),"Log out successful",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });


        keep_going.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, UserTypeActivity.class);
                startActivity(intent);
            }
        });
    }


    private void loadData(){
        testList.clear();
        TEST_IDs = new ArrayList<>();
        firestore.collection("tests").document("testList").
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        int count = Integer.valueOf(doc.getString("count"));

                        for(int i = 1; i <= count; i ++){
//                            String testName = doc.getString("test" + String.valueOf(i)+"_name");
                            String testName = "test"+String.valueOf(i);
                            testList.add(testName);

                            TEST_IDs.add(doc.getString("test"+String.valueOf(i)+"_id"));

                        }

                        loadData2();
//                        loading.cancel();
//                        Toast.makeText(UserProfileActivity.this, "Finished fetching data",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        loading.cancel();
                        Toast.makeText(UserProfileActivity.this, "No tests yet",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else{
                    loading.cancel();
                    Toast.makeText(UserProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void loadData2(){
        surveyList.clear();
        SURVEY_IDs.clear();
        firestore.collection("surveys").document("surveyList").
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        int count = Integer.valueOf(doc.getString("count"));

                        for(int i = 1; i <= count; i ++){
//                            String testName = doc.getString("test" + String.valueOf(i)+"_name");
                            String testName = "survey"+String.valueOf(i);
                            surveyList.add(testName);

                            SURVEY_IDs.add(doc.getString("survey"+String.valueOf(i)+"_id"));

                        }

                        loading.cancel();
                        Toast.makeText(UserProfileActivity.this, "Finished fetching data",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        loading.cancel();
                        Toast.makeText(UserProfileActivity.this, "No survey yet",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else{
                    loading.cancel();
                    Toast.makeText(UserProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}