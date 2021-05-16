package com.univ.tours.apa.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.Structure;
import com.univ.tours.apa.fragments.StructurePickerFragment;

import java.util.List;

public class CollaboratorStructuresRecyclerViewAdapter extends RecyclerView.Adapter<CollaboratorStructuresRecyclerViewAdapter.DataContainer> {
    List<Structure> structures;
    FragmentManager fm;

    public CollaboratorStructuresRecyclerViewAdapter(FragmentManager fm, List<Structure> structures) {
        this.structures = structures;
        this.fm = fm;
    }

    @NonNull
    @Override
    public CollaboratorStructuresRecyclerViewAdapter.DataContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_structure, parent, false);
        return new DataContainer(view);
    }

    @Override
    public void onBindViewHolder(DataContainer holder, int position) {
        holder.nameTextView.setText(structures.get(position).getName());
        holder.disciplineTextView.setText(structures.get(position).getDiscipline());
        holder.materialCardView.setOnClickListener(v -> {
            // Allow the ability to edit structures later
//            parent.setText(structures.get(position).getName());
//            fragment.dismiss();
        });
    }

    @Override
    public int getItemCount() {
        return structures.size();
    }

    public static class DataContainer extends RecyclerView.ViewHolder {
        MaterialCardView materialCardView;
        TextView nameTextView, disciplineTextView;

        public DataContainer(View itemView) {
            super(itemView);
            materialCardView = (MaterialCardView) itemView.findViewById(R.id.materialCardView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            disciplineTextView = (TextView) itemView.findViewById(R.id.disciplineTextView);
        }
    }
}
