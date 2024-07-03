package com.example.carreservation.admin;

import androidx.annotation.NonNull;
<<<<<<< Updated upstream
=======
import androidx.annotation.Nullable;
>>>>>>> Stashed changes
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carreservation.R;
import com.example.carreservation.adapters.ChatMessageRecyclerAdapter;
import com.example.carreservation.adapters.ChatUserRecyclerAdapter;
import com.example.carreservation.helper.FirebaseUtils;
import com.example.carreservation.models.ChatMessageModel;
import com.example.carreservation.models.ChatRoomModel;
import com.example.carreservation.models.Reviews;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
<<<<<<< Updated upstream
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
=======
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

<<<<<<< Updated upstream
=======
import android.os.Bundle;
import android.os.Handler;

>>>>>>> Stashed changes
public class SupportChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText message_editText;
    private ImageView sendBtn, backBtn;

<<<<<<< Updated upstream
    FirebaseFirestore firebaseFirestore;
=======
    private Handler handler;
    private Runnable runnable;

    FirebaseFirestore firebaseFirestore;
    private DatabaseReference databaseReference;


>>>>>>> Stashed changes

    String chatRoomId;
    ChatRoomModel chatRoomModel;

    ChatMessageRecyclerAdapter adapter;
    private ArrayList<ChatMessageModel> chatsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_chat);


        firebaseFirestore = FirebaseFirestore.getInstance();

        String currentUserId = FirebaseUtils.currentUserId();
//        get admin and currnet userId
<<<<<<< Updated upstream
//        TODO: ADMIN ID
=======
//       TODO: ADMIN ID
>>>>>>> Stashed changes
        chatRoomId = FirebaseUtils.getChatRoomId(currentUserId, "EuOP9PZ32jUlPgQ3aRhFKtu0ce32");
        chatsArrayList = new ArrayList<>();

        backBtn = findViewById(R.id.back_btn);
        sendBtn = findViewById(R.id.send_btn);
        message_editText = findViewById(R.id.message_textFeild);
        recyclerView = findViewById(R.id.recyclerview);

        getChatRoomModel();

<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
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
//        }, 1000);
>>>>>>> Stashed changes

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
    private void setupChatRecyclerView() {

        adapter = new ChatMessageRecyclerAdapter(getApplicationContext(), chatsArrayList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

<<<<<<< Updated upstream
        firebaseFirestore.collection("chatrooms").document(chatRoomId).collection("chats").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
=======


//        readMessages();




        // Initialize Realtime Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chatrooms");

         firebaseFirestore
        .collection("chatrooms")
        .document(chatRoomId)
        .collection("chats")
        .orderBy("timestamp", Query.Direction.DESCENDING)
        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
>>>>>>> Stashed changes
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

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
<<<<<<< Updated upstream

                        chatsArrayList.add(messageModel);


                    }
                    adapter.notifyDataSetChanged();
=======
                        chatsArrayList.add(messageModel);
                        adapter.notifyDataSetChanged();
                    }
>>>>>>> Stashed changes
                }

            }
        });


<<<<<<< Updated upstream
//        chatsArrayList.add(new ChatMessageModel("Hello", FirebaseUtils.currentUserId(), Timestamp.now()));
//        adapter.notifyDataSetChanged();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
=======

//        chatsArrayList.add(new ChatMessageModel("Hello", FirebaseUtils.currentUserId(), Timestamp.now()));
//        adapter.notifyDataSetChanged();

//        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                recyclerView.smoothScrollToPosition(0);
//            }
//        });
>>>>>>> Stashed changes

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
    private void readMessages(){
        // For real time Data
        firebaseFirestore
                .collection("chatrooms")
                .document(chatRoomId)
                .collection("chats")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        System.out.println("Func call");
                        if (error != null) {
                            System.err.println("Listen failed: " + error);
                            return;
                        }

                        if (value != null ) {
                            for (DocumentChange dc : value.getDocumentChanges()) {

                                if (dc.getType() == DocumentChange.Type.ADDED) {

                                    System.out.println("NEW DATA RECIEVED");
                                    Map<String, Object> model = dc.getDocument().getData();
                                    ChatMessageModel messageModel = new ChatMessageModel();
                                    messageModel.setMessage(model.get("message").toString());
                                    messageModel.setSenderId(model.get("senderId").toString());
                                    Timestamp timestamp = convertStringToTimestamp(model.get("timestamp").toString());
                                    messageModel.setTimestamp(timestamp);

                                    chatsArrayList.add(messageModel);

                                }
                            }
                            adapter.notifyDataSetChanged();

                        }
                    }

                });
    }

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
                    chatsArrayList.add(0, new ChatMessageModel(message, FirebaseUtils.currentUserId(), Timestamp.now()));
                    adapter.notifyDataSetChanged();
                }
            }
        });

//        chatsArrayList.clear();
//        setupChatRecyclerView();
    }

<<<<<<< Updated upstream
    //    get or create ChatRoomModel
=======
   //    get or create ChatRoomModel
>>>>>>> Stashed changes
    void getChatRoomModel() {
        FirebaseUtils.getChatRoomRefernce(chatRoomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatRoomModel = task.getResult().toObject(ChatRoomModel.class);
                if (chatRoomModel == null) {
                    // First time chat
                    chatRoomModel = new ChatRoomModel(
                            chatRoomId,
                            Arrays.asList(FirebaseUtils.currentUserId(), "EuOP9PZ32jUlPgQ3aRhFKtu0ce32"),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtils.getChatRoomRefernce(chatRoomId).set(chatRoomModel);
                }
            }
        });
    }

}