package com.example.carreservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

public class MainActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users")
                    .document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(
                            new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    System.out.println("++++++++++++++++++++++++");
                                    System.out.println(documentSnapshot.getData());
                                    String userType = documentSnapshot.getData().get("userType").toString();
                                    if (userType.equalsIgnoreCase("Customer")) {
                                        Toast.makeText(MainActivity.this, "Customer", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, UserDashboardActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(MainActivity.this, "Vendor", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, VendorDrawerActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                    );
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, CommonLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_DELAY);
        }
    }
}