package com.example.carreservation.fragments;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.carreservation.R;
import com.example.carreservation.models.AvailableSlot;
import com.example.carreservation.models.Booking;
import com.example.carreservation.models.BookingViewModel;
import com.example.carreservation.models.SelectedSlot;
import com.example.carreservation.models.Spot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectPaymentModeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectPaymentModeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RadioGroup radioGroup;
    BookingViewModel bookingViewModel;
    Button btnNext;

    Map<String, Object> data = new HashMap<>();

    public SelectPaymentModeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectPaymentModeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectPaymentModeFragment newInstance(String param1, String param2) {
        SelectPaymentModeFragment fragment = new SelectPaymentModeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_payment_mode, container, false);
        bookingViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(BookingViewModel.class);

        btnNext = view.findViewById(R.id.btnNext);
        radioGroup = view.findViewById(R.id.radioGPaymentMode);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radioCash)
                    bookingViewModel.setPaymentMode("Cash Payment");
                else if (radioGroup.getCheckedRadioButtonId() == R.id.radioOnline)
                    bookingViewModel.setPaymentMode("Online Payment");
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radioOnline) {
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.flFragment,
                            new OnlinePaymentFragment()).commit();
                } else {
                    SaveBookingToDB();

                }
            }
        });

        return view;
    }

    private void SaveBookingToDB() {
        ProgressDialog progressDialog
                = new ProgressDialog(getActivity());
        progressDialog.setTitle("Process");
        progressDialog.setMessage("Please wait while we are saving your booking.");
        progressDialog.show();

//        to get current user
        DocumentReference dataref = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dataref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String fullName = documentSnapshot.getData().get("fullName").toString(); // for getting user name
                String userFCMToken = documentSnapshot.getData().get("FCM_Token").toString(); // for getting user FCM Token
                System.out.println("User Token"+ userFCMToken);

                bookingViewModel.setPaymentMode("Cash Payment");
                bookingViewModel.setPaymentScreenShotUrl("https://static.vecteezy.com/system/resources/thumbnails/005/835/168/small/hand-holding-money-illustration-free-vector.jpg");
                Booking booking = new Booking(
                        bookingViewModel.getId().getValue(),
                        bookingViewModel.getSpotName().getValue(),// spot Name
                        bookingViewModel.getSpotAddress().getValue(), // spot address
                        "Pending",// bookingStatus
                        bookingViewModel.getVehicleRegistrationNumber().getValue(), // vehicle number
                        bookingViewModel.getPaymentScreenShotUrl().getValue(), // payment screenshot Url
                        bookingViewModel.getSelectedDate().getValue(), // selected date
                        bookingViewModel.getTotalAmount().getValue(), // amount
                        bookingViewModel.getPaymentMode().getValue(), // payment mode - online/cash
                        bookingViewModel.getVendorId().getValue(), // vendor id
                        bookingViewModel.getVendorToken().getValue(), // vendor Token
                        bookingViewModel.getSpotId().getValue(), // spot document id
                        bookingViewModel.getSelectedSlots().getValue(), // selected Slot [List<Map>]
                        FirebaseAuth.getInstance().getCurrentUser().getUid(), // Current User
                        false, // booking completed
                        fullName, // customer name
                        userFCMToken, // USER FCM Token
                        bookingViewModel.gtSpotImages().getValue() // spot Images
                );

//               add value to booking database
                FirebaseFirestore.getInstance().collection("Bookings").add(booking)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Booking Completed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                // Get ParkingSpots database
                final DocumentReference sfDocRef = FirebaseFirestore.getInstance().collection("parkingSpots").document(bookingViewModel.getSpotId().getValue());

//                to update availble slots in parking spots
                FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
                            @Override
                            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                                DocumentSnapshot snapshot = transaction.get(sfDocRef);
//                                System.out.println("++++++++++++++++++++________________________+++++++++++++++++");
                                Map<String, Object> spotData = snapshot.getData();
                                spotData.put("id", snapshot.getId().toString());
                                List<Map<String, Object>> totalSlots = (List<Map<String, Object>>) spotData.get("totalSlots");
                                for (Map<String, Object> tslots : totalSlots) {
//                                    System.out.println(tslots.get("Date"));
                                    if (tslots.get("Date").equals(bookingViewModel.getSelectedDate().getValue())) {
                                        List<Map<String, Object>> slots = (List<Map<String, Object>>) tslots.get("slots");
                                        for (int i = 0; i < bookingViewModel.getSelectedSlots().getValue().size(); i++) {
                                            for (Map<String, Object> mSlot : slots) {
                                                if (mSlot.get("time").equals(bookingViewModel.getSelectedSlots().getValue().get(i).getSlotName())) {
//                                                    System.out.println("Slot Time");
//                                                    System.out.println(bookingViewModel.getSelectedSlots().getValue().get(i).getSlotName());
//                                                    System.out.println("Databse SPot Time and available slots");
//                                                    System.out.println(mSlot.get("time"));
//                                                    System.out.println(mSlot.get("availableSlots"));
                                                    List<Map<String, Object>> availSlot = (List<Map<String, Object>>) mSlot.get("availableSlots");
                                                    for (int j = 0; j < availSlot.size(); j++) {
                                                        if (availSlot.get(j).get("isBooked").equals(false)) {
                                                            availSlot.get(j).put("isBooked", true);
//                                                            System.out.println(availSlot.get(j).get("SlotNo"));
                                                            break;
                                                        }
                                                    }
//                                                    for (Map<String,Object> fslot:availSlot) {
//                                                        if (fslot.get("isBooked").equals(false)){
//                                                            fslot.put("isBooked",true);
//                                                            System.out.println(fslot.get("SlotNo"));
//                                                            break;
//                                                        }
//                                                    }
                                                    mSlot.put("availableSlots", availSlot);
//                                                    System.out.println("Available Slot" + mSlot.get("availableSlots"));
                                                    break;
                                                }
                                            }
//                                            System.out.println(mSlot.get("time"));
                                        }
                                        tslots.put("slot", slots);
//                                        System.out.println("Slots : " + tslots.get("slot"));
                                    }

                                }

//                              update spot data
                                spotData.put("totalSlots",totalSlots);
//                                System.out.println("data ---->  " + spotData);

//                              updateSpotAvailableSlots(spotData);
                                data = spotData;
                                return null;
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
//                                update value
                                updateSpotAvailableSlots(data);
                                Log.d(TAG, "Transaction success!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Transaction failure.", e);
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });

