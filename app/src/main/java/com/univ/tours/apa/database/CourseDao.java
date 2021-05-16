package com.univ.tours.apa.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.User;

import java.util.List;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM course")
    List<Course> getAll();

    @Query("SELECT * FROM course WHERE id IN (:courseIds)")
    List<Course> loadAllByIds(int[] courseIds);

    @Query("SELECT * FROM course WHERE patient=:patient")
    List<Course> findByPatient(User patient);

    @Query("SELECT * FROM course WHERE id=:id LIMIT 1")
    Course findById(Long id);

    @Insert
    Long insert(Course course);

    @Update
    void update(Course course);

    @Insert
    Long[] insertAll(Course... courses);

    @Delete
    void delete(Course course);

    @Query("DELETE FROM course")
    void deleteAll();
}
