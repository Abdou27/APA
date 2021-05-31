package com.univ.tours.apa.adapters;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.User;
import com.univ.tours.apa.fragments.doctor.DoctorPatientReadFragment;

import java.util.ArrayList;
import java.util.List;

public class StatsPatientsCompletionRateRecyclerViewAdapter extends RecyclerView.Adapter<StatsPatientsCompletionRateRecyclerViewAdapter.DataContainer> {
    private final List<Integer> rates;
    android.app.Activity parentActivity;
    List<User> patients;
    FragmentManager fm;

    public StatsPatientsCompletionRateRecyclerViewAdapter(android.app.Activity parentActivity, List<User> patients, List<Integer> rates) {
        this.parentActivity = parentActivity;
        if (patients != null)
            this.patients = patients;
        else
            this.patients = new ArrayList<>();
        if (rates != null)
            this.rates = rates;
        else
            this.rates = new ArrayList<>();
    }

    @NonNull
    @Override
    public StatsPatientsCompletionRateRecyclerViewAdapter.DataContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_patient_completion_rate, parent, false);
        return new DataContainer(view);
    }

    @Override
    public void onBindViewHolder(DataContainer holder, int position) {
        if (position == 0)
            holder.dividerLine.setVisibility(View.GONE);
        holder.patientTextView.setText(patients.get(position).getFullName());
        Integer rate = rates.get(position);

        final ViewTreeObserver observer = holder.completedLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.completedLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    holder.completedLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                int width = holder.fullLayout.getMeasuredWidth();
                ViewGroup.LayoutParams completedLayoutParams = holder.completedLayout.getLayoutParams();
                completedLayoutParams.width = width * rate / 100;
                holder.completedLayout.setLayoutParams(completedLayoutParams);
            }
        });

        String rateString = rate + " %";
        if (rate > 50) {
            holder.notCompletedTextView.setVisibility(View.GONE);
            holder.completedTextView.setVisibility(View.VISIBLE);
            holder.completedTextView.setText(rateString);
        } else {
            holder.completedTextView.setVisibility(View.GONE);
            holder.notCompletedTextView.setVisibility(View.VISIBLE);
            holder.notCompletedTextView.setText(rateString);
        }
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public static class DataContainer extends RecyclerView.ViewHolder {
        ConstraintLayout fullLayout;
        LinearLayout dividerLine, notCompletedLayout, completedLayout;
        TextView patientTextView, notCompletedTextView, completedTextView;

        public DataContainer(View itemView) {
            super(itemView);
            fullLayout = itemView.findViewById(R.id.fullLayout);
            dividerLine = itemView.findViewById(R.id.dividerLine);
            notCompletedLayout = itemView.findViewById(R.id.notCompletedLayout);
            completedLayout = itemView.findViewById(R.id.completedLayout);
            patientTextView = itemView.findViewById(R.id.patientTextView);
            notCompletedTextView = itemView.findViewById(R.id.notCompletedTextView);
            completedTextView = itemView.findViewById(R.id.completedTextView);
        }
    }
}
