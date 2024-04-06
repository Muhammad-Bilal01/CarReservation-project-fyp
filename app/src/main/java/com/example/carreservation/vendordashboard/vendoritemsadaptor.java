//package com.example.carreservation.vendordashboard;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//
//import java.util.List;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.carreservation.R;
//
//public class vendoritemsadaptor extends RecyclerView.Adapter<vendoritemsviewholder> {
//
//
//    Context context;
//    List<vendoritemclass> items;
//
//    public vendoritemsadaptor(Context context, List<vendoritemclass> items) {
//        this.context = context;
//        this.items = items;
//    }
//
//    @NonNull
//    @Override
//    public vendoritemsviewholder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
//        return new vendoritemsviewholder(LayoutInflater.from(context).inflate(R.layout.vendor_item_view,parent,false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull  vendoritemsviewholder holder, int position) {
//        holder.nameView.setText(items.get(position).get_name());
//        holder.emailView.setText(items.get(position).get_email());
//        holder.imageView.setImageResource(items.get(position).get_img());
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//}
