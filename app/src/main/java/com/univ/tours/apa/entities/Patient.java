package com.univ.tours.apa.entities;

public class Patient {
    private static Long patientUid = 1L;

    private Long patientId;

    public Patient() {
        super();
        setPatientId(patientUid);
        patientUid++;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
