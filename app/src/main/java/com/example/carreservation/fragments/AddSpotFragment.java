package com.example.carreservation.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.carreservation.ExpandableHeightListView;
import com.example.carreservation.R;
import com.example.carreservation.adapters.SlotAdapter;
import com.example.carreservation.components.ExpandableHeightGridView;
import com.example.carreservation.models.Slot;
import com.example.carreservation.models.SpotViewModel;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddSpotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSpotFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SpotViewModel spot;
    public AddSpotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddSpotFragment.
     */
    // TODO: Rename and change types and number of parameters
    EditText editTextSpotName,editTextSpotAddress,editTextSpotLat,editTextSpotLng,editTextSpotCharges;


    LabeledSwitch mondaySwitch,tuesdaySwitch,wednesdaySwitch,thursdaySwitch,fridaySwitch,saturdaySwitch,sundaySwitch;

    Button btnAddSpot; ImageView imgAddMondaySlot,imgAddSlotsTuesday,imgAddSlotsWednesday,imgAddSlotsThursday,imgAddSlotsFriday,imgAddSlotsSaturday,imgAddSlotsSunday;
    ExpandableHeightGridView listMonday,listTuesday,listWednesday,listThursday,listFriday,listSaturday,listSunday;
    SlotAdapter mondaySlotAdapter,tuesdayListAdapter,wednesdayListAdapter,thursdayListAdapter,fridayListAdapter,saturdayListAdapter,sundayListAdapter;
    ArrayList<String> mondaySlotList;
    ArrayList<String> tuesdaySlotList;
    ArrayList<String> wednesdaySlotList;
    ArrayList<String> thursdaySlotList;
    ArrayList<String> fridaySlotList;
    ArrayList<String> saturdaySlotList;
    ArrayList<String> sundaySlotList;
    public static String spotLocation;
    public static double spotLat;
    public static double spotLng;
    Button btnSave;
    private Spinner spinnerFrom,spinnerTo,spinnerFromAmPm,spinnerToAmPm;
    private Map<String, List<Slot>> existingWeeklySlots;

    TextView txtSpotLocation;
    EditText txtSpotName,txtSpotAddress,txtSpotCharges, txtNumberOfSpot, txtFromDate,txtToDate, txtLatitude, txtLongitude;
    public static AddSpotFragment newInstance(String param1, String param2) {
        AddSpotFragment fragment = new AddSpotFragment();
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
        spot = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(SpotViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_spot, container, false);

        //FirebaseAuth.getInstance().signOut();
        txtSpotName=view.findViewById(R.id.txtSpotName);
        txtSpotAddress=view.findViewById(R.id.txtSpotAddress);
        txtSpotLocation=view.findViewById(R.id.txtSpotLocation);
        txtSpotCharges=view.findViewById(R.id.txtSpotCharges);
        txtNumberOfSpot=view.findViewById(R.id.txtNumberOfSpot);
        txtFromDate=view.findViewById(R.id.txtFromDate);
        txtToDate=view.findViewById(R.id.txtToDate);
        txtLatitude=view.findViewById(R.id.txtLatitude);
        txtLongitude=view.findViewById(R.id.txtLongitude);
        btnSave=view.findViewById(R.id.btnNext);

        spot = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(SpotViewModel.class);

        txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetDate(0);
            }
        });
        txtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetDate(1);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String spotName,spotAddress,spotLocation,hourlyCharges,numOfSpots,fromDate,toDate;
                spotName=txtSpotName.getText().toString();
                spotAddress=txtSpotAddress.getText().toString();
                spotLocation=txtSpotLocation.getText().toString();
                hourlyCharges=txtSpotCharges.getText().toString();
                numOfSpots=txtNumberOfSpot.getText().toString();
                fromDate=txtFromDate.getText().toString();
                toDate=txtToDate.getText().toString();


                if(spotName.trim().isEmpty() || spotAddress.trim().isEmpty() ||
                        spotLocation.trim().isEmpty() || hourlyCharges.trim().isEmpty() || numOfSpots.trim().isEmpty() ||
                        fromDate.trim().isEmpty() ||toDate.trim().isEmpty() || txtLatitude.getText().toString().trim().isEmpty()
                ||txtLongitude.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getActivity(),"Please complete all details",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!CheckFromToDateIsValid(txtFromDate,txtToDate))
                    return;
                if(AnySwitchIsOnWithoutSlots())
                    return;
                if(mondaySlotList.size()==0 && tuesdaySlotList.size()==0 && wednesdaySlotList.size()==0 && thursdaySlotList.size()==0 && fridaySlotList.size()==0 && saturdaySlotList.size()==0 && sundaySlotList.size()==0)
                {
                    Toast.makeText(getActivity(),"Please add at least one slot",Toast.LENGTH_SHORT).show();
                    return;
                }
                spot.setSpotName(spotName);
                spot.setSpotAddress(spotAddress);
                spot.setSpotLocation(spotLocation);
                spot.setSpotLat(spotLat);
                spot.setSpotLong(spotLng);
                spot.setFromDate(fromDate);
                spot.setToDate(toDate);
                spot.setPricePerHour(hourlyCharges);
                spot.setNumOfSpot(numOfSpots);
                spot.setVendorId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Map<String, List<Slot>> weeklySlots=new HashMap<>();
                if(mondaySwitch.isOn() && mondaySlotList.size()>0)
                    weeklySlots.put("Monday",getSlots(mondaySlotList));
                if(tuesdaySwitch.isOn() && tuesdaySlotList.size()>0)
                    weeklySlots.put("Tuesday",getSlots(tuesdaySlotList));
                if(wednesdaySwitch.isOn() && wednesdaySlotList.size()>0)
                    weeklySlots.put("Wednesday",getSlots(wednesdaySlotList));
                if(thursdaySwitch.isOn() && thursdaySlotList.size()>0)
                    weeklySlots.put("Thursday",getSlots(thursdaySlotList));
                if(fridaySwitch.isOn() && fridaySlotList.size()>0)
                    weeklySlots.put("Friday",getSlots(fridaySlotList));
                if(saturdaySwitch.isOn() && saturdaySlotList.size()>0)
                    weeklySlots.put("Saturday",getSlots(saturdaySlotList));
                if(sundaySwitch.isOn() && sundaySlotList.size()>0)
                    weeklySlots.put("Sunday",getSlots(sundaySlotList));

                spot.setWeeklySlots(weeklySlots);
                //spot.setSpotImages(spot.getSpotImages().getValue());
                ShowAddSpotImagesFragment();


            }
        });
        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), getString(R.string.api_key), Locale.US);
        }
         List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        // Start the autocomplete intent.

        txtSpotLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(getActivity());
                startAutocomplete.launch(intent);
                //startActivity(new Intent(getActivity(), MapsActivity.class));
            }
        });

        mondaySwitch=view.findViewById(R.id.switchMonday);
        tuesdaySwitch=view.findViewById(R.id.switchTuesday);
        wednesdaySwitch=view.findViewById(R.id.switchWednesday);
        thursdaySwitch=view.findViewById(R.id.switchThursday);
        fridaySwitch=view.findViewById(R.id.switchFriday);
        saturdaySwitch=view.findViewById(R.id.switchSaturday);
        sundaySwitch=view.findViewById(R.id.switchSunday);

        imgAddMondaySlot=view.findViewById(R.id.imgAddMondaySlots);
        imgAddSlotsTuesday=view.findViewById(R.id.btnAddTuesdaySlots);
        imgAddSlotsWednesday=view.findViewById(R.id.btnAddWednesdaySlots);
        imgAddSlotsThursday=view.findViewById(R.id.btnAddThursdaySlots);
        imgAddSlotsFriday=view.findViewById(R.id.btnAddFridaySlots);
        imgAddSlotsSaturday=view.findViewById(R.id.btnAddSaturdaySlots);
        imgAddSlotsSunday=view.findViewById(R.id.btnAddSundaySlots);

        listMonday=view.findViewById(R.id.mondaySlotsList);
        listTuesday=view.findViewById(R.id.tuesdaySlotsList);
        listWednesday=view.findViewById(R.id.wednesdaySlotsList);
        listThursday=view.findViewById(R.id.thursdaySlotsList);
        listFriday=view.findViewById(R.id.fridaySlotsList);
        listSaturday=view.findViewById(R.id.saturdaySlotsList);
        listSunday=view.findViewById(R.id.sundaySlotsList);


        mondaySlotList=new ArrayList<>();
        mondaySlotAdapter=new SlotAdapter(getActivity(),mondaySlotList);
        listMonday.setAdapter(mondaySlotAdapter);
        listMonday.setExpanded(true);

        tuesdaySlotList=new ArrayList<>();
        tuesdayListAdapter=new SlotAdapter(getActivity(),tuesdaySlotList);
        listTuesday.setAdapter(tuesdayListAdapter);
        listTuesday.setExpanded(true);

        wednesdaySlotList=new ArrayList<>();
        wednesdayListAdapter=new SlotAdapter(getActivity(),wednesdaySlotList);
        listWednesday.setAdapter(wednesdayListAdapter);
        listWednesday.setExpanded(true);

        thursdaySlotList=new ArrayList<>();
        thursdayListAdapter=new SlotAdapter(getActivity(),thursdaySlotList);
        listThursday.setAdapter(thursdayListAdapter);
        listThursday.setExpanded(true);

        fridaySlotList=new ArrayList<>();
        fridayListAdapter=new SlotAdapter(getActivity(),fridaySlotList);
        listFriday.setAdapter(fridayListAdapter);
        listFriday.setExpanded(true);

        saturdaySlotList=new ArrayList<>();
        saturdayListAdapter=new SlotAdapter(getActivity(),saturdaySlotList);
        listSaturday.setAdapter(saturdayListAdapter);
        listSaturday.setExpanded(true);

        sundaySlotList=new ArrayList<>();
        sundayListAdapter=new SlotAdapter(getActivity(),sundaySlotList);
        listSunday.setAdapter(sundayListAdapter);
        listSunday.setExpanded(true);

        RegisterGridviewListners();

        mondaySwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn)
                {
                    listMonday.setVisibility(View.VISIBLE);
                    imgAddMondaySlot.setVisibility(View.VISIBLE);
                    //setGridViewHeightBasedOnChildren(listMonday,2);
                }
                else
                {
                    listMonday.setVisibility(View.GONE);
                    imgAddMondaySlot.setVisibility(View.GONE);
                }
            }
        });
        tuesdaySwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn)
                {
                    listTuesday.setVisibility(View.VISIBLE);
                    imgAddSlotsTuesday.setVisibility(View.VISIBLE);
                }
                else
                {
                    listTuesday.setVisibility(View.GONE);
                    imgAddSlotsTuesday.setVisibility(View.GONE);
                }
            }
        });

        wednesdaySwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn)
                {
                    listWednesday.setVisibility(View.VISIBLE);
                    imgAddSlotsWednesday.setVisibility(View.VISIBLE);
                }
                else
                {
                    listWednesday.setVisibility(View.GONE);
                    imgAddSlotsWednesday.setVisibility(View.GONE);
                }
            }
        });
        thursdaySwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn)
                {
                    listThursday.setVisibility(View.VISIBLE);
                    imgAddSlotsThursday.setVisibility(View.VISIBLE);
                }
                else
                {
                    listThursday.setVisibility(View.GONE);
                    imgAddSlotsThursday.setVisibility(View.GONE);
                }
            }
        });
        fridaySwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn)
                {
                    listFriday.setVisibility(View.VISIBLE);
                    imgAddSlotsFriday.setVisibility(View.VISIBLE);
                }
                else
                {
                    listFriday.setVisibility(View.GONE);
                    imgAddSlotsFriday.setVisibility(View.GONE);
                }
            }
        });
        saturdaySwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn)
                {
                    listSaturday.setVisibility(View.VISIBLE);
                    imgAddSlotsSaturday.setVisibility(View.VISIBLE);
                }
                else
                {
                    listSaturday.setVisibility(View.GONE);
                    imgAddSlotsSaturday.setVisibility(View.GONE);
                }
            }
        });
        sundaySwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn)
                {
                    listSunday.setVisibility(View.VISIBLE);
                    imgAddSlotsSunday.setVisibility(View.VISIBLE);
                }
                else
                {
                    listSunday.setVisibility(View.GONE);
                    imgAddSlotsSunday.setVisibility(View.GONE);
                }
            }
        });

        imgAddMondaySlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeRangePickerDialog(0,-1);
            }
        });
        imgAddSlotsTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeRangePickerDialog(1,-1);
            }
        });
        imgAddSlotsWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeRangePickerDialog(2,-1);
            }
        });
        imgAddSlotsThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeRangePickerDialog(3,-1);
            }
        });
        imgAddSlotsFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeRangePickerDialog(4,-1);
            }
        });
        imgAddSlotsSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeRangePickerDialog(5,-1);
            }
        });
        imgAddSlotsSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeRangePickerDialog(6,-1);
            }
        });

        listTuesday.setExpanded(true);
        listWednesday.setExpanded(true);
        listThursday.setExpanded(true);
        listFriday.setExpanded(true);
        listSaturday.setExpanded(true);
        listSunday.setExpanded(true);
        setObservers();
        return view;
    }

    private void RegisterGridviewListners() {
        listMonday.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowConfirmationToDeleteSlot(mondaySlotList,0,i);
                mondaySlotAdapter.notifyDataSetChanged();
                return true;
            }
        });
        listMonday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showTimeRangePickerDialog(0,i);
            }
        });

        listTuesday.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowConfirmationToDeleteSlot(tuesdaySlotList,1,i);
                return true;
            }
        });
        listTuesday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showTimeRangePickerDialog(1,i);
            }
        });
        listWednesday.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowConfirmationToDeleteSlot(wednesdaySlotList,2,i);
                wednesdayListAdapter.notifyDataSetChanged();
                return true;
            }
        });
        listWednesday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showTimeRangePickerDialog(2,i);
                wednesdayListAdapter.notifyDataSetChanged();
            }
        });
        listThursday.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowConfirmationToDeleteSlot(thursdaySlotList,3,i);
                thursdayListAdapter.notifyDataSetChanged();
                return true;
            }
        });
        listThursday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showTimeRangePickerDialog(3,i);
                thursdayListAdapter.notifyDataSetChanged();
            }
        });
        listFriday.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowConfirmationToDeleteSlot(fridaySlotList,4,i);
                fridayListAdapter.notifyDataSetChanged();
                return true;
            }
        });
        listFriday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showTimeRangePickerDialog(4,i);
                fridayListAdapter.notifyDataSetChanged();
            }
        });
        listSaturday.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowConfirmationToDeleteSlot(saturdaySlotList,5,i);
                saturdayListAdapter.notifyDataSetChanged();
                return true;
            }
        });
        listSaturday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showTimeRangePickerDialog(5,i);
                saturdayListAdapter.notifyDataSetChanged();
            }
        });
        listSunday.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adaptherView, View view, int i, long l) {
                ShowConfirmationToDeleteSlot(sundaySlotList,6,i);
                sundayListAdapter.notifyDataSetChanged();
                return true;
            }
        });
        listSunday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showTimeRangePickerDialog(6,i);
                sundayListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void ShowConfirmationToDeleteSlot(ArrayList<String> slotList,int day, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure want to delete?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //slotList.remove(position);
                dialog.dismiss();
                switch (day)
                {
                    case 0:
                        mondaySlotList.remove(position);
                        mondaySlotAdapter.notifyDataSetChanged();
                        return;
                    case 1:
                        tuesdaySlotList.remove(position);
                        tuesdayListAdapter.notifyDataSetChanged();
                        return;
                    case 2:
                        wednesdaySlotList.remove(position);
                        wednesdayListAdapter.notifyDataSetChanged();
                        return;
                    case 3:
                        thursdaySlotList.remove(position);
                        thursdayListAdapter.notifyDataSetChanged();
                        return;
                    case 4:
                        fridaySlotList.remove(position);
                        fridayListAdapter.notifyDataSetChanged();
                        return;
                    case 5:
                        saturdaySlotList.remove(position);
                        saturdayListAdapter.notifyDataSetChanged();
                        return;
                    case 6:
                        sundaySlotList.remove(position);
                        sundayListAdapter.notifyDataSetChanged();
                        return;
                }

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null || listAdapter.getCount()==0) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > columns ){
            x = items/columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }
    private boolean AnySwitchIsOnWithoutSlots() {
        if (mondaySwitch.isOn() && mondaySlotList.size()==0)
        {
            Toast.makeText(getActivity(), "Please add slots for monday", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (tuesdaySwitch.isOn() && tuesdaySlotList.size()==0)
        {
            Toast.makeText(getActivity(), "Please add slots for tuesday", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (wednesdaySwitch.isOn() && wednesdaySlotList.size()==0)
        {
            Toast.makeText(getActivity(), "Please add slots for wednesday", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (thursdaySwitch.isOn() && thursdaySlotList.size()==0)
        {
            Toast.makeText(getActivity(), "Please add slots for thursday", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (fridaySwitch.isOn() && fridaySlotList.size()==0)
        {
            Toast.makeText(getActivity(), "Please add slots for friday", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (saturdaySwitch.isOn() && saturdaySlotList.size()==0)
        {
            Toast.makeText(getActivity(), "Please add slots for saturday", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (sundaySwitch.isOn() && sundaySlotList.size()==0)
        {
            Toast.makeText(getActivity(), "Please add slots for sunday", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void setObservers() {
        spot.getSpotAddress().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                txtSpotAddress.setText(s);
            }
        });
        spot.getPricePerHour().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                txtSpotCharges.setText(s);
            }
        });

        spot.getSpotLat().observe(requireActivity(), new Observer<Double>() {
            @Override
            public void onChanged(Double lat) {
                txtLatitude.setText(String.valueOf(lat));
                spotLat=lat;
            }
        });
        spot.getSpotLong().observe(requireActivity(), new Observer<Double>() {
            @Override
            public void onChanged(Double lng) {
                txtLongitude.setText(String.valueOf(lng));
                spotLng=lng;
            }
        });
        spot.getSpotLocation().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                txtSpotLocation.setText(s);
            }
        });
        spot.getSpotName().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                txtSpotName.setText(s);
            }
        });
        spot.getFromDate().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                txtFromDate.setText(s);
            }
        });
        spot.getToDate().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                txtToDate.setText(s);
            }
        });
        spot.getVendorId().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //spot.setVendorId(s);
            }
        });

        spot.getWeeklySlots().observe(requireActivity(), new Observer<Map<String, List<Slot>>>() {
            @Override
            public void onChanged(Map<String, List<Slot>> stringListMap) {
                existingWeeklySlots=new HashMap<>();
                existingWeeklySlots=stringListMap;
                for (Map.Entry<String, List<Slot>> item:stringListMap.entrySet()
                     ) {

                    if(item.getKey().equals("Monday"))
                    {
                        for (Slot slot :
                                item.getValue()) {
                            mondaySlotList.add(slot.getSlotTiming());
                        }
                        mondaySwitch.setOn(true);
                        mondaySlotAdapter.notifyDataSetChanged();
                        SetVisibiltyOfViews(true,listMonday,imgAddMondaySlot);

                    }
                    else if(item.getKey().equals("Tuesday"))
                    {
                        for (Slot slot :
                                item.getValue()) {
                            tuesdaySlotList.add(slot.getSlotTiming());
                        }
                        tuesdaySwitch.setOn(true);
                        tuesdayListAdapter.notifyDataSetChanged();
                        SetVisibiltyOfViews(true,listTuesday,imgAddSlotsTuesday);
                    }
                    else if(item.getKey().equals("Wednesday"))
                    {
                        for (Slot slot :
                                item.getValue()) {
                            wednesdaySlotList.add(slot.getSlotTiming());
                        }
                        wednesdaySwitch.setOn(true);
                        wednesdayListAdapter.notifyDataSetChanged();
                        SetVisibiltyOfViews(true,listWednesday,imgAddSlotsWednesday);
                    }
                    else if(item.getKey().equals("Thursday"))
                    {
                        for (Slot slot :
                                item.getValue()) {
                            thursdaySlotList.add(slot.getSlotTiming());
                        }
                        thursdaySwitch.setOn(true);
                        thursdayListAdapter.notifyDataSetChanged();
                        SetVisibiltyOfViews(true,listThursday,imgAddSlotsThursday);
                    }
                    else if(item.getKey().equals("Friday"))
                    {
                        for (Slot slot :
                                item.getValue()) {
                            fridaySlotList.add(slot.getSlotTiming());
                        }
                        fridaySwitch.setOn(true);
                        fridayListAdapter.notifyDataSetChanged();
                        SetVisibiltyOfViews(true,listFriday,imgAddSlotsFriday);
                    }
                    else if(item.getKey().equals("Saturday"))
                    {
                        for (Slot slot :
                                item.getValue()) {
                            saturdaySlotList.add(slot.getSlotTiming());
                        }
                        saturdaySwitch.setOn(true);
                        saturdayListAdapter.notifyDataSetChanged();
                        SetVisibiltyOfViews(true,listSaturday,imgAddSlotsSaturday);
                    }
                    else if(item.getKey().equals("Sunday"))
                    {
                        for (Slot slot :
                                item.getValue()) {
                            sundaySlotList.add(slot.getSlotTiming());
                        }
                        sundaySwitch.setOn(true);
                        sundayListAdapter.notifyDataSetChanged();
                        SetVisibiltyOfViews(true,listSunday,imgAddSlotsSunday);
                    }

                }
            }
        });

    }

    private void SetVisibiltyOfViews(boolean isOn, ExpandableHeightGridView listView, ImageView imgAddSlot) {
        if (isOn)
        {
            listView.setVisibility(View.VISIBLE);
            imgAddSlot.setVisibility(View.VISIBLE);
        }
        else
        {
            listView.setVisibility(View.GONE);
            imgAddSlot.setVisibility(View.GONE);
        }
    }

    private void ShowViewSpotFragment() {

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,
                new ViewSpotsFragment()).commit();
    }
    private void ShowAddSpotImagesFragment() {

        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.flFragment,
                new SpotImagesFragment()).commit();
    }
    private void SetDate(int i) {
        final Calendar c = Calendar.getInstance();

        // on below line we are getting
        // our day, month and year.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        long now=c.getTimeInMillis();
        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our text view.
                        String day,month;
                        if(dayOfMonth<10)
                            day="0"+dayOfMonth;
                        else
                            day=String.valueOf(dayOfMonth);
                        if ((monthOfYear+1)<10)
                            month="0"+(monthOfYear+1);
                        else
                            month=String.valueOf(monthOfYear+1);
                        if(i==0)
                            txtFromDate.setText(day+"-"+(month)+"-"+year);
                        else if(i==1)
                            txtToDate.setText(day+"-"+(month)+"-"+year);
                        CheckFromToDateIsValid(txtFromDate,txtToDate);

                    }
                },
                // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day);
        // at last we are calling show to
        // display our date picker dialog.
        datePickerDialog.getDatePicker().setMinDate(now);
        datePickerDialog.show();
    }

    private boolean CheckFromToDateIsValid(EditText txtFromDate, EditText txtToDate) {

        if(txtFromDate.getText().toString().length()>0 && txtToDate.getText().toString().length()>0){
            DateTimeFormatter formatter = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            }

            // Parse your date strings
            LocalDate fromDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                fromDate = LocalDate.parse(txtFromDate.getText().toString(), formatter);

                LocalDate toDate = LocalDate.parse(txtToDate.getText().toString(), formatter);
                // Compare dates
                if (fromDate.isAfter(toDate)) {
                    Toast.makeText(getActivity(), "From date should be less than to date.", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    System.out.println("From date is less than to date.");
                    return true;
                }
            }

        }

        return false;
    }


    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    private List<Slot> getSlots(ArrayList<String> mondaySlotList) {
        List<Slot> slots=new ArrayList<>();
        for (String item:mondaySlotList)
            slots.add(new Slot(item));
        return slots;
    }

    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);
                        txtSpotLocation.setText(place.getName());
                        spotLat= place.getLatLng().latitude;
                        spotLng=place.getLatLng().longitude;
                        txtLatitude.setText(String.valueOf(spotLat));
                        txtLongitude.setText(String.valueOf(spotLng));
                        Log.i("TAG", "Place: ${place.getName()}, ${place.getId()}");
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i("TAG", "User canceled autocomplete");
                }
            });
    private void showTimeRangePickerDialog(int dayId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_time_range_picker, null);
        builder.setView(view);
        spinnerFrom=view.findViewById(R.id.spinnerFromTime);
        spinnerTo=view.findViewById(R.id.spinnerToTime);
        spinnerFromAmPm=view.findViewById(R.id.spinnerFromAmPm);
        spinnerToAmPm=view.findViewById(R.id.spinnerToAmPm);

        spinnerToAmPm.setClickable(false);
        spinnerToAmPm.setFocusable(false);
        spinnerToAmPm.setEnabled(false);
        spinnerToAmPm.setLongClickable(false);

        spinnerTo.setClickable(false);
        spinnerTo.setFocusable(false);
        spinnerTo.setEnabled(false);
        spinnerTo.setLongClickable(false);
        String timing= GetTiming(dayId,position);
        if(timing.length()>0)
        {
            String[] fromTo=timing.split("-");
            String[] fromWithTt=fromTo[0].split(" ");
            spinnerFrom.setSelection(Integer.parseInt(fromWithTt[0])-1);
        }
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int fromTime=Integer.parseInt(spinnerFrom.getItemAtPosition(i).toString());
                //Set one hour plus
                if(i==11)
                    spinnerTo.setSelection(0);
                else
                    spinnerTo.setSelection((i+1));
                if(fromTime==11 && spinnerFromAmPm.getSelectedItem().toString().equals("AM"))
                    spinnerToAmPm.setSelection(1);
                if(fromTime==11 && spinnerFromAmPm.getSelectedItem().toString().equals("PM"))
                    spinnerToAmPm.setSelection(0);
                if(fromTime<11 && spinnerFromAmPm.getSelectedItem().toString().equals("AM"))
                    spinnerToAmPm.setSelection(0);
                if(fromTime<11 && spinnerFromAmPm.getSelectedItem().toString().equals("PM"))
                    spinnerToAmPm.setSelection(1);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerFromAmPm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int fromTime=Integer.parseInt(spinnerFrom.getSelectedItem().toString());
                if(fromTime==11 && spinnerFromAmPm.getItemAtPosition(i).toString().equals("AM"))
                    spinnerToAmPm.setSelection(1);
                if(fromTime==11 && spinnerFromAmPm.getItemAtPosition(i).toString().equals("PM"))
                    spinnerToAmPm.setSelection(0);
                if(fromTime<11 && spinnerFromAmPm.getItemAtPosition(i).toString().equals("AM"))
                    spinnerToAmPm.setSelection(0);
                if(fromTime<11 && spinnerFromAmPm.getItemAtPosition(i).toString().equals("PM"))
                    spinnerToAmPm.setSelection(1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fromTime = (String)spinnerFrom.getSelectedItem()+" "+(String)spinnerFromAmPm.getSelectedItem();

                String toTime = (String)spinnerTo.getSelectedItem()+" "+(String)spinnerToAmPm.getSelectedItem();
                //toTimeTextView.setText("To: " + toTime);
                addSlotToRespectiveSlotList(fromTime,toTime,dayId,position);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing or handle cancellation
            }
        });

        builder.show();
    }

    private String GetTiming(int dayId, int position) {
        if(position>=0){
            switch (dayId)
            {
                case 0:
                    return mondaySlotList.get(position);
                case 1:
                    return tuesdaySlotList.get(position);
                case 2:
                    return wednesdaySlotList.get(position);
                case 3:
                    return thursdaySlotList.get(position);
                case 4:
                    return fridaySlotList.get(position);
                case 5:
                    return saturdaySlotList.get(position);
                case 6:
                    return sundaySlotList.get(position);
            }

        }
        return "";
    }

    private void addSlotToRespectiveSlotList(String fromTime, String toTime, int dayId, int position) {
        if (fromTime.equals(toTime))
        {
            Toast.makeText(getActivity(),"Invalid slot selected",Toast.LENGTH_SHORT).show();
            return;
        }
        switch (dayId){

            case 0:
                if(slotAlreadytExists(mondaySlotList,fromTime+" - "+toTime))
                    Toast.makeText(getActivity(),"Slot already exists",Toast.LENGTH_SHORT).show();
                else
                {
                    if (position<0)
                        mondaySlotList.add(fromTime+" - "+toTime);
                    else
                        mondaySlotList.set(position,fromTime+" - "+toTime);
                    mondaySlotAdapter.notifyDataSetChanged();
                }
                //setGridViewHeightBasedOnChildren(listMonday,2);
                return;
            case 1:
                if(slotAlreadytExists(tuesdaySlotList,fromTime+" - "+toTime))
                    Toast.makeText(getActivity(),"Slot already exists",Toast.LENGTH_SHORT).show();
                else
                {
                    if (position<0)
                        tuesdaySlotList.add(fromTime+" - "+toTime);
                    else
                        tuesdaySlotList.set(position,fromTime+" - "+toTime);
                    tuesdayListAdapter.notifyDataSetChanged();
                }

                return;
            case 2:
                if(slotAlreadytExists(wednesdaySlotList,fromTime+" - "+toTime))
                    Toast.makeText(getActivity(),"Slot already exists",Toast.LENGTH_SHORT).show();
                else {
                    if (position<0)
                        wednesdaySlotList.add(fromTime+" - "+toTime);
                    else
                        wednesdaySlotList.set(position,fromTime+" - "+toTime);
                    wednesdayListAdapter.notifyDataSetChanged();
                }
                return;
            case 3:
                if(slotAlreadytExists(thursdaySlotList,fromTime+" - "+toTime))
                    Toast.makeText(getActivity(),"Slot already exists",Toast.LENGTH_SHORT).show();
                else
                {
                    if (position<0)
                        thursdaySlotList.add(fromTime+" - "+toTime);
                    else
                        thursdaySlotList.set(position,fromTime+" - "+toTime);
                    thursdayListAdapter.notifyDataSetChanged();
                }
                return;
            case 4:
                if(slotAlreadytExists(fridaySlotList,fromTime+" - "+toTime))
                    Toast.makeText(getActivity(),"Slot already exists",Toast.LENGTH_SHORT).show();
                else {

                    if (position<0)
                        fridaySlotList.add(fromTime+" - "+toTime);
                    else
                        fridaySlotList.set(position,fromTime+" - "+toTime);
                    fridayListAdapter.notifyDataSetChanged();
                }
                return;
            case 5:
                if(slotAlreadytExists(saturdaySlotList,fromTime+" - "+toTime))
                    Toast.makeText(getActivity(),"Slot already exists",Toast.LENGTH_SHORT).show();
                else {

                    if (position<0)
                        saturdaySlotList.add(fromTime+" - "+toTime);
                    else
                        saturdaySlotList.set(position,fromTime+" - "+toTime);
                    saturdayListAdapter.notifyDataSetChanged();
                }
                return;
            case 6:
                if(slotAlreadytExists(sundaySlotList,fromTime+" - "+toTime))
                    Toast.makeText(getActivity(),"Slot already exists",Toast.LENGTH_SHORT).show();
                else {
                    if (position<0)
                        sundaySlotList.add(fromTime+" - "+toTime);
                    else
                        sundaySlotList.set(position,fromTime+" - "+toTime);
                    sundayListAdapter.notifyDataSetChanged();
                }
                return;
            default:

        }
    }

    private boolean slotAlreadytExists(ArrayList<String> slotList, String s) {
        return slotList.contains(s);
    }

}