package com.example.carreservation.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.carreservation.R;
import com.example.carreservation.UserReviewActivity;
import com.example.carreservation.interfaces.OnItemClickListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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
        private TextView reviewTextView;
        private TextView ratingText;
        private TextView userText;
        private TextView addressTextView;
        private TextView txtSpotsLeft;
        private TextView txtPricePerHour;
        private ImageView imgSpot;
        private ImageView imgInformation;
        private MaterialButton btnBook;
        private MaterialCardView materialCardView;

            private String globalSpotId;

        //        Firebase
        private final FirebaseFirestore db = FirebaseFirestore.getInstance();
        ;


        public SearchSpotViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.txt_spot_name);
            reviewTextView = itemView.findViewById(R.id.review_txt);
            ratingText = itemView.findViewById(R.id.rating_txt);
            userText = itemView.findViewById(R.id.noOfUser_txt);
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
                        onItemClickListener.onMyBookButtonClick(paringSpotList.get(getAdapterPosition()), getAdapterPosition());
                    }
                });

                reviewTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(reviewTextView.getContext(), "Review", Toast.LENGTH_SHORT).show();
                        System.out.println("GLOBAL ID ==== "+ globalSpotId);
                        Intent intent = new Intent(reviewTextView.getContext(), UserReviewActivity.class);
                        intent.putExtra("spotId", globalSpotId);
                        reviewTextView.getContext().startActivity(intent);

                    }
                });

                btnBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        onItemClickListener.onBookButtonClick(spotList.get(getAdapterPosition()),getAdapterPosition());
                        onItemClickListener.onMyBookButtonClick(paringSpotList.get(getAdapterPosition()), getAdapterPosition());
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
                onItemClickListener.onMyBookButtonClick(paringSpotList.get(getAdapterPosition()), getAdapterPosition());
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


        //        Get Reviews from Database
        public void getReviews(String id) {

            //        Get Reviews
            List<Double> reviewsList = new ArrayList<>();

            db.collection("reviews").whereEqualTo("spotId", id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    System.out.println("Review Data ---> ");
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        System.out.println("ID === > " + id);
                        System.out.println(list.toString());
                        for (DocumentSnapshot d : list) {
                            Map<String, Object> reviewsData = d.getData();

                            reviewsList.add(Double.parseDouble(reviewsData.get("reviews").toString()));
                        }
                        Double reviews = calculateAverageRating(reviewsList);
                        int users = reviewsList.size();
                        System.out.println("Reviews Data --> " + reviews + " users" + users);

                        ratingText.setText(reviews.toString());
                        userText.setText(String.valueOf(users));


                    } else {
                        System.out.println("Document Snapshot is empty");
                        ratingText.setText("0");
                        userText.setText("0");

                    }
                }
            });
        }

        //        Calculate Average Ratings
        public double calculateAverageRating(List<Double> ratings) {
            if (ratings == null || ratings.isEmpty()) {
                return 0.0; // Return 0 if the list is empty (to avoid division by zero)
            }

            // Calculate the sum of all ratings
            int sum = 0;
            for (double rating : ratings) {
                sum += rating;
            }

            // Calculate the average by dividing the sum by the number of ratings
            double average = (double) sum / ratings.size();

            return average;
        }

        //        Bind Values with Data
        public void bind(Map<String, Object> spot) {
            String spotId = "";
            String spotTitle = "";
            String spotAddress = "";
            List<Map<String, Object>> totalSlots;
            String freeSlots = "";
            String spotImage = "";
            String pricePerHour = "";
            String reviews = "";


            for (Map.Entry<String, Object> mySpot :
                    spot.entrySet()) {
                String key = mySpot.getKey();
                Object value = mySpot.getValue();


                switch (key) {
                    case "id":
                        spotId = mySpot.getValue().toString();

                        globalSpotId = spotId;
                        System.out.println("ID ===> " + globalSpotId);
                        getReviews(spotId);
                        break;
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
//            ratingText.setText(reviews);
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
