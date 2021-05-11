package com.univ.tours.apa.entities;

import java.time.LocalDateTime;

public class Session {
    private static Long uid = 1L;

    private Long id;
    private LocalDateTime dateTime;
    private Integer duration;
    private Integer completionRate;
    private String patientFeedback;
    private Collaborator collaborator;
    private Patient patient;

    public Session() {
        setId(uid);
        uid++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
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
}
