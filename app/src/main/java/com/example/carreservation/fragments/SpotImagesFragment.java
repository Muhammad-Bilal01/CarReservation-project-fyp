package com.example.carreservation.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.carreservation.R;
import com.example.carreservation.adapters.SpotViewPagerAdapter;
import com.example.carreservation.adapters.ViewPagerAdapter;
import com.example.carreservation.interfaces.OnItemClickListener;
import com.example.carreservation.models.AvailableSlot;
import com.example.carreservation.models.Slot;
import com.example.carreservation.models.Spot;
import com.example.carreservation.models.SpotViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class SpotImagesFragment extends Fragment implements OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btnSave;

    public SpotImagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpotImagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpotImagesFragment newInstance(String param1, String param2) {
        SpotImagesFragment fragment = new SpotImagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    SpotViewModel spotViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        spotViewModel = new ViewModelProvider(requireActivity()).get(SpotViewModel.class);
    }

    // creating object of ViewPager
    ViewPager mViewPager;

    //List<Uri> uploadImageList;
    // images array
    List<Object> images;
    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    // Creating Object of ViewPagerAdapter
    ViewPagerAdapter mViewPagerAdapter;
    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    private ImageView imgAddImage, imgDefault;
    private ImageView[] dots;
    private int dotscount;
    private LinearLayout sliderDots;
    private Spot spot;
    private boolean IsUpdate = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spot_images, container, false);

        // Initializing the ViewPager Object
        mViewPager = (ViewPager) view.findViewById(R.id.viewPagerMain);

        images = new ArrayList<Object>();
        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new ViewPagerAdapter(getActivity(), images);


        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPagerAdapter.setOnItemClickListener(this);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnSave = view.findViewById(R.id.btnSaveSpot);
        imgAddImage = view.findViewById(R.id.img_add_spot_img);
        imgDefault = view.findViewById(R.id.img_default);
        sliderDots = view.findViewById(R.id.SliderDots);
        spot = new Spot();


        imgAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!IsUpdate && images.size() == 0)
                    Toast.makeText(getActivity(), "Spot Images are required", Toast.LENGTH_SHORT).show();
                else if (images.size() > 0) {
//                    print List of Images
//                    System.out.println("+-----------------------------+");
//                    System.out.println("Images " + images);
                    uploadImage();
                }
//                    saveSpotToDB();

            }
        });

        setObservers();

        return view;
    }

    private void setObservers() {
        spotViewModel.getSpotAddress().observe(requireActivity(),
                new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        spot.setSpotAddress(s);
                    }
                });
        spotViewModel.getPricePerHour().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                spot.setPricePerHour(s);
            }
        });
        spotViewModel.getNumOfSpot().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                spot.setNumOfSpot(s);
            }
        });
        spotViewModel.getIsSpotUpdate().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUpdate) {
                IsUpdate = isUpdate;
            }
        });
        spotViewModel.getSpotLat().observe(requireActivity(), new Observer<Double>() {
            @Override
            public void onChanged(Double lat) {
                spot.setSpotLat(lat);
            }
        });
        spotViewModel.getSpotLong().observe(requireActivity(), new Observer<Double>() {
            @Override
            public void onChanged(Double lng) {
                spot.setSpotLong(lng);
            }
        });
        spotViewModel.getSpotLocation().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                spot.setSpotLocation(s);
            }
        });
        spotViewModel.getSpotName().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                spot.setSpotName(s);
            }
        });
        spotViewModel.getFromDate().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                spot.setFromDate(s);
            }
        });
        spotViewModel.getToDate().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                spot.setToDate(s);
            }
        });
        spotViewModel.getVendorId().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                spot.setVendorId(s);
            }
        });

