package com.example.carreservation.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.carreservation.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VendorDashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VendorDashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VendorDashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VendorDashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    ImageView imgAddSpot,imgUpdateSpot,imgDeleteSpot,imgViewAllSpot;
    public static VendorDashboardFragment newInstance(String param1, String param2) {
        VendorDashboardFragment fragment = new VendorDashboardFragment();
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
        View view= inflater.inflate(R.layout.fragment_vendor_dashboard, container, false);
        imgAddSpot=view.findViewById(R.id.img_add_spot);
        imgUpdateSpot=view.findViewById(R.id.img_update_spot);
        imgViewAllSpot=view.findViewById(R.id.img_view_all_spots);
        imgDeleteSpot=view.findViewById(R.id.img_delete_spot);

        imgAddSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getViewModelStore().clear();
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.flFragment,
                        new AddSpotFragment()).commit();
            }
        });
        imgUpdateSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("fragmentTag","1");
                Fragment viewAll=new VendorAllSpotsFragment();
                viewAll.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.flFragment,
                        viewAll).commit();
            }
        });
        imgDeleteSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.flFragment,
                        new DeleteSpotFragment()).commit();
            }
        });

        imgViewAllSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("fragmentTag","2");
                Fragment viewAll=new VendorAllSpotsFragment();
                viewAll.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.flFragment,
                        viewAll).commit();
            }
        });

        return view;
    }
}