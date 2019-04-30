package com.andreimesina.kitesurfingworldwide.data.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.andreimesina.kitesurfingworldwide.data.database.dao.SpotDao;
import com.andreimesina.kitesurfingworldwide.data.model.Spot;
import com.andreimesina.kitesurfingworldwide.data.model.SpotDetails;

@androidx.room.Database(entities = {Spot.class, SpotDetails.class}, version = Database.VERSION)
public abstract class Database extends RoomDatabase {

    public static final int VERSION = 4;

    private static Database instance;
    private static SpotDao spotDao;

    public abstract SpotDao getSpotDao();

    public static synchronized Database getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context, Database.class,
                    "KitesurfingWorldwide")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}
