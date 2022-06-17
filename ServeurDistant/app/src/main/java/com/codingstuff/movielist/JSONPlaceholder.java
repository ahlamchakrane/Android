package com.codingstuff.movielist;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JSONPlaceholder {
    @GET("tasks")
    Call<List<Task>> getTask();
    @POST("tasks")
    Call<Task> createTask(@Body Task task);
    @PUT("tasks/{id}")
    Call<Task> putTask(@Path("id") int id, @Body Task task);
    @DELETE("tasks/{id}")
    Call<Void> deleteTask(@Path("id") int id);
}
