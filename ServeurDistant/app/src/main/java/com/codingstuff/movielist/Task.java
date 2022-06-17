package com.codingstuff.movielist;

public class Task {
    private int id;
    private String task;

    public Task(String task){
        this.task = task;
    }

    public String getTask() {
        return task;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