//        check transaction is successfully completed or not
//        dataref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot doc = task.getResult();
//                    if (doc.exists()) {
//                        Log.d("Document", doc.getData().toString());
//                    } else {
//                        Log.d("Document", "NO Data");
//
//                    }
//                }
//            }
//        });


/*        Booking booking=new Booking(bookingViewModel.getSpotName().getValue(),bookingViewModel.getSpotAddress().getValue(),bookingViewModel.getVehicleRegistrationNumber().getValue(),bookingViewModel.getPaymentScreenShotUrl().getValue(),bookingViewModel.getSelectedDate().getValue(),bookingViewModel.getTotalAmount().getValue(),bookingViewModel.getPaymentMode().getValue(),bookingViewModel.getVendorId().getValue(),bookingViewModel.getSpotId().getValue(), bookingViewModel.getSelectedSlots().getValue(), FirebaseAuth.getInstance().getCurrentUser().getUid(),false);

        FirebaseFirestore.getInstance().collection("Bookings").add(booking)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful())
                            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                    }
                });
        final DocumentReference sfDocRef = FirebaseFirestore.getInstance().collection("spots").document(bookingViewModel.getSpotId().getValue());

        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(sfDocRef);
                        Spot spot=snapshot.toObject(Spot.class);
                        spot.setId(snapshot.getId());

                        for (int i=0;i<spot.getAvailableSlots().size();i++)
                        {
                            AvailableSlot slot=spot.getAvailableSlots().get(i);

                            if(slot.getDate().equals(bookingViewModel.getSelectedDate().getValue()) && isSlotSelectFromAvailableSlots(bookingViewModel.getSelectedSlots().getValue(),slot.getSlotName()))
                            {
                                slot.setIsbooked(true);
                                spot.getAvailableSlots().set(i,slot);
                            }
                        }
                        updateSpotAvailableSlots(spot);

                        // Success
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Transaction success!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                    }
                });*/

    }

    private void updateSpotAvailableSlots(Map<String, Object> spot) {
        FirebaseFirestore.getInstance().collection("parkingSpots").document(spot.get("id").toString()).
                // after setting our document id we are
                // passing our whole object class to it.
                        set(spot).

                // after passing our object class we are
                // calling a method for on success listener.
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // on successful completion of this process
                        // we are displaying the toast message.
                        Toast.makeText(getActivity(), "Your Booking has been confirmed..", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.flFragment,
                                new RecieptFragment()).commit();
                        // ShowableListSpotsFragement();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            // inside on failure method we are
            // displaying a failure message.
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Fail to add the data..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowableListSpotsFragement() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,
                new SearchSpotFragment()).commit();
    }

    private boolean isSlotSelectFromAvailableSlots(List<SelectedSlot> selectedSlots, String slotName) {

        for (SelectedSlot selectedSlot :
                selectedSlots) {
            if (selectedSlot.getSlotName().contains(slotName))
                return true;
        }
        return false;
    }
}