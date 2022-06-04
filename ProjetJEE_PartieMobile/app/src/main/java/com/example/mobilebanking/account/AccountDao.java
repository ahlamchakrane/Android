package com.example.mobilebanking.account;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface AccountDao {

    @Insert(onConflict = REPLACE)
    public abstract void insert(Account accountData);
    //Delete query
    @Delete
    public abstract void delete(Account accountData);

    //Delete all query
    @Delete
    public abstract void reset(List<Account> accountData);

    //update query
    @Query("UPDATE Account SET operationDate = :sDate, amount = :sAmount, type = :sType , description = :sDescription WHERE ID = :sID")
    public abstract void update(int sID, Date sDate, Double sAmount, String sType, String sDescription);

    //Get all data query
    @Query("SELECT * FROM Account")
    public abstract List<Account> getAll();


}
