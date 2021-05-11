package com.univ.tours.apa.entities;

public class Doctor {
    private static Long doctorUid = 1L;

    private Long doctorId;

    public Doctor() {
        super();
        setDoctorId(doctorUid);
        doctorUid++;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
}
