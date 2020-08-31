package com.sujyotraut.chatapp;

import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignInFragment extends Fragment {

    private static final String TAG = "myTag";
    private TextInputLayout emailEditText, passEditText;
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

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        return view;
    }

    public void signIn(){
        String s = emailEditText.getEditText().getText().toString();
        if (!s.isEmpty())
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), "Email Field is Empty", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "sign: signed in");
    }

    public void launchSignUpFragment(){
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_slide_left, R.anim.exit_slide_left)
                    .replace(R.id.fragmentContainer, new SignUpFragment())
                    .commit();
        }
    }

    public void launchForgotPassFragment(){
        if (getFragmentManager() != null){
            getFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainer, new ForgotPasswordFragment())
                    .commit();
        }
    }

    public void initViews(View view){
        emailEditText = view.findViewById(R.id.emailTextField);
        passEditText = view.findViewById(R.id.passwordTextField);

        forgotPassTV = view.findViewById(R.id.forgotPassTV);
        signUpTV = view.findViewById(R.id.signUpTV);

        signInBtn = view.findViewById(R.id.signBtn);

        SpannableString signUpString = new SpannableString(getString(R.string.sign_up_text));
        ClickableSpan signUpSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Log.d(TAG, "onClick: sign up text is clicked");
                launchSignUpFragment();
            }
        };
        signUpString.setSpan(signUpSpan, 16, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUpTV.setText(signUpString);
        signUpTV.setMovementMethod(LinkMovementMethod.getInstance());
        signUpTV.setHighlightColor(Color.TRANSPARENT);


        SpannableString forgotPassString = new SpannableString(getString(R.string.forgot_password));
        ClickableSpan forgotPassSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Reset Password?")
                        .setMessage("Do you really want to reset your password")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: negative response from dailoge");
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                launchForgotPassFragment();
                            }
                        })
                        .show();
            }
        };
        forgotPassString.setSpan(forgotPassSpan, 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgotPassTV.setText(forgotPassString);
        forgotPassTV.setMovementMethod(LinkMovementMethod.getInstance());
        forgotPassTV.setHighlightColor(Color.TRANSPARENT);
    }
}