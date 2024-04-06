package com.example.carreservation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carreservation.R;
import com.example.carreservation.interfaces.OnItemClickListener;
import com.example.carreservation.models.SelectedSlot;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ConfirmBookingAdapter extends RecyclerView.Adapter<ConfirmBookingAdapter.ConfirmBookingSpotViewHolder> {

    private List<SelectedSlot> slotList;
    private OnItemClickListener onItemClickListener;
    public ConfirmBookingAdapter(List<SelectedSlot> slotList) {
        this.slotList = slotList;
    }
   
    @NonNull
    @Override
    public ConfirmBookingAdapter.ConfirmBookingSpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.confirm_booking_item, parent, false);
        return new ConfirmBookingAdapter.ConfirmBookingSpotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmBookingAdapter.ConfirmBookingSpotViewHolder holder, int position) {
        SelectedSlot slot = slotList.get(position);
        holder.bind(slot);
    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }

    public class ConfirmBookingSpotViewHolder extends RecyclerView.ViewHolder {
        private TextView slotName;
        private TextView slotPrice;


        public ConfirmBookingSpotViewHolder(@NonNull View itemView) {
            super(itemView);
            slotName = itemView.findViewById(R.id.txtSlotName);
            slotPrice = itemView.findViewById(R.id.txtSlotPrice);


        }

        public void bind(SelectedSlot slot) {
            slotName.setText(slot.getSlotName());
            slotPrice.setText("PKR "+slot.getSlotPrice());

        }


    }
}
