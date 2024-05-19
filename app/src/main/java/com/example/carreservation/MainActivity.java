package com.example.carreservation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private static final long SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // Notification System
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                                           @Override
                                           public void onComplete(@NonNull Task<String> task) {
                                               if (!task.isSuccessful()) {
                                                   System.out.println("Fetching FCM registration token failed" + task.getException());
                                                   return;
                                               }

//         Get new FCM registration token
                                               String token = task.getResult();
//                        getToken = token;

//                        System.out.println("Device Token : "+ getToken);

                                               FirebaseAuth mAuth = FirebaseAuth.getInstance();

                                               if (mAuth.getCurrentUser() != null) {

                                                   FirebaseFirestore db = FirebaseFirestore.getInstance();

//                            System.out.println("Token " + token.isEmpty());
                                                   db.collection("Users").document(mAuth.getCurrentUser().getUid()).update("FCM_Token", token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       @Override
                                                       public void onSuccess(Void unused) {
                                                           System.out.println("Token Updated" + token);
                                                           db.collection("Users")
                                                                   .document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(
                                                                           new OnSuccessListener<DocumentSnapshot>() {
                                                                               @Override
                                                                               public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                                            System.out.println("++++++++++++++ UID ++++++++++");
//                                                            System.out.println(documentSnapshot.getData().get("uuid"));
                                                                                   String userType = documentSnapshot.getData().get("userType").toString();
                                                                                   if (userType.equalsIgnoreCase("Customer")) {
                                                                                       Toast.makeText(MainActivity.this, "Customer", Toast.LENGTH_SHORT).show();
                                                                                       Intent intent = new Intent(MainActivity.this, UserDashboardActivity.class);
                                                                                       startActivity(intent);
                                                                                       finish();
                                                                                   } else if (userType.equalsIgnoreCase("Admin")) {
                                                                                       Toast.makeText(MainActivity.this, "Admin", Toast.LENGTH_SHORT).show();
                                                                                   } else {
                                                                                       Toast.makeText(MainActivity.this, "Vendor", Toast.LENGTH_SHORT).show();
                                                                                       Intent intent = new Intent(MainActivity.this, VendorDrawerActivity.class);
                                                                                       startActivity(intent);
                                                                                       finish();
                                                                                   }
                                                                               }
                                                                           }
                                                                   );
                                                       }
                                                   });


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
                );

//         f8lejZvuSqi8YY9YrvGohx:APA91bFbQn23YT91r9SY9QjMABXQVgkMeP6lLyEerObrfEmOyVAXI_CkSj-td9sz9jK3VhXi3xvnPHLUSeplAr1
    }


}