//        spotViewModel.getWeeklySlots().observe(requireActivity(), new Observer<Map<String, List<Slot>>>() {
//            @Override
//            public void onChanged(Map<String, List<Slot>> stringListMap) {
//                spot.setWeeklySlots(stringListMap);
//            }
//        });
        spotViewModel.getWeeklySlots().observe(requireActivity(), new Observer<Map<String, List<Slot>>>() {
            @Override
            public void onChanged(Map<String, List<Slot>> stringListMap) {
                spot.setWeeklySlots(stringListMap);
            }
        });
        spotViewModel.getId().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String Id) {
                spot.setId(Id);
            }
        });
        spotViewModel.getSpotImages().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String spotImages) {
                spot.setSpotImages(spotImages);
                String[] imgArray = spotImages.split("\\|");
                images.addAll(Arrays.asList(imgArray));
                mViewPagerAdapter.notifyDataSetChanged();
                if (imgArray.length > 0)
                    imgDefault.setVisibility(View.GONE);
            }
        });

    }




    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        // allowing multiple image to be selected
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(i, SELECT_PICTURE);
        startActivityForResult(i, 1);


        // pass the constant to compare it
        // with the returned requestCode
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

//        startActivityForResult(Intent.createChooser(i,"Select Picture"), 1);
        //getActivity().startActivityForResult( Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        System.out.println("requestCode  : " + requestCode);
//        System.out.println("resultCode  : " + resultCode);
//        System.out.println("data  : " + data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getClipData() != null) {
            int count = data.getClipData().getItemCount();
            for (int i = 0; i < count; i++) {
                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                imgDefault.setVisibility(View.GONE);
                images.add(imageUri);
                mViewPagerAdapter.notifyDataSetChanged();

            }
            mViewPager.setVisibility(View.VISIBLE);
            mViewPager.setAdapter(mViewPagerAdapter);

//                to set image slider dot indicator
            if (images.size() > 0) {

                    mViewPagerAdapter.notifyDataSetChanged();
                    dotscount = mViewPagerAdapter.getCount();
                    dots = new ImageView[dotscount];

                    for (int i = 0; i < dotscount; i++) {

                        dots[i] = new ImageView(getActivity());
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.non_active_dot));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        params.setMargins(8, 0, 8, 0);
                        sliderDots.addView(dots[i], params);
                    }
                    dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));
                }
            }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

//        }

