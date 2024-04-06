package com.example.carreservation.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.carreservation.CommonRegistrationActivity2;
import com.example.carreservation.EditVendorProfileActivity;
import com.example.carreservation.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class VendorProfileFragment extends Fragment {

    TextView txtName,txtEmail,txtMobile, txtCnic, txtAddress, txtAccountTitle, txtAccountNumber, txtBankName;
    Button updateBtn;

    String name = "";
    String mobile = "";
    String cnic = "";
    String address = "";
    String accountTitle = "";
    String accountNumber = "";
    String bankName = "";
    String imageUrl = "";

    CircleImageView profile_image;
    public VendorProfileFragment() {
        // Required empty public constructor
    }


    public static VendorProfileFragment newInstance(String param1, String param2) {
        VendorProfileFragment fragment = new VendorProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
             View view = inflater.inflate(R.layout.fragment_vendor_profile, container, false);
             initializeComponent(view);

//             get value from database
        DocumentReference reference = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                name = documentSnapshot.getData().get("fullName").toString();
                mobile = documentSnapshot.getData().get("mobileNo").toString();
                cnic = documentSnapshot.getData().get("cnicNo").toString();
                address = documentSnapshot.getData().get("address").toString();
                accountTitle = documentSnapshot.getData().get("accountTitle").toString();
                accountNumber = documentSnapshot.getData().get("iban").toString();
                bankName = documentSnapshot.getData().get("bankName").toString();

                txtName.setText(name);
                txtEmail.setText(documentSnapshot.getData().get("email").toString());
                txtAddress.setText(address);
                txtCnic.setText(cnic);
                txtMobile.setText(mobile);
                txtAccountTitle.setText(accountTitle);
                txtAccountNumber.setText(accountNumber);
                txtBankName.setText(bankName);

                if (documentSnapshot.getData().get("profileImage")!= null){
                    imageUrl =  documentSnapshot.getData().get("profileImage").toString();
                    Picasso
                            .get()
                            .load(
                                    imageUrl
                            )
                            .into(profile_image);
                }

            }
        });

//        on Update Button Click
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), EditVendorProfileActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("mobile",mobile);
                intent.putExtra("cnic",cnic);
                intent.putExtra("address",address);
                intent.putExtra("accountTilte",accountTitle);
                intent.putExtra("accountNumber",accountNumber);
                intent.putExtra("bankName",bankName);
                intent.putExtra("profileImage", imageUrl);
                startActivity(intent);
            }
        });

        return view;
    }

// Initialize Component
    private void initializeComponent(View view) {
        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtMobile = view.findViewById(R.id.txtMobile);
        txtCnic = view.findViewById(R.id.txtCnic);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtAccountTitle = view.findViewById(R.id.txtAccountTitle);
        txtAccountNumber = view.findViewById(R.id.txtAccountNumber);
        txtBankName = view.findViewById(R.id.txtBankName);
        updateBtn = view.findViewById(R.id.btnEdit);
        profile_image=view.findViewById(R.id.profile_image);
    }
}