package com.example.projettac.Models.Databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.projettac.Models.MovieModel;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDao {
    //Insert query
    @Insert(onConflict = REPLACE)
    long insert(MovieModel movie);

    //Delete query
    @Delete
    void delete(MovieModel movie);

    //Delete all query
    @Delete
    void reset(List<MovieModel> movie);

    @Query("SELECT * FROM MovieModel")
    List<MovieModel> getAll();
}
