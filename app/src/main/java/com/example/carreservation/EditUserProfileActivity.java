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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserProfileActivity extends AppCompatActivity {

    EditText fullNameET, cnicNoET, mobileNoET, addressET;
    Button updateBtn;
    ImageButton backButton;

    CircleImageView profileImage;

    Uri filePath, downloadUri;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        fullNameET = findViewById(R.id.fullNameET);
        cnicNoET = findViewById(R.id.cnicNoET);
        mobileNoET = findViewById(R.id.mobileNoET);
        addressET = findViewById(R.id.addressET);
        updateBtn = findViewById(R.id.btnUpdate);
        backButton = findViewById(R.id.backButtonIB);
        profileImage = findViewById(R.id.profile_image);

        Intent intent = getIntent();
        fullNameET.setText(intent.getStringExtra("name"));
        cnicNoET.setText(intent.getStringExtra("cnic"));
        mobileNoET.setText(intent.getStringExtra("mobile"));
        addressET.setText(intent.getStringExtra("address"));
        if (intent.getStringExtra("profileImg")!=""){
            Picasso.get().load(intent.getStringExtra("profileImg")).into(profileImage);
        };

        onUpdateClick();
        onBackButtonClick();
        onProfileClick();
    }


    private void onProfileClick() {
        profileImage.setOnClickListener(
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
                profileImage.setImageURI(filePath);
            }
        }
    }


    private void uploadImage() {


        // Code for showing progressDialog while uploading
        ProgressDialog progressDialog
                = new ProgressDialog(EditUserProfileActivity.this);
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

                        String name = fullNameET.getText().toString();
                        String cnicNo = cnicNoET.getText().toString();
                        String mobileNo = mobileNoET.getText().toString();
                        String address = addressET.getText().toString();

                        if (isValid) {
                            addUserToFirebaseAuthenticationMechanism(name,cnicNo,mobileNo,address,downloadUri.toString());

                        } else {
                            Toast.makeText(EditUserProfileActivity.this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        progressDialog.dismiss();

                    }
                }
            });
        }
    }

    private void onBackButtonClick(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void onUpdateClick() {
        updateBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        uploadImage();

//                        String name = fullNameET.getText().toString();
//                        String cnicNo = cnicNoET.getText().toString();
//                        String mobileNo = mobileNoET.getText().toString();
//                        String address = addressET.getText().toString();
//
//                        clearErrors();
//                        boolean isValid = verifyInformation();
//
//                        if (isValid) {
////                            addUserToFirebaseAuthenticationMechanism(name,cnicNo,mobileNo,address);
//
//                        } else {
//                            Toast.makeText(EditUserProfileActivity.this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
//                        }
                    }
                }
        );
    }

    private void addUserToFirebaseAuthenticationMechanism( String fullName, String cnicNo, String mobileNo, String address,String imageUrl) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        Map<String, Object> data = new HashMap<>();
        data.put("fullName", fullName);
        data.put("cnicNo", cnicNo);
        data.put("mobileNo", mobileNo);
        data.put("address", address);
        data.put("profileImage", imageUrl);

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(fullName);
        System.out.println(address);
        System.out.println(data);
        System.out.println(data);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (user != null){

            db.collection("Users").document(user.getUid()).update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditUserProfileActivity.this, "Data Update Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditUserProfileActivity.this,UserDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(EditUserProfileActivity.this, "Data Update Failed", Toast.LENGTH_SHORT).show();
                        }

                    });
        }
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

        return flag;
    }

    private void clearErrors() {

        fullNameET.setError(null);
        cnicNoET.setError(null);
        mobileNoET.setError(null);
        addressET.setError(null);
    }


}