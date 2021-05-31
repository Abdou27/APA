package com.univ.tours.apa.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE role='ROLE_DOCTOR'")
    List<User> getAllDoctors();

    @Query("SELECT * FROM user WHERE role='ROLE_COLLABORATOR'")
    List<User> getAllCollaborators();

    @Query("SELECT * FROM user WHERE role='ROLE_PATIENT'")
    List<User> getAllPatients();

    @Query("SELECT * FROM user WHERE email=:email LIMIT 1")
    User findByEmail(String email);

    @Query("SELECT * FROM user WHERE id=:id LIMIT 1")
    User findById(Long id);

    @Insert
    Long insert(User user);

    @Update
    void update(User user);

    @Insert
    Long[] insertAll(User... users);

    @Delete
    void delete(User user);
}
