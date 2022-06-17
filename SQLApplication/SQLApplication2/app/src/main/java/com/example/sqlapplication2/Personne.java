package com.example.sqlapplication2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "personne_table")
public class Personne {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "first_name")
    public String firstName;
    @ColumnInfo(name = "last_name")
    public String lastName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Personne(){

    }
    @Ignore
    public Personne(int id, String firstName, String lastName){
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
    }
    @Ignore
    public Personne(String firstName, String lastName){
        this.lastName = lastName;
        this.firstName = firstName;
    }
}