package com.example.team_hulk_project_application

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import android.widget.ListView

class WifiDirectBroadcastReceiver(
        private val manager: WifiP2pManager,
        private val channel: WifiP2pManager.Channel,
        private val activity: MainActivity
): BroadcastReceiver(){
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, Intent: Intent) {
        val action: String = Intent.action.toString()
        var wifiDeviceLst = ArrayList<String>()
        when(action){
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = Intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                when (state){
                    WifiP2pManager.WIFI_P2P_STATE_ENABLED ->{
                        Log.d("Test", "You Wifi Online")
                    }
                    else ->{
                        Log.d("Test", "You Wifi Offline")
                    }
                }
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION ->{
                manager?.requestPeers(channel){ peers: WifiP2pDeviceList ->
                        
                        Log.d("Test", "Device found")
                        for (device in peers.deviceList) {
                            wifiDeviceLst.add(device.toString())
                        }
                        val builder = AlertDialog.Builder(context)
                        builder.setIcon(R.drawable.ic_launcher_foreground)
                        builder.setTitle("Select a peer for connection")
                        builder.setItems(wifiDeviceLst.toArray(arrayOf(String())), DialogInterface.OnClickListener { dialog, which ->
                            when(which){
                                0 -> {Log.d("Test", "working")}
                            }
                        })
                        val dialog = builder.create()
                        dialog.show()
                        }


                }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION ->{

            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {


            }
        }
    }
}

