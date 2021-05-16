package com.univ.tours.apa.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Session;

import java.util.List;

@Dao
public interface SessionDao {
    @Query("SELECT * FROM session")
    List<Session> getAll();

    @Query("SELECT * FROM session WHERE id IN (:sessionIds)")
    List<Session> loadAllByIds(int[] sessionIds);

    @Query("SELECT * FROM session WHERE activity IN (:activities)")
    List<Session> findByActivities(List<Activity> activities);

    @Query("SELECT * FROM session WHERE id=:id LIMIT 1")
    Session findById(Long id);

    @Insert
    Long insert(Session session);

    @Update
    void update(Session session);

    @Insert
    Long[] insertAll(Session... sessions);

    @Delete
    void delete(Session session);
}
