package com.sujyotraut.chatapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.activites.ChatsActivity;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    private static final String TAG = "myTag";
    private TextInputLayout nameEditText, emailEditText, passEditText;
    private TextView signInTV;
    private Button signUpBtn;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        initViews(view);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        return view;
    }

    private void signUp(){
        if (isFieldsValid()) {
            Log.d(TAG, "signUn: signing up");

            FirebaseAuth auth =FirebaseAuth.getInstance();

            final String name = nameEditText.getEditText().getText().toString();
            final String email = emailEditText.getEditText().getText().toString();
            String pass = passEditText.getEditText().getText().toString();
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user = task.getResult().getUser();
                        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        user.updateProfile(changeRequest);

                        Map<String, String> map = new HashMap<>();
                        map.put("name", name);
                        map.put("email", user.getEmail());

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference usersRef = db.collection("users");
                        usersRef.document(user.getUid()).set(map);
                    } else {
                        Toast.makeText(getContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean isFieldsValid(){
        //checking if fields are empty
        boolean isNameEmpty = nameEditText.getEditText().getText().toString().isEmpty();
        boolean isEmailEmpty = emailEditText.getEditText().getText().toString().isEmpty();
        boolean isPasswordEmpty = passEditText.getEditText().getText().toString().isEmpty();

        if (isNameEmpty){
            //setting and showing error text
            nameEditText.setError(getString(R.string.invalid_credentials));
            emailEditText.setErrorEnabled(true);
        }

        if (isEmailEmpty){
            //setting and showing error text
            emailEditText.setError(getString(R.string.invalid_credentials));
            emailEditText.setErrorEnabled(true);
        }

        if (isPasswordEmpty){
            //setting and showing error text
            passEditText.setError(getString(R.string.invalid_credentials));
            passEditText.setErrorEnabled(true);
        }

        if (isNameEmpty || isEmailEmpty || isPasswordEmpty){
            return false;
        } else {
            return true;
        }
    }

    private void launchSignInFragment(){
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_slide_right, R.anim.exit_slide_right)
                    .replace(R.id.fragmentContainer, new SignInFragment())
                    .commit();
        }
    }

    private void initViews(View view){

        nameEditText = view.findViewById(R.id.nameTextField);
        emailEditText = view.findViewById(R.id.emailTextField);
        passEditText = view.findViewById(R.id.passwordTextField);

        signInTV = view.findViewById(R.id.signInTV);

        signUpBtn = view.findViewById(R.id.signBtn);

        String signUpString = getString(R.string.sign_in_text);
        SpannableString spannableString = new SpannableString(signUpString);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                launchSignInFragment();
            }
        };
        spannableString.setSpan(clickableSpan, 25, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        signInTV.setText(spannableString);
        signInTV.setMovementMethod(LinkMovementMethod.getInstance());
        signInTV.setHighlightColor(Color.TRANSPARENT);

        nameEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameEditText.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        emailEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailEditText.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passEditText.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}