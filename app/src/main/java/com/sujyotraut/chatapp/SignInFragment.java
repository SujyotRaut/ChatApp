package com.sujyotraut.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.service.autofill.OnClickAction;
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

public class SignInFragment extends Fragment {

    private static final String TAG = "myTag";
    private TextInputEditText emailEditText, passEditText;
    private TextView forgotPassTV, signUpTV;
    private Button signInBtn;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initViews(view);

        return view;
    }

    public void launchSignUpFragment(){
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new SignUpFragment())
                .commit();
    }

    public void initViews(View view){
//        emailEditText = view.findViewById(R.id.emailTextField);
//        passEditText = view.findViewById(R.id.passwordTextField);

        forgotPassTV = view.findViewById(R.id.forgotPassTV);
        signUpTV = view.findViewById(R.id.signUpTV);

        signInBtn = view.findViewById(R.id.signBtn);

        String signUpString = getString(R.string.sign_up_text);
        SpannableString spannableString = new SpannableString(signUpString);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Log.d(TAG, "onClick: sign up text is clicked");
                launchSignUpFragment();
            }
        };

        spannableString.setSpan(clickableSpan, 16, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        signUpTV.setText(spannableString);
        signUpTV.setMovementMethod(LinkMovementMethod.getInstance());

    }
}