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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
private DatabaseReference mdatabasse;
private FirebaseAuth mAuth;
EditText username,email,password;
Button signup,login;
FirebaseAuth.AuthStateListener auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = (EditText)findViewById(R.id.emaile);
        username = (EditText)findViewById(R.id.usernamee);
password = (EditText)findViewById(R.id.passworde);
signup = (Button) findViewById(R.id.signupe);
login = (Button) findViewById(R.id.logine);
mAuth = FirebaseAuth.getInstance();
mdatabasse = FirebaseDatabase.getInstance().getReference().child("Users");
auto = new FirebaseAuth.AuthStateListener() {
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(Register.this,MainActivity.class));

        }
    }
};

    }

    public void sigupclicked(View view) {
        final String emails,passs,names;
        emails = email.getText().toString().trim();
        passs = password.getText().toString().trim();
        names = username.getText().toString().trim();
        if(!TextUtils.isEmpty(emails) && !TextUtils.isEmpty(passs) && !TextUtils.isEmpty(names)){
            mAuth.createUserWithEmailAndPassword(emails,passs).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String userid = mAuth.getCurrentUser().getUid();
                        final DatabaseReference db = mdatabasse.child(userid);
                        db.child("Name").setValue(names);
                        startActivity(new Intent(Register.this,Login.class));
                    }
                }
            });
        }
    }


    public void loginclicked(View view) {
        startActivity(new Intent(Register.this,Login.class));
    }
}
