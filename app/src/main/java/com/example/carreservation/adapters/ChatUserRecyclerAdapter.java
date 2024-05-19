package com.example.carreservation.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carreservation.R;
import com.example.carreservation.models.Reviews;
import com.example.carreservation.models.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUserRecyclerAdapter extends RecyclerView.Adapter<ChatUserRecyclerAdapter.UserModelViewholder> {


    Context context;
    private ArrayList<UserModel> usersArrayList;

    public ChatUserRecyclerAdapter(Context context, ArrayList<UserModel> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public UserModelViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_user_recyvler_view,parent,false);

        return new UserModelViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserModelViewholder holder, int position) {
        UserModel user = usersArrayList.get(position);

        holder.usernameText.setText(user.getUserName());
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    class UserModelViewholder extends RecyclerView.ViewHolder {

        TextView usernameText;
        CircleImageView profilePic;

        public UserModelViewholder(@NonNull View itemView) {
            super(itemView);

            usernameText = itemView.findViewById(R.id.user_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image);


        }
    }

}
