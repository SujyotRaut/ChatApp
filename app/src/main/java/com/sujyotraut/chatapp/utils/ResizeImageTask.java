package com.sujyotraut.chatapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sujyotraut.chatapp.interfaces.ImgResizeCompletionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ResizeImageTask implements Runnable {

    private List<ImgResizeCompletionListener> listeners;
    private Context context;
    private Uri imageUri;

    public ResizeImageTask(Context context, Uri imageUri) {
        this.listeners = new ArrayList<>();
        this.context = context;
        this.imageUri = imageUri;
    }

    @Override
    public void run() {

        final int HEIGHT = 500;
        final int WIDTH = 500;

        FirebaseUser  user = FirebaseAuth.getInstance().getCurrentUser();
        File outPutFile = new File(context.getExternalFilesDir("profilePictures"), user.getUid()+".jpg");

        if (imageUri != null){
            try (InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
                 OutputStream outputStream = new FileOutputStream(outPutFile)) {

                Bitmap inputBitmap = BitmapFactory.decodeStream(inputStream);

                if (inputBitmap.getWidth() >= inputBitmap.getHeight()){

                    int x = (inputBitmap.getWidth() - inputBitmap.getHeight()) / 2;
                    Bitmap centerCropBitmap = Bitmap.createBitmap(inputBitmap, x, 0,
                            inputBitmap.getHeight(), inputBitmap.getHeight());
                    Bitmap resizedImage = Bitmap.createScaledBitmap(centerCropBitmap, WIDTH, HEIGHT, true);
                    resizedImage.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);

                }else {
                    int y = (inputBitmap.getHeight() - inputBitmap.getWidth()) / 2;
                    Bitmap centerCropBitmap = Bitmap.createBitmap(inputBitmap, 0, y,
                            inputBitmap.getWidth(), inputBitmap.getWidth());
                    Bitmap resizedImage = Bitmap.createScaledBitmap(centerCropBitmap, WIDTH, HEIGHT, true);
                    resizedImage.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                }

                for (ImgResizeCompletionListener listener: listeners){
                    listener.onComplete(Uri.fromFile(outPutFile));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            for (ImgResizeCompletionListener listener: listeners){
                listener.onComplete(null);
            }
        }
    }

    public void addCompletionListener(ImgResizeCompletionListener listener){
        this.listeners.add(listener);
    }

    public void removeCompletionListener(ImgResizeCompletionListener listener){
        this.listeners.remove(listener);
    }
}
