package com.andreimesina.kitesurfingworldwide.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.andreimesina.kitesurfingworldwide.data.model.Spot;

import java.util.List;

@Dao
public interface SpotDao {

    @Insert
    void insertAllSpots(List<Spot> spots);

    @Query("UPDATE Spot SET isFavorite = 1 WHERE id = :spotId")
    void addSpotToFavorites(int spotId);

    @Query("UPDATE Spot SET isFavorite = 0 WHERE id = :spotId")
    void removeSpotFromFavorites(int spotId);

    @Query("SELECT * FROM Spot ORDER BY id DESC")
    LiveData<List<Spot>> getAllSpots();

    @Query("SELECT * FROM Spot WHERE id = :spotId")
    LiveData<Spot> getSpotDetails(int spotId);
}