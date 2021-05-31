package com.univ.tours.apa.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.Structure;
import com.univ.tours.apa.fragments.common.StructureBrowseFragment;
import com.univ.tours.apa.fragments.common.StructureEditFragment;

import java.util.List;

public class BrowseStructuresRecyclerViewAdapter extends RecyclerView.Adapter<BrowseStructuresRecyclerViewAdapter.DataContainer> {
    public List<Structure> structures;
    FragmentManager fm;
    StructureBrowseFragment fragment;

    public BrowseStructuresRecyclerViewAdapter(FragmentManager fm, List<Structure> structures, StructureBrowseFragment fragment) {
        this.structures = structures;
        this.fragment = fragment;
        this.fm = fm;
    }

    @NonNull
    @Override
    public BrowseStructuresRecyclerViewAdapter.DataContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_structure, parent, false);
        return new DataContainer(view);
    }

    @Override
    public void onBindViewHolder(DataContainer holder, int position) {
        holder.nameTextView.setText(structures.get(position).getName());
        holder.disciplineTextView.setText(structures.get(position).getDiscipline());
        holder.materialCardView.setOnClickListener(v -> {
            StructureEditFragment structureEditFragment = StructureEditFragment.newInstance(structures.get(position), fragment);
            structureEditFragment.show(fm, "editStructureFragment");
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
