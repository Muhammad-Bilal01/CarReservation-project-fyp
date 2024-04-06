package com.example.carreservation.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.carreservation.MainActivity;
import com.example.carreservation.R;
import com.example.carreservation.adapters.SearchSpotAdapter;
import com.example.carreservation.interfaces.OnItemClickListener;
import com.example.carreservation.models.ParkingSpots;
import com.example.carreservation.models.Spot;
import com.example.carreservation.models.SpotViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchSpotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchSpotFragment extends Fragment implements OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_LOCATION = 100;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchSpotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchSpotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchSpotFragment newInstance(String param1, String param2) {
        SearchSpotFragment fragment = new SearchSpotFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView recyclerView;
    private SearchSpotAdapter searchSpotAdapter;
    private FirebaseFirestore db;
    private SpotViewModel spot;
    private Switch switchWithinRadius;
    double latitude; // latitude
    double longitude; // longitude

    private RadioGroup radioGroup;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager

    LocationManager locationManager;
    List<Spot> globalSpotList;
    List<Map<String, Object>> globalParkingSpots;
    private SearchView txtSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_spot, container, false);

        view.setBackgroundColor(view.getResources().getColor(R.color.white));
        final List<Spot>[] spotList = new List[]{new ArrayList<>()};
        final List<Map<String, Object>>[] parkingSpotList = new List[]{new ArrayList<>()};

        globalSpotList = new ArrayList<>();
        globalParkingSpots = new ArrayList<>();

        switchWithinRadius = view.findViewById(R.id.switch_within);
        txtSearch = view.findViewById(R.id.txt_search_spot);
        radioGroup = view.findViewById(R.id.searchSpotListMapRg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.mapView) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,
                            new MapsFragment()).commit();
                }
            }
        });
//        for Search
        txtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                spotList[0].clear();
                parkingSpotList[0].clear();
//                for (Spot spot :
//                        globalSpotList) {
//                    if((spot.getSpotName().toLowerCase().contains(s.toLowerCase())||spot.getSpotAddress().toLowerCase().contains(s.toLowerCase()))){
//                        spotList[0].add(spot);
//                    }
//                }

                for (Map<String, Object> parkingSpots : globalParkingSpots) {
                    for (Map.Entry<String, Object> parkSpot : parkingSpots.entrySet()) {
                        String key = parkSpot.getKey();
                        Object value = parkSpot.getValue();

                        // Do something with the key and value
                        System.out.println("Key: " + key + ", Value: " + value);
                    }
                }

                searchSpotAdapter.notifyDataSetChanged();
                return false;
            }
        });

//        Switch within 10 KM
        switchWithinRadius.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    float[] results = new float[1];
                    spotList[0].clear();
                    for (Spot spot :
                            globalSpotList) {
                        Location.distanceBetween(latitude, longitude, spot.getSpotLat(), spot.getSpotLong(), results);
                        if ((results[0] / 1000) <= 10) {
                            if (txtSearch.getQuery().length() <= 0 || spot.getSpotName().toLowerCase().contains(txtSearch.getQuery().toString().toLowerCase())) {
                                spotList[0].add(spot);
                            }
                        }
                    }
                    searchSpotAdapter.notifyDataSetChanged();
                } else {
                    spotList[0].clear();
                    for (Spot spot : globalSpotList)
                        spotList[0].add(spot);
                    searchSpotAdapter.notifyDataSetChanged();
                }
            }
        });
        // RecyclerView setup
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        searchSpotAdapter = new SearchSpotAdapter(spotList[0], 1);
        searchSpotAdapter = new SearchSpotAdapter(parkingSpotList[0], 1);
        recyclerView.setAdapter(searchSpotAdapter);
        db = FirebaseFirestore.getInstance();
        searchSpotAdapter.setOnItemClickListener(this);
        spot = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(SpotViewModel.class);
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
        // FAB setup

//        Get Data from Firebase
        db.collection("parkingSpots").get()
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
//                                Spot spot= d.toObject(Spot.class);
//                                spot.setId(d.getId());
//                                globalSpotList.add(spot);

                                Map<String, Object> parkingSpots = d.getData();
                                parkingSpots.put("id", d.getId());

//                                ParkingSpots myParkingSpot = d.toObject(ParkingSpots.class);
//                                myParkingSpot.setId(d.getId());

                                globalParkingSpots.add(parkingSpots);
                                parkingSpotList[0].add(parkingSpots);

//                                System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
//                                System.out.println(d.getId());
//                                System.out.println(parkingSpots);
//                                System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
//                                System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
//                                System.out.println(globalParkingSpots);
//                                System.out.println(parkingSpotList[0]);
//                                System.out.println("+++++++++++++++++++++++++++++++++++++++++++");

                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
//                                spotList[0].add(spot);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifyDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            searchSpotAdapter.notifyDataSetChanged();
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
                });
        return view;
    }



    @Override
    public void onDeleteButtonClick(Map<String,Object> model, int position) {

    }

    @Override
    public void onUpdateButtonClick(Map<String,Object> model, int position) {

    }

    @Override
    public void onDetailsButtonClick(Map<String,Object>  model, int position) {


    }

    @Override
    public void onBookButtonClick(Spot model, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("documentId", model.getId());
        ViewSpotDetailsFragment spotDetailsFragment = new ViewSpotDetailsFragment();
        spotDetailsFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.flFragment,
                spotDetailsFragment).commit();
    }

    @Override
    public void onMyBookButtonClick(Map<String, Object> model, int position) {

        String id = "";
        for (Map.Entry<String,Object> paringSpot:model.entrySet()) {
            String key = paringSpot.getKey();
            Object value = paringSpot.getValue();

            System.out.println("Key: " + key + ", Value: " + value);
            if(key.equalsIgnoreCase("id")){
                System.out.println(id);
                id = value.toString();
            }

        }

        Bundle bundle = new Bundle();
        bundle.putString("documentId", id);
        ViewSpotDetailsFragment spotDetailsFragment = new ViewSpotDetailsFragment();
        spotDetailsFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.flFragment,
                spotDetailsFragment).commit();

    }


    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                getLocation();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            List<String> providers = locationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;

                }
                latitude = l.getLatitude();
                longitude = l.getLongitude();
                //Toast.makeText(getActivity(), String.valueOf(latitude)+" "+String.valueOf(longitude), Toast.LENGTH_SHORT).show();

                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location:
                    bestLocation = l;
                }
            }

        }
    }
}