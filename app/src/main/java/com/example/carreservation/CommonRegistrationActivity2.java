package com.example.carreservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommonRegistrationActivity2 extends AppCompatActivity {

    private EditText fullNameET;
    private EditText cnicNoET;
    private EditText mobileNoET;
    private EditText addressET;
    private Button nextBtn;
    private ImageButton backButtonIB;
    private ProgressDialog progressDialog;

    CircleImageView profile_image;
    Uri filePath, downloadUri;
    //    Firebase
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_registration2);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        initializeComponents();

        nextBtnOnClickListener();
        backButtonIBOnClickListener();
        onImageClick(); // call to select Image
        checkUserType();
    }

    private void initializeComponents() {

        fullNameET = findViewById(R.id.fullNameET);
        cnicNoET = findViewById(R.id.cnicNoET);
        mobileNoET = findViewById(R.id.mobileNoET);
        addressET = findViewById(R.id.addressET);
        nextBtn = findViewById(R.id.nextBtn);
        backButtonIB = findViewById(R.id.backButtonIB);
        profile_image = findViewById(R.id.profile_image);
        progressDialog = new ProgressDialog(this);
    }

    //    Click on Image to select Image
    private void onImageClick() {
        profile_image.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageChooser();
                    }
                }
        );
    }


    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
        //getActivity().startActivityForResult( Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 1) {
                // Get the url of the image from data
                filePath = data.getData();
