package com.example.i190417_i190468_i190260.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.i190417_i190468_i190260.Adapters.ExerciseAdapter;
import com.example.i190417_i190468_i190260.Models.Exercise;
import com.example.i190417_i190468_i190260.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ExercisesFragment extends Fragment {
    RecyclerView exercisesRecyclerView;
    List<Exercise> exercisesList, tempList;
    ExerciseAdapter exerciseAdapter;
    EditText searchBar;
    FirebaseDatabase database;
    public ExercisesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercises, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        exercisesRecyclerView = view.findViewById(R.id.exercisesRecyclerView);
        searchBar = view.findViewById(R.id.search_bar);
        exercisesList = new ArrayList<>();
        tempList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText;
                searchText = searchBar.getText().toString();
                exercisesList.clear();
                if(searchText.isEmpty()){
                    exercisesList.addAll(tempList);
                }
                else{
                    exercisesList.clear();
                    for(Exercise u1: tempList){
                        if(u1.getName().toLowerCase().contains(searchText.toLowerCase())){
                            exercisesList.add(u1);
                        }
                        exerciseAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });


        exercisesList.clear();
        getExerciseData();

        tempList.clear();
        tempList.addAll(exercisesList);

        exerciseAdapter = new ExerciseAdapter(exercisesList, getActivity());
        exercisesRecyclerView.setAdapter(exerciseAdapter);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
        exercisesRecyclerView.setLayoutManager(lm);

    }

    public void getExerciseData(){
        database.getReference().child("Exercises").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(DataSnapshot snapshot: task.getResult().getChildren()){
                    Exercise exercise = snapshot.getValue(Exercise.class);
                    exercisesList.add(exercise);
                }
                exerciseAdapter.notifyDataSetChanged();
            }
        });


    }

}