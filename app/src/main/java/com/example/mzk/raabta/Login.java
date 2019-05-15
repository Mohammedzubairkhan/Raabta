package com.example.mzk.raabta;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    DatabaseReference mdatabasse;
    FirebaseAuth mAuth;
    EditText email,password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.emaill);

        password = (EditText)findViewById(R.id.passwordl);

        login = (Button) findViewById(R.id.loginl);
        mAuth = FirebaseAuth.getInstance();
        mdatabasse = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    public void loginclickedd(View view) {
        final String emails,passs;
        emails = email.getText().toString().trim();
        passs = password.getText().toString().trim();

        if(!TextUtils.isEmpty(emails) && !TextUtils.isEmpty(passs)){
            mAuth.signInWithEmailAndPassword(emails,passs).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        checkUserExists();
                    }
                }
            });
        }
    }

    private void checkUserExists() {
    final String userid = mAuth.getCurrentUser().getUid();
    mdatabasse.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.hasChild(userid)){
                startActivity(new Intent(Login.this,MainActivity.class));
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
    }

}
