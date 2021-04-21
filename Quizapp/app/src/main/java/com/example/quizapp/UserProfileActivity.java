package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class UserProfileActivity extends AppCompatActivity {

    private ImageView image;
    private TextView user_name;
    private Button log_out, keep_going;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        image = findViewById(R.id.image);
        user_name = findViewById(R.id.user_name);
        log_out = findViewById(R.id.log_out_button);
        keep_going = findViewById(R.id.keep_going);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        

        if(firebaseUser != null){
            //set image on image view
            Glide.with(UserProfileActivity.this).load(firebaseUser.getPhotoUrl())
                    .into(image);

            //Set name on text view
            user_name.setText(firebaseUser.getDisplayName());
        }

        //Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(UserProfileActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);

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
}