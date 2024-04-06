package com.example.carreservation.adapters;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.carreservation.R;
import com.example.carreservation.interfaces.OnItemClickListener;
import com.example.carreservation.models.AvailableSlot;
import com.example.carreservation.models.Spot;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchSpotAdapter extends RecyclerView.Adapter<SearchSpotAdapter.SearchSpotViewHolder> {

//    private List<Spot> spotList;

    private List<Map<String, Object>> paringSpotList;

    // 1 mean used for spot details fragment
    //2 means used for spot delete fragment
    public static int AdapterUsedFor;
    private OnItemClickListener onItemClickListener;
//    public SearchSpotAdapter(List<Spot> spotList, int adapterUsedFor) {
//        this.spotList = spotList;
//        this.AdapterUsedFor=adapterUsedFor;
//    }

    public SearchSpotAdapter(List<Map<String, Object>> paringSpotList, int adapterUsedFor) {
        this.paringSpotList = paringSpotList;
        this.AdapterUsedFor = adapterUsedFor;
    }

    // Method to set the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public SearchSpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_booking_spot_item, parent, false);
        return new SearchSpotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchSpotViewHolder holder, int position) {
//        System.out.println("position : "+ position);
//        Spot spot = spotList.get(position);
        Map<String, Object> parkingSpot = paringSpotList.get(position);
        holder.bind(parkingSpot);
    }

    @Override
    public int getItemCount() {
        return paringSpotList.size();
    }

    public class SearchSpotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameTextView;
        private TextView addressTextView;
        private TextView txtSpotsLeft;
        private TextView txtPricePerHour;
        private ImageView imgSpot;
        private ImageView imgInformation;
        private MaterialButton btnBook;
        private MaterialCardView materialCardView;


        public SearchSpotViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.txt_spot_name);
            addressTextView = itemView.findViewById(R.id.txt_spot_address);
            txtSpotsLeft = itemView.findViewById(R.id.txt_spot_left);
            txtPricePerHour = itemView.findViewById(R.id.txt_price_per_hour);
            imgSpot = itemView.findViewById(R.id.img_spot);
            imgInformation = itemView.findViewById(R.id.img_more_info);
            btnBook = itemView.findViewById(R.id.btnBook);
            materialCardView = itemView.findViewById(R.id.cardView);
            itemView.setOnClickListener(this);

            if (AdapterUsedFor == 1) {
                materialCardView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
//                        onItemClickListener.onBookButtonClick(spotList.get(getAdapterPosition()),getAdapterPosition());
                        System.out.println("Clicked...");
                        onItemClickListener.onMyBookButtonClick(paringSpotList.get(getAdapterPosition()),getAdapterPosition());
                    }
                });

                btnBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        onItemClickListener.onBookButtonClick(spotList.get(getAdapterPosition()),getAdapterPosition());
                        onItemClickListener.onMyBookButtonClick(paringSpotList.get(getAdapterPosition()),getAdapterPosition());
                    }
                });
                imgInformation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        onItemClickListener.onDetailsButtonClick(spotList.get(getAdapterPosition()),getAdapterPosition());
                    }
                });

            }
           /* else if(AdapterUsedFor==2)
            {
                btnDetails.setText(" Delete ");

            }*/


        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btnBook) {
//                onItemClickListener.onDetailsButtonClick(spotList.get(getAdapterPosition()),getAdapterPosition());
                onItemClickListener.onMyBookButtonClick(paringSpotList.get(getAdapterPosition()),getAdapterPosition());
            }
        }
       /* public void bind(Spot spot) {
            nameTextView.setText(spot.getSpotName());
            addressTextView.setText(spot.getSpotAddress());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N && spot.getAvailableSlots()!=null) {
                long count=spot.getAvailableSlots().stream().filter(x->!x.getIsbooked()).count();
                txtSpotsLeft.setText(count+" Spots Left");
            }
            else
                txtSpotsLeft.setText("");
            if(spot.getSpotImages().length()>0)
                if(spot.getSpotImages().split("|").length>0)
                {
                    String[] url=spot.getSpotImages().split("\\|");
                    Picasso.get().load(url[0]).into(imgSpot);
                }
                else
                    Picasso.get().load(spot.getSpotImages()).into(imgSpot);

            //hourlyChargeTextView.setText("Hourly Charge: $" + spot.getWeeklySlots();
        } */

