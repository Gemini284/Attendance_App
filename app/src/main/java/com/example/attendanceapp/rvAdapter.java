package com.example.attendanceapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class rvAdapter extends FirebaseRecyclerAdapter<modelCourse, rvAdapter.myViewHolder> {

    public rvAdapter(@NonNull FirebaseRecyclerOptions<modelCourse> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull modelCourse model) {
        holder.course.setText(model.getName());
        holder.code.setText(model.getCode());
        holder.startE.setText(model.getEndHour());
        holder.startH.setText(model.getStartHour());
        holder.place.setText(model.getPlace());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courseitem, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView course, startH, startE, place, code;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            course = itemView.findViewById(R.id.nameCourse);
            startH = itemView.findViewById(R.id.startHour);
            startE = itemView.findViewById(R.id.finishHour);
            place = itemView.findViewById(R.id.place);
            code = itemView.findViewById(R.id.code);
        }
    }
}
