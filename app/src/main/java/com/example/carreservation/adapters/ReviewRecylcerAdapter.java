package com.example.carreservation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carreservation.R;
import com.example.carreservation.models.Reviews;

import java.util.ArrayList;

public class ReviewRecylcerAdapter extends RecyclerView.Adapter<ReviewRecylcerAdapter.RecyclerViewSpotHolder> {

    private ArrayList<Reviews> reviewsArrayList;
    private Context context;

    public ReviewRecylcerAdapter(Context context , ArrayList<Reviews> reviewsArrayList) {
        this.context = context;
        this.reviewsArrayList = reviewsArrayList;

    }

    @NonNull
    @Override
    public ReviewRecylcerAdapter.RecyclerViewSpotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.custom_reviews ,parent,false);

        return new RecyclerViewSpotHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRecylcerAdapter.RecyclerViewSpotHolder holder, int position) {
        Reviews review = reviewsArrayList.get(position);
        holder.rating.setText(review.getRating());
        holder.review.setText(review.getMessage());
        holder.username.setText(review.getUsername());
    }

    @Override
    public int getItemCount() {
        return reviewsArrayList.size();
    }

    public class RecyclerViewSpotHolder extends RecyclerView.ViewHolder {
        TextView review;
        TextView rating;
        TextView username;
        public RecyclerViewSpotHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_text);
            rating = itemView.findViewById(R.id.rating_text);
            review = itemView.findViewById(R.id.review_text);
        }
    }
}