//        Bind Values with Data
        public void bind(Map<String, Object> spot) {
            String spotTitle = "";
            String spotAddress = "";
            List<Map<String, Object>> totalSlots;
            String freeSlots = "";
            String spotImage = "";
            String pricePerHour = "";

            for (Map.Entry<String, Object> mySpot :
                    spot.entrySet()) {
                String key = mySpot.getKey();
                Object value = mySpot.getValue();

                switch (key) {
                    case "spotTitle":
                        spotTitle = mySpot.getValue().toString();
                        break;
                    case "spotAddress":
                        spotAddress = mySpot.getValue().toString();
                        break;
                    case "totalSlots":
                        totalSlots = (List<Map<String, Object>>) mySpot.getValue();
                        freeSlots = String.valueOf(calculateIsBookedFalseCount(totalSlots));
                        System.out.println("totalSlots: " + totalSlots.toString());
                        break;
                    case "spotImages":
                        spotImage = mySpot.getValue().toString();
                        System.out.println("spotImages: " + mySpot.getValue());
                        break;
                    case "PricePerHalfMinute":
                        pricePerHour = mySpot.getValue().toString();
                        break;

                }
//                System.out.println("Key: " + key + ", Value: " + value);

            }
            nameTextView.setText(spotTitle);
            addressTextView.setText(spotAddress);
            txtSpotsLeft.setText(freeSlots + " Spots Left");
            txtPricePerHour.setText("PKR " + pricePerHour + "/hour");
//            Picasso.get().load(spotImage).into(imgSpot);

            if (spotImage.length() > 0)
                if (spotImage.split("|").length > 0) {
                    String[] url = spotImage.split("\\|");
                    Picasso.get().load(url[0]).into(imgSpot);
                } else
                    Picasso.get().load(spotImage).into(imgSpot);
//
//            //hourlyChargeTextView.setText("Hourly Charge: $" + spot.getWeeklySlots();
//        }


//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N && spot.getAvailableSlots()!=null) {
//                long count=spot.getAvailableSlots().stream().filter(x->!x.getIsbooked()).count();
//                txtSpotsLeft.setText(count+" Spots Left");
//            }
//            else
//                txtSpotsLeft.setText("");
//            if(spot.getSpotImages().length()>0)
//                if(spot.getSpotImages().split("|").length>0)
//                {
//                    String[] url=spot.getSpotImages().split("\\|");
//                    Picasso.get().load(url[0]).into(imgSpot);
//                }
//                else
//                    Picasso.get().load(spot.getSpotImages()).into(imgSpot);
//
//            //hourlyChargeTextView.setText("Hourly Charge: $" + spot.getWeeklySlots();
//        }

        }

        // Function to calculate the count of 'isBooked=false'
        private int calculateIsBookedFalseCount(List<Map<String, Object>> totalSlots) {
            int count = 0;

            // Iterate through the 'totalSlots' list
            for (Map<String, Object> slot : totalSlots) {
                // Check if the 'slots' key is present and its value is a list
                if (slot.containsKey("slots") && slot.get("slots") instanceof List) {
                    List<Map<String, Object>> slots = (List<Map<String, Object>>) slot.get("slots");

                    // Iterate through the 'slots' list
                    for (Map<String, Object> slotDetails : slots) {
                        // Check if the 'availableSlots' key is present and its value is a list
                        if (slotDetails.containsKey("availableSlots") && slotDetails.get("availableSlots") instanceof List) {
                            List<Map<String, Object>> availableSlots = (List<Map<String, Object>>) slotDetails.get("availableSlots");

                            // Iterate through the 'availableSlots' list
                            for (Map<String, Object> availableSlot : availableSlots) {
                                // Check if the 'isBooked' key is present and its value is a boolean
                                if (availableSlot.containsKey("isBooked") && availableSlot.get("isBooked") instanceof Boolean) {
                                    // Check if 'isBooked' is false
                                    if (!(Boolean) availableSlot.get("isBooked")) {
                                        count++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return count;
        }

    }
}
