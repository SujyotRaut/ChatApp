package com.sujyotraut.chatapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.ProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.activites.ChatsActivity;
import com.sujyotraut.chatapp.activites.MainActivity;
import com.sujyotraut.chatapp.interfaces.ImgResizeCompletionListener;
import com.sujyotraut.chatapp.utils.ResizeImageTask;

public class SetUpProfileFragment extends Fragment {

    private static final int OPEN_REQUEST_CODE = 40;
    private static final String TAG = MainActivity.TAG;
    private Uri profileImage;

    private ImageView profileImageView;
    private TextInputLayout statusInputTextLayout;
    private Button nextBtn;
    private ProgressIndicator progressIndicator;

    public SetUpProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_up_profile, container, false);

        Log.d(TAG, "onCreateView: ");
        initViews(view);
        Log.d(TAG, "onCreateView: ");

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        ResizeImageTask imageTask = new ResizeImageTask(getContext(), profileImage);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/jpeg");
                startActivityForResult(intent, OPEN_REQUEST_CODE);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResizeImageTask task = new ResizeImageTask(getContext(), profileImage);
                task.addCompletionListener(new ImgResizeCompletionListener() {
                    @Override
                    public void onComplete(Uri resizedImg) {
                        Log.d(TAG, "onComplete: image is resized");
                        final String statusText = statusInputTextLayout.getEditText().getText().toString();
                        Log.d(TAG, "onComplete: ");
                        if (resizedImg != null){
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference profilePicturesRef = storage.getReference("profilePictures");
                            StorageReference currDpRef = profilePicturesRef.child(user.getUid()+".jpg");
                            currDpRef.putFile(resizedImg).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()){
                                        Log.d(TAG, "onComplete: upload dp successful");
                                        String defaultStatus = getResources().getString(R.string.default_status);
                                        String myStatus = statusInputTextLayout.getEditText().getText().toString();
                                        String status = statusText.isEmpty() ? myStatus: defaultStatus;
                                        DocumentReference currentUserRef = db.collection("users").document(user.getUid());
                                        currentUserRef.update("status", status);
                                        launchChatsActivity();
                                    }else {
                                        Log.d(TAG, "onComplete: upload dp failed");
                                        Toast.makeText(getContext(), "Profile Update Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else if(!statusText.isEmpty()){
                            String status = statusInputTextLayout.getEditText().getText().toString();
                            DocumentReference currentUserRef = db.collection("users").document(user.getUid());
                            currentUserRef.update("status", status);
                            launchChatsActivity();
                        }else {
                            String status = getResources().getString(R.string.default_status);
                            DocumentReference currentUserRef = db.collection("users").document(user.getUid());
                            currentUserRef.update("status", status);
                            launchChatsActivity();
                        }
                    }
                });

                new Thread(task).start();
            }
        });

        return view;
    }

    private void launchChatsActivity(){
        Intent intent = new Intent(getActivity(), ChatsActivity.class);
        startActivity(intent);
    }

    private void initViews(View view) {
        profileImageView = view.findViewById(R.id.setProfileImageView);
        statusInputTextLayout = view.findViewById(R.id.statusTextField);
        nextBtn = view.findViewById(R.id.nextBtn);

        progressIndicator = view.findViewById(R.id.progressBar);
        progressIndicator.setVisibility(View.INVISIBLE);

        TextView textView = getActivity().findViewById(R.id.logoTextView);
        textView.setVisibility(View.GONE);

        FrameLayout frameLayout = getActivity().findViewById(R.id.fragmentContainer);
        ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        frameLayout.setLayoutParams(layoutParams);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        int pxHeight = displayMetrics.heightPixels;
        int pxWidth = displayMetrics.widthPixels;

        Log.d("myTag", "onCreateView: " + dpWidth);

        profileImageView = view.findViewById(R.id.setProfileImageView);
        ViewGroup.LayoutParams layoutParams1 = profileImageView.getLayoutParams();
        layoutParams1.height = pxWidth;
        profileImageView.setLayoutParams(layoutParams1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK){
            profileImage = data.getData();
            profileImageView.setImageURI(profileImage);
        }
    }
}