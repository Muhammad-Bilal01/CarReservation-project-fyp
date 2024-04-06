package com.example.carreservation.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carreservation.CommonLoginActivity;
import com.example.carreservation.R;
import com.example.carreservation.UserDashboardActivity;
import com.example.carreservation.VendorDrawerActivity;
import com.example.carreservation.adapters.SpotViewPagerAdapter;
import com.example.carreservation.models.AvailableSlot;
import com.example.carreservation.models.BookingViewModel;
import com.example.carreservation.models.ParkingSpots;
import com.example.carreservation.models.SelectedSlot;
import com.example.carreservation.models.Slot;
import com.example.carreservation.models.Spot;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewSpotDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewSpotDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewSpotDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewSpotDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewSpotDetailsFragment newInstance(String param1, String param2) {
        ViewSpotDetailsFragment fragment = new ViewSpotDetailsFragment();
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

    ViewPager viewPager;
    List<String> images;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    Spot spot;
    Map<String, Object> parkingSpot;

    ParkingSpots myParkingSpot = new ParkingSpots();
    List<Object> times = new ArrayList<>();


    TextView txtSpotName, txtSpotAddress, txtSpotCharges, txtListView, txtMapView;
    ChipGroup chipGroup;
    Button btnNext;
    BookingViewModel bookingViewModel;
    TextView txtChooseDate;
    ImageView imgListView, imMapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_spot_details, container, false);
        images = new ArrayList<>();
        chipGroup = view.findViewById(R.id.chipGroup);
        bookingViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(BookingViewModel.class);
        sliderDotspanel = (LinearLayout) view.findViewById(R.id.SliderDots);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        txtSpotName = view.findViewById(R.id.txtSpotName);
        txtSpotAddress = view.findViewById(R.id.txtSpotAddress);
        txtSpotCharges = view.findViewById(R.id.txtSpotCharges);
        txtChooseDate = view.findViewById(R.id.txtChooseDate);

        imgListView = view.findViewById(R.id.imgList);
        imMapView = view.findViewById(R.id.imgMap);
        txtListView = view.findViewById(R.id.txtListView);
        txtMapView = view.findViewById(R.id.txtMapView);
        txtListView.setTypeface(null, Typeface.BOLD);
        txtMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenMapsFragment();
            }
        });
        imMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenMapsFragment();
            }
        });
        txtChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseDate();
            }
        });

        btnNext = view.findViewById(R.id.btnNext);
        SpotViewPagerAdapter viewPagerAdapter = new SpotViewPagerAdapter(getActivity(), images);

        viewPager.setAdapter(viewPagerAdapter);

        Bundle args = getArguments();
//        String spotId = args.getString("documentId");
//        DocumentReference dataref = FirebaseFirestore.getInstance().collection("spots").document("spotId");

//       Get Parking Spots data with specific ID
        String parkingSpotId = args.getString("documentId");

        System.out.println("id-----> " + args.getString("documentId"));
        DocumentReference dataref = FirebaseFirestore.getInstance().collection("parkingSpots").document(parkingSpotId);

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

//                Manage Value
//                System.out.println("++++++++++++++++++++++++++++++++++++");
                System.out.println(documentSnapshot.getId());
//                System.out.println(parkingSpot);


                parkingSpot = documentSnapshot.getData();

                spot = documentSnapshot.toObject(Spot.class);
                spot.setId(documentSnapshot.getId());

                for (Map.Entry<String, Object> parkSpot : parkingSpot.entrySet()) {
                    String key = parkSpot.getKey();
                    Object value = parkSpot.getValue();


                    switch (key) {
                        case "spotTitle":
                            spot.setSpotName(value.toString());
                            txtSpotName.setText(parkSpot.getValue().toString());
                            break;
                        case "spotAddress":
                            spot.setSpotAddress(value.toString());
                            txtSpotAddress.setText(parkSpot.getValue().toString());
                            break;
                        case "VendorID":
                            spot.setVendorId(value.toString());
                            break;
                        case "PricePerHalfMinute":
                            spot.setPricePerHour(value.toString());
                            txtSpotCharges.setText("PKR " + value.toString());
                            break;
                        case "spotImages":
                            spot.setSpotImages(value.toString());
                            myParkingSpot.setSpotImages(parkSpot.getValue().toString());
                            break;
                        case "fromData":
                            spot.setFromDate(value.toString());
                            myParkingSpot.setFromDate(parkSpot.getValue().toString());
                            System.out.println(myParkingSpot.getFromDate());
                            break;
                        case "toData":
                            spot.setToDate(value.toString());
                            myParkingSpot.setToDate(parkSpot.getValue().toString());
                            System.out.println(myParkingSpot.getToDate());
                            break;
                        case "totalSlots":
                            times = (List<Object>) parkSpot.getValue();
                            break;

                        default:
                            break;
                    }

                }

