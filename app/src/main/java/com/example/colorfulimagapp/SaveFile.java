package com.example.colorfulimagapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.SystemClock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class SaveFile {

    public static File saveFile(Activity myActivity, Bitmap bitmap) throws IOException {

        String externalStorage= Environment.getExternalStorageState();
        File myFile=null;

        if(externalStorage.equals(Environment.MEDIA_MOUNTED)){

            File pictureDirectory=myActivity.getExternalFilesDir("ColorfulImageApp");

            Date currentDate=new Date();
            long elapsedTime= SystemClock.elapsedRealtime();
            String uniqueImageName="/"+currentDate+"_"+elapsedTime+".png";

            myFile = new File(pictureDirectory+uniqueImageName);
            long remainingSpace = pictureDirectory.getFreeSpace();
            long requiredSpace = bitmap.getByteCount();

            if(requiredSpace * 1.8 < remainingSpace){

                try{

                    FileOutputStream fileOutputStream = new FileOutputStream(myFile);
                    boolean isImageSavedSuccessfully = bitmap.compress(Bitmap.CompressFormat.PNG,
                                               100,fileOutputStream);

                    if (isImageSavedSuccessfully) {

                        return myFile;

                     }else {
                        throw new IOException("The image is not saved successFully to External Storage");
                    }

                }catch (Exception e){

                  throw new IOException("The operation of saving image to External Storage went wrong");

                }

            }else{

                throw new IOException("There is no enough space in order to save the image to external storage");

            }

        }else {

            throw new IOException("This Device does not have an External Storage");
        }

    }
}
