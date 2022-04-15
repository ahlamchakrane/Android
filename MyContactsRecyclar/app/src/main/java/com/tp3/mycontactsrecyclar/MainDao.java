package com.tp3.mycontactsrecyclar;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface MainDao {

    @Insert(onConflict = REPLACE)
    public abstract void insert(MainData mainData);
    //Delete query
    @Delete
    public abstract void delete(MainData mainData);

    //Delete all query
    @Delete
    public abstract void reset(List<MainData> mainData);

    //update query
    @Query("UPDATE contacts SET name = :sName, last_name = :sLastName, job = :sJob , phone = :sPhone, email = :sEmail WHERE ID = :sID")
    public abstract void update(int sID, String sName, String sLastName, String sJob, String sPhone, String sEmail);

    //Get all data query
    @Query("SELECT * FROM contacts")
    public abstract List<MainData> getAll();


}