//                to set image slider dot indicator
                if (myParkingSpot.getSpotImages().length() > 0) {
                    String[] urls = myParkingSpot.getSpotImages().split("\\|");
                    for (String url :
                            urls) {
                        images.add(url);

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

                /*
                spot = documentSnapshot.toObject(Spot.class);
                spot.setId(documentSnapshot.getId());
                txtSpotName.setText(spot.getSpotName());
                txtSpotAddress.setText(spot.getSpotAddress());
                txtSpotCharges.setText("PKR " + spot.getPricePerHour());


                if (spot.getSpotImages().length() > 0) {
                    String[] urls = spot.getSpotImages().split("\\|");
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
*/

            }
        });



        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(spot);
                List<SelectedSlot> slots = new ArrayList<>();
                for (int i = 0; i < 24; i++) {
                    Chip chip = (Chip) chipGroup.getChildAt(i);
                    if (chip.isChecked()) {
                        SelectedSlot slot = new SelectedSlot(chip.getText().toString(), Double.parseDouble(spot.getPricePerHour()));
                        slots.add(slot);
                    }

                }
                if (slots.size() == 0)
                    Toast.makeText(getActivity(), "Please select slot to proceed", Toast.LENGTH_SHORT).show();
                else {
                    bookingViewModel.setSpotName(spot.getSpotName());
                    bookingViewModel.setSpotAddress(spot.getSpotAddress());
                    bookingViewModel.setSelectedSlots(slots);
                    bookingViewModel.setVendorId(spot.getVendorId());
                    bookingViewModel.setSpotId(spot.getId());
                    bookingViewModel.setSpotImages(spot.getSpotImages());
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.flFragment,
                            new BookingVehicleDetailsFragment()).commit();

                }

            }
        });

        return view;
    }


    private void OpenMapsFragment() {
        Bundle bundle = new Bundle();

        bundle.putString("documentId", spot.getId());
        bundle.putDouble("Latitude", Double.parseDouble(parkingSpot.get("lat").toString()));
        bundle.putDouble("Longitude", Double.parseDouble(parkingSpot.get("long").toString()));
        bundle.putString("SpotLocation", parkingSpot.get("spotLocation").toString());

//        bundle.putDouble("Latitude", spot.getSpotLat());
//        bundle.putDouble("Longitude", spot.getSpotLat());
//        bundle.putString("SpotLocation", spot.getSpotLocation());

        Fragment mapsFragment = new MapsFragment();
        mapsFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,
                mapsFragment).commit();
    }

    private void ChooseDate() {
        final Calendar c = Calendar.getInstance();

        // on below line we are getting
        // our day, month and year.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        long now = c.getTimeInMillis();

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our text view.
                        String day, month;
                        if (dayOfMonth < 10)
                            day = "0" + dayOfMonth;
                        else
                            day = String.valueOf(dayOfMonth);
                        if ((monthOfYear + 1) < 10)
                            month = "0" + (monthOfYear + 1);
                        else
                            month = String.valueOf(monthOfYear + 1);
                        String date = year + "-" + (month) + "-" + day;
                        bookingViewModel.setSelectedDate(date);
                        txtChooseDate.setText(day + "-" + (month) + "-" + year);
                        showAvailableSlots(date);

                    }
                },
                // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day);
        // at last we are calling show to
        // display our date picker dialog.
        //datePickerDialog.getDatePicker().setMinDate(now);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        try {
