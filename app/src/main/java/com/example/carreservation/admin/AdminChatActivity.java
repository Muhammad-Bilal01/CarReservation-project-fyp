package com.example.carreservation.admin;

import androidx.annotation.NonNull;
<<<<<<< Updated upstream
=======
import androidx.annotation.Nullable;
>>>>>>> Stashed changes
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.carreservation.R;
import com.example.carreservation.adapters.ChatMessageRecyclerAdapter;
import com.example.carreservation.helper.FirebaseUtils;
import com.example.carreservation.models.ChatMessageModel;
import com.example.carreservation.models.ChatRoomModel;
import com.example.carreservation.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
<<<<<<< Updated upstream
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
=======
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
>>>>>>> Stashed changes
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
<<<<<<< Updated upstream
=======
import android.os.Bundle;
import android.os.Handler;
>>>>>>> Stashed changes

public class AdminChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText message_editText;
    private ImageView sendBtn, backBtn;

    FirebaseFirestore firebaseFirestore;
<<<<<<< Updated upstream
=======
    private DatabaseReference databaseReference;
>>>>>>> Stashed changes

    String chatRoomId;
    ChatRoomModel chatRoomModel;

    ChatMessageRecyclerAdapter adapter;

    private ArrayList<ChatMessageModel> chatsArrayList;

    UserModel otherUser;

<<<<<<< Updated upstream
=======
    private Handler handler;
    private Runnable runnable;

>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chat);

         otherUser = new UserModel();

        Intent intent = getIntent();

        String uuid = intent.getStringExtra("uuid");
        String name = intent.getStringExtra("name");
        String profile = intent.getStringExtra("profile");

        System.out.println("UUID --> "+ uuid);

        otherUser.setUserId(uuid);
        otherUser.setUserName(name);
        otherUser.setProfileUrl(profile);

<<<<<<< Updated upstream
=======
//        Realtime

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chatrooms");




// -----


>>>>>>> Stashed changes
        firebaseFirestore = FirebaseFirestore.getInstance();

        String currentUserId = FirebaseUtils.currentUserId();

        chatRoomId = FirebaseUtils.getChatRoomId(otherUser.getUserId(),currentUserId);
        chatsArrayList = new ArrayList<>();

        backBtn = findViewById(R.id.back_btn);
        sendBtn = findViewById(R.id.send_btn);
        message_editText = findViewById(R.id.message_textFeild);
        recyclerView = findViewById(R.id.recyclerview);

        getChatRoomModel();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(SupportChatActivity.this,"Click",Toast.LENGTH_SHORT).show();
                String message = message_editText.getText().toString().trim();
                if (message.isEmpty())
                    return;
                sendMessageToUser(message);
<<<<<<< Updated upstream
=======
//                sendMessage(message);
>>>>>>> Stashed changes
            }
        });

        setupChatRecyclerView();
<<<<<<< Updated upstream
=======
        // Initialize the Handler and Runnable
//        handler = new Handler();
//
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                System.out.println("myHandler: here!"); // Do your work here
//                setupChatRecyclerView();
//                handler.postDelayed(this, 1000);
//            }
//        }, 10000);
>>>>>>> Stashed changes

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< Updated upstream
                onBackPressed();
=======
//                onBackPressed();
                Intent intent = new Intent(AdminChatActivity.this,AdminDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                AdminChatActivity.this.finish();
>>>>>>> Stashed changes
            }
        });

    }

    private void setupChatRecyclerView() {

        adapter = new ChatMessageRecyclerAdapter(getApplicationContext(), chatsArrayList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

<<<<<<< Updated upstream
        firebaseFirestore.collection("chatrooms").document(chatRoomId).collection("chats").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

=======

//        For Real time Data
/*
        firebaseFirestore.collection("chatrooms")
                .document(chatRoomId)
                .collection("chats")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed: " + error);
                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                switch (dc.getType()) {
                                    case ADDED:
                                        Map<String, Object> model = dc.getDocument().getData();
                                        ChatMessageModel messageModel = new ChatMessageModel();
                                        messageModel.setMessage(model.get("message").toString());
                                        messageModel.setSenderId(model.get("senderId").toString());
                                        Timestamp timestamp = convertStringToTimestamp(model.get("timestamp").toString());
                                        messageModel.setTimestamp(timestamp);

                                        chatsArrayList.add(messageModel);
                                        break;

                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        */


        firebaseFirestore.collection("chatrooms")
                .document(chatRoomId)
                .collection("chats")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


>>>>>>> Stashed changes
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {

//                        System.out.println("Docs --> " + d);
                        Map<String, Object> model = d.getData();
//                        System.out.println("reviews --> " + model.get("reviews"));

                        ChatMessageModel messageModel = new ChatMessageModel();
                        messageModel.setMessage(model.get("message").toString());
                        messageModel.setSenderId(model.get("senderId").toString());
                        Timestamp timestamp = convertStringToTimestamp(model.get("timestamp").toString());
                        messageModel.setTimestamp(timestamp);

                        chatsArrayList.add(messageModel);


                    }
                    adapter.notifyDataSetChanged();
                }
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
            }
        });


//        chatsArrayList.add(new ChatMessageModel("Hello", FirebaseUtils.currentUserId(), Timestamp.now()));
//        adapter.notifyDataSetChanged();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });

    }

    private Timestamp convertStringToTimestamp(String dateString) {
        // Define your date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // Parse the date string into a Date object
            Date date = sdf.parse(dateString);
            // Convert Date object to Firestore Timestamp
            return new Timestamp(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

<<<<<<< Updated upstream
=======
//    private void sendMessage(String messageText) {
//        ChatMessageModel chatMessageModel = new ChatMessageModel(messageText, FirebaseUtils.currentUserId(), Timestamp.now());
//        databaseReference.push().setValue(chatMessageModel);
//    }

>>>>>>> Stashed changes
    private void sendMessageToUser(String message) {


        chatRoomModel.setLastMessageTimestamp(Timestamp.now());
        chatRoomModel.setLastMessageSenderId(FirebaseUtils.currentUserId());
<<<<<<< Updated upstream
        chatRoomModel.setLastmessage(message);
=======
        chatRoomModel.setLastMessage(message);
>>>>>>> Stashed changes
        FirebaseUtils.getChatRoomRefernce(chatRoomId).set(chatRoomModel);


        ChatMessageModel chatMessageModel = new ChatMessageModel(message, FirebaseUtils.currentUserId(), Timestamp.now());
        FirebaseUtils.getChatRoomMessageReference(chatRoomId).add(chatMessageModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {

                    message_editText.setText("");
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
                    chatsArrayList.add(0, new ChatMessageModel(message, FirebaseUtils.currentUserId(), Timestamp.now()));
                    adapter.notifyDataSetChanged();
                }
            }
        });

//        chatsArrayList.clear();
//        setupChatRecyclerView();
    }

    //    get or create ChatRoomModel
    void getChatRoomModel() {
        FirebaseUtils.getChatRoomRefernce(chatRoomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatRoomModel = task.getResult().toObject(ChatRoomModel.class);
                if (chatRoomModel == null) {
                    // First time chat
                    chatRoomModel = new ChatRoomModel(
                            chatRoomId,
                            Arrays.asList(FirebaseUtils.currentUserId(), otherUser.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtils.getChatRoomRefernce(chatRoomId).set(chatRoomModel);
                }
            }
        });
    }


}