package com.example.colorfulimagapp;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Colorful {

    private Bitmap bitmap;
    private float redColorValue,greenColorValue,blueColorValue;

    public Colorful(Bitmap bitmap,float redColorValue,float greenColorValue,float blueColorValue){

        this.bitmap=bitmap;
       setBlueColorValue(blueColorValue);
       setRedColorValue(redColorValue);
       setGreenColorValue(greenColorValue);

    }

    public void setRedColorValue(float redColorValue) {
        if(redColorValue>=0 && redColorValue<=1) {
            this.redColorValue = redColorValue;
        }
    }

    public void setGreenColorValue(float greenColorValue) {
        if(greenColorValue>=0 && greenColorValue<=1) {
            this.greenColorValue = greenColorValue;
        }
    }

    public void setBlueColorValue(float blueColorValue) {
        if(blueColorValue>=0 && blueColorValue<=1) {
            this.blueColorValue = blueColorValue;
        }
    }

    public float getRedColorValue() {
        return redColorValue;
    }

    public float getGreenColorValue() {
        return greenColorValue;
    }

    public float getBlueColorValue() {
        return blueColorValue;
    }

    public Bitmap returnTheColorizeBitmap(){

        int bitmapWidth=bitmap.getWidth();
        int bitmapHeight=bitmap.getHeight();

        Bitmap.Config bitmapConfig=bitmap.getConfig();

        Bitmap localBitmap= Bitmap.createBitmap(bitmapWidth,bitmapHeight,bitmapConfig);

        for(int row=0;row<bitmapWidth;row++){

            for(int column=0;column<bitmapHeight;column++){

                int pixelColor = bitmap.getPixel(row,column);
                pixelColor = Color.argb(Color.alpha(pixelColor),
                        (int)redColorValue * Color.red(pixelColor),
                        (int)greenColorValue * Color.green(pixelColor),
                        (int)blueColorValue * Color.blue(pixelColor));
                localBitmap.setPixel(row,column,pixelColor);
            }
        }
        return  localBitmap;
    }
}
