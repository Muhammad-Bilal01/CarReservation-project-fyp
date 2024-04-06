package com.example.carreservation.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.carreservation.R;
import com.example.carreservation.interfaces.OnItemClickListener;
import com.example.carreservation.models.Spot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    // Context object
    Context context;

    // Array of images
    List<Object> images= new ArrayList<>();

    // Layout Inflater
    LayoutInflater mLayoutInflater;
    private OnItemClickListener onItemClickListener;

    // Viewpager Constructor
    public ViewPagerAdapter(Context context, List<Object> images) {
        this.context = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getCount() {
        // return the number of images
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.slider_image_item, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);
        ImageView imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onItemClickListener.onDeleteButtonClick(new Spot(),position);
            }
        });


        // setting the image in the imageView
        if(images.get(position).toString().contains("file://") || images.get(position).toString().contains("content://"))
            imageView.setImageURI(Uri.parse(images.get(position).toString()));
        else
            Picasso.get().load(Uri.parse(images.get(position).toString())).into(imageView);

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((RelativeLayout) object);
    }
}

