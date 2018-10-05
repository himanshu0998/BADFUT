package com.example.himanshu.trial4;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Main extends AppCompatActivity {

    EditText memail,mpwd;
    TextView msignup;
    Button mlogin;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final int RC_SIGN_IN = 200;
    private static final String PATH_TOS = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        memail = (EditText)findViewById(R.id.memail);
        mpwd = (EditText)findViewById(R.id.mpwd);
        mlogin = (Button)findViewById(R.id.login);
        msignup = (TextView)findViewById(R.id.msignup);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    Intent intent = new Intent(Main.this,PostSignUp.class);
                    startActivity(intent);
                }
            }
        };

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setTosUrl(PATH_TOS).build(),RC_SIGN_IN);
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN)
        {
            if(resultCode==RESULT_OK)
            {
                firebaseUser = mAuth.getCurrentUser();
                sendVerificationEmail();
                //Toast.makeText(Main.this,"Registered Successfully!",Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(Main.this, PostSignUp.class);
                //startActivity(intent);
            }
        }
    }

    public void sendVerificationEmail()
    {
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                      if(task.isSuccessful())
                      {
                          Toast.makeText(Main.this,"Email Sent For Verification!! Please Verify",Toast.LENGTH_SHORT).show();
                      }
                      else
                      {
                          Toast.makeText(Main.this,"Could Not Send Email!!",Toast.LENGTH_SHORT).show();
                      }
                    }
                });
    }



    public void userLogin()
    {
        String email = memail.getText().toString();
        String pwd = mpwd.getText().toString();

        if(email.length()<=0)
        {
            Toast.makeText(this,"Please Enter Email ID!",Toast.LENGTH_SHORT).show();
        }
        else if(pwd.length()<=0)
        {
            Toast.makeText(this,"Please Enter Valid Password!",Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser.isEmailVerified()) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Main.this, "Logged In Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Main.this, PostSignUp.class);
                                    startActivity(intent);
                                } else
                                    Toast.makeText(Main.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Main.this, "Email is Not Verified!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you Sure?")
                .setNegativeButton("No",null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_OK, new Intent().putExtra("EXIT", true));
                        finish();
                    }
                }).create().show();
    }
}
