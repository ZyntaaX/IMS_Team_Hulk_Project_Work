package com.example.team_hulk_project_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.team_hulk_project_application.Firestore.db
import com.example.team_hulk_project_application.Firestore.firestoreManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firestoreManager.TestFirestore("HEJHEJ");
        connectToMower()
    }

    private fun connectToMower(){
        val connectButton = findViewById<Button>(R.id.connectButton)
        val wifiIcon = findViewById<ImageView>(R.id.wifiEnabledIcon)

        var connectionStatus = 0

        connectButton.setOnClickListener{
            if(connectionStatus == 0){
                wifiIcon.visibility = View.VISIBLE
                connectButton.text = "Disconnect from Mower"
                connectionStatus = 1
                Toast.makeText(this, "You are connected to the Mower", Toast.LENGTH_SHORT).show()
            }
            else{
                wifiIcon.visibility = View.INVISIBLE
                connectButton.text = "Connect to Mower"
                connectionStatus = 0
                Toast.makeText(this, "You are disconnected from the Mower", Toast.LENGTH_SHORT).show()
            }
        }
    }
}