package com.tp3.mycontactsrecyclar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class addPersonPage extends AppCompatActivity {
    EditText editText1, editText2, editText3, editText4, editText5;
    public static CircleImageView profil;
    public static String photoUriPath;
    List<MainData> dataList = new ArrayList<>();
    RoomDB database ;
    MainAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forms);
        editText1 = findViewById(R.id.firstNameID);
        editText2 = findViewById(R.id.lastNameID);
        editText3 = findViewById(R.id.jobID);
        editText4 = findViewById(R.id.phoneID);
        editText5 = findViewById(R.id.emailID);
        profil = findViewById(R.id.profil);
        profil .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(addPersonPage.this);
            }
        });
    }

    public void back(View v){
        Intent intent = new Intent(addPersonPage.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void saveContact(View v){
        database = RoomDB.getInstance(this);
        String firstName = editText1.getText().toString().trim();
        String lastName= editText2.getText().toString().trim();
        String job= editText3.getText().toString().trim();
        String phone = editText4.getText().toString().trim();
        String email= editText5.getText().toString().trim();
        //traitement de sauvegarde
        if (!(TextUtils.isEmpty(editText1.getText().toString().trim()) || TextUtils.isEmpty(editText2.getText().toString().trim()) || TextUtils.isEmpty(editText3.getText().toString().trim()) || TextUtils.isEmpty(editText4.getText().toString().trim()) || TextUtils.isEmpty(editText5.getText().toString().trim())) && photoUriPath != null ) {
            // when text is not empty
            // Initialize main data
            MainData data = new MainData();
            // set text on main data
            data.setName(firstName);
            data.setLast_name(lastName);
            data.setJob(job);
            data.setPhone(phone);
            data.setEmail(email);
            //image
          String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/MyContacts";
            String photoFileName = email+".jpg";
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
            }
            data.setProfil(photoFileName);
            database.mainDao().insert(data);
            // clear edit text
            editText1.setText("");
            editText2.setText("");
            editText3.setText("");
            editText4.setText("");
            editText5.setText("");
            Picasso.get().load(R.drawable.ic_person_add).into(profil);
            //Notify when data is inserted
            dataList.clear();
            dataList.addAll(database.mainDao().getAll());
            //adapter.notifyDataSetChanged();
            //revenir vers la age d'acceuil
            Intent intent = new Intent(addPersonPage.this, MainActivity.class);
            startActivity(intent);
            //finish();
        } else {
            Toast.makeText(this,"Merci de remplir tous les champs",Toast.LENGTH_LONG).show();
        }

    }
    public void reset(View v){
        editText1.setText("");
        editText2.setText("");
        editText3.setText("");
        editText4.setText("");
        editText5.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                photoUriPath = result.getUri().getPath();
                profil.setImageURI(result.getUri());

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("Tag" + "crop_error", error.toString());
            }
        }
    }


    }