//        if (resultCode == RESULT_OK) {
//
//            // compare the resultCode with the
//            // SELECT_PICTURE constant
//            if (requestCode == SELECT_PICTURE) {
//                // Get the url of the image from data
//                Uri selectedImageUri = data.getData();
//                if (null != selectedImageUri) {
//
//                    imgDefault.setVisibility(View.GONE);
//                    mViewPagerAdapter.notifyDataSetChanged();
//                    images.add(selectedImageUri);
//                    mViewPagerAdapter.notifyDataSetChanged();
//                    mViewPager.setVisibility(View.VISIBLE);
//                    mViewPager.setAdapter(mViewPagerAdapter);
//                }
//            }
//        }
    }

    private void uploadImage() {
        List<String> spotDownloadImageUrl = new ArrayList<>();
        // Code for showing progressDialog while uploading
        ProgressDialog progressDialog
                = new ProgressDialog(getActivity());
        progressDialog.setTitle("Process");
        if (IsUpdate)
            progressDialog.setMessage("Please wait while we are updating your spot.");
        else
            progressDialog.setMessage("Please wait while we are adding your spot.");
        progressDialog.show();
        final int[] imagesCount = {images.size()};
        for (Object path : images) {
            if (path != null) {

                if (path.toString().contains("https://")) {
                    imagesCount[0] -= 1;
                    if (imagesCount[0] == 0) {
                        saveSpotToDB();
                        progressDialog.dismiss();
                    }
                    continue;
                }
                Uri filePath = Uri.parse(path.toString());
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
                            Uri downloadUri = task.getResult();
                            imagesCount[0] -= 1;
                            spotDownloadImageUrl.add(downloadUri.toString());
                            if (imagesCount[0] == 0) {

                                String existingImages = spot.getSpotImages();
                                if (existingImages != null && existingImages.length() > 0)
                                    spot.setSpotImages(existingImages + "|" + String.join("|", spotDownloadImageUrl));
                                else
                                    spot.setSpotImages(String.join("|", spotDownloadImageUrl));
                                saveSpotToDB();
                                progressDialog.dismiss();
                            }
                        } else {
                            progressDialog.dismiss();
                            // Handle failures
                            // ...
                        }
                    }
                });
            }
        }
    }

    private Object timing() {
        HashMap<String, Object> data = new HashMap<>();

        data.put("id", "");
        data.put("VendorID", spot.getVendorId());
        data.put("PricePerHalfMinute", spot.getPricePerHour());
        data.put("spotTitle", spot.getSpotName());
        data.put("spotLocation", spot.getSpotLocation());
        data.put("spotAddress", spot.getSpotAddress());
        data.put("lat", spot.getSpotLat());
        data.put("long", spot.getSpotLong());
        data.put("fromData", spot.getFromDate());
        data.put("toData", spot.getToDate());
        data.put("NumberOfSlots", spot.getNumOfSpot());
        data.put("spotImages", spot.getSpotImages());

//       add date and Time according to the slots
        ArrayList<Object> slots = new ArrayList<Object>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");

            LocalDate strFromDate = LocalDate.parse(spot.getFromDate(), formatter);
            LocalDate strToDate = LocalDate.parse(spot.getToDate(), formatter);

            long noOfDays = ChronoUnit.DAYS.between(strFromDate, strToDate);
//                    System.out.println("Number of days: " + noOfDays);

            LocalDate currentDate = strFromDate;

            while (!currentDate.isAfter(strToDate)) {
                HashMap<String, Object> SpotPerDay = new HashMap();
                ArrayList<Object> timing = new ArrayList<>();
//                HashMap<String, Object> timeDetail = new HashMap<>();

                Map<String, List<Slot>> weeks = spot.getWeeklySlots();

                for (Map.Entry<String, List<Slot>> entry : weeks.entrySet()) {
                    String key = entry.getKey();
                    List<Slot> values = entry.getValue();
                    System.out.println("Key: " + key);

                    if (key.equalsIgnoreCase(currentDate.getDayOfWeek().toString())) {
                        SpotPerDay.put("Date", currentDate.toString());
                        SpotPerDay.put("Day", currentDate.getDayOfWeek().toString());


                        List<Slot> timeSlots = values;
                        System.out.println(timeSlots);

                        for (Slot element : timeSlots) {
                            // add availble slots slots and managed bookings
                            ArrayList<Object> availableSlots = new ArrayList();
                            HashMap<String, Object> timeDetail = new HashMap<>(); // Move this line inside the loop
//
                            System.out.println(element.getSlotTiming());
                            timeDetail.put("time", element.getSlotTiming());

                            int noOfSlots = Integer.parseInt(spot.getNumOfSpot());

                            for (int i = 1; i <= noOfSlots; i++) {
                                HashMap<String, Object> slotDetail = new HashMap();
                                slotDetail.put("SlotNo", i);
                                slotDetail.put("isBooked", false);
                                // System.out.println(slotDetail);
                                availableSlots.add(slotDetail);
                            }

                            timeDetail.put("availableSlots", availableSlots);
                            timing.add(timeDetail);
                        }


//                        timing.add(timeDetail);
                        SpotPerDay.put("slots", timing);
                    }
                }
//                timing.add(timeDetail);
//                SpotPerDay.put("slots",timing);

  /*
                // add slots and managed bookings
                ArrayList<Object> slotList = new ArrayList();
                int noOfSlots = Integer.parseInt(spot.getNumOfSpot());
                for (int i = 1; i <= noOfSlots; i++) {
                    HashMap<String, Object> slotDetail = new HashMap();
                    slotDetail.put("SlotNo", i);
                    slotDetail.put("isBooked", false);
                    // System.out.println(slotDetail);
                    slotList.add(slotDetail);
                }
*/

                // System.out.println(slotList);

                // Only add SpotPerDay to slots if it has any entries
                if (!SpotPerDay.isEmpty()) {
                    slots.add(SpotPerDay);
                }
//                slots.add(SpotPerDay);
                // System.out.println(currentDay.getDayOfWeek());
                currentDate = currentDate.plusDays(1);
            }
//            System.out.println(slots);

        }

        data.put("totalSlots", slots);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(data);
        return data;
    }

    private void saveSpotToDB() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


