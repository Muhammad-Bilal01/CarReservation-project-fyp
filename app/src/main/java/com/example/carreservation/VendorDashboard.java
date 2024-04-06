package com.example.carreservation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.carreservation.vendordashboard.vendoritemclass;
//import com.example.carreservation.vendordashboard.vendoritemsadaptor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VendorDashboard extends AppCompatActivity {

    FloatingActionButton add_service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_dashboard);

//        RecyclerView recyclerView = findViewById(R.id.vendoritems_recyclerview);
//
//        List<vendoritemclass> items = new ArrayList<vendoritemclass>();
//        items.add(new vendoritemclass("John wick","john.wick@email.com",R.drawable.splash_screen));
//        items.add(new vendoritemclass("Robert j","robert.j@email.com",R.drawable.splash_screen));
//        items.add(new vendoritemclass("James Gunn","james.gunn@email.com",R.drawable.splash_screen));
//        items.add(new vendoritemclass("Ricky tales","rickey.tales@email.com",R.drawable.splash_screen));
//        items.add(new vendoritemclass("Micky mose","mickey.mouse@email.com",R.drawable.splash_screen));
//        items.add(new vendoritemclass("Pick War","pick.war@email.com",R.drawable.splash_screen));
//        items.add(new vendoritemclass("Leg piece","leg.piece@email.com",R.drawable.splash_screen));
//        items.add(new vendoritemclass("Apple Mac","apple.mac@email.com",R.drawable.splash_screen));
//        items.add(new vendoritemclass("John wick","john.wick@email.com",R.drawable.splash_screen));
//
//
//
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new vendoritemsadaptor(getApplicationContext(),items));
//
       /* add_service = findViewById(R.id.add_service);
        add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorDashboard.this, AddVendorService.class);
                startActivity(intent);
            }
        });*/
    }
}