package com.univ.tours.apa.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Course {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private String category;
    @NonNull
    private User doctor;
    @NonNull
    private User patient;

    public Course() {
        //
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    @NonNull
    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(@NonNull User doctor) {
        this.doctor = doctor;
    }

    @NonNull
    public User getPatient() {
        return patient;
    }

    public void setPatient(@NonNull User patient) {
        this.patient = patient;
    }
}
