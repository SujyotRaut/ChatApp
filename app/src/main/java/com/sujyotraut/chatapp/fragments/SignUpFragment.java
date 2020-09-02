package com.sujyotraut.chatapp.fragments;

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

import com.google.android.material.textfield.TextInputLayout;
import com.sujyotraut.chatapp.R;

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

        return view;
    }

    private void signUp(){
        if (isFieldsValid()) {
            Log.d(TAG, "signUn: signing up");
            //sign up
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
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

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