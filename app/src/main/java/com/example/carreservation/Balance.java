package com.example.carreservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Balance extends AppCompatActivity {

    private TextView penalty_text;
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        penalty_text = findViewById(R.id.penalty_txt);
        back_btn = findViewById(R.id.back_btn);



        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUser = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        FirebaseFirestore.getInstance().collection("Users").document(currentUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Check if the penalty field exists and is not null
                    if (documentSnapshot.contains("penalty") && documentSnapshot.get("penalty") != null) {
                        // Get the penalty value
                        double userPenalty = documentSnapshot.getDouble("penalty"); // Assuming penalty is stored as a double
                        String penaltyTxt = String.valueOf(userPenalty);
                        penalty_text.setText(penaltyTxt);
                    }

                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}