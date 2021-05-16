package com.univ.tours.apa.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.database.AppDatabase;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Entity(indices = {@Index(value = {"email"}, unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    @NonNull
    private String lastName;
    @NonNull
    private String firstName;
    @NonNull
    private LocalDate birthday;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String role;
    @NonNull
    private String phoneNumber;

    public User() {
        //
    }

    public User(Long id, String lastName, String firstName, LocalDate birthday, String email, String password, String role, String phoneNumber) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@NonNull String lastName) {
        this.lastName = lastName;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    @NonNull
    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(@NonNull LocalDate birthday) {
        this.birthday = birthday;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    @NonNull
    public String getRole() {
        return role;
    }

    public void setRole(@NonNull String role) {
        this.role = role;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Session> getSessions() {
        AtomicReference<List<Session>> sessions = new AtomicReference<>();
        new Thread((Runnable) () -> {
            AppDatabase db = MainActivity.db;
            List<Course> courses = db.courseDao().findByPatient(this);
            List<Activity> activities = db.activityDao().findByCourses(courses);
            sessions.set(db.sessionDao().findByActivities(activities));
        }).start();
        return sessions.get();
    }

    public Integer getAge() {
        LocalDate today = LocalDate.now();
        return Period.between(birthday, today).getYears();
    }

    public String getFullName() {
        return lastName + " " + firstName;
    }

    @Override
    public String toString() {
        return getFullName() + ", " + getAge();
    }
}
