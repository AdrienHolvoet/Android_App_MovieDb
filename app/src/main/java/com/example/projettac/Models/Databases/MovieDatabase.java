package com.example.projettac.Models.Databases;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.projettac.Models.MovieModel;

//Add database entities
@Database(entities = {MovieModel.class},version = 3,exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao MovieDao();
}
