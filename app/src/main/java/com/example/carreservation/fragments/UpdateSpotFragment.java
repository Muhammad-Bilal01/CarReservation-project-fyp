package com.example.carreservation.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carreservation.R;
import com.example.carreservation.adapters.SpotAdapter;
import com.example.carreservation.interfaces.OnItemClickListener;
import com.example.carreservation.models.Spot;
import com.example.carreservation.models.SpotViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateSpotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateSpotFragment extends Fragment implements OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateSpotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateSpotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateSpotFragment newInstance(String param1, String param2) {
        UpdateSpotFragment fragment = new UpdateSpotFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private RecyclerView recyclerView;
    private SpotAdapter spotAdapter;
    private FirebaseFirestore db;
    private SpotViewModel spot;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_update_spot, container, false);
//        List<Spot> spotList = new ArrayList<>();
        List<Map<String,Object>> spotDataList = new ArrayList<>() ;
        // RecyclerView setup
//        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
//        spotAdapter = new SpotAdapter(spotList, 1);
        spotAdapter = new SpotAdapter(spotDataList, 1);
        recyclerView.setAdapter(spotAdapter);
        db = FirebaseFirestore.getInstance();
        spotAdapter.setOnItemClickListener(this);
        spot = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(SpotViewModel.class);

        // FAB setup

        db.collection("parkingSpots").whereEqualTo("VendorID", FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
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

                                Map<String,Object> spot = d.getData();
                                spot.put("id",d.getId());
                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
//                                spotList.add(spot);
                                spotDataList.add(spot);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifyDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            spotAdapter.notifyDataSetChanged();
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(getActivity(), "No data found abc in Database", Toast.LENGTH_SHORT).show();
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

//    Go to update Spot Fragment
    private void showAddSpotFragment() {
        // Replace the fragment with AddSpotFragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddSpotFragment fragment = new AddSpotFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,
                new AddSpotFragment()).commit();
    }

    @Override
    public void onUpdateButtonClick(Map<String,Object> model, int position) {

        List<Map<String, Object>> sl = (List<Map<String, Object>>) model.get("totalSlots");
        boolean bookingFound = false;

        for (Map<String, Object> d : sl) {
            for (Map.Entry<String, Object> slots :
                    d.entrySet()) {
                if (slots.getKey().equalsIgnoreCase("slots")) {
                    List<Map<String, Object>> slot = (List<Map<String, Object>>) slots.getValue();
                    for (Map<String, Object> totalSlot : slot) {
                        List<Map<String, Object>> avSlot = (List<Map<String, Object>>) totalSlot.get("availableSlots");
                        for (int i = 0; i < avSlot.size(); i++) {
                            if (avSlot.get(i).get("isBooked").toString().equalsIgnoreCase("true")) {
                                bookingFound=true;
                            }
                        }
                    }
                }

            }
        }

        System.out.println("Clicked...");


        if (bookingFound){
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Booking Found")
                    .setMessage("You are not able to delete this spot")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle the "OK" button click
                            dialog.dismiss(); // Dismiss the dialog
                        }
                    });
            builder.create();
            builder.show();
        }


//        spot.setSpotName(model.getSpotName());
//        spot.setSpotAddress(model.getSpotAddress());
//        spot.setSpotLocation(model.getSpotLocation());
//        spot.setSpotLat(model.getSpotLat());
//        spot.setSpotLong(model.getSpotLong());
//        spot.setFromDate(model.getFromDate());
//        spot.setToDate(model.getToDate());
//        spot.setPricePerHour(model.getPricePerHour());
//        spot.setIsSpotUpdate(true);
//        spot.setVendorId(model.getVendorId());
//        spot.setWeeklySlots(model.getWeeklySlots());
//        spot.setId(model.getId());
//        spot.setSpotImages(model.getSpotImages());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,
                new AddSpotFragment()).commit();
    }

    @Override
    public void onDeleteButtonClick(Map<String,Object> model, int position) {

    }

    @Override
    public void onDetailsButtonClick(Map<String,Object> model, int position) {

    }

    @Override
    public void onBookButtonClick(Spot model, int position) {

    }

    @Override
    public void onMyBookButtonClick(Map<String, Object> model, int position) {

    }
}