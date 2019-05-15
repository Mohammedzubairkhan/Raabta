package com.example.mzk.raabta;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import static android.icu.util.Calendar.getInstance;

public class MainActivity extends AppCompatActivity {
private DatabaseReference mdatabase;
private EditText message;
private RecyclerView recycler;
private FirebaseAuth.AuthStateListener auto;
private FirebaseAuth mAuth;
private FirebaseUser mcurrentuser;
private DatabaseReference musersdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = (EditText)findViewById(R.id.type);
       mdatabase = FirebaseDatabase.getInstance().getReference().child("Messages");
       recycler = (RecyclerView)findViewById(R.id.recyclerview);
       mAuth = FirebaseAuth.getInstance();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recycler.setLayoutManager(manager);
        auto = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mAuth.getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,Register.class));

                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Messages,MessageViewHolder> FDRA = new FirebaseRecyclerAdapter<Messages, MessageViewHolder>(Messages.class,R.layout.message_layout,MessageViewHolder.class,mdatabase) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Messages model, int position) {
                viewHolder.setContent(model.getContent());
                viewHolder.setusername(model.getUsername());
                viewHolder.setime(model.getTime());
            }
        };
        recycler.setAdapter(FDRA);
    }

    public void sendClick(View view) {
        final String msg2 = message.getText().toString().trim();
        mcurrentuser = mAuth.getCurrentUser();
        musersdb = FirebaseDatabase.getInstance().getReference().child("Users").child(mcurrentuser.getUid());
        if(!TextUtils.isEmpty(msg2)){
           final DatabaseReference mpost = mdatabase.push();
           musersdb.addValueEventListener(new ValueEventListener() {
               @RequiresApi(api = Build.VERSION_CODES.N)
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   mpost.child("Content").setValue(msg2);
                   mpost.child("time").setValue(timereturn());
                   mpost.child("username").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           Toast.makeText(MainActivity.this,"sent",Toast.LENGTH_LONG).show();
                       }
                   });
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });

        }

        recycler.smoothScrollToPosition(recycler.getAdapter().getItemCount());
        message.getText().clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String timereturn(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        String nam = " "+simpleDateFormat.format(cal.getTime());
       return nam;
    }
    public  static class MessageViewHolder extends RecyclerView.ViewHolder{
      View view;
        public MessageViewHolder(View itemView) {
            super(itemView);
        view=itemView;
        }
    public void setContent(String con){
        TextView messagesd = (TextView)view.findViewById(R.id.msg);
        messagesd.setText(con);
    }
    public void setusername(String nam){
        TextView user = (TextView)view.findViewById(R.id.name);
        user.setText(nam);
        }

        public void setime(String nam){

            TextView user = (TextView)view.findViewById(R.id.time);

            user.setText(nam);
        }
    }

}
