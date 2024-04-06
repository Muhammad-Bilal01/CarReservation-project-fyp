package com.example.carreservation.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carreservation.CommonLoginActivity;
import com.example.carreservation.R;
import com.example.carreservation.UserDashboardActivity;
import com.example.carreservation.VendorDrawerActivity;
import com.example.carreservation.models.AvailableSlot;
import com.example.carreservation.models.Booking;
import com.example.carreservation.models.BookingViewModel;
import com.example.carreservation.models.SelectedSlot;
import com.example.carreservation.models.Spot;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnlinePaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnlinePaymentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int SELECT_PICTURE = 100;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseStorage storage;
    StorageReference storageReference;

    Map<String, Object> data = new HashMap<>();
    Uri filePath,downloadUri;
    public OnlinePaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnlinePaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnlinePaymentFragment newInstance(String param1, String param2) {
        OnlinePaymentFragment fragment = new OnlinePaymentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    BookingViewModel bookingViewModel;
    FirebaseFirestore db;
    EditText txtTitle,txtIban,txtBankName,txtBranchName;
    TextView txtViewAttachment;
    Button btmCompletebooking;

    ImageView paymentImage;

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
        View view= inflater.inflate(R.layout.fragment_online_payment, container, false);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        txtBankName=view.findViewById(R.id.bankNameET);
        txtBranchName=view.findViewById(R.id.branchNameET);
        txtIban=view.findViewById(R.id.ibanET);
        txtTitle=view.findViewById(R.id.accountTitleET);
        txtViewAttachment=view.findViewById(R.id.imgAttachment);
        paymentImage = view.findViewById(R.id.paymentImage);
        btmCompletebooking=view.findViewById(R.id.btnCompleteBooking);
        bookingViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(BookingViewModel.class);

        bookingViewModel.getVendorId().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String vendorId) {
                FirebaseFirestore.getInstance().collection("Users").document(vendorId)
                   .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String title=documentSnapshot.getData().get("accountTitle").toString();
                        String iban=documentSnapshot.getData().get("iban").toString();
                        String bankName=documentSnapshot.getData().get("bankName").toString();
                        String branchName=documentSnapshot.getData().get("branchName").toString();
                        txtTitle.setText(title);
                        txtIban.setText(iban);
                        txtBankName.setText(bankName);
                        txtBranchName.setText(branchName);

                    }
                });
            }
        });
        txtViewAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageChooser();
            }
        });
        btmCompletebooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filePath==null)
                    Toast.makeText(getActivity(), "Please attach screenshot of payment", Toast.LENGTH_SHORT).show();
                else
                {
                    uploadImage();
                }
            }
        });
        return view;
    }
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_PICTURE);
        //getActivity().startActivityForResult( Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                filePath = data.getData();
//                System.out.println("_+_+_+__+_+_+_+_++__+___+___+__++_++_+__+_+_+__+_+_+__+__+_++__++_++");
//                Show Image on UI
                paymentImage.setVisibility(View.VISIBLE);
                paymentImage.setImageURI(filePath);
//                System.out.println(data.getData());

            }
        }
    }
    private void uploadImage()
    {
        // Code for showing progressDialog while uploading
        ProgressDialog progressDialog
                = new ProgressDialog(getActivity());
        progressDialog.setTitle("Process");

        progressDialog.setMessage("Please wait while we are processing your request");
        progressDialog.show();
        if (filePath != null) {

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            Task<Uri> urlTask = ref.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }



                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                         downloadUri = task.getResult();
                         bookingViewModel.setPaymentScreenShotUrl(downloadUri.toString());
                         SaveBookingToDB();
                         progressDialog.dismiss();

                    } else {
                        progressDialog.dismiss();
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }

    private void SaveBookingToDB() {


        DocumentReference dataref = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dataref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String fullName=documentSnapshot.getData().get("fullName").toString();
                Booking booking=new Booking(bookingViewModel.getId().getValue(),
                        bookingViewModel.getSpotName().getValue(),
                        bookingViewModel.getSpotAddress().getValue(),
                        "Pending",
                        bookingViewModel.getVehicleRegistrationNumber().getValue(),
                        bookingViewModel.getPaymentScreenShotUrl().getValue(),
                        bookingViewModel.getSelectedDate().getValue(),
                        bookingViewModel.getTotalAmount().getValue(),
                        bookingViewModel.getPaymentMode().getValue(),
                        bookingViewModel.getVendorId().getValue(),
                        bookingViewModel.getSpotId().getValue(),
                        bookingViewModel.getSelectedSlots().getValue(),
                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        false,
                        fullName,
                        bookingViewModel.gtSpotImages().getValue()
                );

                FirebaseFirestore.getInstance().collection("Bookings").add(booking)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
//                to update availble slots in parking spots
                final DocumentReference sfDocRef = FirebaseFirestore.getInstance().collection("parkingSpots").document(bookingViewModel.getSpotId().getValue());
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
        });

/*
                final DocumentReference sfDocRef = FirebaseFirestore.getInstance().collection("parkingSpots").document(bookingViewModel.getSpotId().getValue());
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
                        });

            }
        });
  */
        dataref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        Log.d("Document", doc.getData().toString());
                    }else{
                        Log.d("Document", "NO Data");

                    }
                }
            }
        });

    }

    private void updateSpotAvailableSlots(Map<String, Object> spot) {
        FirebaseFirestore.getInstance().collection("spots").document(spot.get("id").toString()).
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
                        //ShowableListSpotsFragement();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,
                                new RecieptFragment()).commit();
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

        for (SelectedSlot selectedSlot:
                selectedSlots) {
            if (selectedSlot.getSlotName().contains(slotName))
                return true;
        }
        return false;
    }

}