//            Date maxDate = sdf.parse(spot.getToDate());
//            Date minDate = sdf.parse(spot.getFromDate());

            Date maxDate = sdf.parse(myParkingSpot.getToDate());
            Date minDate = sdf.parse(myParkingSpot.getFromDate());


            // Set the maximum date for the DatePicker
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(minDate);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            calendar.setTime(maxDate);
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());


        } catch (ParseException e) {
            e.printStackTrace();
        }

        datePickerDialog.getDatePicker().setMinDate(now);

        if (txtChooseDate.getText().toString().contains("-")) {
            System.out.println(txtChooseDate.getText().toString());
            String[] completeDate = txtChooseDate.getText().toString().split("-");
            datePickerDialog.getDatePicker().init(Integer.parseInt(completeDate[2]), Integer.parseInt(completeDate[1]), Integer.parseInt(completeDate[0]), null);
        }
        datePickerDialog.show();
    }

    private void showAvailableSlots(String selectedDate) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {


            List<Map<String, Object>> DaySlots = new ArrayList<>();
            List<Map<String, Object>> timeSlots = new ArrayList<>();

            for (Object slot :
                    times) {
                Map<String, Object> allSlots = (Map<String, Object>) slot;

                for (Map.Entry<String, Object> availableTimes : allSlots.entrySet()) {
                    Object key = availableTimes.getKey();
                    String val = availableTimes.getValue().toString();

//                    System.out.println("Key "+key+" Value "+val);
                    if (key.equals("Date") && val.equalsIgnoreCase(selectedDate)) {
//                        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
//                        System.out.println("Date:: " + val + " selected Date:: "+selectedDate);
                        DaySlots.add(allSlots);
                    }

                }
            }

            Map<String, Object> selectedSlots = new HashMap<>();
            for (Map<String, Object> slotMap :
                    DaySlots) {
                for (Map.Entry<String, Object> entry : slotMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    // Do something with the key and value

                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
                    if (key.toString().equalsIgnoreCase("slots")) {

                        selectedSlots.put("slot", value);
                    }
                    System.out.println("Key: " + key + ", Value: " + value);
                }
            }

            for (Map.Entry<String, Object> timesAvailable :
                    selectedSlots.entrySet()) {
                Object key = timesAvailable.getKey();
                Object val = timesAvailable.getValue();


                if (key.toString() == "slot") {
                    List<Object> myList = (List<Object>) val;

//                    System.out.println("Key " + key + " val " + val);

                    for (Object item :
                            myList) {

                        Map<String, Object> timeMap = (Map<String, Object>) item;

                        List<Object> availableSlots = (List<Object>) timeMap.get("availableSlots");


                        boolean flag = true;
                        for (Object freeSlot :
                                availableSlots) {

                            Map<String, Object> slot = (Map<String, Object>) freeSlot;
                            System.out.println(slot.get("isBooked"));
                            if (slot.get("isBooked").toString().equalsIgnoreCase("false")) {

                                int slotNo = Integer.parseInt(slot.get("SlotNo").toString());
                                boolean isBooked = Boolean.parseBoolean(slot.get("isBooked").toString());
                                Map<String, Object> map = new HashMap<>();
                                map.put("time", timeMap.get("time").toString());
                                map.put("slotNo", slotNo);
                                map.put("isBooked", isBooked);
                                timeSlots.add(map);
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("time", timeMap.get("time").toString());
                            map.put("slotNo", 0);
                            map.put("isBooked", true);
                            timeSlots.add(map);
                        }

                        System.out.println("time ::: " + timeSlots);

                    }
                }


            }

            System.out.println(timeSlots);


//            To create Chips object
            for (int i = 0; i < 24; i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                chip.setSelected(false);
                chip.setVisibility(View.GONE);
                chip.setClickable(true);
                chip.setChecked(false);
                chip.setChipBackgroundColorResource(R.color.background_color_chip_state_list);
            }

            if (timeSlots != null && timeSlots.size() > 0) {
//                List<AvailableSlot> availableSlots = spot.getAvailableSlots().stream().filter(x -> x.getDate().equals(selectedDate)).collect(Collectors.toList());

                for (int i = 0; i < timeSlots.size(); i++) {
                    Chip chip = (Chip) chipGroup.getChildAt(i);
                    chip.setText(timeSlots.get(i).get("time").toString());
                    chip.setVisibility(View.VISIBLE);
                    if (timeSlots.get(i).get("isBooked").toString().equalsIgnoreCase("true")) {
                        chip.setClickable(false);
                        chip.setChipBackgroundColorResource(com.google.android.libraries.places.R.color.quantum_grey500);
                    }
                }
//                if (timeSlots.size() == 0)
//                    Toast.makeText(getActivity(), "No slot available try some other dates", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(getActivity(), "No slot available try some other dates", Toast.LENGTH_SHORT).show();
            }


            /*
            if (spot.getAvailableSlots() != null && spot.getAvailableSlots().size() > 0) {
                List<AvailableSlot> availableSlots = spot.getAvailableSlots().stream().filter(x -> x.getDate().equals(selectedDate)).collect(Collectors.toList());
                for (int i = 0; i < 24; i++) {
                    Chip chip = (Chip) chipGroup.getChildAt(i);
                    chip.setSelected(false);
                    chip.setVisibility(View.GONE);
                    chip.setClickable(true);
                    chip.setChecked(false);
                    chip.setChipBackgroundColorResource(R.color.background_color_chip_state_list);
                }
                for (int i = 0; i < availableSlots.size(); i++) {
                    Chip chip = (Chip) chipGroup.getChildAt(i);
                    chip.setText(availableSlots.get(i).getSlotName());
                    chip.setVisibility(View.VISIBLE);
                    if (availableSlots.get(i).getIsbooked()) {
                        chip.setClickable(false);
                        chip.setChipBackgroundColorResource(com.google.android.libraries.places.R.color.quantum_grey500);
                    }
                }
                if (availableSlots.size() == 0)
                    Toast.makeText(getActivity(), "No slot available try some other dates", Toast.LENGTH_SHORT).show();

            }
*/
        }
    }

    private String formatDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private Calendar stringToCalendar(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        try {
            Date date = dateFormat.parse(dateString);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }
}