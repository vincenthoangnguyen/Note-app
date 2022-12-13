package com.vincent.hoangnguyen.notepro;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {
    static void showToast(Context context , String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // muốn lưu dữ liệu trên firestore thì mỗi 1 user nên có 1 bộ sưu tập, và mỗi bộ có 1 id riêng biệt
    static CollectionReference getCollectionReferenceForeNotes(){
        // khởi tạo người dùng để lấy id
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        return FirebaseFirestore.getInstance().collection("NOTES")
                .document(currentUser.getUid()).collection("My_notes");

    }


    static String timeStampToString(Timestamp timestamp){
        return new SimpleDateFormat("dd-MM-yyyy   HH:mm:ss").format(timestamp.toDate());
    }
}
