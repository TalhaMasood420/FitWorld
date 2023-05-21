package com.example.i190417_i190468_i190260.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.i190417_i190468_i190260.Adapters.ExerciseAdapter;
import com.example.i190417_i190468_i190260.Models.Exercise;
import com.example.i190417_i190468_i190260.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    RecyclerView historyRecyclerView;
    List<Exercise> exercisesList;
    ExerciseAdapter exerciseAdapter;
    FirebaseDatabase database;


    public HistoryFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        historyRecyclerView = view.findViewById(R.id.historyRecyclerView);
        exercisesList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();

        exercisesList.clear();
        getHistoryData();

        exerciseAdapter = new ExerciseAdapter(exercisesList, getActivity());
        historyRecyclerView.setAdapter(exerciseAdapter);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
        historyRecyclerView.setLayoutManager(lm);

    }

    public void getHistoryData(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.getReference().child("Workouts").child(userId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DataSnapshot snapshot = task.getResult();
                for(DataSnapshot ds : snapshot.getChildren()){
                    Exercise exercise = ds.getValue(Exercise.class);
                    String updCal = exercise.getCalories();
                    String updCal1 = updCal.replace(" kCal", "");
                    exercise.setCalories(updCal1);
                    String updTime = exercise.getTime();
                    String updTime1 = updTime.replace(" seconds", "");
                    exercise.setTime(updTime1);
                    exercisesList.add(exercise);
                    Log.d("HistoryFragment", "getHistoryData: " + exercise.getCalories());
                }
                exerciseAdapter.notifyDataSetChanged();
            }
        });
    }
}