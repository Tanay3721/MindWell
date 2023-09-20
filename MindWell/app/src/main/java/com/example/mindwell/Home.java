package com.example.mindwell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    CardView quiz;
    TextView con_quiz;
    FirebaseAuth mAuth;
    DatabaseReference db;
    String progress;
    ProgressBar homeProgressBar;
    TextView percentageValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        quiz=findViewById(R.id.home_cardquiz);
        con_quiz=findViewById(R.id.home_continue);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        homeProgressBar = findViewById(R.id.home_progress);
        percentageValue = findViewById(R.id.home_scoreper);

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this,Survey.class);
                startActivity(i);
            }
        });

        con_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this,Survey.class);
                startActivity(i);
            }
        });

        CheckCurrentUser();
    }

    void CheckCurrentUser()
    {
        db.child("Data").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    if(snapshot1.getKey().equals("Score"))
                    {
                        progress = snapshot1.getValue().toString();
                        homeProgressBar.setProgress(Integer.parseInt(progress));
                        percentageValue.setText(progress);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}