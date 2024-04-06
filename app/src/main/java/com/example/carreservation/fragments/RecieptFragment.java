package com.example.carreservation.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carreservation.R;
import com.example.carreservation.models.BookingViewModel;
import com.example.carreservation.models.SelectedSlot;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecieptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecieptFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    BookingViewModel bookingViewModel;
    MaterialButton txtDuration,txtTotalPrice,btnRedirectToGoogleMap, btnSave;
    TextView txtSpotName,txtSpotAddress,txtSpotId;
    public RecieptFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecieptFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecieptFragment newInstance(String param1, String param2) {
        RecieptFragment fragment = new RecieptFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,
                        new SearchSpotFragment()).commit();
            }
        });
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_reciept, container, false);
        bookingViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(BookingViewModel.class);

        txtDuration=view.findViewById(R.id.txtDuration);
        txtTotalPrice=view.findViewById(R.id.txtPrice);
        txtSpotName=view.findViewById(R.id.txtSpotName);
        txtSpotAddress=view.findViewById(R.id.txtSpotAddress);
        txtSpotId=view.findViewById(R.id.txtSpotId);
        btnRedirectToGoogleMap=view.findViewById(R.id.btnRedirectToGoogleMaps);
        btnSave=view.findViewById(R.id.btnSave);
        btnRedirectToGoogleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
        SetObservers();
        return view;
    }

    private void SetObservers() {
        bookingViewModel.getSpotName().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String spotName) {
                txtSpotName.setText(spotName);
            }
        });
        bookingViewModel.getSpotAddress().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String address) {
                txtSpotAddress.setText(address);
            }
        });
        bookingViewModel.getTotalAmount().observe(requireActivity(), new Observer<Double>() {
            @Override
            public void onChanged(Double amount) {
                txtTotalPrice.setText("PKR "+amount);
            }
        });
        bookingViewModel.getSelectedSlots().observe(requireActivity(), new Observer<List<SelectedSlot>>() {
            @Override
            public void onChanged(List<SelectedSlot> selectedSlots) {
                if (selectedSlots!=null && selectedSlots.size()>0)
                    txtDuration.setText(selectedSlots.size()+" h");
                else
                    txtDuration.setText("0 h");
            }
        });
        bookingViewModel.getSpotId().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String spotId) {
                txtSpotId.setText("Spot# "+spotId);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getViewModelStore().clear();
    }
}