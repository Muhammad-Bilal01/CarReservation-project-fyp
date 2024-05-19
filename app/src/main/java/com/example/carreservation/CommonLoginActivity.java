package com.example.carreservation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carreservation.admin.AdminDashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.messaging.FirebaseMessaging;

public class CommonLoginActivity extends AppCompatActivity {

    private TextView emailET;
    private TextView passwordET;
    private TextView goToSignUpTV;
    private Button signInBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_login);

        initializeComponents();
        //TODO Need to remove after development done
/*        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            startActivity(new Intent(CommonLoginActivity.this, VendorDrawerActivity.class));
            finish();
        }*/

        goToSignUpTVOnClickListener();
        signInBtnOnClickListener();
    }

    private void signInBtnOnClickListener() {

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearErrors();
                boolean isValid = verifyInformation();


                if (isValid) {
                    String email = emailET.getText().toString();
                    String password = passwordET.getText().toString();
                    if (email.equalsIgnoreCase("admin@admin.com") && password.equalsIgnoreCase("admin123")) {
                            Intent intent = new Intent(CommonLoginActivity.this, AdminDashboardActivity.class);
                            startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Admin Login", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.setMessage("Verifying User");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        validateUserFromFirebase(email, password);
                    }


                }
            }
        });
    }

    private void validateUserFromFirebase(String email, String password) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {

                            if (user.isEmailVerified()) {
//                                if(FirebaseFirestore.getInstance().collection("Users").whereEqualTo("userType", "Customer").get().isSuccessful()){
//                                    startActivity(new Intent(CommonLoginActivity.this, UserDashboardActivity.class));
//                                    Toast.makeText(CommonLoginActivity.this, "Logged In Successfully(Customer)", Toast.LENGTH_SHORT).show();
//                                }

                                DocumentReference dataref = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
                                dataref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        //        // Notification System
                                        FirebaseMessaging.getInstance().getToken()
                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                                           @Override
                                                                           public void onComplete(@NonNull Task<String> task) {
                                                                               if (!task.isSuccessful()) {
                                                                                   System.out.println("Fetching FCM registration token failed" + task.getException());
                                                                                   return;
                                                                               }

//                                                                              Get new FCM registration token
                                                                               String token = task.getResult();
                                                                               dataref.update("FCM_Token",token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                   @Override
                                                                                   public void onSuccess(Void unused) {
                                                                                       Toast.makeText(CommonLoginActivity.this, "FCM Token updated", Toast.LENGTH_SHORT).show();
                                                                                   }
                                                                               });

                                                                           }
                                                                       }
                                                );


                                        String usertype = documentSnapshot.getData().get("userType").toString();
                                        if (usertype.equals("Customer")) {
                                            startActivity(new Intent(CommonLoginActivity.this, UserDashboardActivity.class));
                                            Toast.makeText(CommonLoginActivity.this, "Logged In Successfully(Customer)", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else if (usertype.equals("Admin")) {
//                                            startActivity(new Intent(CommonLoginActivity.this, UserDashboardActivity.class));
                                            Toast.makeText(CommonLoginActivity.this, "Admin", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else if (usertype.equals("Vendor")) {
                                            startActivity(new Intent(CommonLoginActivity.this, VendorDrawerActivity.class));
                                            Toast.makeText(CommonLoginActivity.this, "Logged In Successfully(Vendor)", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                });
                                dataref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot doc = task.getResult();
                                            if (doc.exists()) {
                                                Log.d("Document", doc.getData().toString());
                                            } else {
                                                Log.d("Document", "NO Data");

                                            }
                                        }
                                    }
                                });
//                                                                            startActivity(new Intent(CommonLoginActivity.this, VendorDrawerActivity.class));
//                                            Toast.makeText(CommonLoginActivity.this, "Logged In Successfully(Vendor)", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(this, "Verify Email In Order To Use App", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(this, "Invalid Username Or Password", Toast.LENGTH_SHORT).show();
                    }

                    progressDialog.dismiss();
                });
    }

    private boolean verifyInformation() {

        boolean flag = true;

        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        if (email.isEmpty()) {
            flag = false;
            emailET.setError("Required");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            flag = false;
            emailET.setError("Invalid Email");
        }

        if (password.isEmpty()) {
            flag = false;
            passwordET.setError("Required");
        } else if (password.length() < 7) {
            flag = false;
            Toast.makeText(this, "Invalid Email Or Password", Toast.LENGTH_SHORT).show();
        }

        return flag;
    }

    private void clearErrors() {

        emailET.setError(null);
        passwordET.setError(null);
    }

    private void goToSignUpTVOnClickListener() {

        goToSignUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CommonLoginActivity.this, CommonRegistrationActivity1.class);
                startActivity(intent);
            }
        });
    }

    private void initializeComponents() {

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        goToSignUpTV = findViewById(R.id.goToSignUpTV);
        signInBtn = findViewById(R.id.signInBtn);
        progressDialog = new ProgressDialog(this);
    }
}