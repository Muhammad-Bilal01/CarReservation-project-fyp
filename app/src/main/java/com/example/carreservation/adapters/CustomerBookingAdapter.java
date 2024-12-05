package com.example.carreservation.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carreservation.CustomerBookingDetails;
import com.example.carreservation.R;
import com.example.carreservation.fragments.ReviewBottomSheetFragments;
import com.example.carreservation.interfaces.OnItemClickListener;
import com.example.carreservation.interfaces.SelectBookingListener;
import com.example.carreservation.models.Booking;
import com.example.carreservation.models.SelectedSlot;
import com.example.carreservation.models.Spot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomerBookingAdapter extends RecyclerView.Adapter<CustomerBookingAdapter.CustomerBookingViewHolder> {

    private List<Booking> bookings;
    private SelectBookingListener listener;
    private Context mContext;

    public CustomerBookingAdapter(Context context, List<Booking> bookings, SelectBookingListener listener) {
        this.mContext = context;
        this.bookings = bookings;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomerBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_booking_item, parent, false);
        return new CustomerBookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerBookingViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Booking bookingViewModel = bookings.get(position);
        holder.bind(bookingViewModel);

        //            on item Click
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClicked(bookings.get(position), position);
            }
        });


//        on review button click
        holder.btnReview.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Objects.equals(bookingViewModel.getBookingStatus(), "Booked")) {
                            Dialog dialog = new Dialog(mContext);
                            dialog.setContentView(R.layout.custom_dialog_box);
                            dialog.show();

//                        Initialize the component
                            RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
                            EditText reviewMsg = dialog.findViewById(R.id.reviewTxt);
                            Button submitBtn = dialog.findViewById(R.id.submitBtn);

                            submitBtn.setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            float rating = ratingBar.getRating();
                                            Map<String, Object> reviewsData = new HashMap<String, Object>();
                                            reviewsData.put("customerId", bookingViewModel.getCustomerId());
                                            reviewsData.put("customerName",bookingViewModel.getCustomerName());
                                            reviewsData.put("spotId", bookingViewModel.getSpotId());
                                            reviewsData.put("vendorId", bookingViewModel.getVendorId());
                                            reviewsData.put("reviews", rating);
                                            reviewsData.put("message", reviewMsg.getText().toString());
                                            FirebaseFirestore.getInstance().collection("reviews").add(reviewsData).addOnSuccessListener(
                                                    new OnSuccessListener<DocumentReference>() {

                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            FirebaseFirestore.getInstance().collection("Bookings")
                                                                    .document(bookingViewModel.getId())
                                                                    .update("bookingStatus", "Success").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            Toast.makeText(mContext, "Status Update", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    });
                                                            Toast.makeText(mContext, "Thanks for Your Review", Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                        }
                                                    }
                                            );
//                                        System.out.println(reviewsData);
                                        }
                                    }
                            );
                        }

                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class CustomerBookingViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView, txtArriving, txtLeaving, txtCarRegistration, txtCarName, txtCarYear, txtTotalPrice;
        private TextView addressTextView;
        private TextView txtTimeLeft;
        private ImageView imgExpandClose, imgSpot;
        private ImageView imgInformation;
        private MaterialButton btnCompeleteOrIngProgres, btnReview, btnDuration;
        private MaterialCardView materialCardView;
        private RelativeLayout hideView;
        private CardView cardView;
        private Chip chipIsApproved;


        public CustomerBookingViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            nameTextView = itemView.findViewById(R.id.txt_spot_name);
            addressTextView = itemView.findViewById(R.id.txt_spot_address);
            txtTimeLeft = itemView.findViewById(R.id.txt_time_left);
            txtArriving = itemView.findViewById(R.id.txt_arriving_title);
            txtLeaving = itemView.findViewById(R.id.txt_leaving_title);
            txtCarName = itemView.findViewById(R.id.txt_car_name);
            txtCarRegistration = itemView.findViewById(R.id.txt_car_registration);
            txtCarYear = itemView.findViewById(R.id.txt_car_year);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            btnDuration = itemView.findViewById(R.id.txtDuration);
            btnReview = itemView.findViewById(R.id.btnReview);
            btnCompeleteOrIngProgres = itemView.findViewById(R.id.btnCompleteOrInProgress);
            materialCardView = itemView.findViewById(R.id.cardView);
            imgExpandClose = itemView.findViewById(R.id.img_open_close);
            imgSpot = itemView.findViewById(R.id.img_spot);
            materialCardView = itemView.findViewById(R.id.cardView);
            hideView = itemView.findViewById(R.id.hideView);
            chipIsApproved = itemView.findViewById(R.id.chipIsApproved);


