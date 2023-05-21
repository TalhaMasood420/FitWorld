package com.example.i190417_i190468_i190260.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.i190417_i190468_i190260.Models.Exercise;
import com.example.i190417_i190468_i190260.R;
import com.example.i190417_i190468_i190260.WorkoutDetails;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    List<Exercise> exercisesList;
    Context c;

    public ExerciseAdapter(List<Exercise> exercisesList, Context c) {
        this.exercisesList = exercisesList;
        this.c = c;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(c).inflate(R.layout.all_exercises_row, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.exercise_title.setText(exercisesList.get(position).getName());
        holder.exercise_toughness.setText(exercisesList.get(position).getCalories());
        holder.exercise_time.setText(exercisesList.get(position).getTime());
        Glide.with(c).load(exercisesList.get(position).getImage()).into(holder.exercise_image);

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c, WorkoutDetails.class);
                intent.putExtra("exerciseName", exercisesList.get(position).getName());
                intent.putExtra("exerciseCalories", exercisesList.get(position).getCalories());
                intent.putExtra("exerciseTime", exercisesList.get(position).getTime());
                intent.putExtra("exerciseImage", exercisesList.get(position).getImage());
                intent.putExtra("exerciseDescription", exercisesList.get(position).getDescription());
                intent.putExtra("exerciseVideo", exercisesList.get(position).getLink());
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercisesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView exercise_title, exercise_time, exercise_toughness;
        ImageView exercise_image;
        LinearLayout rootLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exercise_title = itemView.findViewById(R.id.exercise_title);
            exercise_time = itemView.findViewById(R.id.exercise_time);
            exercise_toughness = itemView.findViewById(R.id.exercise_toughness);
            exercise_image = itemView.findViewById(R.id.exercise_image);
            rootLayout = itemView.findViewById(R.id.rootLayout);
        }
    }
}
