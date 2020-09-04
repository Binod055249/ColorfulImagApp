package com.example.colorfulimagapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnTakePicture,btnSavePicture,btnShare;
    private ImageView imgPhoto;
    private SeekBar redColorSeekBar,greenColorSeekBar,blueColorSeekBar;
    private TextView txtRedColorValue,txtGreenColorValue,txtBlueColorValue;

    private static final int CAMERA_IMAGE_REQUEST_CODE=1000;

    private Bitmap bitmap;

    private  Colorful colorful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSavePicture=findViewById(R.id.btnSavePicture);
        btnTakePicture=findViewById(R.id.btnTakePicture);
        btnShare=findViewById(R.id.btnShare);
        imgPhoto=findViewById(R.id.imgPhoto);
        blueColorSeekBar=findViewById(R.id.blueColorSeekBar);
        redColorSeekBar=findViewById(R.id.redColorSeekBar);
        greenColorSeekBar=findViewById(R.id.greenColorSeekBar);
        txtBlueColorValue=findViewById(R.id.txtBlueColorValue);
        txtRedColorValue=findViewById(R.id.txtRedColorValue);
        txtGreenColorValue=findViewById(R.id.txtGreenColorValue);

        btnTakePicture.setOnClickListener(MainActivity.this);
        btnSavePicture.setOnClickListener(MainActivity.this);
        btnShare.setOnClickListener(MainActivity.this);


        ColorizationHandler colorizationHandler=new ColorizationHandler();


        redColorSeekBar.setOnSeekBarChangeListener(colorizationHandler);
        greenColorSeekBar.setOnSeekBarChangeListener(colorizationHandler);
        blueColorSeekBar.setOnSeekBarChangeListener(colorizationHandler);

    }

    @Override
    public void onClick(View buttonView) {

        switch (buttonView.getId()){

            case R.id.btnTakePicture:

                int permissionResult= ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);

                if(permissionResult== PackageManager.PERMISSION_GRANTED){

                    PackageManager packageManager=getPackageManager();
                    if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent,CAMERA_IMAGE_REQUEST_CODE);

                    }else{

                        Toast.makeText(this, "This device does not have camera", Toast.LENGTH_SHORT).show();
                    }

                }else{

                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
                }
                break;

            case R.id.btnSavePicture:

                int permissionCheck=ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permissionCheck == PackageManager.PERMISSION_GRANTED){

                    try{

                        SaveFile.saveFile(MainActivity.this,bitmap);
                        Toast.makeText(this, "The image is now SuccessFully saved to the External Storage", Toast.LENGTH_SHORT).show();

                    }catch (Exception e){

                        e.printStackTrace();
                    }

                }else{

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},2000);
                }

                break;

            case R.id.btnShare:

                try{
                    File myPictureFile = SaveFile.saveFile(MainActivity.this, bitmap);
                    Uri myUri = Uri.fromFile(myPictureFile);
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                            "This Picture is sent from the Color App that I created Myself!");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, myUri);
                    startActivity(Intent.createChooser(shareIntent,
                            "Let's Share your Picture with Others!"));


                    Toast.makeText(this, "sharing is done", Toast.LENGTH_SHORT).show();
                }catch (Exception e){

                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(this, "onActivityResult is called", Toast.LENGTH_SHORT).show();

        if(requestCode==CAMERA_IMAGE_REQUEST_CODE && resultCode==RESULT_OK){

            Bundle bundle=data.getExtras();

            bitmap=(Bitmap)bundle.get("data");

            colorful=new Colorful(bitmap,0.0f,0.0f,0.0f);

            imgPhoto.setImageBitmap(bitmap);
        }

    }

    private class ColorizationHandler implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if(fromUser){

                if(seekBar==redColorSeekBar){

                    colorful.setRedColorValue(progress/100.0f);
                    redColorSeekBar.setProgress((int)(100*(colorful.getRedColorValue())));
                    txtRedColorValue.setText(colorful.getRedColorValue()+"");

                }else if(seekBar==greenColorSeekBar){

                    colorful.setGreenColorValue(progress/100.0f);
                    greenColorSeekBar.setProgress((int)(100*colorful.getGreenColorValue()));
                    txtGreenColorValue.setText(colorful.getGreenColorValue()+"");

                }else if(seekBar==blueColorSeekBar){

                    colorful.setBlueColorValue(progress/100.0f);
                    blueColorSeekBar.setProgress((int)(100*colorful.getBlueColorValue()));
                    txtBlueColorValue.setText(colorful.getBlueColorValue()+"");

                }

                bitmap=colorful.returnTheColorizeBitmap();
                imgPhoto.setImageBitmap(bitmap);
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}