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

    CardView quiz,scoreCardView;
    TextView con_quiz,previousButton;
    FirebaseAuth mAuth;
    DatabaseReference db;
    String progress;
    ProgressBar homeProgressBar;
    TextView percentageValue;
    boolean userFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        quiz=findViewById(R.id.home_cardquiz);
        scoreCardView = findViewById(R.id.home_card2);
        con_quiz=findViewById(R.id.home_continue);
        previousButton = findViewById(R.id.home_prevrecd);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        homeProgressBar = findViewById(R.id.home_progress);
        percentageValue = findViewById(R.id.home_scoreper);

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this,Survey.class);
                startActivity(i);
                finish();
            }
        });

        con_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this,Survey.class);
                startActivity(i);
                finish();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,PScoreDisplay.class);
                startActivity(i);
                finish();
            }
        });

        if(userFirstTime)
        {
            scoreCardView.setVisibility(View.GONE);
            con_quiz.setVisibility(View.VISIBLE);
        }
        else
        {
            scoreCardView.setVisibility(View.VISIBLE);
            con_quiz.setVisibility(View.GONE);
        }

        CheckCurrentUser();
    }

    void CheckCurrentUser()
    {
        db.child("User").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    if(snapshot1.getKey().equals("Score"))
                    {
                        userFirstTime = false;
                        progress = snapshot1.getValue().toString();
                        homeProgressBar.setProgress(Integer.parseInt(progress));
                        percentageValue.setText(progress);
                        if(userFirstTime)
                        {
                            scoreCardView.setVisibility(View.GONE);
                            con_quiz.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            scoreCardView.setVisibility(View.VISIBLE);
                            con_quiz.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}