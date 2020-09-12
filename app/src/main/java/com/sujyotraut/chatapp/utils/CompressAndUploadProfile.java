package com.sujyotraut.chatapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sujyotraut.chatapp.interfaces.OnCompletionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class CompressAndUploadProfile implements Runnable {

    private OnCompletionListener listener;
    private Context context;
    private Uri imageUri;

    public CompressAndUploadProfile(Context context, Uri imageUri) {
        this.context = context;
        this.imageUri = imageUri;
    }

    @Override
    public void run() {

        final int HEIGHT = 500;
        final int WIDTH = 500;

        FirebaseUser  user = FirebaseAuth.getInstance().getCurrentUser();
        File outPutFile = new File(context.getExternalFilesDir("profilePictures"), user.getUid()+".jpg");

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

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference profilePicturesRef = storage.getReference("profilePictures");
            StorageReference currDpRef = profilePicturesRef.child(user.getUid()+".jpg");
            currDpRef.putFile(Uri.fromFile(outPutFile)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    listener.onComplete(task.isSuccessful());
                }
            });

        } catch (IOException e) {
            listener.onComplete(false);
            e.printStackTrace();
        }
    }

    public void addOnCompletionListener(OnCompletionListener listener){
        this.listener = listener;
    }
}
