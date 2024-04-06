package com.example.carreservation.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.example.carreservation.R;
import com.example.carreservation.adapters.CustomizedExpandableListAdapter;
import com.example.carreservation.adapters.SpotAdapter;
import com.example.carreservation.adapters.SpotViewPagerAdapter;
import com.example.carreservation.interfaces.OnItemClickListener;
import com.example.carreservation.models.BookingViewModel;
import com.example.carreservation.models.SelectedSlot;
import com.example.carreservation.models.Slot;
import com.example.carreservation.models.Spot;
import com.example.carreservation.models.SpotViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewSpotsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewSpotsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewSpotsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewSpotsFragment.
     */
    // TODO: Rename and change types and number of parameters
    ViewPager viewPager;
    List<String> images;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    //    Spot spot;
    Map<String, Object> spotObj;

    TextView txtSpotName, txtSpotAddress, txtSpotCharges;
    ExpandableListView expandableListViewExample;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableTitleList;
    HashMap<String, List<String>> expandableDetailList;

    public static ViewSpotsFragment newInstance(String param1, String param2) {
        ViewSpotsFragment fragment = new ViewSpotsFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_spots, container, false);
        images = new ArrayList<>();
        sliderDotspanel = (LinearLayout) view.findViewById(R.id.SliderDots);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        txtSpotName = view.findViewById(R.id.txtSpotName);
        txtSpotAddress = view.findViewById(R.id.txtSpotAddress);
        txtSpotCharges = view.findViewById(R.id.txtSpotCharges);

        SpotViewPagerAdapter viewPagerAdapter = new SpotViewPagerAdapter(getActivity(), images);

        viewPager.setAdapter(viewPagerAdapter);

        Bundle args = getArguments();
        String spotId = args.getString("documentId");
//        System.out.println("Id === "+ spotId);
        DocumentReference dataref = FirebaseFirestore.getInstance().collection("parkingSpots").document(spotId);
        expandableListViewExample = (ExpandableListView) view.findViewById(R.id.expandableListView);
        expandableDetailList = new HashMap<>();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        dataref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

//              spot=documentSnapshot.toObject(Spot.class);
                spotObj = documentSnapshot.getData();
                spotObj.put("id", documentSnapshot.getId());
                System.out.println("Object ---> " + spotObj);

//                spot.setId(documentSnapshot.getId());
                txtSpotName.setText(spotObj.get("spotTitle").toString());
                txtSpotAddress.setText(spotObj.get("spotAddress").toString());
                txtSpotCharges.setText("PKR " + spotObj.get("PricePerHalfMinute").toString());

//                To show weekly slots
//                ArrayList<String> sortedKeys
//                        = new ArrayList<String>(spot.getWeeklySlots().keySet());

                // Initialize the output map
                Map<String, List<String>> outputData = new HashMap<>();
                // Iterate over the totalSlots list
                List<Map<String, Object>> totalSlots = (List<Map<String, Object>>) spotObj.get("totalSlots");
                for (Map<String, Object> slot : totalSlots) {
                    // Extract the day
                    String day = slot.get("Day").toString();
                    // Initialize the list for the day if not already present
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        outputData.putIfAbsent(day, new ArrayList<>());
                    }

                    // Extract the slots for the day
                    List<Map<String, Object>> slots = (List<Map<String, Object>>) slot.get("slots");

                    for (Map<String, Object> timeSlot : slots) {
                        String time = timeSlot.get("time").toString();
                        // Add the time to the list for the day
                        outputData.get(day).add(time);
                    }
                }

                // Print the output
                for (Map.Entry<String, List<String>> entry : outputData.entrySet()) {
                    System.out.println("Expected Output" + entry.getKey() + ": " + entry.getValue());
                    expandableDetailList.put( entry.getKey(), entry.getValue());
                }



//                Collections.sort(sortedKeys);
//                for(String key : sortedKeys) {
//                    List<String> slots=new ArrayList<>();
//                    for (Slot value : spot.getWeeklySlots().get(key)) {
//                        slots.add(value.getSlotTiming());
//                    }
//                    expandableDetailList.put(key,slots);
//                }


                expandableTitleList = new ArrayList<String>(expandableDetailList.keySet());
                expandableListAdapter = new CustomizedExpandableListAdapter(getActivity(), expandableTitleList, expandableDetailList);
                expandableListViewExample.setAdapter(expandableListAdapter);


//                to show images and dots indicator
                if (spotObj.get("spotImages").toString().length() > 0) {
                    String[] urls = spotObj.get("spotImages").toString().split("\\|");
                    for (String url :
                            urls) {
                        images.add(url);
                    }

                    viewPagerAdapter.notifyDataSetChanged();
                    dotscount = viewPagerAdapter.getCount();
                    dots = new ImageView[dotscount];

                    for (int i = 0; i < dotscount; i++) {

                        dots[i] = new ImageView(getActivity());
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.non_active_dot));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        params.setMargins(8, 0, 8, 0);

                        sliderDotspanel.addView(dots[i], params);

                    }
                    dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));
                }

            }
        });

        return view;
//    }
//    private void showAddSpotFragment() {
//        // Replace the fragment with AddSpotFragment
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        AddSpotFragment fragment = new AddSpotFragment();
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,
//                new AddSpotFragment()).commit();


    }
}