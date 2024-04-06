package com.example.carreservation;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carreservation.vendordashboard.vendoritemclass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AddVendorService extends AppCompatActivity {

    //private Button image_upload_button;
    //private ImageView upload_image;

    ImageView upload_image;
    ImageButton btn_camera;
    ProgressBar loader;


    private EditText parking_Name;
    private EditText location;
    private  EditText slot;
    private EditText hourly_Price;
    SimpleDateFormat date_Picker;
    private Button submit_Button;

    LinearLayout startDatePicker;
    TextView showstartDate;

    String st_date;

    FirebaseAuth auth;
    FirebaseUser user;

    StorageReference storage;
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri storeUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor_service);

       // image_upload_button = findViewById(R.id.image_upload_button);
       // upload_image = findViewById(R.id.uploadimage);

        upload_image = findViewById(R.id.uploadimage);
        btn_camera = findViewById(R.id.btn_camera);


        parking_Name=findViewById(R.id.name);
        location = findViewById(R.id.location);
        slot = findViewById(R.id.slot);
        hourly_Price = findViewById(R.id.hourly_price);
        submit_Button = findViewById(R.id.submit_button);

        startDatePicker = findViewById(R.id.date_picker);
        showstartDate = findViewById(R.id.selectedDateTextView);
        loader = findViewById(R.id.loader);



        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        storage = FirebaseStorage.getInstance().getReference("Service Pictures");
//        loader.setVisibility(View.VISIBLE);
//
//        Uri uri = user.getPhotoUrl();
//        if (uri!=null) {
//            Picasso.get().load(uri).into(upload_image);
//            loader.setVisibility(View.GONE);
//        }
//        else{
//            loader.setVisibility(View.GONE);
//        }


        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
                loader.setVisibility(View.VISIBLE);
                //uploadToFirebase(storeUri);
            }
        });


        submit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertParkingArea();
            }
        });
        startDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDatePicker();
            }
        });

        date_Picker = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.US);

    }

    private void insertParkingArea(){
        String _parking_Name = parking_Name.getText().toString();
        String _location = location.getText().toString();
        String _slot = slot.getText().toString();
        String _hourly_Price = hourly_Price.getText().toString();

        //FirebaseUser userid = auth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        clearErrors();
        st_date = showstartDate.getText().toString();
        Boolean isValid = verifyInformation();

        if (isValid == true)
            if (TextUtils.isEmpty(st_date)) {
                Toast.makeText(AddVendorService.this, "Please select date", Toast.LENGTH_LONG).show();
            }else{
                vendoritemclass vendoritem = new vendoritemclass(_parking_Name, _location, _slot, st_date, _hourly_Price,  storeUri, user);
                Map<String, Object> data = new HashMap<>();
                data.put("parkingName", _parking_Name);
                data.put("location", _location);
                data.put("slot", _slot);
                data.put("date", st_date);
                data.put("hourlyRate", _hourly_Price);
                data.put("image", storeUri);
                data.put("userId", user);
                db.collection("Parking Service").document(user.getUid()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddVendorService.this, "Service Uploaded", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AddVendorService.this,VendorDashboard.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(AddVendorService.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        else{
            Toast.makeText(AddVendorService.this, "Please fill all fields", Toast.LENGTH_SHORT).show();

        }
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uriImage = data.getData();
            upload_image.setImageURI(uriImage);
            storeUri = uriImage;
            Log.i("uriImage", uriImage.toString());
            Log.i("uri", storeUri.toString());
            uploadToFirebase(storeUri);

        }
    }


    private void showStartDatePicker(){

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Handle the selected date
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, monthOfYear, dayOfMonth);
                        String formattedDate = date_Picker.format(selectedCalendar.getTime());
                        showstartDate.setText(formattedDate);
                        showstartDate.setVisibility(View.VISIBLE);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }


    private boolean verifyInformation() {

        String _parking_Name = parking_Name.getText().toString();
        String _location = location.getText().toString();
        String _slot = slot.getText().toString();
        String _hourly_Price = hourly_Price.getText().toString();

        boolean flag = true;

        if (_parking_Name.toString().isEmpty())
        {
            flag = false;
            parking_Name.setError("Required");
        }
        if (_location.toString().isEmpty() )
        {
            flag = false;
            location.setError("Required");
        }
        if (_slot.toString().isEmpty())
        {
            flag = false;
            slot.setError("Required");
        }
        if (_hourly_Price.toString().isEmpty())
        {
            flag = false;
            hourly_Price.setError("Required");
        }


        return flag;
    }

    private void clearErrors() {

        parking_Name.setError(null);
        location.setError(null);
        slot.setError(null);
        hourly_Price.setError(null);
    }

    private void uploadToFirebase(Uri uri)
    {
        if (uri!=null) {

            Log.i("uploadUri",uri.toString());
            StorageReference fileReference = storage.child(user.getUid() + "."
                    + getFileExtension(uri));

            //upload file to storage
            fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri).build();

                            user.updateProfile(profileUpdates);
                        }
                    });

                }
            });
            loader.setVisibility(View.GONE);
            Toast.makeText(this,"Profile Picture Uploaded Successfully", Toast.LENGTH_LONG).show();
        }
        else{
            Log.i("Info", "Uri is Empty "+uri);
        }
    }

    private String getFileExtension(Uri uriImage){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        Log.i("imageUri", mime.getExtensionFromMimeType(cR.getType(uriImage)));
        return mime.getExtensionFromMimeType(cR.getType(uriImage));
    }





//    private void addparking(String parking_Name, String location, String slot_Number, String date, String hourly_Price, String image){
//        // Create a Tournament object
//        vendoritemclass tournament = new vendoritemclass(parking_Name, location, slot_Number, date, hourly_Price, image);
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Parking Places");
//
//        // Push the tournament to Firebase
//        String key = databaseReference.push().getKey();
//        databaseReference.child(key).setValue(tournament, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@NonNull DatabaseError error, @NonNull DatabaseReference ref) {
//                loader.setVisibility(View.GONE);
//                if (error == null) {
//                    Toast.makeText(AddVendorService.this, "Tournament added successfully", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(AddVendorService.this, VendorDashboard.class);
//                    startActivity(intent);
//                    finish();
//
//                } else {
//                    Toast.makeText(AddVendorService.this, "Failed to add tournament. Please try again.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


   // }
}