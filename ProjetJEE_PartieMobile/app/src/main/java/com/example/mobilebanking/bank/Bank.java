package com.example.mobilebanking.bank;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mobilebanking.enums.AccountStatus;

import java.io.Serializable;
import java.sql.Date;


@Entity(tableName = "bankAccount")
public class Bank implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Long ID;
    @ColumnInfo(name = "balance")
    private Double balance;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "createdAt")
    private String createdAt;
    @ColumnInfo(name = "status")
    private AccountStatus status;
    @ColumnInfo(name = "description")
    private  String description;
    @ColumnInfo(name = "interestRate")
    private  Double interestRate;
    @ColumnInfo(name = "overDraft")
    private  Double overDraft;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Bank() {
    }

    @Override
    public String toString() {
        return "BankData{" +
                "ID=" + ID +
                ", balance=" + balance +
                ", createdAt='" + createdAt + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", interestRate=" + interestRate +
                ", overDraft=" + overDraft +
                '}';
    }

    public Double getOverDraft() {
        return overDraft;
    }

    public void setOverDraft(Double overDraft) {
        this.overDraft = overDraft;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
