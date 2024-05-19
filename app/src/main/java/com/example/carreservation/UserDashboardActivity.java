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

import com.example.carreservation.admin.SupportChatActivity;
import com.example.carreservation.databinding.ActivityUserDashboardBinding;
import com.example.carreservation.fragments.AddSpotFragment;
import com.example.carreservation.fragments.CustomerBookingsFragment;
import com.example.carreservation.fragments.DeleteSpotFragment;
import com.example.carreservation.fragments.SearchSpotFragment;
import com.example.carreservation.fragments.UpdateSpotFragment;
import com.example.carreservation.fragments.UserProfileFragments;
import com.example.carreservation.fragments.VendorBookingsFragment;
import com.example.carreservation.fragments.VendorDashboardFragment;
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

public class UserDashboardActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityUserDashboardBinding binding;
    BottomNavigationView bottomNavigationView;
    LinearLayout profileLayout,viewBookingsLayout,logoutLayout, supportLayout;
    private ImageView imgDrawer;
    private TextView txtProfileTitle;

    private ImageView img_user_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        supportLayout=findViewById(R.id.support_layout);
        viewBookingsLayout=findViewById(R.id.view_bookings_layout);
        logoutLayout=findViewById(R.id.application_layout);
        profileLayout=findViewById(R.id.profile_layout);
        txtProfileTitle=findViewById(R.id.txt_profile_title);
        img_user_profile=findViewById(R.id.img_user_profile);
        //txtProfileTitle.setText("Test");
        DocumentReference reference = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                txtProfileTitle.setText(documentSnapshot.getData().get("fullName").toString());
//                img_user_profile.setImageURI();

                if(documentSnapshot.getData().get("profileImage") != null) {
                    Picasso
                            .get()
                            .load(documentSnapshot.getData().get("profileImage").toString())
                            .into(img_user_profile);
                }
            }
        });

//      on Logout
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserDashboardActivity.this,CommonLoginActivity.class));
                finish();
            }
        });
//        on support
        supportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDrawer();

                Intent intent = new Intent(UserDashboardActivity.this, SupportChatActivity.class);
                startActivity(intent);

               /* getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new AddSpotFragment())
                        .commit();*/
            }
        });
//      View Booking Layout
        viewBookingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDrawer();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new CustomerBookingsFragment())
                        .commit();
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


        // go to first home page
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, new SearchSpotFragment())
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

    SearchSpotFragment searchSpotFragment = new SearchSpotFragment();
    UserProfileFragments userProfileFragments = new UserProfileFragments();

    CustomerBookingsFragment customerBookingsFragment = new CustomerBookingsFragment();

    //SecondFragment secondFragment = new SecondFragment();
    //ThirdFragment thirdFragment = new ThirdFragment();


    // on bottom navigation
    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {

        int itemId = item.getItemId();
        if (itemId == R.id.imgHome) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("")
                    .replace(R.id.flFragment, searchSpotFragment)
                    .commit();
            return true;
        } else if (itemId == R.id.img_booking) {
//            Toast.makeText(this,"View Booking is not complete", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, new CustomerBookingsFragment())
                    .commit();
            return true;
        } else if (itemId == R.id.imgProfile) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, userProfileFragments)
                    .commit();
            return true;
        }
        return false;
    }
}