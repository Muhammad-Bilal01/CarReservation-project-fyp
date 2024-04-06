package com.example.carreservation.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carreservation.R;
import com.example.carreservation.models.Spot;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {

    private String id;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            Bundle bundle = getArguments();
            id = bundle.getString("documentId");
            Double latitude = bundle.getDouble("Latitude");
            Double longitude = bundle.getDouble("Longitude");
            String spotLocation = bundle.getString("SpotLocation");

            LatLng location = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(location).title(spotLocation));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,16.0f));

            //Move the camera to the user's location and zoom in!
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 12.0f));

//            System.out.println("Id " + id + " Latitude " + latitude + " Longitude " + longitude);

            /*FirebaseFirestore db=FirebaseFirestore.getInstance();
            List<Spot> spotList=new ArrayList<>();
            db.collection("spots").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            // after getting the data we are calling on success method
                            // and inside this method we are checking if the received
                            // query snapshot is empty or not.
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // if the snapshot is not empty we are
                                // hiding our progress bar and adding
                                // our data in a list.
                                //loadingPB.setVisibility(View.GONE);
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    // after getting this list we are passing
                                    // that list to our object class.
                                    Spot spot= d.toObject(Spot.class);
                                    spot.setId(d.getId());
                                    LatLng location = new LatLng(spot.getSpotLat(), spot.getSpotLong());
                                    googleMap.addMarker(new MarkerOptions().position(location).title(spot.getSpotAddress()));
                                    // and we will pass this object class
                                    // inside our arraylist which we have
                                    // created for recycler view.
                                    spotList.add(spot);
                                }
                            } else {
                                // if the snapshot is empty we are displaying a toast message.
                                Toast.makeText(getActivity(), "No data found in Database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // if we do not get any data or any error we are displaying
                            // a toast message that we do not get any data
                            Toast.makeText(getActivity(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
                        }
                    });*/
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }


    };

    private RadioGroup radioGroup;
    FirebaseFirestore db;
    TextView txtListView, txtMapView;
    ImageView imgListView, imMapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

//        radioGroup=view.findViewById(R.id.searchSpotListMapRg);

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                if(radioGroup.getCheckedRadioButtonId()==R.id.listView)
//                {
//                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,
//                            new SearchSpotFragment()).commit();
//                }
//            }
//        });

        imgListView = view.findViewById(R.id.imgList);
        imMapView = view.findViewById(R.id.imgMap);
        txtListView = view.findViewById(R.id.txtListView);
        txtMapView = view.findViewById(R.id.txtMapView);

        txtListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenListFragment();
            }
        });

        imgListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenListFragment();
            }
        });

        txtMapView.setTypeface(null, Typeface.BOLD);
        return view;
    }

    private void OpenListFragment() {
        Bundle bundle = new Bundle();
//        String id = bundle.getString("documentId");
        bundle.putString("documentId",id);
        System.out.println("ID = "+ id);
        Fragment listFragment = new ViewSpotDetailsFragment();
        listFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,
                listFragment).commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


}