//            Image expand collapse
            imgExpandClose.setOnClickListener(view -> {
                // If the CardView is already expanded, set its visibility
                // to gone and change the expand less icon to expand more.
                if (hideView.getVisibility() == View.VISIBLE) {
                    // The transition of the hiddenView is carried out by the TransitionManager class.
                    // Here we use an object of the AutoTransition Class to create a default transition
                    TransitionManager.beginDelayedTransition(materialCardView, new AutoTransition());
                    hideView.setVisibility(View.GONE);
                    imgExpandClose.setImageResource(R.drawable.baseline_keyboard_arrow_up_24);
                }

                // If the CardView is not expanded, set its visibility to
                // visible and change the expand more icon to expand less.
                else {
                    TransitionManager.beginDelayedTransition(materialCardView, new AutoTransition());
                    hideView.setVisibility(View.VISIBLE);
                    imgExpandClose.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                }
            });

        }


        public void bind(Booking booking) {
            nameTextView.setText(booking.getSpotName());
            addressTextView.setText(booking.getSpotAddress());
            chipIsApproved.setText(booking.getBookingStatus() != null ? booking.getBookingStatus() : "waiting");

//            for image
            if (booking.getSpotImages() != null && booking.getSpotImages().length() > 0)
                if (booking.getSpotImages().split("\\|").length > 0) {
                    String[] url = booking.getSpotImages().split("\\|");
                    Picasso.get().load(url[0]).into(imgSpot);
                } else
                    Picasso.get().load(booking.getSpotImages()).into(imgSpot);

//            for selected slots
            if (booking.getSelectedSlots() != null && booking.getSelectedSlots().size() > 0) {
                List<SelectedSlot> selectedSlots = booking.getSelectedSlots();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    selectedSlots.sort(Comparator.comparing(SelectedSlot::getSlotName));
                    String selectedDate = booking.getSelectedDate();
                    String[] time = selectedSlots.get(0).getSlotName().split("-");

                    LocalDateTime targetDateTime = parseDateTime(selectedDate, time[0]);

                    // Get the current date and time
                    LocalDateTime currentDateTime = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        currentDateTime = LocalDateTime.now();
                    }

                    // Calculate the duration between the current time and the target time
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        Duration timeLeft = Duration.between(currentDateTime, targetDateTime);
                        if (!timeLeft.isNegative()) {
                            txtTimeLeft.setText(formatDuration(timeLeft));
                        } else {
                            txtTimeLeft.setText("Time Left: 0 min");
                        }
                        txtArriving.setText("Arriving\n" + selectedDate + " " + time[0]);
                        txtLeaving.setText("Leaving\n" + selectedDate + " " + selectedSlots.get(selectedSlots.size() - 1).getSlotName().split("-")[1]);
                        btnDuration.setText(selectedSlots.size() + " h");



                        if (timeLeft.isNegative()) {
                            btnReview.setVisibility(View.VISIBLE);
                            btnCompeleteOrIngProgres.setText("Completed");
                        } else {
                            btnReview.setVisibility(View.GONE);
                            btnCompeleteOrIngProgres.setText("In-Progress");
                        }
                    }

                } else {
                    txtTimeLeft.setText("");
                }
                if (Objects.equals(booking.getBookingStatus(), "Success") || Objects.equals(booking.getBookingStatus(), "Pending")){
                    btnReview.setVisibility(View.GONE);

                }
                txtCarRegistration.setText(booking.getVehicleRegistrationNumber());
                txtTotalPrice.setText("PKR " + booking.getTotalAmount().toString());

    /*    if (booking.getSpotImages().length() > 0)
            if (booking.getSpotImages().split("|").length > 0) {
                String[] url = booking.getSpotImages().split("\\|");
                Picasso.get().load(url[0]).into(imgSpot);
            } else
                Picasso.get().load(booking.getSpotImages()).into(imgSpot);

                //hourlyChargeTextView.setText("Hourly Charge: $" + booking.getWeeklySlots();
           */

            }


        }


        private static LocalDateTime parseDateTime(String dateStr, String timeStr) {

            // Parse the date string
            LocalDate date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                date = LocalDate.parse(dateStr);
            }

            // Parse the time string and adjust the date if needed
            LocalTime time = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                // Split the time string by space
                String[] parts = timeStr.split("\\s+");

                // Parse the hour and AM/PM indicator
                int hour = Integer.parseInt(parts[0]);
                boolean isAM = parts[1].equalsIgnoreCase("AM");

                // Adjust hour for PM
                if (!isAM && hour < 12) {
                    hour += 12;
                }

                // Parse the time string into LocalTime
                // Construct LocalTime object
                time = LocalTime.of(hour % 24, 0);

//                System.out.println("Parsed LocalTime: " + time);


            }

            LocalDateTime dateTime = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                dateTime = LocalDateTime.of(date, time);
            }

            return dateTime;
        }

        private static String formatDuration(Duration duration) {
            long days = 0, hours = 0, minutes = 0, seconds = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                days = duration.toDays();
                hours = duration.toHours() % 24;
                minutes = (duration.toMinutes() % 60);
                seconds = (duration.getSeconds() % 60);
            }


            return String.format("Time Left: %02d d :%02d h :%02d m", days, hours, minutes);
        }

        public static String trimEnd(String s, String suffix) {

            if (s.endsWith(suffix)) {

                return s.substring(0, s.length() - suffix.length());

            }
            return s;
        }

    }
}

