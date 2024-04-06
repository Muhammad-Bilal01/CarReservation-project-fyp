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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditVendorProfileActivity extends AppCompatActivity {
    EditText fullNameEt, cnicNoET, mobileNoET, addressET, accountTitleET, ibanET, bankNameET;
    Button btnUpdate;
    ImageButton backButton;

    CircleImageView profileImage;

    Uri filePath, downloadUri;
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vendor_profile2);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

//        Initialize Component
        initializeComponent();

//        Update Profile
        onClickUpdate();

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
                = new ProgressDialog(EditVendorProfileActivity.this);
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

                        String name = fullNameEt.getText().toString();
                        String cnicNo = cnicNoET.getText().toString();
                        String mobileNo = mobileNoET.getText().toString();
                        String address = addressET.getText().toString();
                        String accountTitle = accountTitleET.getText().toString();
                        String iban = ibanET.getText().toString();
                        String bank = bankNameET.getText().toString();


                        if (isValid) {

                            updateVendor(name, cnicNo, mobileNo, address, accountTitle, iban, bank,downloadUri.toString());

                        } else {
                            Toast.makeText(EditVendorProfileActivity.this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
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

    private void initializeComponent() {

        fullNameEt = findViewById(R.id.fullNameET);
        cnicNoET = findViewById(R.id.cnicNoET);
        mobileNoET = findViewById(R.id.mobileNoET);
        addressET = findViewById(R.id.addressET);
        accountTitleET = findViewById(R.id.accountTitleET);
        ibanET = findViewById(R.id.ibanET);
        bankNameET = findViewById(R.id.bankNameET);
        btnUpdate = findViewById(R.id.btnUpdate);
        backButton = findViewById(R.id.backButtonIB);
        profileImage = findViewById(R.id.profile_image);

//        Set Default value
        Intent intent = getIntent();
        fullNameEt.setText(intent.getStringExtra("name"));
        cnicNoET.setText(intent.getStringExtra("cnic"));
        mobileNoET.setText(intent.getStringExtra("mobile"));
        addressET.setText(intent.getStringExtra("address"));
        accountTitleET.setText(intent.getStringExtra("accountTilte"));
        ibanET.setText(intent.getStringExtra("accountNumber"));
        bankNameET.setText(intent.getStringExtra("bankName"));
        if (intent.getStringExtra("profileImage")!=""){
            Picasso.get().load(intent.getStringExtra("profileImage")).into(profileImage);
        };


    }

    private void onClickUpdate() {

        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        uploadImage();

//                        String name = fullNameEt.getText().toString();
//                        String cnicNo = cnicNoET.getText().toString();
//                        String mobileNo = mobileNoET.getText().toString();
//                        String address = addressET.getText().toString();
//                        String accountTitle = accountTitleET.getText().toString();
//                        String iban = ibanET.getText().toString();
//                        String bank = bankNameET.getText().toString();
//
//                        clearErrors();
//                        boolean isValid = verifyInformation();
//
//                        if (isValid) {
////                            updateVendor(name, cnicNo, mobileNo, address, accountTitle, iban, bank);
//
//                        } else {
//                            Toast.makeText(EditVendorProfileActivity.this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
//                        }
                    }
                }
        );


    }

    private void updateVendor(
            String fullName,
            String cnicNo,
            String mobileNo,
            String address,
            String account,
            String iban,
            String bank,
            String url
    ) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        Map<String, Object> data = new HashMap<>();
        data.put("fullName", fullName);
        data.put("cnicNo", cnicNo);
        data.put("mobileNo", mobileNo);
        data.put("address", address);
        data.put("accountTitle", account);
        data.put("iban", iban);
        data.put("bankName", bank);
        data.put("profileImage", url);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (user != null) {

            db.collection("Users").document(user.getUid()).update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditVendorProfileActivity.this, "Data Update Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditVendorProfileActivity.this, VendorDrawerActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditVendorProfileActivity.this, "Data Update Failed", Toast.LENGTH_SHORT).show();
                        }

                    });
        }
    }


    private boolean verifyInformation() {

        boolean flag = true;

        String fullName = fullNameEt.getText().toString();
        String cnicNo = cnicNoET.getText().toString();
        String mobileNo = mobileNoET.getText().toString();
        String address = addressET.getText().toString();
        String accountTitle = accountTitleET.getText().toString();
        String iban = ibanET.getText().toString();
        String bank = bankNameET.getText().toString();

        if (fullName.isEmpty()) {
            flag = false;
            fullNameEt.setError("Required");
        } else if (!fullName.matches("^[a-zA-Z ]+$")) {
            flag = false;
            fullNameEt.setError("Invalid Full Name");
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

        if (accountTitle.isEmpty()) {
            flag = false;
            accountTitleET.setError("Required");
        } else if (!accountTitle.matches("^[a-zA-Z ]+$")) {
            flag = false;
            accountTitleET.setError("Invalid Title");
        }

        if (iban.isEmpty()) {
            flag = false;
            ibanET.setError("Required");
        }

        if (bank.isEmpty()) {
            flag = false;
            bankNameET.setError("Required");
        } else if (!bank.matches("^[a-zA-Z ]+$")) {
            flag = false;
            bankNameET.setError("Invalid Bank Name");
        }

        return flag;
    }

    private void clearErrors() {

        fullNameEt.setError(null);
        cnicNoET.setError(null);
        mobileNoET.setError(null);
        addressET.setError(null);
        accountTitleET.setError(null);
        ibanET.setError(null);
        bankNameET.setError(null);
    }


}