package com.example.carreservation.adapters;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carreservation.R;
import com.example.carreservation.interfaces.OnItemClickListener;
import com.example.carreservation.interfaces.VendorBookingListner;
import com.example.carreservation.models.Booking;
import com.example.carreservation.models.VendorBooking;
import com.example.carreservation.models.SelectedSlot;
import com.example.carreservation.models.Spot;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class VendorBookingAdapter extends RecyclerView.Adapter<VendorBookingAdapter.VendorBookingViewHolder> {

    private List<Booking> bookings;
    private VendorBookingListner vendorBookingListner;
    public VendorBookingAdapter(List<Booking> bookings, VendorBookingListner vendorBookingListner) {
        this.bookings = bookings;
        this.vendorBookingListner = vendorBookingListner;
    };

    @NonNull
    @Override
    public VendorBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_booking_item, parent, false);
        return new VendorBookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorBookingViewHolder holder, @SuppressLint("RecyclerView")  int position) {
        Booking bookingViewModel = bookings.get(position);
        holder.bind(bookingViewModel);

//        on card click
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendorBookingListner.onItemClicked(bookings.get(position),position);
            }
        });

        if (bookingViewModel.getBookingStatus() != null && bookingViewModel.getBookingStatus().equalsIgnoreCase("pending")) {
//            holder.btnStatus.setText("Check");
                holder.btnStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vendorBookingListner.onItemApproveClicked( bookings.get(position), position);
                        holder.btnStatus.setText("Approved");
                    }
                });
        }



    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class VendorBookingViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView, txtArriving, txtLeaving, txtCarRegistration, txtCarName, txtCarYear;
        private TextView addressTextView;
        private TextView txtTimeLeft;
        private ImageView imgInformation,img_spot;
        private MaterialButton btnAction, btnStatus, btnDuration;
        private MaterialCardView materialCardView;
        private RelativeLayout hideView;


        public VendorBookingViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.txtUsername);
            addressTextView = itemView.findViewById(R.id.txtSpotId);
            txtTimeLeft = itemView.findViewById(R.id.txt_time_left);
            txtArriving = itemView.findViewById(R.id.txt_arriving_title);
            txtLeaving = itemView.findViewById(R.id.txt_leaving_title);
            txtCarName = itemView.findViewById(R.id.txt_car_name);
            txtCarRegistration = itemView.findViewById(R.id.txt_car_registration);
            txtCarYear = itemView.findViewById(R.id.txt_car_year);
            btnDuration = itemView.findViewById(R.id.txtDuration);
            btnStatus = itemView.findViewById(R.id.btnStatus);
            btnAction = itemView.findViewById(R.id.btnAction);
            materialCardView = itemView.findViewById(R.id.cardView);
            imgInformation=itemView.findViewById(R.id.img_more_info);
            img_spot=itemView.findViewById(R.id.img_spot);
            materialCardView=itemView.findViewById(R.id.cardView);
            hideView=itemView.findViewById(R.id.hideView);
        }


        public void bind(Booking booking) {
            nameTextView.setText(booking.getCustomerName());
            addressTextView.setText("Spot# "+booking.getSpotId());
            if (booking.getSelectedSlots() != null && booking.getSelectedSlots().size()>0) {
                List<SelectedSlot> selectedSlots=booking.getSelectedSlots();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    selectedSlots.sort(Comparator.comparing(SelectedSlot::getSlotName));
                    String selectedDate=booking.getSelectedDate();
                    String[] time=selectedSlots.get(0).getSlotName().split("-");
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
                        }else{
                            txtTimeLeft.setText("Time Left: 0 min");
                        }
                        txtArriving.setText("Arriving\n"+selectedDate+" "+time[0]);
                        txtLeaving.setText("Leaving\n"+selectedDate+" "+selectedSlots.get(selectedSlots.size()-1).getSlotName().split("-")[1]);
                        btnDuration.setText(selectedSlots.size()+" h");


                        if (booking.getBookingStatus() != null && booking.getBookingStatus().equalsIgnoreCase("pending")) {
                            btnStatus.setText("Approve");
                        }else if(booking.getBookingStatus() != null && booking.getBookingStatus().equalsIgnoreCase("Booked")){
                            btnStatus.setText("Approved");
                        }


                        if (timeLeft.isNegative() && !booking.isBookingCompleted())
                        {
//                            btnStatus.setText("Car didn't left");
                            //btnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fffff")));
                            //btnStatus.setForegroundTintList(ColorStateList.valueOf(Color.parseColor("#00000")));

                            btnAction.setText("Call for action");
                            //materialCardView.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                        }
                        else if(timeLeft.toDays()==0 && timeLeft.toHours()%24<=selectedSlots.size()) {
//                            btnStatus.setText("Car parked");
                            //btnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#008000")));
                            //btnStatus.setForegroundTintList(ColorStateList.valueOf(Color.parseColor("#fffff")));

                            btnAction.setText("Check authenticity");
                            //materialCardView.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#00000")));
                        }
                        else
                        {
//                            btnStatus.setText("Not Parked");
                            //btnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#008000")));
                            //btnStatus.setForegroundTintList(ColorStateList.valueOf(Color.parseColor("#fffff")));

                            btnAction.setText("Check authenticity");
                        }

                    }

                }
            } else
                txtTimeLeft.setText("");
            txtCarRegistration.setText(booking.getVehicleRegistrationNumber());
        if (booking.getSpotImages().length() > 0)
            if (booking.getSpotImages().split("|").length > 0) {
                String[] url = booking.getSpotImages().split("\\|");
                Picasso.get().load(url[0]).into(img_spot);
            } else
                Picasso.get().load(booking.getSpotImages()).into(img_spot);

            //hourlyChargeTextView.setText("Hourly Charge: $" + booking.getWeeklySlots();
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
//                    time = LocalTime.parse(timeStr, formatter);
            System.out.println("Parsed LocalTime: " + time);


        }

        LocalDateTime dateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.of(date, time);
        }

        return dateTime;
    }

    private static String formatDuration(Duration duration) {
        long days=0,hours = 0, minutes=0, seconds=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            days=duration.toDays();
            hours = duration.toHours()%24;
            minutes = (duration.toMinutes() % 60);
            seconds = (duration.getSeconds() % 60);
        }


        return String.format("Time Left: %02d d :%02d h :%02d m", days, hours, minutes);
    }
    public static String trimEnd( String s,  String suffix) {

        if (s.endsWith(suffix)) {

            return s.substring(0, s.length() - suffix.length());

        }
        return s;
    }

}

