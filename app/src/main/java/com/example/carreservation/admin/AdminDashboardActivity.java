package com.example.carreservation.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.carreservation.R;
import com.example.carreservation.adapters.ChatUserRecyclerAdapter;
import com.example.carreservation.models.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {

    FirebaseFirestore firebase;

    DatabaseReference ref;

    private RecyclerView recyclerView;
    ChatUserRecyclerAdapter adapter;

    private ArrayList<UserModel> usersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        recyclerView = findViewById(R.id.recyclerview);

        adapter = new ChatUserRecyclerAdapter(getApplicationContext(),usersArrayList );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        firebase = FirebaseFirestore.getInstance();
        usersArrayList = new ArrayList<>();

        usersArrayList.add(new UserModel("Bilal","",""));
        usersArrayList.add(new UserModel("Bilal","",""));
        usersArrayList.add(new UserModel("Bilal","",""));
        usersArrayList.add(new UserModel("Bilal","",""));


        adapter.notifyDataSetChanged();



    }


//    get or create chatroom
    void getOrCreateChatRoomModel(){}


}