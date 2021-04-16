package com.example.team_hulk_project_application

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.team_hulk_project_application.Firestore.firestoreManager
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.Socket

class MainActivity : AppCompatActivity() {
    private val ACCESS_FINE_LOCATION_CODE = 101

    val manager: WifiP2pManager by lazy(LazyThreadSafetyMode.NONE) {
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    }
    var channel: WifiP2pManager.Channel? = null
    var receiver: BroadcastReceiver? = null
    val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPermissions()
        firestoreManager.TestFirestore("HEJHEJ");
        channel = manager.initialize(this, mainLooper, null)
        channel?.also { channel -> receiver = WifiDirectBroadcastReceiver(manager, channel, this) }
        findPeers(manager, channel)
        connectToMower()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            ACCESS_FINE_LOCATION_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d("permissionLog", "Permission has been denied by user")
                    finishAffinity()
                } else {
                    Log.d("permissionLog", "Permission has been granted by user")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        receiver?.also { receiver ->
            registerReceiver(receiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        receiver?.also { receiver ->
            unregisterReceiver(receiver)
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d("permissionLog", "Permission for GPS denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION_CODE)
    }

    @SuppressLint("MissingPermission")
    private fun findPeers(manager: WifiP2pManager, channelListener: WifiP2pManager.Channel?){
        manager?.discoverPeers(channelListener, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Toast.makeText(this, "There are peers close by", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(reasonCode: Int) {
                Toast.makeText(this, "There are no peers close by", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun connectToMower() {
        val connectButton = findViewById<Button>(R.id.connectButton)
        val wifiIcon = findViewById<ImageView>(R.id.wifiEnabledIcon)

        var connectionStatus = 0

        connectButton.setOnClickListener {

            val host = "127.0.0.1"
            val port = 60003
            val message: String
            val client = Socket(host, port)
            val outputStream = client.getOutputStream()
            val inputStream = client.getInputStream()
            val buf = ByteArray(1024)

            if (connectionStatus == 0) {
                outputStream.write("Hello Mower from App".toByteArray())
                message = inputStream.read(buf).toString()
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                wifiIcon.visibility = View.VISIBLE
                connectButton.text = "Disconnect from Mower"
                connectionStatus = 1
                Toast.makeText(this, "You are connected to the Mower", Toast.LENGTH_SHORT).show()
            } else {
                client.close()
                wifiIcon.visibility = View.INVISIBLE
                connectButton.text = "Connect to Mower"
                connectionStatus = 0
                Toast.makeText(this, "You are disconnected from the Mower", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
