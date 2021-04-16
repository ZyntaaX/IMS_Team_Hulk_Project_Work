package com.example.team_hulk_project_application

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log

class WifiDirectBroadcastReceiver(
        private val manager: WifiP2pManager,
        private val channel: WifiP2pManager.Channel,
        private val activity: MainActivity
): BroadcastReceiver(){
    override fun onReceive(p0: Context, Intent: Intent) {
        val action: String = Intent.action.toString()
        var deviceList: WifiP2pDeviceList
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
                    if (peers.deviceList.isEmpty()){
                        Log.d("Test", "You no device found")
                    }
                    else{
                        Log.d("Test", "Device found")
                        deviceList = peers.deviceList.addAll()
                    }
                }
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION ->{
                "Do stuff"
            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                "Do stuff"
            }
        }
    }
}