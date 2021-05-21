package com.example.team_hulk_project_application

import android.Manifest
import android.annotation.SuppressLint
import android.app.usage.UsageEvents
import android.content.pm.PackageManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.accessibility.AccessibilityEventCompat.getAction
import bitmapRepository
import com.example.team_hulk_project_application.MowerVisualizer.ImageLayerKeyword
import com.example.team_hulk_project_application.MowerVisualizer.bitmapGenerator
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.net.Socket
import java.security.Key

class MainActivity : AppCompatActivity() {
    private val ACCESS_FINE_LOCATION_CODE = 101

    private lateinit var database: DatabaseReference

    private lateinit var connectButton: Button
    private lateinit var disconnectButton: Button
    private lateinit var wifiIcon: ImageView
    private lateinit var collisionVisualizer: ImageView
    private lateinit var switchCollision: Button
    private lateinit var switchControlMode: Button

    private lateinit var arrowUp: ImageButton
    private lateinit var arrowDown: ImageButton
    private lateinit var arrowLeft: ImageButton
    private lateinit var arrowRight: ImageButton

    private var manualMode = false

    private val manager: WifiP2pManager by lazy(LazyThreadSafetyMode.NONE) {
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    }
    private var channel: WifiP2pManager.Channel? = null
    private var receiver: BroadcastReceiver? = null
    private val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    private var socketHandler = SocketHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Firebase.database.reference

        // Initializing all references to components in the layout-file
        connectButton = findViewById(R.id.connectButton)
        disconnectButton = findViewById(R.id.disconnectButton)
        wifiIcon = findViewById(R.id.wifiEnabledIcon)
        collisionVisualizer = findViewById(R.id.mower_collision)
        switchCollision = findViewById(R.id.switchcollisionindicator)
        switchControlMode = findViewById(R.id.switchControlMode)

        arrowUp = findViewById(R.id.arrow_up)
        arrowDown = findViewById(R.id.arrow_down)
        arrowLeft = findViewById(R.id.arrow_left)
        arrowRight = findViewById(R.id.arrow_right)

        setupPermissions()
        channel = manager.initialize(this, mainLooper, null)
        channel?.also { channel -> receiver = WifiDirectBroadcastReceiver(manager, channel, this) }
        findPeers(manager, channel, this)

        setAppButtonListeners()

