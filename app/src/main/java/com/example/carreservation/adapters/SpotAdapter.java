package com.example.carreservation.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.carreservation.R;
import com.example.carreservation.UserReviewActivity;
import com.example.carreservation.interfaces.OnItemClickListener;
import com.example.carreservation.models.Spot;
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

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotViewHolder> {

    //    private List<Spot> spotList;
    private List<Map<String, Object>> spotList;

    // 1 mean used for spot details fragment
    //2 means used for spot delete fragment
    public static int AdapterUsedFor;
    private OnItemClickListener onItemClickListener;

    public SpotAdapter(List<Map<String, Object>> spotList, int adapterUsedFor) {
        this.spotList = spotList;
        this.AdapterUsedFor = adapterUsedFor;
    }

    // Method to set the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public SpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spot_item, parent, false);
        return new SpotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpotViewHolder holder, int position) {
//        Spot spot = spotList.get(position);
        Map<String, Object> spot = spotList.get(position);
        holder.bind(spot);
    }

    @Override
    public int getItemCount() {
        return spotList.size();
    }

    public class SpotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameTextView;

        private TextView reviewTextView;
        private TextView ratingText;
        private TextView userText;
        private TextView addressTextView;
        private ImageView imgSpot;
        private MaterialButton btnDetails;
        private MaterialCardView cardSpot;

        private final FirebaseFirestore db = FirebaseFirestore.getInstance();

        private String globalSpotId;


        public SpotViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.txt_spot_name);
            reviewTextView = itemView.findViewById(R.id.review_txt);
            ratingText = itemView.findViewById(R.id.rating_txt);
            userText = itemView.findViewById(R.id.noOfUser_txt);
            addressTextView = itemView.findViewById(R.id.txt_spot_address);
            imgSpot = itemView.findViewById(R.id.img_spot);
            btnDetails = itemView.findViewById(R.id.btnDetails);
            cardSpot = itemView.findViewById(R.id.spotCard);
            itemView.setOnClickListener(this);

//            review button click
            reviewTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(reviewTextView.getContext(), "Review", Toast.LENGTH_SHORT).show();
                    System.out.println("GLOBAL ID ==== " + globalSpotId);
                    Intent intent = new Intent(reviewTextView.getContext(), UserReviewActivity.class);
                    intent.putExtra("spotId", globalSpotId);
                    reviewTextView.getContext().startActivity(intent);

                }
            });

            if (AdapterUsedFor == 1) {
                cardSpot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        onItemClickListener.onUpdateButtonClick(spotList.get(getAdapterPosition()),getAdapterPosition());
                    }
                });
                btnDetails.setText(" Update ");
                btnDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("++++++++++++++");
                        onItemClickListener.onUpdateButtonClick(spotList.get(getAdapterPosition()), getAdapterPosition());
                    }
                });

            } else if (AdapterUsedFor == 2) {
                btnDetails.setText(" Details ");
                btnDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onDetailsButtonClick(spotList.get(getAdapterPosition()), getAdapterPosition());
                    }
                });

            }


        }

        @Override
        public void onClick(View view) {
            if (AdapterUsedFor == 1)
                onItemClickListener.onUpdateButtonClick(spotList.get(getAdapterPosition()), getAdapterPosition());
//            else if(AdapterUsedFor==2)
//                onItemClickListener.onDetailsButtonClick(spotList.get(getAdapterPosition()),getAdapterPosition());

        }

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


        public void bind(Map<String, Object> spot) {

//            System.out.println("Spot View ===> " + spot.get("id"));
//            System.out.println("ID ===> " + globalSpotId);

            globalSpotId = spot.get("id").toString();
            getReviews(globalSpotId);

            nameTextView.setText(spot.get("spotTitle").toString());
            addressTextView.setText(spot.get("spotAddress").toString());
            if (spot.get("spotImages").toString().length() > 0)
                if (spot.get("spotImages").toString().split("|").length > 0) {
                    String[] url = spot.get("spotImages").toString().split("\\|");
                    Picasso.get().load(url[0]).into(imgSpot);
                } else
                    Picasso.get().load(spot.get("spotImages").toString()).into(imgSpot);

            //hourlyChargeTextView.setText("Hourly Charge: $" + spot.getWeeklySlots();
        }


    }
}
