package com.example.carreservation.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carreservation.R;
import com.example.carreservation.admin.AdminChatActivity;
import com.example.carreservation.helper.FirebaseUtils;
import com.example.carreservation.models.ChatRoomModel;
import com.example.carreservation.models.Reviews;
import com.example.carreservation.models.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUserRecyclerAdapter extends RecyclerView.Adapter<ChatUserRecyclerAdapter.ChatRoomModelViewholder> {


    Context context;
    private ArrayList<ChatRoomModel> usersArrayList;

    public ChatUserRecyclerAdapter(Context context, ArrayList<ChatRoomModel> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public ChatRoomModelViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_user_recyvler_view, parent, false);

        return new ChatRoomModelViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomModelViewholder holder, int position) {
        ChatRoomModel model = usersArrayList.get(position);

        FirebaseUtils.getOtherUserFromChatRooms(model.getUserIds()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                Map<String, Object> result = task.getResult().getData();

//                System.out.println("Task --> "+  task.getResult().toString());
                UserModel userModel = new UserModel();
//                System.out.println("Task --> "+  result.get("fullName"));


                userModel.setUserId(Objects.requireNonNull(result.get("uuid")).toString());
                userModel.setUserName(Objects.requireNonNull(result.get("fullName")).toString());
                userModel.setProfileUrl(Objects.requireNonNull(result.get("profileImage")).toString());

//                holder.usernameText.setText(userModel.getUserName());
                holder.usernameText.setText(userModel.getUserName());


//                onClick Listner
                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, AdminChatActivity.class);
                    intent.putExtra("uuid", userModel.getUserId());
                    intent.putExtra("name", userModel.getUserName());
                    intent.putExtra("profile", userModel.getProfileUrl());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
            }
        });


    }

    @Override
    public int getItemCount() {
//        return 0;
        return usersArrayList.size();
    }

    class ChatRoomModelViewholder extends RecyclerView.ViewHolder {

        TextView usernameText;
        CircleImageView profilePic;

        public ChatRoomModelViewholder(@NonNull View itemView) {
            super(itemView);

            usernameText = itemView.findViewById(R.id.user_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image);


        }
    }

}
