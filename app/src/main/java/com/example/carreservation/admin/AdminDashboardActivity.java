package com.example.carreservation.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.carreservation.R;
import com.example.carreservation.adapters.ChatUserRecyclerAdapter;
import com.example.carreservation.helper.FirebaseUtils;
import com.example.carreservation.models.ChatMessageModel;
import com.example.carreservation.models.ChatRoomModel;
import com.example.carreservation.models.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminDashboardActivity extends AppCompatActivity {

    FirebaseFirestore firebase;

    DatabaseReference ref;

    private RecyclerView recyclerView;
    ChatUserRecyclerAdapter adapter;

    private ArrayList<ChatRoomModel> usersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        recyclerView = findViewById(R.id.recyclerview);
        firebase = FirebaseFirestore.getInstance();
        usersArrayList = new ArrayList<>();


        adapter = new ChatUserRecyclerAdapter(getApplicationContext(), usersArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

//        Query query = FirebaseUtils.allChatRoomsCollectionsReference().whereArrayContains("userIds", FirebaseUtils.currentUserId()).orderBy("lastMessageTimestamplastMessageTimestamp", com.google.firebase.firestore.Query.Direction.DESCENDING);

        FirebaseUtils.allChatRoomsCollectionsReference().whereArrayContains("userIds", FirebaseUtils.currentUserId()).orderBy("lastMessageTimestamp", com.google.firebase.firestore.Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                System.out.println("QUERY -->" + queryDocumentSnapshots);
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        Map<String, Object> model = d.getData();
                        System.out.println("model --> " + model);
                        ChatRoomModel chatRoomModel = new ChatRoomModel();
                        chatRoomModel.setUserIds((List<String>) model.get("userIds"));
                        usersArrayList.add(chatRoomModel);

                    }
                    adapter.notifyDataSetChanged();

                }

            }
        });





    }



}