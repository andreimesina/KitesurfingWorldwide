package com.andreimesina.kitesurfingworldwide.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.andreimesina.kitesurfingworldwide.data.model.Spot;

import java.util.List;

@Dao
public interface SpotDao {

    @Insert
    void insert(Spot spot);

    @Update
    void update(Spot spot);

    @Delete
    void delete(Spot spot);

    @Query("DELETE FROM Spot")
    void deleteAll();

    @Query("SELECT * FROM Spot ORDER BY id DESC")
    LiveData<List<Spot>> getAll();
}