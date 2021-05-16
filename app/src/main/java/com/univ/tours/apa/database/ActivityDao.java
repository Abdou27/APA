package com.univ.tours.apa.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.entities.Structure;
import com.univ.tours.apa.entities.User;

import java.util.List;

@Dao
public interface ActivityDao {
    @Query("SELECT * FROM activity")
    List<Activity> getAll();

    @Query("SELECT * FROM activity WHERE id IN (:activityIds)")
    List<Activity> loadAllByIds(int[] activityIds);

    @Query("SELECT * FROM activity WHERE course IN (:courses)")
    List<Activity> findByCourses(List<Course> courses);

    @Query("SELECT * FROM activity WHERE id=:id LIMIT 1")
    Activity findById(Long id);

    @Insert
    Long insert(Activity activity);

    @Update
    void update(Activity activity);

    @Insert
    Long[] insertAll(Activity... activities);

    @Delete
    void delete(Activity activity);

    @Query("DELETE FROM activity")
    void deleteAll();
}
