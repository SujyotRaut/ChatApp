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
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.activites.ChatsActivity;
import com.sujyotraut.chatapp.activites.MainActivity;
import com.sujyotraut.chatapp.interfaces.OnCompletionListener;
import com.sujyotraut.chatapp.utils.CompressAndUploadProfile;

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

        initViews(view);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                String status = statusInputTextLayout.getEditText().getText().toString();
                if (!status.isEmpty()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DocumentReference userRef = db.collection("users").document(user.getUid());
                    userRef.update("status", status).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "Status updated", Toast.LENGTH_SHORT).show();
                                launchChatsActivity();
                            }else {
                                Toast.makeText(getContext(), "Status update failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    launchChatsActivity();
                }
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
        progressIndicator.hide();


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

        profileImageView = view.findViewById(R.id.setProfileImageView);
        ViewGroup.LayoutParams layoutParams1 = profileImageView.getLayoutParams();
        layoutParams1.height = pxWidth;
        profileImageView.setLayoutParams(layoutParams1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK){
            profileImage = data.getData();

            nextBtn.setEnabled(false);
            progressIndicator.show();
            CompressAndUploadProfile task = new CompressAndUploadProfile(getContext(), profileImage);
            task.addOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onComplete(boolean isSuccessful) {
                    if (isSuccessful){
                        Toast.makeText(getContext(), "profile uploaded", Toast.LENGTH_SHORT).show();
                        profileImageView.setImageURI(profileImage);
                    }else {
                        Toast.makeText(getContext(), "Upload profile failed", Toast.LENGTH_SHORT).show();
                    }
                    progressIndicator.hide();
                    nextBtn.setEnabled(true);
                }
            });
            new Thread(task).start();
        }
    }
}