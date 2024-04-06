package com.example.carreservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carreservation.models.Booking;
import com.example.carreservation.models.SelectedSlot;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class CustomerBookingDetails extends AppCompatActivity {

    private ImageView backBtn, imgSpot,payment_image;
    private TextView spotNameTxt, spotAddressTxt, spotNo, arrivingTime, leavingTime, amountTxt, seller_name, seller_id,payment_text;

    private LinearLayout payment_box;

    Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_booking_details);
//        Map view with data
        intialization();

        Intent intent = getIntent();
        String id = intent.getStringExtra("documentId");
//        System.out.println("Id " + id);

        DocumentReference dataref = FirebaseFirestore.getInstance().collection("Bookings").document(id);
        dataref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                booking = documentSnapshot.toObject(Booking.class);
                booking.setId(documentSnapshot.getId());
//                System.out.println("____________+++++______");
//                System.out.println(booking.toString());

                spotNameTxt.setText(booking.getSpotName());
                spotAddressTxt.setText(booking.getSpotAddress());
                amountTxt.setText(booking.getTotalAmount().toString());
                spotNo.setText("Spot # "+ booking.getSpotId());
//                set spot images
                Picasso.get().load(booking.getSpotImages()).into(imgSpot);
//                set arriving and leaving time
                List<SelectedSlot> selectedSlots = booking.getSelectedSlots();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    selectedSlots.sort(Comparator.comparing(SelectedSlot::getSlotName));
                    String selectedDate = booking.getSelectedDate().replace("-"," ");
                    String[] time = selectedSlots.get(0).getSlotName().split("-");

                    String arrTime = selectedDate + " " + time[0];
                    arrivingTime.setText(arrTime);

                    String leaveTime = selectedDate + " " + time[1];
                    leavingTime.setText(leaveTime);
                }

//                Set payment Method

//                if (booking.getPaymentMode().equalsIgnoreCase("Online Payment")){
                        seller_name.setText(booking.getCustomerName());
                        seller_id.setText(booking.getPaymentMode());
                    Picasso.get().load(booking.getPaymentScreenShotUrl()).into(payment_image);
//                }
//                else{
//                    payment_box.setVisibility(View.GONE);
//                    payment_text.setVisibility(View.GONE);
//                    seller_name.setVisibility(View.GONE);
//                    seller_id.setVisibility(View.GONE);
//                    payment_image.setVisibility(View.GONE);
//                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerBookingDetails.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });


//        On back pressed
        onBackPress();
    }

    void intialization() {
        backBtn = findViewById(R.id.back_btn);
        imgSpot = findViewById(R.id.img_spot);
        spotAddressTxt = findViewById(R.id.txt_spot_address);
        spotNameTxt = findViewById(R.id.txt_spot_name);
        spotNo = findViewById(R.id.spotNo);
        arrivingTime = findViewById(R.id.arriving_time);
        leavingTime = findViewById(R.id.leaving_time);
        amountTxt = findViewById(R.id.amountTxt);
        payment_box = findViewById(R.id.payment_box);
        payment_image = findViewById(R.id.payment_img);
        seller_name = findViewById(R.id.seller_name);
        seller_id = findViewById(R.id.seller_id);
        payment_text = findViewById(R.id.payment_text);
    }

    void onBackPress() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}