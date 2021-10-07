package com.example.tariqi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;


import java.util.List;
import java.util.concurrent.Callable;

public class SignInActivity extends AppCompatActivity {
    public static  String PREFS_NAME = "MyPrefsFile";
    private TextView signUp,forgetPassword;
    private Button btnsignIn;
    private EditText emailSignIn,passSignIn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private ImageView googleSignIn,facebookSignIn,twiterSignIn;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 1000;
    private GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressDialog;
    TwitterAuthClient twitterAuthClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progressDialog= new ProgressDialog(this);
        Twitter.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        emailSignIn=findViewById(R.id.sign_In_mail_edittext);
        passSignIn=findViewById(R.id.sign_In_password_edittext);
        signUp=findViewById(R.id.txtSignUpPage);
        forgetPassword=findViewById(R.id.txtForgetPassword);
        mAuth= FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        btnsignIn=findViewById(R.id.btnSignin);
        googleSignIn=findViewById(R.id.googleSignIn);
        facebookSignIn=findViewById(R.id.faceboofSignIn);
        twiterSignIn=findViewById(R.id.twiterSignIn);



        //to go to sign up page
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,Sign_Up.class));
            }
        });

        //forget password
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newEmail= new EditText(v.getContext());
                final AlertDialog.Builder passDialog = new AlertDialog.Builder(v.getContext());
                passDialog.setTitle("Forget Your password?");
                passDialog.setMessage("Enter Your Email To change Password");
                passDialog.setView(newEmail);
                passDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Email= newEmail.getText().toString();
                        mAuth.sendPasswordResetEmail(Email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(SignInActivity.this, "Reset Link to your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignInActivity.this, " Error! Link is not send to your Email"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passDialog.create().show();
            }
        });

        //sign in button
        btnsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= emailSignIn.getText().toString().trim();
                String password= passSignIn.getText().toString().trim();

                if (email.isEmpty()){
                    emailSignIn.setError("email is empty");
                    emailSignIn.requestFocus();
                    return;
                }
                if (password.isEmpty()){
                    passSignIn.setError("password is empty");
                    passSignIn.requestFocus();
                    return;
                }
                // signin with firebase
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){ Toast.makeText(SignInActivity.this, "User logged is successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignInActivity.this,Home.class));
                            progressBar.setVisibility(View.GONE);
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile",0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("hasLoggedIn",true);
                            editor.commit();
                        }
                        else {
                            Toast.makeText(SignInActivity.this, " log in Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        //signIn with google
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(SignInActivity.this, gso);

        mAuth = FirebaseAuth.getInstance();

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Google Sign In ...");
                progressDialog.show();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });


    }
//google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

                // Google Sign In failed, update UI appropriately
                Toast.makeText(SignInActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser usergoogle= mAuth.getCurrentUser();
                String uid= usergoogle.getUid();
                String emailgoogle= usergoogle.getEmail();
                if(authResult.getAdditionalUserInfo().isNewUser()){
                    Toast.makeText(SignInActivity.this, "Account creadted.."+emailgoogle, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SignInActivity.this, "Existing User.."+emailgoogle, Toast.LENGTH_SHORT).show();

                }
                Intent homeIntent= new Intent(SignInActivity.this,Home.class);
                startActivity(homeIntent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

}
