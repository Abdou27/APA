package com.univ.tours.apa.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.entities.Structure;

import java.util.List;

@Dao
public interface StructureDao {
    @Query("SELECT * FROM structure")
    List<Structure> getAll();

    @Query("SELECT * FROM structure WHERE id IN (:structureIds)")
    List<Structure> loadAllByIds(int[] structureIds);

    @Query("SELECT * FROM structure WHERE id=:id LIMIT 1")
    Structure findById(Long id);

    @Insert
    Long insert(Structure structure);

    @Update
    void update(Structure structure);

    @Insert
    Long[] insertAll(Structure... structures);

    @Delete
    void delete(Structure structure);
}
