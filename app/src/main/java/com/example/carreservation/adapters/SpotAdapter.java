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
import com.example.carreservation.models.Spot;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotViewHolder> {

//    private List<Spot> spotList;
    private List<Map<String,Object>> spotList;

    // 1 mean used for spot details fragment
    //2 means used for spot delete fragment
    public static int AdapterUsedFor;
    private OnItemClickListener onItemClickListener;
    public SpotAdapter(List<Map<String,Object>> spotList, int adapterUsedFor) {
        this.spotList = spotList;
        this.AdapterUsedFor=adapterUsedFor;
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
        Map<String,Object> spot = spotList.get(position);
        holder.bind(spot);
    }

    @Override
    public int getItemCount() {
        return spotList.size();
    }

    public class SpotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameTextView;
        private TextView addressTextView;
        private ImageView imgSpot;
        private MaterialButton btnDetails;
        private MaterialCardView cardSpot;


        public SpotViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.txt_spot_name);
            addressTextView = itemView.findViewById(R.id.txt_spot_address);
            imgSpot = itemView.findViewById(R.id.img_spot);
            btnDetails=itemView.findViewById(R.id.btnDetails);
            cardSpot=itemView.findViewById(R.id.spotCard);
            itemView.setOnClickListener(this);

            if (AdapterUsedFor==1)
            {
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
                        onItemClickListener.onUpdateButtonClick(spotList.get(getAdapterPosition()),getAdapterPosition());
                    }
                });

            }
            else if(AdapterUsedFor==2)
            {
                btnDetails.setText(" Details ");
                btnDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onDetailsButtonClick(spotList.get(getAdapterPosition()),getAdapterPosition());
                    }
                });

            }


        }
        @Override
        public void onClick(View view) {
            if(AdapterUsedFor==1)
                onItemClickListener.onUpdateButtonClick(spotList.get(getAdapterPosition()),getAdapterPosition());
//            else if(AdapterUsedFor==2)
//                onItemClickListener.onDetailsButtonClick(spotList.get(getAdapterPosition()),getAdapterPosition());

        }
        public void bind(Map<String,Object> spot) {
            nameTextView.setText(spot.get("spotTitle").toString());
            addressTextView.setText(spot.get("spotAddress").toString());
            if(spot.get("spotImages").toString().length()>0)
                if(spot.get("spotImages").toString().split("|").length>0)
                {
                    String[] url=spot.get("spotImages").toString().split("\\|");
                    Picasso.get().load(url[0]).into(imgSpot);
                }
            else
                    Picasso.get().load(spot.get("spotImages").toString()).into(imgSpot);

            //hourlyChargeTextView.setText("Hourly Charge: $" + spot.getWeeklySlots();
        }


    }
}
