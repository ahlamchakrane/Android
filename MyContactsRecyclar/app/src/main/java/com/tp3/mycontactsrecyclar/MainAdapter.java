package com.tp3.mycontactsrecyclar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<MainData> dataList;
    private Activity context;
    private RoomDB database;
    public static CircleImageView profil;
    public static String photoUriPath;
    //create constructor
    public MainAdapter(Activity context, List<MainData> dataList) {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainData data = dataList.get(position);
        //Initialize database
        database = RoomDB.getInstance(context);
        //Set text on text view
        holder.textView.setText(data.toString());

        holder.btEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //image
                profil = view.findViewById(R.id.profil);
              /*  profil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1, 1)
                                .start(context);
                    }
                });*/
                //Initialize main data
                MainData d=dataList.get(holder.getAdapterPosition());
                //Get id
                int sID = d.getID();

                //Get tet
                String sName = d.getName();
                String sLastName = d.getLast_name();
                String sJob = d.getJob();
                String sPhone = d.getPhone();
                String sEmail = d.getEmail();

                //create dialog
                Dialog dialog= new Dialog(context);

                //Set content vie
                dialog.setContentView(R.layout.dialog_update);

                //initialize width
                int width = WindowManager.LayoutParams.MATCH_PARENT;

                //initialize height
                int height = WindowManager.LayoutParams.WRAP_CONTENT;

                //set layout
                dialog.getWindow().setLayout(width, height);

                //show dialog
                dialog.show();

                //initialize and assign variable
                EditText editText = dialog.findViewById(R.id.edit_name);
                EditText editText2 = dialog.findViewById(R.id.edit_lastName);
                EditText editText3 = dialog.findViewById(R.id.edit_job);
                EditText editText4 = dialog.findViewById(R.id.edit_phone);
                EditText editText5 = dialog.findViewById(R.id.edit_email);
                CircleImageView profil = dialog.findViewById(R.id.profil);

                Button btUpdate= dialog.findViewById(R.id.bt_update);

                //set text on edit text
                editText.setText(sName);
                editText2.setText(sLastName);
                editText3.setText(sJob);
                editText4.setText(sPhone);
                editText5.setText(sEmail);

                if (d.getProfil() != null) {
                    String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyContacts";
                    File file = new File(rootPath,d.getProfil() );
                    Picasso.get().load(file).into(profil);
                }

                btUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!(TextUtils.isEmpty(editText.getText().toString().trim()) || TextUtils.isEmpty(editText2.getText().toString().trim()) || TextUtils.isEmpty(editText3.getText().toString().trim()) || TextUtils.isEmpty(editText4.getText().toString().trim()) || TextUtils.isEmpty(editText5.getText().toString().trim())) /*&& photoUriPath != null */) {

                            //dismiss dialog
                            dialog.dismiss();
                            //image
                           /* String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyContacts";
                            String photoFileName = editText5.getText().toString().split("@")[0] + ".jpg";
                            File file = new File(rootPath, photoFileName);
                            FileOutputStream stream;
                            try {
                                stream = new FileOutputStream(file);
                                try {
                                    byte[] bytesArray = new byte[(int) new File(photoUriPath).length()];

                                    FileInputStream fileInputStream = new FileInputStream(new File(photoUriPath));
                                    fileInputStream.read(bytesArray);
                                    stream.write(bytesArray);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    stream.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/
                            //get updated text from edit text
                            String uName = editText.getText().toString().trim();
                            String uLastName = editText2.getText().toString().trim();
                            String ujob = editText3.getText().toString().trim();
                            String uPhone = editText4.getText().toString().trim();
                            String uEmail = editText5.getText().toString().trim();

                            //update text in database
                            database.mainDao().update(sID, uName, uLastName, ujob, uPhone, uEmail);
                            //Notify when data is updated
                            dataList.clear();
                            Picasso.get().load(R.drawable.ic_person_add).into(profil);
                            dataList.addAll(database.mainDao().getAll());
                            notifyDataSetChanged();
                        }else {
                            Snackbar.make(view, "Please fill in all the fields correctly !", Snackbar.LENGTH_LONG).setAction("Okay", null).show();
                        }
                    }
                });

            }
        });

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
                            database.mainDao().delete(dataList.get(x));
                            dataList.remove(x);
                            notifyItemRemoved(x);
                        }
                    }
                });
                alert.create().show();
                return  false;
            }
        });

        holder.btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                MainData d=dataList.get(holder.getAdapterPosition());
                String number = d.getPhone();
                Log.e("Num",number);
                if(!number.trim().isEmpty()){
                    intent.setData(Uri.parse("tel:"+number));
                    context.startActivity(intent);
                }
            }
        });
        holder.sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",data.getPhone(), null)));
            }
        });

        if (data.getProfil() != null) {
            String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyContacts";
            File file = new File(rootPath, data.getProfil());
            Picasso.get().load(file).into(holder.profil);
        }
        holder.profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainData contact = dataList.get(holder.getAdapterPosition());
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.image_viewer);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                CircleImageView image = dialog.findViewById(R.id.profil);
                String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyContacts";
                File file = new File(rootPath, contact.getProfil());
                Picasso.get().load(file).into(image);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public  void filterList(ArrayList<MainData> filteredList){
        dataList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView btEdit, btCall, sms;
        CircleImageView profil;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            btEdit = itemView.findViewById(R.id.bt_edit);
            btCall= itemView.findViewById(R.id.bt_call);
            profil = itemView.findViewById(R.id.profil);
            sms = itemView.findViewById(R.id.bt_sms);
        }


    }

}


