package com.univ.tours.apa.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.univ.tours.apa.R;
import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.fragments.collaborator.CollaboratorSessionEditFragment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CollaboratorSessionsRecyclerViewAdapter extends RecyclerView.Adapter<CollaboratorSessionsRecyclerViewAdapter.DataContainer> {
    List<Session> sessions;
    FragmentManager fm;
    Fragment parent;

    public CollaboratorSessionsRecyclerViewAdapter(FragmentManager fm, List<Session> sessions, Fragment parent) {
        this.sessions = sessions;
        this.fm = fm;
        this.parent = parent;
    }

    @NonNull
    @Override
    public CollaboratorSessionsRecyclerViewAdapter.DataContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_session, parent, false);
        return new DataContainer(view);
    }

    @Override
    public void onBindViewHolder(DataContainer holder, int position) {
        Session session = sessions.get(position);
        String date = session.getDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String time = session.getDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String datetime = date + " " + parent.getString(R.string.at) + " " + time;
        String duration = parent.getString(R.string.Duration) + " " + session.getDuration() + " " + parent.getString(R.string.minutes);
        String structure = parent.getString(R.string.Structure) + " " + session.getStructure().getName();
        holder.dateTimeTextView.setText(datetime);
        holder.durationTextView.setText(duration);
        holder.structureTextView.setText(structure);
        holder.materialCardView.setOnClickListener(v -> {
            if (session.getDateTime().isBefore(LocalDateTime.now())) {
                Toast.makeText(parent.getContext(), parent.getContext().getString(R.string.cant_modify_or_delete_past_session), Toast.LENGTH_LONG).show();
            } else {
                CollaboratorSessionEditFragment collaboratorSessionEditFragment = CollaboratorSessionEditFragment.newInstance(session, sessions, parent);
                collaboratorSessionEditFragment.show(fm, "collaboratorEditSessionFragment");
            }
        });
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public static class DataContainer extends RecyclerView.ViewHolder {
        MaterialCardView materialCardView;
        TextView dateTimeTextView, durationTextView, structureTextView;

        public DataContainer(View itemView) {
            super(itemView);
            materialCardView = itemView.findViewById(R.id.materialCardView);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            structureTextView = itemView.findViewById(R.id.structureTextView);
        }
    }
}
