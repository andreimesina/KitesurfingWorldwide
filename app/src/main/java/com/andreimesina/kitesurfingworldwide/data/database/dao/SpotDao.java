package com.andreimesina.kitesurfingworldwide.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.andreimesina.kitesurfingworldwide.data.model.Spot;
import com.andreimesina.kitesurfingworldwide.data.model.SpotDetails;

import java.util.List;

@Dao
public interface SpotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllSpots(List<Spot> spots);

    @Query("SELECT * FROM Spot WHERE toDisplay = 1 ORDER BY name ASC")
    LiveData<List<Spot>> getAllSpots();

    @Query("UPDATE Spot SET toDisplay = 0")
    void invalidateAllSpots();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertSpotDetails(SpotDetails spotDetails);

    @Query("SELECT * FROM SpotDetails WHERE spotId = :spotId")
    LiveData<SpotDetails> getSpotDetails(String spotId);

    @Query("UPDATE Spot SET isFavorite = 1 WHERE id = :spotId")
    void addSpotToFavorites(String spotId);

    @Query("UPDATE Spot SET isFavorite = 0 WHERE id = :spotId")
    void removeSpotFromFavorites(String spotId);
}