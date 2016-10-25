package com.example.mmizukami.petprotector;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class PetListActivity extends AppCompatActivity {

    private ImageView petImageView;

    // This member variable stores the URI to whatever image has been selected
    // Default: none.png (R.drawable.none)
    private Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        petImageView = (ImageView) findViewById(R.id.petImageView);

        imageURI = getUriToResource(this,R.drawable.none);

        petImageView.setImageURI(imageURI);
    }

    public void selectPetImage(View view)
    {
        ArrayList<String> permList = new ArrayList<>();

        int cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if(cameraPermission != PackageManager.PERMISSION_GRANTED)
            permList.add( android.Manifest.permission.CAMERA);

        int readExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(readExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            permList.add( Manifest.permission.READ_EXTERNAL_STORAGE);

        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            permList.add( Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int requestCode = 100;
        // if the List has items (size > 0), we need to request permissions from the user
        if(permList.size() > 0)
        {
            //Convert the Array List into an Array of String
            String[] perms = new String[permList.size()];

            ActivityCompat.requestPermissions(this,permList.toArray(perms),requestCode);
        }

        // if we have all 3 permissions, open ImageGallery
        if(cameraPermission == PackageManager.PERMISSION_GRANTED
                &&readExternalStoragePermission == PackageManager.PERMISSION_GRANTED
                &&writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED)
        {
            // Use intent to launch gallery and take picture
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent,requestCode);
        }
        else
            Toast.makeText(this, "Pet Protector requires camera and external storage permission", Toast.LENGTH_LONG).show();

    }


    public static Uri getUriToResource(@NonNull Context context, @AnyRes int resId)throws Resources.NotFoundException {
    Resources res = context.getResources();

        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
        "://" + res.getResourcePackageName(resId)
        + '/' + res.getResourceTypeName(resId)
        + '/' + res.getResourceEntryName(resId));


    }
}

