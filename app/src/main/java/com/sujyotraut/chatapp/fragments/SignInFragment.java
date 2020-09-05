package com.sujyotraut.chatapp.fragments;

import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sujyotraut.chatapp.R;
import com.sujyotraut.chatapp.activites.ChatsActivity;

public class SignInFragment extends Fragment {

    private static final String TAG = "myTag";
    private TextInputLayout emailEditText, passEditText;
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

    private void signIn(){
        if (isFieldsValid()) {
            Log.d(TAG, "signIn: signing in");
            //sign in
            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.getEditText().getText().toString(),
                    passEditText.getEditText().getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            launchChatsActivity();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Sign In Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean isFieldsValid(){
        //checking if fields are empty
        boolean isEmailEmpty = emailEditText.getEditText().getText().toString().isEmpty();
        boolean isPasswordEmpty = passEditText.getEditText().getText().toString().isEmpty();

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

        if (isEmailEmpty || isPasswordEmpty){
            return false;
        } else {
            return true;
        }
    }

    private void launchChatsActivity() {
        Intent intent = new Intent(getContext(), ChatsActivity.class);
        startActivity(intent);
    }

    private void launchSignUpFragment(){
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_slide_left, R.anim.exit_slide_left)
                    .replace(R.id.fragmentContainer, new SignUpFragment())
                    .commit();
        }
    }

    private void launchForgotPassFragment(){
        Log.d(TAG, "launchForgotPassFragment: forgot password fragment");
        if (getFragmentManager() != null){
            getFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainer, new ForgotPassFragment())
                    .commit();
            Log.d(TAG, "launchForgotPassFragment: forgot password fragment launched");
        }
    }

    private void showResetPassDialog(){
        Log.d(TAG, "showForgotPassDialog: showing reset password dialog");
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Reset Password?")
                .setMessage("Do you really want to reset your password")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: negative action form dialog");
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: positive action from dialog");
                        launchForgotPassFragment();
                    }
                })
                .show();
    }

    private void initViews(View view){

        emailEditText = view.findViewById(R.id.emailTextField);
        passEditText = view.findViewById(R.id.passwordTextField);

        TextView forgotPassTV = view.findViewById(R.id.forgotPassTV);
        TextView signUpTV = view.findViewById(R.id.signUpTV);

        signInBtn = view.findViewById(R.id.signBtn);

        SpannableString signUpString = new SpannableString(getString(R.string.sign_up_text));
        ClickableSpan signUpSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
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
                showResetPassDialog();
            }
        };
        forgotPassString.setSpan(forgotPassSpan, 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgotPassTV.setText(forgotPassString);
        forgotPassTV.setMovementMethod(LinkMovementMethod.getInstance());
        forgotPassTV.setHighlightColor(Color.TRANSPARENT);

        emailEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: ");
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
                Log.d(TAG, "onTextChanged: ");
                passEditText.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}