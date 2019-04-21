package com.andreimesina.kitesurfingworldwide.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.andreimesina.kitesurfingworldwide.data.model.Spot;

@Database(entities = Spot.class, version = 1)
public abstract class SpotDatabase extends RoomDatabase {

    private static SpotDatabase instance;
    private static SpotDao spotDao;

    public abstract SpotDao getSpotDao();

    public synchronized SpotDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), SpotDatabase.class,
                    "Kitesurfing_Worldwide")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}
