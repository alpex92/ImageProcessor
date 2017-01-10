package com.alpex.imageprocessor.model.components;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import com.alpex.imageprocessor.model.listeners.BitmapChangedListener;

/**
 * Created by Alpex on 25.11.2014.
 */
public class ImageLoader {

    private static final int GALLERY_CODE = 10006;
    private static final int CAMERA_CODE = 10007;

    private BitmapChangedListener bitmapChangedListener;
    public void setBitmapChangedListener(BitmapChangedListener listener) {
        bitmapChangedListener = listener;
    }

    protected void setBitmap(Bitmap bmp) {
        Bitmap preview = PreviewManager.makePreview(bmp);
        bmp.recycle();
        if (bitmapChangedListener != null) {
            bitmapChangedListener.onBitmapChanged(preview);
        }
    }

    public void openFileGallery(Activity a) {

        Intent intent = new Intent( Intent.ACTION_GET_CONTENT );
        intent.setType( "image/*" );

        if (intent.resolveActivity(a.getPackageManager()) != null) {
            a.startActivityForResult(intent, GALLERY_CODE);
        }
    }

    public void openFileCamera(Activity a) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(a.getPackageManager()) != null) {
            a.startActivityForResult(takePictureIntent, CAMERA_CODE);
        }
    }

    public void onFileOpened(Context c, int requestCode, Intent data) {

        switch (requestCode) {
            case GALLERY_CODE:
                Uri selectedImage = data.getData();
                try {
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(c.getContentResolver(), selectedImage);
                    setBitmap(bmp);
                } catch (Exception e) {
                    Log.e("ImageLoader", "Can't open file: " + selectedImage.getPath());
                }
                break;
            case CAMERA_CODE:
                Bundle extras = data.getExtras();
                setBitmap((Bitmap) extras.get("data"));
                break;
        }
    }


}
