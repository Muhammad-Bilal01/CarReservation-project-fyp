package com.example.carreservation.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carreservation.R;
import com.example.carreservation.adapters.DeleteSpotAdapter;
import com.example.carreservation.interfaces.OnItemClickListener;
import com.example.carreservation.models.Spot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeleteSpotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteSpotFragment extends Fragment implements OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private DeleteSpotAdapter spotAdapter;
    private FirebaseFirestore db;
    private List<Spot> spotList;
    private List<Map<String, Object>> spotsDataList;

    public DeleteSpotFragment() {
        // Required empty public constructor
    }


    public static DeleteSpotFragment newInstance(String param1, String param2) {
        DeleteSpotFragment fragment = new DeleteSpotFragment();
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

    //List<Spot> spotList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete_spot, container, false);
        spotList = new ArrayList<>();
        spotsDataList = new ArrayList<>();

        // RecyclerView setup
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        spotAdapter = new DeleteSpotAdapter(spotList, 2);
        spotAdapter = new DeleteSpotAdapter(spotsDataList, 2);
        spotAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(spotAdapter);
        db = FirebaseFirestore.getInstance();
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
                                Spot spot = d.toObject(Spot.class);
                                spot.setId(d.getId());

                                Map<String, Object> mapData = d.getData();
                                mapData.put("id", d.getId());


                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
                                spotList.add(spot);
                                spotsDataList.add(mapData);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifyDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            spotAdapter.notifyDataSetChanged();
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
/*
   private void getData() {
        //spotList = new ArrayList<>();
        db.collection("spots").whereEqualTo("vendorId", FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
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

                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
                                //spotList.add(spot);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifyDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            spotAdapter.notifyDataSetChanged();
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
    }
*/


    @Override
    public void onDeleteButtonClick(Map<String, Object> model, int position) {

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


        if (bookingFound) {
            System.out.println("True");
            // Set the dialog title and message
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
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation")
                    .setMessage("Are you sure you want to proceed?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteSpot(model);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create();
            builder.show();
        }
    }


    @Override
    public void onUpdateButtonClick(Map<String,Object> model, int position) {

    }

    @Override
    public void onDetailsButtonClick(Map<String,Object> model, int position) {

    }


    private void deleteSpot(Map<String, Object> model) {

        System.out.println(model.get("id").toString());

        db.collection("parkingSpots").
                // after that we are getting the document
                // which we have to delete.
                        document(model.get("id").toString()).

                // after passing the document id we are calling
                // delete method to delete this document.
                        delete().
                // after deleting call on complete listener
                // method to delete this data.
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // inside on complete method we are checking
                        // if the task is success or not.
                        if (task.isSuccessful()) {
                            // this method is called when the task is success
                            // after deleting we are starting our MainActivity.
                            Toast.makeText(getActivity(), "Your Spot has been deleted from Database.", Toast.LENGTH_SHORT).show();
                            spotList.remove(model);
                            spotsDataList.remove(model);
                            spotAdapter.notifyDataSetChanged();
                        } else {
                            // if the delete operation is failed
                            // we are displaying a toast message.
                            Toast.makeText(getActivity(), "Fail to delete the spot. ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onBookButtonClick(Spot model, int position) {

    }

    @Override
    public void onMyBookButtonClick(Map<String, Object> model, int position) {

    }
}