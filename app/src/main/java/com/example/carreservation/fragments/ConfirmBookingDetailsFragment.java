package com.example.carreservation.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.carreservation.R;
import com.example.carreservation.adapters.ConfirmBookingAdapter;
import com.example.carreservation.adapters.SpotAdapter;
import com.example.carreservation.models.BookingViewModel;
import com.example.carreservation.models.SelectedSlot;
import com.example.carreservation.models.SpotViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmBookingDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmBookingDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfirmBookingDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmBookingDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmBookingDetailsFragment newInstance(String param1, String param2) {
        ConfirmBookingDetailsFragment fragment = new ConfirmBookingDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    ConfirmBookingAdapter confirmBookingAdapter;
    List<SelectedSlot> selectedSlots;
    RecyclerView recyclerView;
    TextView txtSpotName,txtSelectedDate, txtTotalCharges;
    BookingViewModel bookingViewModel;
    Button btnNext;
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
        View view = inflater.inflate(R.layout.fragment_confirm_booking_details, container, false);
        bookingViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(BookingViewModel.class);

        selectedSlots=new ArrayList<>();
        txtSpotName=view.findViewById(R.id.txtSpotName);
        txtSelectedDate=view.findViewById(R.id.txtSelectedDate);
        txtTotalCharges=view.findViewById(R.id.txtTotalCharges);
        // RecyclerView setup
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        confirmBookingAdapter = new ConfirmBookingAdapter(selectedSlots);
        recyclerView.setAdapter(confirmBookingAdapter);
        btnNext=view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.flFragment,
                        new SelectPaymentModeFragment()).commit();
            }
        });

        setObserver();


        return view;
    }

    private void setObserver() {
        bookingViewModel.getSpotName().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                txtSpotName.setText(s);
            }
        });
        bookingViewModel.getSelectedDate().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                txtSelectedDate.setText(s);
            }
        });
        bookingViewModel.getSelectedSlots().observe(requireActivity(), new Observer<List<SelectedSlot>>() {
            @Override
            public void onChanged(List<SelectedSlot> selectedSlotsViewModel) {
                selectedSlots.clear();
                double charges=0;
                for (SelectedSlot slot : selectedSlotsViewModel) {
                    selectedSlots.add(slot);
                    charges+=slot.getSlotPrice();
                }
                confirmBookingAdapter.notifyDataSetChanged();

                txtTotalCharges.setText(String.valueOf(charges));
                bookingViewModel.setTotalAmount(charges);
            }
        });
    }
}