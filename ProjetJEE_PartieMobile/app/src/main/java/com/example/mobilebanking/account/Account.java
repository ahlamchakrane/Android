package com.example.mobilebanking.account;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mobilebanking.enums.OperationType;

import java.io.Serializable;
import java.util.Date;


@Entity(tableName = "accountOperation")
public class Account implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "operationDate")
    private Date operationDate;
    @ColumnInfo(name = "amount")
    private Double amount;
    @ColumnInfo(name = "type")
    private OperationType type;
    @ColumnInfo(name = "description")
    private String description;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AccountData{" +
                "ID=" + ID +
                ", operationDate='" + operationDate + '\'' +
                ", amount='" + amount + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                '}';
    }
}
