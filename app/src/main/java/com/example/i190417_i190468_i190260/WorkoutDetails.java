package com.example.i190417_i190468_i190260;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.i190417_i190468_i190260.Models.Exercise;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WorkoutDetails extends AppCompatActivity {

    TextView exerciseName, exerciseDescription, exerciseCalories, exerciseTime, time_view;
    VideoView simpleVideoView;
    ImageView backButton;
    Button start_button;
    CountDownTimer cTimer = null;
    FirebaseDatabase database;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_details);

        Intent intent = getIntent();
        String exerciseNameStr = intent.getStringExtra("exerciseName");
        String exerciseDescriptionStr = intent.getStringExtra("exerciseDescription");
        String exerciseCaloriesStr = intent.getStringExtra("exerciseCalories");
        String exerciseTimeStr = intent.getStringExtra("exerciseTime");
        String exerciseImageStr = intent.getStringExtra("exerciseImage");
        String exerciseVideoStr = intent.getStringExtra("exerciseVideo");

        if (exerciseTimeStr.contains(" seconds")){
            exerciseTimeStr = exerciseTimeStr.replace(" seconds", "");
        }

        int exerciseTimeInt = Integer.parseInt(exerciseTimeStr);
        exerciseTimeInt *= 1000;
        final int exerciseTimeIntFinal = exerciseTimeInt;


        exerciseName = findViewById(R.id.exerciseName);
        exerciseName.setText(exerciseNameStr);
        exerciseDescription = findViewById(R.id.exerciseDescription);
        time_view = findViewById(R.id.time_view);
        exerciseDescription.setText(exerciseDescriptionStr);
        exerciseTime = findViewById(R.id.exerciseTime);
        exerciseTimeStr += " seconds";
        exerciseTime.setText(exerciseTimeStr);
        exerciseCalories = findViewById(R.id.exerciseCalories);
        exerciseCaloriesStr += " kCal";
        exerciseCalories.setText(exerciseCaloriesStr);
        database = FirebaseDatabase.getInstance();

        // play video in video view
        simpleVideoView = findViewById(R.id.simpleVideoView);
        simpleVideoView.setVideoPath(exerciseVideoStr);
        simpleVideoView.start();

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        start_button = findViewById(R.id.start_button);
        String finalExerciseCaloriesStr = exerciseCaloriesStr;
        String finalExerciseTimeStr = exerciseTimeStr;
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start_button.getText().toString().equals("Start")) {
                    simpleVideoView.stopPlayback();
                    start_button.setText("Mark as Completed");
                    startTimer(exerciseTimeIntFinal);
                } else {
                    start_button.setText("Start");
                    simpleVideoView.start();
                    cTimer.cancel();
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String timestamp = String.valueOf(System.currentTimeMillis());
                    Exercise exercise = new Exercise(exerciseNameStr, exerciseVideoStr, exerciseDescriptionStr, finalExerciseCaloriesStr, finalExerciseTimeStr, exerciseImageStr);
                    // get date in yyyy-mm-dd format
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    exercise.setTimestamp(date);
                    database.getReference().child("Workouts").child(userID).child(timestamp).setValue(exercise);
                    database.getReference().child("Scores").child(userID).child("Score").get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String score = task.getResult().getValue().toString();
                            int scoreInt = Integer.parseInt(score);
                            scoreInt += 10;
                            // convert to string
                            String scoreStr = String.valueOf(scoreInt);
                            database.getReference().child("Scores").child(userID).child("Score").setValue(scoreStr);
                        }
                    });
                }
            }
        });




    }

    public void startTimer(int seconds) {
        cTimer = new CountDownTimer(seconds, 1000) {
            public void onTick(long millisUntilFinished) {
                String val = String.valueOf(millisUntilFinished / 1000);
                time_view.setText(val);
            }
            public void onFinish() {
                String val = "Done";
                time_view.setText(val);
            }
        };
        cTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cTimer!=null)
            cTimer.cancel();
    }
}

