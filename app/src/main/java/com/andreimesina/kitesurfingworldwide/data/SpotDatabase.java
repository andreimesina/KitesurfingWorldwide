package com.andreimesina.kitesurfingworldwide.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.andreimesina.kitesurfingworldwide.data.model.Spot;

@Database(entities = Spot.class, version = 1)
public abstract class SpotDatabase extends RoomDatabase {

    private static SpotDatabase instance;
    private static SpotDao spotDao;

    public abstract SpotDao getSpotDao();

}
