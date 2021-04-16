package com.example.team_hulk_project_application

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.team_hulk_project_application.Firestore.db
import com.example.team_hulk_project_application.Firestore.firestoreManager

class MainActivity : AppCompatActivity() {
    private val ACCESS_FINE_LOCATION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPermissions()
        firestoreManager.TestFirestore("HEJHEJ");
        connectToMower()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            ACCESS_FINE_LOCATION_CODE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Log.d("permissionLog", "Permission has been denied by user")
                    finishAffinity()
                }
                else{
                    Log.d("permissionLog", "Permission has been granted by user")
                }
            }
        }
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

    private fun setupPermissions(){
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if(permission != PackageManager.PERMISSION_GRANTED){
            Log.d("permissionLog", "Permission for GPS denied")
            makeRequest()
        }
    }

    private fun makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION_CODE)
    }
}