package com.example.carreservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carreservation.fragments.AddSpotFragment;
import com.example.carreservation.fragments.DeleteSpotFragment;
import com.example.carreservation.fragments.UpdateSpotFragment;
import com.example.carreservation.fragments.VendorAllSpotsFragment;
import com.example.carreservation.fragments.VendorBookingsFragment;
import com.example.carreservation.fragments.VendorDashboardFragment;
import com.example.carreservation.fragments.VendorProfileFragment;
import com.example.carreservation.fragments.ViewSpotsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carreservation.databinding.ActivityVendorDrawerBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class VendorDrawerActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityVendorDrawerBinding binding;
    BottomNavigationView bottomNavigationView;
    LinearLayout profileLayout,addSpotLayout,updateSpotLayout,deleteSpotLayout,viewSpotLayout,viewAllBookingsLayout,logoutLayout;
    private ImageView imgDrawer;
    private ImageView userProfile;

    private TextView txtProfileTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVendorDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addSpotLayout=findViewById(R.id.add_spot_layout);
        updateSpotLayout=findViewById(R.id.update_spot_layout);
        deleteSpotLayout=findViewById(R.id.delete_spot_layout);
        viewSpotLayout=findViewById(R.id.view_spot_layout);
        viewAllBookingsLayout=findViewById(R.id.view_bookings_layout);
        logoutLayout=findViewById(R.id.application_layout);
        profileLayout=findViewById(R.id.profile_layout);
        txtProfileTitle=findViewById(R.id.txt_profile_title);
        userProfile = findViewById(R.id.img_user_profile);

//        Disable
        updateSpotLayout.setEnabled(false);
        addSpotLayout.setEnabled(false);
        deleteSpotLayout.setEnabled(false);
        viewAllBookingsLayout.setEnabled(false);
        viewSpotLayout.setEnabled(false);

        //txtProfileTitle.setText("Test");
        DocumentReference reference = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                txtProfileTitle.setText(documentSnapshot.getData().get("fullName").toString());

                if(documentSnapshot.getData().get("profileImage") != null) {
                    Picasso
                            .get()
                            .load(documentSnapshot.getData().get("profileImage").toString())
                            .into(userProfile);
                }

            }
        });
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(VendorDrawerActivity.this,CommonLoginActivity.class));
                finish();
            }
        });
        addSpotLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDrawer();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new AddSpotFragment())
                        .commit();
            }
        });
        updateSpotLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDrawer();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new UpdateSpotFragment())
                        .commit();
            }
        });
        deleteSpotLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDrawer();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new DeleteSpotFragment())
                        .commit();
            }
        });
        viewSpotLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDrawer();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new ViewSpotsFragment())
                        .commit();
            }
        });
        viewAllBookingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDrawer();
                Toast.makeText(VendorDrawerActivity.this,"Booking is not complete yet",Toast.LENGTH_SHORT).show();
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.flFragment, new VendorBookingsFragment())
//                        .commit();
            }
        });


        imgDrawer=findViewById(R.id.img_dialog);
        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.open();
            }
        });
        bottomNavigationView
                = findViewById(R.id.bottm_navigation);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.spots);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, new VendorDashboardFragment())
                .commit();

        DrawerLayout drawer = binding.drawerLayout;
      /*  NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_vendor_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
*/    }

    private void handleDrawer() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vendor_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_vendor_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    VendorDashboardFragment vendorDashboardFragment = new VendorDashboardFragment();
    VendorProfileFragment vendorProfileFragment = new VendorProfileFragment();
    VendorBookingsFragment vendorBookingsFragment = new VendorBookingsFragment();

    //SecondFragment secondFragment = new SecondFragment();
    //ThirdFragment thirdFragment = new ThirdFragment();

    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {

        int itemId = item.getItemId();
        if (itemId == R.id.imgHome) {
            getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.flFragment,
                    new VendorDashboardFragment()).commit();
            return true;
        } else if (itemId == R.id.img_booking) {
//            Toast.makeText(this,"Booking is not complete yet",Toast.LENGTH_SHORT).show();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, new VendorBookingsFragment()).addToBackStack("")
                    .commit();
//            return true;
        } else if (itemId == R.id.imgProfile) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, vendorProfileFragment)
                    .commit();
            return true;
        }
        return false;
    }
}