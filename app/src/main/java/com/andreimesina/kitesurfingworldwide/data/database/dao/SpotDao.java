package com.andreimesina.kitesurfingworldwide.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.andreimesina.kitesurfingworldwide.data.model.Spot;

import java.util.List;

@Dao
public interface SpotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllSpots(List<Spot> spots);

    @Query("SELECT * FROM Spot WHERE toDisplay = 1 ORDER BY name ASC")
    LiveData<List<Spot>> getAllSpots();

    @Query("UPDATE Spot SET toDisplay = 0")
    void invalidateAllSpots();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSpotDetails(Spot spot);

    @Query("SELECT * FROM Spot WHERE id = :spotId")
    LiveData<Spot> getSpotDetails(String spotId);

    @Query("UPDATE Spot SET isFavorite = 1 WHERE id = :spotId")
    void addSpotToFavorites(String spotId);

    @Query("UPDATE Spot SET isFavorite = 0 WHERE id = :spotId")
    void removeSpotFromFavorites(String spotId);
}