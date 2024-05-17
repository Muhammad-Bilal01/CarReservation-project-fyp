package com.example.carreservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.carreservation.adapters.ReviewRecylcerAdapter;
import com.example.carreservation.models.Reviews;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserReviewActivity extends AppCompatActivity {
    private ReviewRecylcerAdapter adapter;
    private ArrayList<Reviews> reviewsArrayList;
    private RecyclerView recyclerView;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review);
        recyclerView = findViewById(R.id.review_recylcer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsArrayList = new ArrayList<>();

        Intent intent = getIntent();
        String id = intent.getStringExtra("spotId");
//        System.out.println(" Intent Id ++++" + id);


//        Get Data from Firebase
        db = FirebaseFirestore.getInstance();
        db.collection("reviews").whereEqualTo("spotId", id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {

//                        System.out.println("Docs --> " + d);
                        Map<String, Object> reviews = d.getData();
                        System.out.println("reviews --> " + reviews.get("reviews"));
                        reviewsArrayList.add(new Reviews(  reviews.get("reviews").toString(), reviews.get("message").toString()));


                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });



        adapter = new ReviewRecylcerAdapter(this, reviewsArrayList);
        recyclerView.setAdapter(adapter);
    }
}