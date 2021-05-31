package com.univ.tours.apa.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity
public class Session {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    @NonNull
    private LocalDateTime dateTime;
    private LocalDateTime rescheduledDateTime;
    @NonNull
    private Integer duration;
    private Integer completionRate;
    private String patientFeedback;
    @NonNull
    private Structure structure;
    @NonNull
    private Activity activity;

    public Session() {
        //
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(@NonNull LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getRescheduledDateTime() {
        return rescheduledDateTime;
    }

    public void setRescheduledDateTime(LocalDateTime rescheduledDateTime) {
        this.rescheduledDateTime = rescheduledDateTime;
    }

    @NonNull
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(@NonNull Integer duration) {
        this.duration = duration;
    }

    public Integer getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(Integer completionRate) {
        this.completionRate = completionRate;
    }

    public String getPatientFeedback() {
        return patientFeedback;
    }

    public void setPatientFeedback(String patientFeedback) {
        this.patientFeedback = patientFeedback;
    }

    @NonNull
    public Structure getStructure() {
        return structure;
    }

    public void setStructure(@NonNull Structure structure) {
        this.structure = structure;
    }

    @NonNull
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(@NonNull Activity activity) {
        this.activity = activity;
    }
}
