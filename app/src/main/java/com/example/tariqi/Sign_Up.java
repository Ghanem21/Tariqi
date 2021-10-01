package com.example.tariqi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
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
import com.google.firebase.database.FirebaseDatabase;

import org.chromium.base.Log;

public class Sign_Up extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView sginintxt;
    private EditText editTextEmail,editTextPass,editTextConfirmPass;
    private ProgressBar  progressBar;
    private Button signupButton;
    String email,password,confirmpass;
    private ImageView googleSignUp,facebookSignUp,twiterSignUp;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progressDialog= new ProgressDialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        sginintxt=findViewById(R.id.txtSignInPage);
        editTextEmail=findViewById(R.id.sign_up_mail_edittext);
        editTextPass=findViewById(R.id.sign_up_password_edittext);
        editTextConfirmPass=findViewById(R.id.sign_up_confirm_password_edittext);
        progressBar=findViewById(R.id.progressBar);
        signupButton=findViewById(R.id.btn_signup);
        googleSignUp=findViewById(R.id.googleSignUp);
        facebookSignUp=findViewById(R.id.facebookSignUp);
        twiterSignUp=findViewById(R.id.twetterSignUp);
        //to go to signin page
        sginintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sign_Up.this,SignInActivity.class));
            }
        });
        // sign up button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 email=editTextEmail.getText().toString().trim();
                 password=editTextPass.getText().toString().trim();
                 confirmpass=editTextConfirmPass.getText().toString().trim();
                 if (email.isEmpty()){
                     editTextEmail.setError("email is empty");
                     editTextEmail.requestFocus();
                     return;
                 }
                 if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                     editTextEmail.setError("please provide the valid email !");
                 }
                if (password.isEmpty()){
                    editTextPass.setError("password is empty");
                    editTextPass.requestFocus();
                    return;
                }
                if (password.length() <6){
                    editTextPass.setError("Password must be more than 6 characters ");
                    editTextPass.requestFocus();
                    return;
                }

                if (confirmpass.isEmpty()){
                    editTextConfirmPass.setError("confirm password is empty");
                    editTextConfirmPass.requestFocus();
                    return;
                }
                if(! password.equals(confirmpass)){
                    editTextConfirmPass.setError("Confirm the password does not match the password");
                    editTextConfirmPass.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                // signin with  firebase
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user= new User(email,password);
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Sign_Up.this, "register is done successfuly ", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(Sign_Up.this,History.class));
                                    }
                                    else {
                                        Toast.makeText(Sign_Up.this, "Faild to register ! please try again", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }else {
                            Toast.makeText(Sign_Up.this, "Faild to register ! please try again", Toast.LENGTH_SHORT).show();
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

        mGoogleSignInClient= GoogleSignIn.getClient(Sign_Up.this, gso);

        mAuth = FirebaseAuth.getInstance();

        googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Google Sign In ...");
                progressDialog.show();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

    }

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
                Toast.makeText(Sign_Up.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Sign_Up.this, "Account creadted.."+emailgoogle, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Sign_Up.this, "Existing User.."+emailgoogle, Toast.LENGTH_SHORT).show();

                }
                Intent homeIntent= new Intent(Sign_Up.this,Home.class);
                startActivity(homeIntent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Sign_Up.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }
}