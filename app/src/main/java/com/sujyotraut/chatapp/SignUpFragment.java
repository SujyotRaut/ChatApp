package com.sujyotraut.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpFragment extends Fragment {

    private static final String TAG = "myTag";
    private TextInputEditText nameEditText, emailEditText, passEditText;
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

    public void launchSignInFragment(){
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new SignInFragment())
                .commit();
    }

    public void initViews(View view){
//        nameEditText = view.findViewById(R.id.nameTextField);
//        emailEditText = view.findViewById(R.id.emailTextField);
//        passEditText = view.findViewById(R.id.passwordTextField);

        signInTV = view.findViewById(R.id.signUpTV);

        signUpBtn = view.findViewById(R.id.signBtn);

        String signUpString = getString(R.string.sign_in_text);
        SpannableString spannableString = new SpannableString(signUpString);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Log.d(TAG, "onClick: sign up text is clicked");
                launchSignInFragment();
            }
        };

        spannableString.setSpan(clickableSpan, 25, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        signInTV.setText(spannableString);
        signInTV.setMovementMethod(LinkMovementMethod.getInstance());
    }
}