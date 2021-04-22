package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    
    public static ArrayList<String> testList = new ArrayList<>();

    private FirebaseFirestore firestore;

    private Dialog loading;

    private SignInButton sign_in_button;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connect to the interface
//        start = findViewById(R.id.StartButton);
        firestore = FirebaseFirestore.getInstance();

        sign_in_button = findViewById(R.id.sign_in_button);

        //a loading interface when fetching data from database
        loading = new Dialog(MainActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();


        loadData();


        //Initialize sign in options
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("333298613809-fp86udebtmgo0jmhteue717tfngli46t.apps.googleusercontent.com")
                .requestEmail().build();


        //Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, googleSignInOptions);


        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize sign in intent
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent,100);
            }
        });

        //Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            //directly go to user profile activity
            startActivity(new Intent(MainActivity.this, UserProfileActivity.class)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }





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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            if(signInAccountTask.isSuccessful()){
                String s  = "sign in successful";
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();

                //Initialize sign in account
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    if(googleSignInAccount != null){
                        //Initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);

                        //check credential
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    startActivity(new Intent(MainActivity.this, UserProfileActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


                                }
                            }
                        });

                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

