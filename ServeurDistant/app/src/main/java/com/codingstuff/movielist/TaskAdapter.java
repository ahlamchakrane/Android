package com.codingstuff.movielist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codingstuff.movielist.config.AppConfig;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private Context context;
    private List<Task> taskList;
    JSONPlaceholder jsonPlaceholder ;
    public TaskAdapter(Context context , List<Task> tasks){
        this.context = context;
        taskList = tasks;
    }
    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item , parent , false);
        return new TaskHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = taskList.get(position);
        holder.task.setText(task.getTask());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.APIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholder = retrofit.create((JSONPlaceholder.class));
        holder.btEdit.setTag(task);
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialize main data
                Task d=taskList.get(holder.getAdapterPosition());
                //Get id
                int sID = d.getId();
                //Get tet
                String sTask = d.getTask();
                //create dialog
                Dialog dialog= new Dialog(context);
                //Set content vie
                dialog.setContentView(R.layout.dialogue_update);
                //initialize width
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                //initialize height
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                //set layout
                dialog.getWindow().setLayout(width, height);
                //show dialog
                dialog.show();
                //initialize and assign variable
                EditText editText = dialog.findViewById(R.id.edit_task);
                Button btUpdate= dialog.findViewById(R.id.bt_update);
                //set text on edit text
                editText.setText(sTask);
                btUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!(TextUtils.isEmpty(editText.getText().toString().trim()))) {
                            //dismiss dialog
                            dialog.dismiss();
                            String uTask = editText.getText().toString().trim();
                            //update text in database
                            d.setTask(uTask);
                            int index = taskList.indexOf(d);

                            Call<Task> call = jsonPlaceholder.putTask(sID, task);
                            call.enqueue(new Callback<Task>() {
                                @Override
                                public void onResponse(Call<Task> call, Response<Task> response) {
                                    if(! response.isSuccessful()){
                                        Snackbar.make(view, response.code(), Snackbar.LENGTH_LONG).setAction("Error", null).show();
                                        return ;
                                    }
                                    TaskAdapter taskAdapter = new TaskAdapter(context, taskList);
                                    MainActivity.recyclerView.setAdapter(taskAdapter);

                                }
                                @Override
                                public void onFailure(Call<Task> call, Throwable t) {
                                    Snackbar.make(view, t.getMessage(), Snackbar.LENGTH_LONG).setAction("Error", null).show();
                                }
                            });
                        }else {
                            Snackbar.make(view, "Please fill in all the fields correctly !", Snackbar.LENGTH_LONG).setAction("Okay", null).show();
                        }
                    }
                });
            }
        });
        //delete
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int x = position;
                CharSequence[] delete = {
                        "Delete"
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setItems(delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            Call<Void> call = jsonPlaceholder.deleteTask(taskList.get(x).getId());
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(!response.isSuccessful()){
                                        Snackbar.make(view, response.code(), Snackbar.LENGTH_LONG).setAction("Error", null).show();
                                        return;
                                    }
                                    taskList.remove(x);
                                    notifyDataSetChanged();
                                }
                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Snackbar.make(view, t.getMessage(), Snackbar.LENGTH_LONG).setAction("Error", null).show();
                                }
                            });
                            notifyItemRemoved(x);
                        }
                    }
                });
                alert.create().show();
                return  false;
            }
        });
    }
    @Override
    public int getItemCount() {
        return taskList.size();
    }
    public  void filterList(ArrayList<Task> filteredList){
        taskList = filteredList;
        notifyDataSetChanged();
    }
    public class TaskHolder extends RecyclerView.ViewHolder{
        TextView task;
        ConstraintLayout constraintLayout;
        ImageView btEdit;
        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.task_tv);
            btEdit = itemView.findViewById(R.id.bt_edit);
            constraintLayout = itemView.findViewById(R.id.main_layout);
        }
    }
}





