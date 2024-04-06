package com.example.carreservation;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carreservation.fragments.VendorDashboardFragment;
import com.example.carreservation.fragments.VendorProfileFragment;
import com.example.carreservation.fragments.ViewSpotsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VendorMain extends AppCompatActivity   implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_main);

        bottomNavigationView
                = findViewById(R.id.bottm_navigation);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.spots);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, new ViewSpotsFragment())
                .commit();
    }
    VendorDashboardFragment vendorDashboardFragment = new VendorDashboardFragment();
    VendorProfileFragment vendorProfileFragment = new VendorProfileFragment();

    //SecondFragment secondFragment = new SecondFragment();
    //ThirdFragment thirdFragment = new ThirdFragment();

    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {

        int itemId = item.getItemId();
        if (itemId == R.id.imgHome) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, vendorDashboardFragment)
                    .commit();
            return true;
        } else if (itemId == R.id.img_booking) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, vendorDashboardFragment)
                    .commit();
            return true;
        } else if (itemId == R.id.imgProfile) {
            System.out.println("Clicked");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, vendorProfileFragment)
                    .commit();
            return true;
        }
        return false;
    }
}