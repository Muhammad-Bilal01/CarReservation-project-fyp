package com.example.carreservation.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.carreservation.CommonLoginActivity;
import com.example.carreservation.CustomerBookingDetails;
import com.example.carreservation.R;
import com.example.carreservation.UserDashboardActivity;
import com.example.carreservation.VendorDrawerActivity;
import com.example.carreservation.adapters.CustomerBookingAdapter;
import com.example.carreservation.adapters.VendorBookingAdapter;
import com.example.carreservation.interfaces.VendorBookingListner;
import com.example.carreservation.models.Booking;
import com.example.carreservation.models.Spot;
import com.example.carreservation.models.VendorBooking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VendorBookingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VendorBookingsFragment extends Fragment implements VendorBookingListner {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VendorBookingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VendorBookingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VendorBookingsFragment newInstance(String param1, String param2) {
        VendorBookingsFragment fragment = new VendorBookingsFragment();
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
    private VendorBookingAdapter vendorBookingAdapter;
    private FirebaseFirestore db;
    List<Booking> globalBookingList;
    private SearchView txtSearch;
    private Spinner spinner;

    //    Dropdown menu options
    Set<String> uniqueValues = new HashSet<>();
    ArrayList<String> options = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor_bookings, container, false);
        List<Booking> bookings = new ArrayList<>();
        globalBookingList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        vendorBookingAdapter = new VendorBookingAdapter(bookings, this);
        recyclerView.setAdapter(vendorBookingAdapter);

        spinner = view.findViewById(R.id.spinner);
//        to maintiain the height of dropdown
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(3);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        options.add("All Spots");
//        to show items on spinner
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!parent.getItemAtPosition(position).toString().equalsIgnoreCase("All Spots")){
                bookings.clear();
                for (Booking booking :
                        globalBookingList) {
                    if ((booking.getSpotName().equalsIgnoreCase(parent.getItemAtPosition(position).toString()))) {
                        bookings.add(booking);
                    }
                }
                }else{
                    bookings.addAll(globalBookingList);
                }
                vendorBookingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        txtSearch = view.findViewById(R.id.txt_search_booking);
// hide the search box
        txtSearch.setVisibility(View.GONE);
        txtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                bookings.clear();
                for (Booking booking :
                        globalBookingList) {
                    if ((booking.getSpotName().toLowerCase().contains(s.toLowerCase()) || booking.getSpotAddress().toLowerCase().contains(s.toLowerCase()))) {
                        bookings.add(booking);
                    }
                }
                vendorBookingAdapter.notifyDataSetChanged();
                return false;
            }
        });
        db = FirebaseFirestore.getInstance();
        db.collection("Bookings").whereEqualTo("vendorId", FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
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
                                Booking booking = d.toObject(Booking.class);
                                booking.setId(d.getId().toString());

//                                For getting all unique value
                                uniqueValues.add(booking.getSpotName());

                                bookings.add(booking);
                                globalBookingList.add(booking);


                            }
                            //        to set dropdown item
                            options.addAll(uniqueValues);

                            // after adding the data to recycler view.
                            // we are calling recycler view notifyDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            vendorBookingAdapter.notifyDataSetChanged();
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
    public void onItemApproveClicked(Booking model, int position) {
        updatePendingStatus(model.getId(), model);
//        Toast.makeText(getActivity(), "Vendor - "+model.getId() , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClicked(Booking model, int position) {
        String id = "";
        id = model.getId();

        Intent intent = new Intent(getContext(), CustomerBookingDetails.class);
        intent.putExtra("documentId", id);
        startActivity(intent);
    }

    //    to update the booking status of the user bookings
    void updatePendingStatus(String documentId, Booking model) {
        model.setBookingStatus("Booked");
        model.setId(documentId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Bookings").document(documentId).set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(), "Update Status ", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Update Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}