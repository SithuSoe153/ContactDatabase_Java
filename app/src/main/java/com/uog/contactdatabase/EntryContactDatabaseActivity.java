package com.uog.contactdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EntryContactDatabaseActivity extends AppCompatActivity {
    // Constants
    private static final int PICK_IMAGE_REQUEST = 1;

    // UI elements
    private ImageView avatarImageView;

    private EditText txt_Name,txt_Phone, txt_Address, txt_Age;
    private Button btn_Save, btn_Detail;

    // Database helper
    private DatabaseHelper dataBaseHelper;

    // User information
    private Integer id;

    public String imagePath;
    public String avatarFilePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_contact_database);

        // Initialize UI elements
        txt_Name = findViewById(R.id.txt_Name);
        txt_Phone = findViewById(R.id.txt_Phone);
        txt_Address = findViewById(R.id.txt_Address);
        txt_Age = findViewById(R.id.txt_Age);

        btn_Save=findViewById(R.id.btn_Save);
        btn_Detail=findViewById(R.id.btn_Detail);

        // Initialize database helper
        dataBaseHelper = new DatabaseHelper(getBaseContext());

        avatarImageView = findViewById(R.id.avatarImageView);

        // Retrieve data from intent if available
        Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            id=bundle.getInt(DatabaseHelper.PERSON_ID);
            txt_Name.setText(bundle.getString(DatabaseHelper.NAME));
            txt_Address.setText(bundle.getString(DatabaseHelper.ADDRESS));
            txt_Phone.setText(bundle.getString(DatabaseHelper.PHONE));
            txt_Age.setText(bundle.getInt(DatabaseHelper.AGE) + "");
            avatarFilePath = bundle.getString(DatabaseHelper.AVATAR_FILE_PATH);

            // Load and display avatar image if available
            if (avatarFilePath != null) {

                // Handle the image file path, for example, set it to an ImageView
                Bitmap bitmap = BitmapFactory.decodeFile(avatarFilePath);
                if (bitmap != null) {
                    avatarImageView.setImageBitmap(bitmap);
                } else {
                    // Handle the case where the image couldn't be loaded
                    avatarImageView.setImageResource(R.drawable.default_avatar); // Set a default image
                }

            } else {
                // Handle the case where the image file path is not available
                // holder.iv_profilepic.setImageResource(R.drawable.default_avatar); // Set a default image

            }

        }

//        OnCLick
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long result = 0;

                if (id == null){
                    result = dataBaseHelper.save(
                            txt_Name.getText().toString(),
                            txt_Address.getText().toString(),
                            txt_Phone.getText().toString(),
                            Integer.parseInt(txt_Age.getText().toString()),
                            imagePath);

                } else if (id != null) {

                    if (imagePath==null){
                        imagePath = bundle.getString(DatabaseHelper.AVATAR_FILE_PATH);
                    }

                    result = dataBaseHelper.update(
                            id,
                            txt_Name.getText().toString(),
                            txt_Address.getText().toString(),
                            txt_Phone.getText().toString(),
                            Integer.parseInt(txt_Age.getText().toString()),
                            imagePath);

                    Log.i("test", avatarFilePath);




                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    finish();

                }

                if (result>0){
                    new AlertDialog.Builder(EntryContactDatabaseActivity.this).setTitle("Data is stored").setMessage("Data successfully saved to database").show();

                }else{
                    new AlertDialog.Builder(EntryContactDatabaseActivity.this).setTitle("Data cannot saved").setMessage("Failed to store data").show();

                }

                txt_Name.setText("");
                txt_Address.setText("");
                txt_Phone.setText("");
                txt_Age.setText("");
                avatarImageView.setImageResource(R.drawable.default_avatar);

            }
        });

        btn_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ContactDatabaseListActivity.class);
                startActivity(intent);
            }
        });

    }

    public void selectImage(View view) {
        // Open the image picker
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            // Display the selected image in the ImageView
            avatarImageView.setImageURI(selectedImageUri);

            // Save the selected image to local storage
            saveImageToLocalStorage(selectedImageUri);
        }
    }

    private void saveImageToLocalStorage(Uri selectedImageUri) {
        try {
            // Generate a unique file name based on the current timestamp
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "IMG_" + timeStamp + ".jpg";
            // Open a writable database
//            SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

            // Create a file with the generated name
            File imageFile = new File(getFilesDir(), imageFileName);

            // Open an input stream from the selected image URI
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);

            // Open an output stream to the file
            OutputStream outputStream = new FileOutputStream(imageFile);

            Log.i("test", String.valueOf(imageFile));
            this.imagePath = String.valueOf(imageFile);


            // Copy the image data from the input stream to the output stream
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close the streams
            inputStream.close();
            outputStream.close();

            // Close the database
//            db.close();

            // Now, 'imageFile' contains the saved image
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}