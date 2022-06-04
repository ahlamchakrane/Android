package com.example.mobilebanking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilebanking.bank.BankActivity;
import com.example.mobilebanking.bank.addCurrentAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import config.AppConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerHolder> {
    private Context context;
    private List<Customer> customerList;
    JSONPlaceholder jsonPlaceholder ;
    RecyclerView recyclerView;
    public static Long idCustomer;
    public CustomerAdapter(Context context , List<Customer> customers){
        this.context = context;
        customerList = customers;
    }
    @NonNull
    @Override
    public CustomerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer , parent , false);
        return new CustomerHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CustomerHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.customer.setText(customer.getName());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.APIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholder = retrofit.create((JSONPlaceholder.class));
        holder.btEdit.setTag(customer);
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialize main data
                Customer d=customerList.get(holder.getAdapterPosition());
                //Get id
                Long sID = d.getID();
                //Get tet
                String sName= d.getName();
                String sEmail= d.getEmail();
                //create dialog
                Dialog dialog= new Dialog(context);
                //Set content vie
                dialog.setContentView(R.layout.dialog_update_customer);
                //initialize width
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                //initialize height
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                //set layout
                dialog.getWindow().setLayout(width, height);
                //show dialog
                dialog.show();
                //initialize and assign variable
                EditText editText1 = dialog.findViewById(R.id.edit_name);
                EditText editText2 = dialog.findViewById(R.id.edit_email);
                Button btUpdate= dialog.findViewById(R.id.bt_update);
                //set text on edit text
                editText1.setText(sName);
                editText2.setText(sEmail);
                btUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (! (TextUtils.isEmpty(editText1.getText().toString().trim())) ||  (TextUtils.isEmpty(editText2.getText().toString().trim()))) {
                            //dismiss dialog
                            dialog.dismiss();
                            String uName = editText1.getText().toString().trim();
                            String uEmail = editText2.getText().toString().trim();
                            //update text in database
                            d.setName(uName);
                            d.setEmail(uEmail);
                            int index = customerList.indexOf(d);

                            Call<Customer> call = jsonPlaceholder.putCustomer(sID, customer);
                            call.enqueue(new Callback<Customer>() {
                                @Override
                                public void onResponse(Call<Customer> call, Response<Customer> response) {
                                    if(! response.isSuccessful()){
                                        Snackbar.make(view, response.code(), Snackbar.LENGTH_LONG).setAction("Error", null).show();
                                        return ;
                                    }
                                    CustomerAdapter customerAdapter = new CustomerAdapter(context, customerList);
                                    MainActivity.recyclerView.setAdapter(customerAdapter);

                                }
                                @Override
                                public void onFailure(Call<Customer> call, Throwable t) {
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
        //list banks
        holder.btListBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialize main data
                Customer d=customerList.get(holder.getAdapterPosition());
                //Get id
                idCustomer = d.getID();
                Intent intent = new Intent(context, BankActivity.class);
                context.startActivity(intent);
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
                            Call<Void> call = jsonPlaceholder.deleteCustomer(customerList.get(x).getID());
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(!response.isSuccessful()){
                                        Snackbar.make(view, response.code(), Snackbar.LENGTH_LONG).setAction("Error", null).show();
                                        return;
                                    }
                                    customerList.remove(x);
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
        return customerList.size();
    }
    public  void filterList(ArrayList<Customer> filteredList){
        customerList = filteredList;
        notifyDataSetChanged();
    }
    public class CustomerHolder extends RecyclerView.ViewHolder{
        TextView customer;
        Button btEdit, btListBank;
        ConstraintLayout constraintLayout;
        public CustomerHolder(@NonNull View itemView) {
            super(itemView);
            customer = itemView.findViewById(R.id.customerName);
            btEdit = itemView.findViewById(R.id.editCustomer);
            btListBank = itemView.findViewById(R.id.listBanks);
            constraintLayout = itemView.findViewById(R.id.customer_layout);
        }
    }
}







