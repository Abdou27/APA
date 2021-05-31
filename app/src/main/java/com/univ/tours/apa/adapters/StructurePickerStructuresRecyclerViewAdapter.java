package com.univ.tours.apa.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.Structure;
import com.univ.tours.apa.fragments.common.StructurePickFragment;

import java.util.List;

public class StructurePickerStructuresRecyclerViewAdapter extends RecyclerView.Adapter<StructurePickerStructuresRecyclerViewAdapter.DataContainer> {
    List<Structure> structures;
    StructurePickFragment fragment;
    EditText parent;

    public StructurePickerStructuresRecyclerViewAdapter(StructurePickFragment fragment, EditText parent, List<Structure> structures) {
        this.structures = structures;
        this.fragment = fragment;
        this.parent = parent;
    }

    @NonNull
    @Override
    public StructurePickerStructuresRecyclerViewAdapter.DataContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_structure, parent, false);
        return new DataContainer(view);
    }

    @Override
    public void onBindViewHolder(DataContainer holder, int position) {
        Structure structure = structures.get(position);
        holder.nameTextView.setText(structure.getName());
        holder.disciplineTextView.setText(structure.getDiscipline());
        holder.materialCardView.setOnClickListener(v -> {
            parent.setText(structure.getName());
            if (fragment.collaboratorSessionAddFragment != null)
                fragment.collaboratorSessionAddFragment.structure = structure;
            else if (fragment.collaboratorSessionEditFragment != null)
                fragment.collaboratorSessionEditFragment.structure = structure;
            fragment.dismiss();
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
            materialCardView = itemView.findViewById(R.id.materialCardView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            disciplineTextView = itemView.findViewById(R.id.disciplineTextView);
        }
    }
}
