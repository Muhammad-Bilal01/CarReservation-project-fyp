package com.example.carreservation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VendorRegistrationActivity2 extends AppCompatActivity {

    private Button nextBtn;
    private ImageButton backButtonIB;
    private ImageView identityVerificationIV;
    private Drawable defaultDrawable;
    private ProgressDialog progressDialog;

    private int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_registration2);

        initializeComponents();

        identityVerificationIVOnClickListener();
        nextBtnOnClickListener();
        backButtonIBOnClickListener();
    }

    private void backButtonIBOnClickListener() {

        backButtonIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    private void identityVerificationIVOnClickListener() {

        identityVerificationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageChooser();
            }
        });
    }

    private void initializeComponents() {

        nextBtn = findViewById(R.id.nextBtn);
        backButtonIB = findViewById(R.id.backButtonIB);
        identityVerificationIV = findViewById(R.id.identityVerificationIV);
        defaultDrawable = identityVerificationIV.getDrawable();
        progressDialog = new ProgressDialog(this);
    }

    private void imageChooser() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            if (requestCode == SELECT_PICTURE)
            {
                Uri selectedImageUri = data.getData();

                if (null != selectedImageUri)
                {
                    identityVerificationIV.setImageURI(selectedImageUri);
                }
            }
        }
    }

    private void nextBtnOnClickListener() {

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValid = verifyInformation();

                if (isValid)
                {
                    progressDialog.setMessage("Creating User");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Intent intent = getIntent();
                    String email = intent.getStringExtra("email");
                    String password = intent.getStringExtra("password");
                    String userType = intent.getStringExtra("userType");
                    String fullName = intent.getStringExtra("fullName");
                    String cnicNo = intent.getStringExtra("cnicNo");
                    String mobileNo = intent.getStringExtra("mobileNo");
                    String address = intent.getStringExtra("address");
                    String accountTitle = intent.getStringExtra("accountTitle");
                    String iban = intent.getStringExtra("iban");
                    String bankName = intent.getStringExtra("bankName");
                    String branchName = intent.getStringExtra("branchName");
                    String imageUrl = intent.getStringExtra("imageUrl");

                    performFirebaseOperations(email, password, userType, fullName, cnicNo, mobileNo, address, accountTitle, iban, bankName, branchName,imageUrl);
                }
            }
        });
    }

    private void performFirebaseOperations(String email, String password, String userType, String fullName, String cnicNo, String mobileNo, String address, String accountTitle, String iban, String bankName, String branchName,String imageUrl) {

        addIdentityProofToFirebaseStorage(email, password, userType, fullName, cnicNo, mobileNo, address, accountTitle, iban, bankName, branchName,imageUrl);
    }

    private void addIdentityProofToFirebaseStorage(String email, String password, String userType, String fullName, String cnicNo, String mobileNo, String address, String accountTitle, String iban, String bankName, String branchName,String imgUrl) {

        identityVerificationIV.setDrawingCacheEnabled(true);
        identityVerificationIV.buildDrawingCache();
        Bitmap bitmap = identityVerificationIV.getDrawingCache();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        storageRef.child("images/" + UUID.randomUUID().toString() + ".jpg")
                .putBytes(data)
                .addOnSuccessListener(taskSnapshot ->
                {
                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                    downloadUrl.addOnSuccessListener(uri ->
                    {
                        String imageUrl = uri.toString();
                        addUserToFirebaseAuthenticationMechanism(email, password, userType, fullName, cnicNo, mobileNo, address, accountTitle, iban, bankName, branchName, imageUrl,imgUrl);
                    });
                })
                .addOnFailureListener(exception ->
                {
                    progressDialog.dismiss();
                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private void addUserToFirebaseAuthenticationMechanism(String email, String password, String userType, String fullName, String cnicNo, String mobileNo, String address, String accountTitle, String iban, String bankName, String branchName, String imageUrl,String imgUrl) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful())
            {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null)
                {
                    user.sendEmailVerification()
                            .addOnCompleteListener(task1 ->
                            {
                                if (task1.isSuccessful())
                                {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("uuid", user.getUid());
                                    data.put("email", email);
                                    data.put("userType", userType);
                                    data.put("fullName", fullName);
                                    data.put("cnicNo", cnicNo);
                                    data.put("mobileNo", mobileNo);
                                    data.put("address", address);
                                    data.put("accountTitle", accountTitle);
                                    data.put("iban", iban);
                                    data.put("bankName", bankName);
                                    data.put("branchName", branchName);
                                    data.put("verificationProofURL", imageUrl);
                                    data.put("profileImage", imgUrl);

                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("Users").document(user.getUid()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            progressDialog.dismiss();
                                            Toast.makeText(VendorRegistrationActivity2.this, "Success", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(VendorRegistrationActivity2.this, CommonLoginActivity.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            progressDialog.dismiss();
                                            Toast.makeText(VendorRegistrationActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                }
            }
            else
            {
                progressDialog.dismiss();
                Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean verifyInformation() {

        boolean flag = true;

        Drawable currentDrawable = identityVerificationIV.getDrawable();

        if (currentDrawable.getConstantState().equals(defaultDrawable.getConstantState()))
        {
            flag = false;
            Toast.makeText(this, "Please Select An Image", Toast.LENGTH_SHORT).show();
        }

        return flag;
    }
}