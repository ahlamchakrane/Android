package com.tp3.mycontactsrecyclar;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


//@Database(entities = {MainData.class} , version = 1, exportSchema = false)
@Database(entities = {MainData.class} , version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    // create databse instance
    public static RoomDB database ;
    // define ddatabase name
    private static String DATABASE_NAME =  "database";
    public synchronized static RoomDB getInstance(Context context){
        if(database == null){
            // when database is null inisyelayziha :)
            database = Room.databaseBuilder(context.getApplicationContext()
                    ,RoomDB.class,DATABASE_NAME).allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();

        }
        //  return database
        return database ;
    }
    // create Dao
    public abstract MainDao mainDao();




}