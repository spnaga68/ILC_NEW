package realmstudy.fragments.regLogin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import realmstudy.MainFragmentActivity;
import realmstudy.R;
import realmstudy.data.CommanData;
import realmstudy.data.SessionSave;
import realmstudy.lib.Util;

/**
 * Created by Admin on 15-08-2017.
 */

public class Signup extends AppCompatActivity {
    private static final String TAG = "Signup";
    EditText input_name, input_email, input_password;
    Button btn_signup;
    TextView link_login;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private int type = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.signup_lay);
        type = getIntent().getIntExtra("type", 0);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        input_name = (EditText) findViewById(R.id.input_name);
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        link_login = (TextView) findViewById(R.id.link_login);
        if (type == 1) {
            link_login.setVisibility(View.GONE);
            input_name.setVisibility(View.GONE);
            btn_signup.setText(getString(R.string.login));
        }

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                //finish();
                type = 1;
                link_login.setVisibility(View.GONE);
                input_name.setVisibility(View.GONE);
                btn_signup.setText(getString(R.string.login));
            }
        });


    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.signup_lay, container, false);
//
//       
//
//        return v;
//
//    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public void onStart() {

        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

    public void signup() {
        Log.d(TAG, "Signup");
        if (!validate()) {
            onSignupFailed();
            return;
        }

        btn_signup.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Signup.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = input_name.getText().toString();
        String email = input_email.getText().toString();
        String password = input_password.getText().toString();
        Task<AuthResult> authResult = null;
        if (type == 1) {
            System.out.println("came!!!!!");
            authResult = mAuth.signInWithEmailAndPassword(email, password);
        } else {
            System.out.println("came!!!!!***");
            authResult = mAuth.createUserWithEmailAndPassword(email, password);
        }
        authResult.addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                System.out.println("came!!!!!**");
                if (Signup.this != null) {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                    progressDialog.cancel();

                    btn_signup.setEnabled(true);
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
//                    Toast.makeText(Signup.this, R.string.auth_failed,
//                            Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful() + "__" + task.getResult().getUser().getUid());
//                        SessionSave.saveSession(CommanData.USERID,task.getResult().getUser().getUid(),Signup.this);
//                        SessionSave.saveSession(CommanData.PROFILE_NAME,task.getResult().getUser().getDisplayName(),Signup.this);
//                        SessionSave.saveSession(CommanData.PH_NO,task.getResult().getUser().getPhoneNumber(),Signup.this);
//                        SessionSave.saveSession(CommanData.EMAIL_ID,task.getResult().getUser().getEmail(),Signup.this);
                        if (type == 0)
                            createNewUser(task.getResult().getUser());
                        else {
                            Intent returnIntent = new Intent(Signup.this, MainFragmentActivity.class);
                            startActivity(returnIntent);
                            finish();
                        }
                    }
                }
            }
        });
        authResult.addOnFailureListener(Signup.this, new OnFailureListener() {
            public AlertDialog alert11;

            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Signup.this, e.getLocalizedMessage(),
                //Toast.LENGTH_SHORT).show();
                if (Signup.this != null) {
                    btn_signup.setEnabled(true);
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Signup.this);
                    builder1.setMessage(e.getLocalizedMessage());
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    alert11 = builder1.create();


                    alert11.setOnShowListener(new android.content.DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(android.content.DialogInterface dialogs) {
                            alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(Signup.this, R.color.colorPrimary));
                            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(Signup.this, R.color.colorPrimary));
                        }
                    });
                    alert11.show();
                }
            }
        });
    }

    private void createNewUser(final FirebaseUser userFromRegistration) {
//        String username = "username";
//        String email = userFromRegistration.getEmail();
//        String userId = userFromRegistration.getUid();
//
//
//
//
//        String username = "username";
//        String email = userFromRegistration.getEmail();
//        String userId = userFromRegistration.getUid();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(input_name.getText().toString())
                //  .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            userFromRegistration.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");
                                                Intent returnIntent = new Intent(Signup.this, MainFragmentActivity.class);
                                                startActivity(returnIntent);
                                                finish();
                                            }
                                        }
                                    });
                        }
                    }
                });

        //  User user = new User(username, email);

        //mDatabase.child("users").child(userId).setValue(user);

    }

    public void onSignupSuccess() {
        btn_signup.setEnabled(true);
//        setResult(RESULT_OK, null);
//        finish();
    }

    public void onSignupFailed() {

        // Toast.makeText(Signup.this, "Login failed", Toast.LENGTH_LONG).show();

        btn_signup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = input_name.getText().toString();
        String email = input_email.getText().toString();
        String password = input_password.getText().toString();

        if (type == 0 && (name.isEmpty() || name.length() < 3)) {
            input_name.setError("at least 3 characters");
            valid = false;
        } else {
            input_name.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("enter a valid email address");
            valid = false;
        } else {
            input_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            input_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            input_password.setError(null);
        }

        return valid;
    }
}
