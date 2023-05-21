package com.example.i190417_i190468_i190260.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.i190417_i190468_i190260.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    TextView personalFitscore, name1, name2, name3, name4, name5, name6, name7, points1, points2, points3, points4, points5, points6, points7;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    public LeaderboardFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        personalFitscore = view.findViewById(R.id.personalFitscore);
        name1 = view.findViewById(R.id.name1);
        name2 = view.findViewById(R.id.name2);
        name3 = view.findViewById(R.id.name3);
        name4 = view.findViewById(R.id.name4);
        name5 = view.findViewById(R.id.name5);
        name6 = view.findViewById(R.id.name6);
        name7 = view.findViewById(R.id.name7);
        points1 = view.findViewById(R.id.points1);
        points2 = view.findViewById(R.id.points2);
        points3 = view.findViewById(R.id.points3);
        points4 = view.findViewById(R.id.points4);
        points5 = view.findViewById(R.id.points5);
        points6 = view.findViewById(R.id.points6);
        points7 = view.findViewById(R.id.points7);

        updateScore();
    }

    public void updateScore(){
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        database.getReference().child("Scores").child(mAuth.getCurrentUser().getUid()).child("Score").get().addOnSuccessListener(dataSnapshot -> {
            String score = dataSnapshot.getValue(String.class);
            personalFitscore.setText(score);
        });



        List<String> names = new ArrayList<>();
        List<String> scores = new ArrayList<>();

        database.getReference().child("Scores").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                    String name = dataSnapshot.child("Name").getValue(String.class);
                    String score = dataSnapshot.child("Score").getValue(String.class);
                    names.add(name);
                    scores.add(score);
                    Log.d("TAG", "onComplete: " + name + " " + score);
                }
                if (names.size() > 0) {
                    int maxIndex = 0;
                    for (int i = 0; i < scores.size(); i++) {
                        if (Integer.parseInt(scores.get(i)) > Integer.parseInt(scores.get(maxIndex))) {
                            maxIndex = i;
                        }
                    }
                    points1.setText(scores.get(maxIndex));
                    name1.setText(names.get(maxIndex));
                    scores.remove(maxIndex);
                }
                if (names.size() > 1) {
                    int maxIndex = 0;
                    for (int i = 0; i < scores.size(); i++) {
                        if (Integer.parseInt(scores.get(i)) > Integer.parseInt(scores.get(maxIndex))) {
                            maxIndex = i;
                        }
                    }
                    points2.setText(scores.get(maxIndex));
                    name2.setText(names.get(maxIndex));
                    scores.remove(maxIndex);
                }
                if (names.size() > 2) {
                    int maxIndex = 0;
                    for (int i = 0; i < scores.size(); i++) {
                        if (Integer.parseInt(scores.get(i)) > Integer.parseInt(scores.get(maxIndex))) {
                            maxIndex = i;
                        }
                    }
                    points3.setText(scores.get(maxIndex));
                    name3.setText(names.get(maxIndex));
                    scores.remove(maxIndex);
                }
                if (names.size() > 3) {
                    int maxIndex = 0;
                    for (int i = 0; i < scores.size(); i++) {
                        if (Integer.parseInt(scores.get(i)) > Integer.parseInt(scores.get(maxIndex))) {
                            maxIndex = i;
                        }
                    }
                    points4.setText(scores.get(maxIndex));
                    name4.setText(names.get(maxIndex));
                    scores.remove(maxIndex);
                }
                if (names.size() > 4) {
                    int maxIndex = 0;
                    for (int i = 0; i < scores.size(); i++) {
                        if (Integer.parseInt(scores.get(i)) > Integer.parseInt(scores.get(maxIndex))) {
                            maxIndex = i;
                        }
                    }
                    points5.setText(scores.get(maxIndex));
                    name5.setText(names.get(maxIndex));
                    scores.remove(maxIndex);
                }
                if (names.size() > 5) {
                    int maxIndex = 0;
                    for (int i = 0; i < scores.size(); i++) {
                        if (Integer.parseInt(scores.get(i)) > Integer.parseInt(scores.get(maxIndex))) {
                            maxIndex = i;
                        }
                    }
                    points6.setText(scores.get(maxIndex));
                    name6.setText(names.get(maxIndex));
                    scores.remove(maxIndex);
                }
                if (names.size() > 6) {
                    int maxIndex = 0;
                    for (int i = 0; i < scores.size(); i++) {
                        if (Integer.parseInt(scores.get(i)) > Integer.parseInt(scores.get(maxIndex))) {
                            maxIndex = i;
                        }
                    }
                    points7.setText(scores.get(maxIndex));
                    name7.setText(names.get(maxIndex));
                    scores.remove(maxIndex);
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateScore();
    }
}