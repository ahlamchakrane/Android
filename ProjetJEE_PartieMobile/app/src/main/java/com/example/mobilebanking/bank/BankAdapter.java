package com.example.mobilebanking.bank;

import android.annotation.SuppressLint;
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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilebanking.JSONPlaceholder;
import com.example.mobilebanking.R;
import com.example.mobilebanking.account.AccountActivity;
import com.example.mobilebanking.enums.AccountStatus;
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

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.BankHolder> {
    private Context context;
    private List<Bank> bankList;
    JSONPlaceholder jsonPlaceholder ;
    RecyclerView recyclerView;
    public static Long idBank;
    public BankAdapter(Context context , List<Bank> banks){
        this.context = context;
        bankList = banks;
    }
    @NonNull
    @Override
    public BankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bank , parent , false);
        return new BankHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BankHolder holder, int position) {
        Bank bank = bankList.get(position);
        holder.bank.setText(bank.getDescription());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.APIURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceholder = retrofit.create((JSONPlaceholder.class));
        holder.btEdit.setTag(bank);
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialize main data
                Bank d=bankList.get(holder.getAdapterPosition());
                //Get id
                Long sID = d.getID();
                //Get tet
                String sCreatedAt= d.getCreatedAt();
                String sDescription= d.getDescription();
                Double sBalance= d.getBalance();
                AccountStatus sStatus= d.getStatus();
                Double sInterestRate= d.getInterestRate();
                Double sOverDraft= d.getOverDraft();
                //create dialog
                Dialog dialog= new Dialog(context);
                //Set content vie
                dialog.setContentView(R.layout.dialog_update_bank);
                //initialize width
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                //initialize height
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                //set layout
                dialog.getWindow().setLayout(width, height);
                //show dialog
                dialog.show();
                //initialize and assign variable
                EditText editText1 = dialog.findViewById(R.id.edit_createdAt);
                EditText editText2 = dialog.findViewById(R.id.edit_description);
                EditText editText6 = dialog.findViewById(R.id.edit_balance);
                EditText editText3 = dialog.findViewById(R.id.edit_status);
                Spinner editText4 = dialog.findViewById(R.id.edit_interestRate);
                EditText editText5 = dialog.findViewById(R.id.edit_overDraft);

                Button btUpdate= dialog.findViewById(R.id.bt_update);
                //set text on edit text
                editText1.setText(sCreatedAt);
                editText2.setText(sDescription);
                editText3.setText(sBalance.toString());
                editText5.setText(sInterestRate.toString());
                editText6.setText(sOverDraft.toString());
                btUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (! (TextUtils.isEmpty(editText1.getText().toString().trim())) ||  (TextUtils.isEmpty(editText2.getText().toString().trim()))) {
                            //dismiss dialog
                            dialog.dismiss();
                            String uCreatedAt = editText1.getText().toString().trim();
                            String uDescription = editText2.getText().toString().trim();
                            String uBalance = editText3.getText().toString().trim();
                            String uStatus = editText4.getSelectedItem().toString();
                            String uOverDraft = editText5.getText().toString().trim();

                            //update text in database
                            d.setCreatedAt(uCreatedAt);
                            d.setDescription(uDescription);
                            d.setBalance(Double.parseDouble(uBalance));
                            d.setStatus(AccountStatus.valueOf(uStatus));
                            d.setOverDraft(Double.parseDouble(uOverDraft));

                            int index = bankList.indexOf(d);

                            Call<Bank> call = jsonPlaceholder.putBank(sID, bank);
                            call.enqueue(new Callback<Bank>() {
                                @Override
                                public void onResponse(Call<Bank> call, Response<Bank> response) {
                                    if(! response.isSuccessful()){
                                        Snackbar.make(view, response.code(), Snackbar.LENGTH_LONG).setAction("Error", null).show();
                                        return ;
                                    }
                                    BankAdapter bankAdapter = new BankAdapter(context, bankList);
                                    BankActivity.recyclerView.setAdapter(bankAdapter);

                                }
                                @Override
                                public void onFailure(Call<Bank> call, Throwable t) {
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
        holder.btListAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialize main data
                Bank d=bankList.get(holder.getAdapterPosition());
                //Get id
                idBank = d.getID();
                Intent intent = new Intent(context, AccountActivity.class);
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
                            Call<Void> call = jsonPlaceholder.deleteBank(bankList.get(x).getID());
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(!response.isSuccessful()){
                                        Snackbar.make(view, response.code(), Snackbar.LENGTH_LONG).setAction("Error", null).show();
                                        return;
                                    }
                                    bankList.remove(x);
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
        return bankList.size();
    }
    public  void filterList(ArrayList<Bank> filteredList){
        bankList = filteredList;
        notifyDataSetChanged();
    }
    public class BankHolder extends RecyclerView.ViewHolder{
        TextView bank;
        Button btEdit, btListAccount;
        ConstraintLayout constraintLayout;
        public BankHolder(@NonNull View itemView) {
            super(itemView);
            bank = itemView.findViewById(R.id.bankName);
            btEdit = itemView.findViewById(R.id.editBank);
            btListAccount = itemView.findViewById(R.id.listAccounts);
            constraintLayout = itemView.findViewById(R.id.bank_layout);
        }
    }
}