//                Show Image on UI
                profile_image.setImageURI(filePath);
            }
        }
    }

    private void uploadImage() {


        // Code for showing progressDialog while uploading
        ProgressDialog progressDialog
                = new ProgressDialog(CommonRegistrationActivity2.this);
        progressDialog.setTitle("Process");

        progressDialog.setMessage("Please wait while we are processing your request");
        progressDialog.show();
        if (filePath != null) {

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child("images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            Task<Uri> urlTask = ref.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        downloadUri = task.getResult();
                        progressDialog.dismiss();

                        clearErrors();
                        boolean isValid = verifyInformation();

                        if (isValid) {
                            System.out.println("_______________________________________");

                            Intent intent = getIntent();

                            String email = intent.getStringExtra("email");
                            String password = intent.getStringExtra("password");
                            String confirmPassword = intent.getStringExtra("confirmPassword");
                            String userType = intent.getStringExtra("userType");

                            String fullName = fullNameET.getText().toString();
                            String cnicNo = cnicNoET.getText().toString();
                            String mobileNo = mobileNoET.getText().toString();
                            String address = addressET.getText().toString();

                            if (userType.equalsIgnoreCase("Customer")) {

                                progressDialog.setMessage("Creating User");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                System.out.println("+++++++++++++++++++++++++");
                                System.out.println(downloadUri);
                                performFirebaseOperations(email, password, userType, fullName, cnicNo, mobileNo, address, downloadUri.toString());
                            } else {

                                intent = new Intent(CommonRegistrationActivity2.this, VendorRegistrationActivity1.class);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                intent.putExtra("confirmPassword", confirmPassword);
                                intent.putExtra("userType", userType);
                                intent.putExtra("fullName", fullName);
                                intent.putExtra("cnicNo", cnicNo);
                                intent.putExtra("mobileNo", mobileNo);
                                intent.putExtra("address", address);
                                intent.putExtra("imageUrl", downloadUri.toString());
                                startActivity(intent);
                            }

                        }

                    } else {
                        progressDialog.dismiss();

                    }
                }
            });
        }
    }

    private void nextBtnOnClickListener() {

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();

//                clearErrors();
//                boolean isValid = verifyInformation();
//
//                if (isValid) {
//                    System.out.println("_______________________________________");
//
//                    Intent intent = getIntent();
//
//                    String email = intent.getStringExtra("email");
//                    String password = intent.getStringExtra("password");
//                    String confirmPassword = intent.getStringExtra("confirmPassword");
//                    String userType = intent.getStringExtra("userType");
//
//                    String fullName = fullNameET.getText().toString();
//                    String cnicNo = cnicNoET.getText().toString();
//                    String mobileNo = mobileNoET.getText().toString();
//                    String address = addressET.getText().toString();
//
//                    if (userType.equalsIgnoreCase("Customer")) {
//
//                        progressDialog.setMessage("Creating User");
//                        progressDialog.setCancelable(false);
//                        progressDialog.show();
////                        uploadImage();
//                        System.out.println("+++++++++++++++++++++++++");
//                        System.out.println(downloadUri);
////                        performFirebaseOperations(email, password, userType, fullName, cnicNo, mobileNo, address, downloadUri.toString());
//                    } else {
//
//                        intent = new Intent(CommonRegistrationActivity2.this, VendorRegistrationActivity1.class);
//                        intent.putExtra("email", email);
//                        intent.putExtra("password", password);
//                        intent.putExtra("confirmPassword", confirmPassword);
//                        intent.putExtra("userType", userType);
//                        intent.putExtra("fullName", fullName);
//                        intent.putExtra("cnicNo", cnicNo);
//                        intent.putExtra("mobileNo", mobileNo);
//                        intent.putExtra("address", address);
//                        intent.putExtra("imageUrl", downloadUri.getPath());
////                        uploadImage();
//                        startActivity(intent);
//                    }
//
//                }
            }
        });
    }

    private void performFirebaseOperations(String email, String password, String userType, String fullName, String cnicNo, String mobileNo, String address, String imageUrl) {

        addUserToFirebaseAuthenticationMechanism(email, password, userType, fullName, cnicNo, mobileNo, address, imageUrl);
    }

    private void addUserToFirebaseAuthenticationMechanism(String email, String password, String userType, String fullName, String cnicNo, String mobileNo, String address, String imageUrl) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {

                    user.sendEmailVerification()
                            .addOnCompleteListener(task1 ->
                            {
                                if (task1.isSuccessful()) {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("uuid", user.getUid());
                                    data.put("email", email);
                                    data.put("userType", userType);
                                    data.put("fullName", fullName);
                                    data.put("cnicNo", cnicNo);
                                    data.put("mobileNo", mobileNo);
                                    data.put("address", address);
                                    data.put("profileImage", imageUrl);

                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("Users").document(user.getUid()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            progressDialog.dismiss();
                                            Toast.makeText(CommonRegistrationActivity2.this, "Success", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(CommonRegistrationActivity2.this, CommonLoginActivity.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            progressDialog.dismiss();
                                            Toast.makeText(CommonRegistrationActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void backButtonIBOnClickListener() {

        backButtonIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    private boolean verifyInformation() {

        boolean flag = true;

        String fullName = fullNameET.getText().toString();
        String cnicNo = cnicNoET.getText().toString();
        String mobileNo = mobileNoET.getText().toString();
        String address = addressET.getText().toString();


        if (fullName.isEmpty()) {
            flag = false;
            fullNameET.setError("Required");
        } else if (!fullName.matches("^[a-zA-Z ]+$")) {
            flag = false;
            fullNameET.setError("Invalid Full Name");
        }

        if (cnicNo.isEmpty()) {
            flag = false;
            cnicNoET.setError("Required");
        } else if (!cnicNo.matches("^[0-9]{5}-[0-9]{7}-[0-9]$")) {
            flag = false;
            cnicNoET.setError("Invalid CNIC");
        }

        if (mobileNo.isEmpty()) {
            flag = false;
            mobileNoET.setError("Required");
        } else if (!mobileNo.matches("^((\\+92)|(0092))-{0,1}\\d{3}-{0,1}\\d{7}$|^\\d{11}$|^\\d{4}-\\d{7}$")) {
            flag = false;
            mobileNoET.setError("Invalid Mobile No");
        }

        if (address.isEmpty()) {
            flag = false;
            addressET.setError("Required");
        }

        if (filePath == null) {
            Toast.makeText(CommonRegistrationActivity2.this, "Please Upload a Profile Image", Toast.LENGTH_LONG).show();
            flag = false;
        }

        return flag;
    }

    private void clearErrors() {

        fullNameET.setError(null);
        cnicNoET.setError(null);
        mobileNoET.setError(null);
        addressET.setError(null);
    }

    private void checkUserType() {

        Intent intent = getIntent();
        String userType = intent.getStringExtra("userType");

        if (userType.equalsIgnoreCase("Customer")) {
            nextBtn.setText("Complete Registration");
        }
    }


/*
    public static class EditVendorProfileActivity extends AppCompatActivity {

        EditText fullNameET, cnicNoET, mobileNoET, addressET, accountTitleET, ibanET, bankNameET, branchNameET;

        Button updateBtn;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_common_registration2);

            fullNameET = findViewById(R.id.fullNameET);
            cnicNoET = findViewById(R.id.cnicNoET);
            mobileNoET = findViewById(R.id.mobileNoET);
            addressET = findViewById(R.id.addressET);
//            accountTitleET = findViewById(R.id.accountTitleET);
//            ibanET = findViewById(R.id.ibanET);
//            bankNameET = findViewById(R.id.bankNameET);
//            branchNameET = findViewById(R.id.branchNameET);
//            updateBtn = findViewById(R.id.btnUpdate);

            Intent intent = getIntent();
            fullNameET.setText(intent.getStringExtra("name"));
            cnicNoET.setText(intent.getStringExtra("cnic"));
            mobileNoET.setText(intent.getStringExtra("mobile"));
            addressET.setText(intent.getStringExtra("address"));
    //        accountTitleET.setText(intent.getStringExtra("address"));
    //        ibanET.setText(intent.getStringExtra("address"));
    //        bankNameET.setText(intent.getStringExtra("address"));
    //        branchNameET.setText(intent.getStringExtra("address"));




        }
    }

*/
}
