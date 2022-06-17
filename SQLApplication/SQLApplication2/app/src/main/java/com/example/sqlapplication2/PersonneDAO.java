package com.example.sqlapplication2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
interface PersonneDao {

    @Insert
    long insert(Personne personne);

    @Delete
    void delete(Personne personne);

    @Update
    void update(Personne personne);

    @Query("SELECT * FROM personne_table")
    List<Personne> getAllPersonnes();

    @Query("SELECT * FROM personne_table where first_name like :first_name")
    Personne getPersonneByFirstName(String first_name);

}