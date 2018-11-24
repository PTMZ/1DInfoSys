package com.example.kensi.infosys1d;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class VendorUploadImage extends AppCompatActivity implements View.OnClickListener{

    EditText editTextItemName;
    Button buttonChoose;
    Button buttonUpload;
    ImageView image;
    Bitmap bitmap;
    private static final String TAG = "Vendor";
    private static final int IMG_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: vendor" );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimg);

        editTextItemName = findViewById(R.id.editTextItemName);
        buttonChoose = findViewById(R.id.buttonChooseImage);
        buttonUpload = findViewById(R.id.buttonUpload);
        image = findViewById(R.id.imageViewUpload);

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, IMG_REQUEST);
            }
        });

        buttonUpload.setOnClickListener(this);
        buttonChoose.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        if(v.getId() == buttonChoose.getId()){
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(i, IMG_REQUEST);
        }
        else if(v.getId() == buttonUpload.getId()){
            if (bitmap != null && editTextItemName.getText().toString().length()>0) {
                VendorRequests.uploadImage(VendorUploadImage.this, editTextItemName.getText().toString(), bitmap, new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result) {
                        Log.d("UPLOAD",result);
                        finish();
                    }
                });
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == IMG_REQUEST && resultCode==RESULT_OK && data!=null){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                image.setImageBitmap(bitmap);
                image.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}