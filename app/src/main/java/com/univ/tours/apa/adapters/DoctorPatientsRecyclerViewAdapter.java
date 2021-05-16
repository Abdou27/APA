package com.univ.tours.apa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.User;
import com.univ.tours.apa.fragments.DoctorReadPatientFragment;

import java.util.ArrayList;
import java.util.List;

public class DoctorPatientsRecyclerViewAdapter extends RecyclerView.Adapter<DoctorPatientsRecyclerViewAdapter.DataContainer> {
    android.app.Activity context;
    List<User> patients;
    FragmentManager fm;

    public DoctorPatientsRecyclerViewAdapter(android.app.Activity context, List<User> patients, FragmentManager fm) {
        this.context = context;
        this.fm = fm;
        if (patients != null)
            this.patients = patients;
        else
            this.patients = new ArrayList<>();
    }

    @NonNull
    @Override
    public DoctorPatientsRecyclerViewAdapter.DataContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_doctor_patient, parent, false);
        return new DataContainer(view);
    }

    @Override
    public void onBindViewHolder(DataContainer holder, int position) {
        holder.titleTextView.setText(patients.get(position).getFullName());
        holder.durationTextView.setText(patients.get(position).getAge() + " " + context.getString(R.string.years));
        holder.materialCardView.setOnClickListener(v -> {
            DoctorReadPatientFragment doctorReadPatientFragment = DoctorReadPatientFragment.newInstance(patients.get(position), fm, context);
            doctorReadPatientFragment.show(fm, "doctorReadPatientFragment");
        });
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public static class DataContainer extends RecyclerView.ViewHolder {
        TextView titleTextView, durationTextView;
        MaterialCardView materialCardView;

        public DataContainer(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            durationTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            materialCardView = itemView.findViewById(R.id.materialCardView);
        }
    }
}