//      Reference to the Firestore collection
        CollectionReference dbspots = db.collection("parkingSpots");
        System.out.println("+++++++++++++++++++++++_________________ISUPDATE_________________+++++++++++++++++++++++++++++");
        if (IsUpdate) {
            Map<String,Object> data = (Map<String, Object>) timing();
            data.put("id",spot.getId());
            Task<Void> dbSpots = dbspots.document(spot.getId()).set(data).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Your Spot has been updated to Firebase Firestore", Toast.LENGTH_SHORT).show();
                            ShowVendorDashboardFragment();
                        }
                    }
            );
        } else {
            Task<DocumentReference> dbSpots = dbspots.add(timing()).addOnSuccessListener(
                    new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getActivity(), "Your Spot has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
                            ShowVendorDashboardFragment();
                        }
                    }
            ).addOnFailureListener(
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }

    /*
    private List<AvailableSlot> GetSlots() {
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        }

        // Convert string dates to LocalDate objects
        LocalDate startDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startDate = LocalDate.parse(spot.getFromDate(), formatter);
        }
        LocalDate endDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            endDate = LocalDate.parse(spot.getToDate(), formatter);
        }

        List<AvailableSlot> availableSlots = new ArrayList<>();
        LocalDate currentDate = startDate;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            while (!currentDate.isAfter(endDate)) {
                // Print the weekday name and the date
                System.out.println(currentDate.getDayOfWeek() + " " + currentDate.format(formatter));
                String day = currentDate.getDayOfWeek().toString();
                String dayOfWeek = day.charAt(0) + day.substring(1).toLowerCase();
                List<Slot> slots = spot.getWeeklySlots().get(dayOfWeek);
                if (slots != null) {
                    for (Slot slot : slots) {
                        AvailableSlot availableSlot = new AvailableSlot("", currentDate.toString(), dayOfWeek, slot.getSlotTiming(), false);
                        availableSlots.add(availableSlot);
                    }
                }
                // Move to the next day
                currentDate = currentDate.plusDays(1);
            }
        }
        return availableSlots;
    }
*/
    private void ShowVendorDashboardFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,
                new VendorDashboardFragment()).commit();
    }

    @Override
    public void onDeleteButtonClick(Map<String, Object> model, int position) {

        List<String> updatedImages = new ArrayList<>();
        if (IsUpdate && images.size() > 0) {
            String[] imgArray = spot.getSpotImages().split("\\|");
            for (String url :
                    imgArray) {
                if (!images.get(position).toString().equals(url)) {
                    updatedImages.add(url);
                }
            }
            if (updatedImages.size() > 0)
                spot.setSpotImages(String.join("|", updatedImages));
        }
        images.remove(position);
        mViewPagerAdapter.notifyDataSetChanged();
        if (images.size() == 0) {
            imgDefault.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
            mViewPagerAdapter.notifyDataSetChanged();
        }
        Toast.makeText(getActivity(), "Image removed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateButtonClick(Map<String, Object> model, int position) {

    }

    @Override
    public void onDetailsButtonClick(Map<String,Object> model, int position) {

    }

    @Override
    public void onBookButtonClick(Spot model, int position) {

    }

    @Override
    public void onMyBookButtonClick(Map<String, Object> model, int position) {

    }
}