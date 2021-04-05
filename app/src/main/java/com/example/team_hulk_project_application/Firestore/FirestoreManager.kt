package com.example.team_hulk_project_application.Firestore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

var db = FirebaseFirestore.getInstance();

val firestoreManager = FirestoreManager()
    .apply{ }


class FirestoreManager {
    public fun TestFirestore(text: String) {
        val data = hashMapOf(
            "Test" to "Testing",
            "Test2" to "Testing2"
        );

        db.collection("Test")
            .add(data)
            .addOnCompleteListener() { Task ->
                if (Task.isSuccessful) {
                    Log.d("Firestore", "success");
                } else {
                    Log.d("Firestore", "not success");
                }
            }
    }
}

