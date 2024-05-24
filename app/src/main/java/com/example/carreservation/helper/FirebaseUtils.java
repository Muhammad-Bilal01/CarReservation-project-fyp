package com.example.carreservation.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FirebaseUtils {

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static DocumentReference getChatRoomRefernce(String chatRoomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomId);
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("Users");
    }


    public static CollectionReference getChatRoomMessageReference(String chatroomId){
        return getChatRoomRefernce(chatroomId).collection("chats");
    }

    public static CollectionReference allChatRoomsCollectionsReference(){
        return  FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtherUserFromChatRooms(List<String> userIds){
        if(userIds.get(0).equals(currentUserId())) {
            return allUserCollectionReference().document(userIds.get(1));
        }else{
            return allUserCollectionReference().document(userIds.get(0));
        }
    }

    public static String getChatRoomId(String userId1, String userid2){
       if(userId1.hashCode()<userid2.hashCode()){
           return userId1 +"_"+userid2;
       }else{
           return userId1 +"_"+userid2;
       }
    }


}
