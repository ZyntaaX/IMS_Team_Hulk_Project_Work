package com.example.team_hulk_project_application

import android.util.Log
import java.io.DataOutputStream
import java.net.Socket
import java.net.SocketException

class SocketHandler {

    //Socket attributes
    private val host = "192.168.8.88" //192.168.115.88 RPI 10.0.2.2 Local
    private val port = 60003

    //Mower drive commands
    private val COMMAND_START = "START"
    private val COMMAND_STOP = "STOP"
    private val COMMAND_MOVE_FORWARD = "FORWARD"
    private val COMMAND_MOVE_BACKWARD = "BACKWARD"
    private val COMMAND_MOVE_RIGHT = "RIGHT"
    private val COMMAND_MOVE_LEFT = "LEFT"

    private val BUTTON_COMMAND_START = 0
    private val BUTTON_COMMAND_STOP = 1
    private val BUTTON_COMMAND_MOVE_FORWARD = 2
    private val BUTTON_COMMAND_MOVE_BACKWARD = 3
    private val BUTTON_COMMAND_MOVE_RIGHT = 4
    private val BUTTON_COMMAND_MOVE_LEFT = 5

    public fun onKeyDown(keyCode: Int){
        Log.d("Key code of pressed key", keyCode.toString())

        when(keyCode){
            BUTTON_COMMAND_START -> {
                Thread(Runnable{
                    try{
                        val client = Socket(host, port)
                        val outputStream = DataOutputStream(client.getOutputStream())
                        outputStream.write(COMMAND_START.toByteArray())
                        client.close()
                    }
                    catch(exception: SocketException){
                        Log.d("Exception", exception.toString())
                    }
                }).start()
            }

            BUTTON_COMMAND_STOP -> {
                Thread(Runnable{
                    try{
                        val client = Socket(host, port)
                        val outputStream = DataOutputStream(client.getOutputStream())
                        outputStream.write(COMMAND_STOP.toByteArray())
                        client.close()
                    }
                    catch(exception: SocketException){
                        Log.d("Exception", exception.toString())
                    }
                }).start()
            }

            BUTTON_COMMAND_MOVE_FORWARD -> {
                Thread(Runnable{
                    try{
                        val client = Socket(host, port)
                        val outputStream = DataOutputStream(client.getOutputStream())
                        outputStream.write(COMMAND_MOVE_FORWARD.toByteArray())
                        client.close()
                    }
                    catch(exception: SocketException){
                        Log.d("Exception", exception.toString())
                    }
                }).start()
            }

            BUTTON_COMMAND_MOVE_BACKWARD -> {
                Thread(Runnable{
                    try{
                        val client = Socket(host, port)
                        val outputStream = DataOutputStream(client.getOutputStream())
                        outputStream.write(COMMAND_MOVE_BACKWARD.toByteArray())
                        client.close()
                    }
                    catch(exception: SocketException){
                        Log.d("Exception", exception.toString())
                    }
                }).start()
            }

            BUTTON_COMMAND_MOVE_RIGHT -> {
                Thread(Runnable{
                    try{
                        val client = Socket(host, port)
                        val outputStream = DataOutputStream(client.getOutputStream())
                        outputStream.write(COMMAND_MOVE_RIGHT.toByteArray())
                        client.close()
                    }
                    catch(exception: SocketException){
                        Log.d("Exception", exception.toString())
                    }
                }).start()
            }

            BUTTON_COMMAND_MOVE_LEFT -> {
                Thread(Runnable{
                    try{
                        val client = Socket(host, port)
                        val outputStream = DataOutputStream(client.getOutputStream())
                        outputStream.write(COMMAND_MOVE_LEFT.toByteArray())
                        Log.d("LeftTest", "Left click works")
                        client.close()
                    }
                    catch(exception: SocketException){
                        Log.d("Exception", exception.toString())
                    }
                }).start()
            }

            else -> {
                Log.d("No action", "An unused key was pressed")
            }
        }
    }
}