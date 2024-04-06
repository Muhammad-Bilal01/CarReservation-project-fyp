package com.example.carreservation.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.carreservation.EditUserProfileActivity;
import com.example.carreservation.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragments extends Fragment {

    TextView txtName, txtCnic, txtEmail, txtAddress,txtMobile;
    Button btnEdit;

    CircleImageView profile_image;

    String name = "";
    String mobile = "";
    String cnic = "";
    String address = "";
    String imageUrl = "";


    public UserProfileFragments() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile_fragments, container, false);



        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtCnic = view.findViewById(R.id.txtCnic);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtMobile = view.findViewById(R.id.txtMobile);
        btnEdit = view.findViewById(R.id.btnEdit);
        profile_image = view.findViewById(R.id.profile_image);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), EditUserProfileActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("mobile",mobile);
                intent.putExtra("cnic",cnic);
                intent.putExtra("address",address);
                intent.putExtra("profileImg",imageUrl);
                startActivity(intent);
            }
        });


        DocumentReference reference = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                name = documentSnapshot.getData().get("fullName").toString();
                mobile = documentSnapshot.getData().get("mobileNo").toString();
                cnic = documentSnapshot.getData().get("cnicNo").toString();
                address = documentSnapshot.getData().get("address").toString();

                txtName.setText(name);
                txtEmail.setText(documentSnapshot.getData().get("email").toString());
                txtAddress.setText(address);
                txtCnic.setText(cnic);
                txtMobile.setText(mobile);

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




        return view;
    }

}