        connectToMower()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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
        val permission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d("permissionLog", "Permission for GPS denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            ACCESS_FINE_LOCATION_CODE
        )
    }

    @SuppressLint("MissingPermission")
    private fun findPeers(
        manager: WifiP2pManager,
        channelListener: WifiP2pManager.Channel?,
        context: Context
    ) {
        manager.discoverPeers(channelListener, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Toast.makeText(context, "There are peers close by", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(reasonCode: Int) {
                Toast.makeText(context, "There are no peers close by", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun connectToMower() {
        // MOVE THIS INIT TO A SPLASHSCREEN FOR CLEANER CODE
        bitmapRepository.init(resources) { finished ->
            if (finished) {
                bitmapRepository.changeVisibilityByKeyword(ImageLayerKeyword.MowerCollision1, false)
                bitmapRepository.changeVisibilityByKeyword(ImageLayerKeyword.MowerCollision2, false)
                bitmapRepository.changeVisibilityByKeyword(ImageLayerKeyword.MowerCollision3, false)

                bitmapGenerator.createBitmap(bitmapRepository.getMutableList()!!) { bitmap, _, _ ->
                    collisionVisualizer.setImageBitmap(bitmap)
                }
            }
        }

        // NEEDS TO BE CLEANED UP, WAITING FOR ACTUAL MOWER DATA
        var counter = 3
        switchCollision.setOnClickListener {
            counter--
            if (counter < 0)
                counter = 3

            bitmapRepository.setLayersHidden()

            when (counter) {
                2 -> {
                    bitmapRepository.changeVisibilityByKeyword(
                        ImageLayerKeyword.MowerCollision1,
                        true
                    )
                }
                1 -> {
                    bitmapRepository.changeVisibilityByKeyword(
                        ImageLayerKeyword.MowerCollision2,
                        true
                    )
                }
                0 -> {
                    bitmapRepository.changeVisibilityByKeyword(
                        ImageLayerKeyword.MowerCollision3,
                        true
                    )
                }
            }

            bitmapGenerator.createBitmap(bitmapRepository.getMutableList()!!) { bitmap, _, _ ->
                collisionVisualizer.setImageBitmap(bitmap)
            }
        }

        var connectionStatus = 0

        if (connectionStatus == 0) {
            //setViewConnectedToMowerManualControl()
            //setViewConnectedToMowerAutoControl()
            setViewDisconnectedFromMower()
        } else {
            if (manualMode) {
                setViewConnectedToMowerManualControl()
            } else {
                setViewConnectedToMowerAutoControl()
            }
        }

        connectButton.setOnClickListener {
            if (connectionStatus == 0) {
                if (manualMode) {
                    setViewConnectedToMowerManualControl()
                } else {
                    setViewConnectedToMowerAutoControl()
                }
                connectionStatus = 1
            }
        }

        disconnectButton.setOnClickListener {
            setViewDisconnectedFromMower()
            connectionStatus = 0
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setAppButtonListeners() {
        switchControlMode.setOnClickListener {
            Log.d("clientTest", "HEJ")
            if (manualMode) {
                socketHandler.onKeyDown(0)
                setViewConnectedToMowerAutoControl()
            } else {
                socketHandler.onKeyDown(0)
                setViewConnectedToMowerManualControl()
            }
        }

        arrowRight.setOnTouchListener { v, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    socketHandler.onKeyDown(4)
                    Toast.makeText(this, "Turn right", Toast.LENGTH_SHORT).show()
                }
                MotionEvent.ACTION_UP -> {
                    socketHandler.onKeyDown(1)
                    Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        arrowLeft.setOnTouchListener { v, event ->
            val action = event.action
            when(action) {
                MotionEvent.ACTION_DOWN -> {
                    socketHandler.onKeyDown(5)
                    Toast.makeText(this, "Turn left", Toast.LENGTH_SHORT).show()
                }
                MotionEvent.ACTION_UP -> {
                    socketHandler.onKeyDown(1)
                    Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        arrowUp.setOnTouchListener { v, event->
            val action = event.action
            when(action) {
                MotionEvent.ACTION_DOWN -> {
                    socketHandler.onKeyDown(2)
                    Toast.makeText(this, "Forward", Toast.LENGTH_SHORT).show()
                }
                MotionEvent.ACTION_UP -> {
                    socketHandler.onKeyDown(1)
                    Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        arrowDown.setOnTouchListener { v, event->
            val action = event.action
            when(action) {
                MotionEvent.ACTION_DOWN -> {
                    socketHandler.onKeyDown(3)
                    Toast.makeText(this, "Reverse", Toast.LENGTH_SHORT).show()
                }
                MotionEvent.ACTION_UP -> {
                    socketHandler.onKeyDown(1)
                    Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    private fun setViewDisconnectedFromMower() {
        hideAllViews()

        connectButton.visibility = View.VISIBLE
    }

    private fun setViewConnectedToMowerAutoControl() {
        hideAllViews()

        manualMode = false
        switchControlMode.text = resources.getString(R.string.switchToManualMode)

        disconnectButton.visibility = View.VISIBLE
        wifiIcon.visibility = View.VISIBLE
        collisionVisualizer.visibility = View.VISIBLE
        switchControlMode.visibility = View.VISIBLE
        switchCollision.visibility = View.VISIBLE
    }

    private fun setViewConnectedToMowerManualControl() {
        hideAllViews()

        manualMode = true
        switchControlMode.text = resources.getString(R.string.switchToAutoMode)

        disconnectButton.visibility = View.VISIBLE
        wifiIcon.visibility = View.VISIBLE
        switchControlMode.visibility = View.VISIBLE

        arrowUp.visibility = View.VISIBLE
        arrowDown.visibility = View.VISIBLE
        arrowLeft.visibility = View.VISIBLE
        arrowRight.visibility = View.VISIBLE
    }

    private fun hideAllViews() {
        connectButton.visibility = View.INVISIBLE
        disconnectButton.visibility = View.INVISIBLE
        wifiIcon.visibility = View.INVISIBLE
        collisionVisualizer.visibility = View.INVISIBLE
        switchCollision.visibility = View.INVISIBLE
        switchControlMode.visibility = View.INVISIBLE

        arrowUp.visibility = View.INVISIBLE
        arrowDown.visibility = View.INVISIBLE
        arrowLeft.visibility = View.INVISIBLE
        arrowRight.visibility = View.INVISIBLE
    }
}
