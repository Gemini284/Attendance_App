package com.example.attendanceapp;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.R;
import com.example.attendanceapp.modelCourse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class rvAdapter extends FirebaseRecyclerAdapter<modelCourse, rvAdapter.myViewHolder> {

    private boolean isTeacher;

    public rvAdapter(@NonNull FirebaseRecyclerOptions<modelCourse> options, boolean isTeacher) {
        super(options);
        this.isTeacher = isTeacher;
    }
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(modelCourse model);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull modelCourse model) {
        holder.course.setText(model.getName());
        holder.code.setText(model.getCode());
        holder.startE.setText(model.getEndHour());
        holder.startH.setText(model.getStartHour());
        holder.place.setText(model.getPlace());
        holder.status.setText(model.getStatus());

        // Set an OnClickListener for the entire item
        holder.itemView.setOnClickListener(view -> {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(model);
                    }
                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTeacher) {
                    showChangeStatusDialog(holder, model);
                } else {
                    // For students
                    if ("Open".equals(model.getStatus())) {
                        showSignInDialog(holder, model);
                    } else {
                        // Handle logic for other statuses (e.g., show a message)
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courseitem, parent, false);
        return new myViewHolder(view);
    }



    class myViewHolder extends RecyclerView.ViewHolder {
        TextView course, startH, startE, place, code, status;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            course = itemView.findViewById(R.id.nameCourse);
            startH = itemView.findViewById(R.id.startHour);
            startE = itemView.findViewById(R.id.finishHour);
            code = itemView.findViewById(R.id.code);
            place = itemView.findViewById(R.id.place);
            status = itemView.findViewById(R.id.status);
        }
    }

    private void showChangeStatusDialog(myViewHolder holder, modelCourse model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        builder.setTitle("Change Status");
        builder.setMessage("Select a new status:");

        //status options here
        String[] statusOptions = {"Open", "Canceled", "Closed"};


        LinearLayout layout = new LinearLayout(holder.itemView.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        // Add a button for each status option
        for (String status : statusOptions) {
            Button button = new Button(holder.itemView.getContext());
            button.setText(status);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateStatusInFirebase(model, status);
                    if (alertDialog != null) {
                        alertDialog.dismiss();  // Dismiss the dialog
                    }
                }
            });
            layout.addView(button);
        }

        builder.setView(layout);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (alertDialog != null) {
                    alertDialog.dismiss();  // Dismiss the dialog
                }
            }
        });

        // Create the dialog
        alertDialog = builder.create();
        alertDialog.setCancelable(true);  // Allow dismissing the dialog by tapping outside of it
        alertDialog.show();
    }

    private void showSignInDialog(myViewHolder holder, modelCourse model) {


        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        builder.setTitle("Sign In");
        builder.setMessage("Do you want to sign in to this class?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //COUNT ASSISTANCE
                // You can add your code here to handle the sign-in action
                dialogInterface.dismiss();  // Dismiss the dialog
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();  // Dismiss the dialog
            }
        });

        AlertDialog signInDialog = builder.create();
        signInDialog.setCancelable(true);  // Allow dismissing the dialog by tapping outside of it
        signInDialog.show();
    }

    private AlertDialog alertDialog;

    private void updateStatusInFirebase(modelCourse model, String selectedStatus) {
        String currentDay = getCurrentDayOfWeek();

        // could be mistake here?? CHECK
        if (model.getCode() != null) {
            DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference()
                    .child("courses").child(currentDay).child(model.getCode());

            // Update the 'status' field
            courseRef.child("status").setValue(selectedStatus);
        } else {
            // Handle the case where model.getCode() is null (log, show a message, etc.)
            Log.e("TAG", "model.getCode() is null");
        }
    }

    private String getCurrentDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String[] daysOfWeek = {"", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
        return daysOfWeek[dayOfWeek];
    }
}