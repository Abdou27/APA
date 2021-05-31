package com.univ.tours.apa.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.fragments.collaborator.CollaboratorSessionBrowseReschedulingRequestsFragment;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CollaboratorRequestsRecyclerViewAdapter extends RecyclerView.Adapter<CollaboratorRequestsRecyclerViewAdapter.DataContainer> {
    List<Session> sessions;
    FragmentManager fm;
    CollaboratorSessionBrowseReschedulingRequestsFragment collaboratorSessionBrowseReschedulingRequestsFragment;
    Context context;
    private ProgressDialog loadingDialog;

    public CollaboratorRequestsRecyclerViewAdapter(Context context, List<Session> sessions, FragmentManager fm, Fragment parent) {
        this.sessions = sessions;
        this.fm = fm;
        this.collaboratorSessionBrowseReschedulingRequestsFragment = (CollaboratorSessionBrowseReschedulingRequestsFragment) parent;
        this.context = context;
    }

    @NonNull
    @Override
    public CollaboratorRequestsRecyclerViewAdapter.DataContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_session_request, parent, false);
        return new DataContainer(view);
    }

    @Override
    public void onBindViewHolder(DataContainer holder, int position) {
        Session session = sessions.get(position);

        String datetime = getDatetimeString(session.getDateTime());
        holder.dateTimeTextView.setText(datetime);

        String requestedDatetime = getDatetimeString(session.getRescheduledDateTime());
        holder.requestedDateTimeTextView.setText(requestedDatetime);

        String activity = session.getActivity().getTitle();
        holder.activityTextView.setText(activity);

        String structure = session.getStructure().getName();
        holder.structureTextView.setText(structure);

        String patient = session.getActivity().getCourse().getPatient().getFullName();
        holder.patientTextView.setText(patient);

        holder.materialCardView.setOnClickListener(v -> {
            if (session.getDateTime().isBefore(LocalDateTime.now())) {
                Toast.makeText(context, context.getString(R.string.cant_modify_or_delete_past_session), Toast.LENGTH_LONG).show();
            } else {
                new AlertDialog.Builder(context)
                        .setMessage(R.string.confirm_session_rescheduling)
                        .setPositiveButton(R.string.confirm_session_rescheduling_yes, (dialog, whichButton) -> reschedule(session))
                        .setNegativeButton(R.string.confirm_session_rescheduling_no, (dialog, which) -> removeRequest(session)).show();
            }
        });
    }

    private void removeRequest(Session session) {
        loadingDialog = ProgressDialog.show(context, "", context.getString(R.string.loading_progress_bar_text), true);
        session.setRescheduledDateTime(null);
        new Thread(() -> {
            MainActivity.db.sessionDao().update(session);
            collaboratorSessionBrowseReschedulingRequestsFragment.getActivity().runOnUiThread(() -> {
                collaboratorSessionBrowseReschedulingRequestsFragment.requests.remove(session);
                collaboratorSessionBrowseReschedulingRequestsFragment.mAdapter.notifyDataSetChanged();
                collaboratorSessionBrowseReschedulingRequestsFragment.refreshEmptyTextView();
                loadingDialog.dismiss();
            });
        }).start();
    }

    private void reschedule(Session session) {
        loadingDialog = ProgressDialog.show(context, "", context.getString(R.string.loading_progress_bar_text), true);
        session.setDateTime(session.getRescheduledDateTime());
        session.setRescheduledDateTime(null);
        new Thread(() -> {
            MainActivity.db.sessionDao().update(session);
            collaboratorSessionBrowseReschedulingRequestsFragment.getActivity().runOnUiThread(() -> {
                collaboratorSessionBrowseReschedulingRequestsFragment.requests.remove(session);
                collaboratorSessionBrowseReschedulingRequestsFragment.mAdapter.notifyDataSetChanged();
                collaboratorSessionBrowseReschedulingRequestsFragment.refreshEmptyTextView();
                loadingDialog.dismiss();
            });
        }).start();
    }

    @NotNull
    private String getDatetimeString(LocalDateTime dateTime) {
        String date = dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String time = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        return date + " " + context.getString(R.string.at) + " " + time;
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public static class DataContainer extends RecyclerView.ViewHolder {
        MaterialCardView materialCardView;
        TextView activityTextView, structureTextView, patientTextView, dateTimeTextView, requestedDateTimeTextView;

        public DataContainer(View itemView) {
            super(itemView);
            materialCardView = itemView.findViewById(R.id.materialCardView);
            activityTextView = itemView.findViewById(R.id.activityTextView);
            structureTextView = itemView.findViewById(R.id.structureTextView);
            patientTextView = itemView.findViewById(R.id.patientTextView);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
            requestedDateTimeTextView = itemView.findViewById(R.id.requestedDateTimeTextView);
        }
    }
}
