package com.example.team_hulk_project_application

import android.util.Log
import java.io.DataOutputStream
import java.net.Socket
import java.net.SocketException

class SocketHandler {

    //Socket attributes
    private val host = "10.0.0.2"
    private val port = 60003
    private var socket = Socket(host, port)

    //Mower drive commands
    private val COMMAND_AUTO = "auto"
    private val COMMAND_MANUAL = "manual"
    private val COMMAND_STOP = "stop"
    private val COMMAND_MOVE_FORWARD = "move_forward"
    private val COMMAND_MOVE_BACKWARD = "move_backward"
    private val COMMAND_MOVE_RIGHT = "move_right"
    private val COMMAND_MOVE_LEFT = "move_left"

    private val BUTTON_COMMAND_AUTO = 0
    private val BUTTON_COMMAND_MANUAL = 1
    private val BUTTON_COMMAND_STOP = 2
    private val BUTTON_COMMAND_MOVE_FORWARD = 3
    private val BUTTON_COMMAND_MOVE_BACKWARD = 4
    private val BUTTON_COMMAND_MOVE_RIGHT = 5
    private val BUTTON_COMMAND_MOVE_LEFT = 6

    public fun onKeyDown(keyCode: Int){
        Log.d("Key code of pressed key", keyCode.toString())

        when(keyCode){
            BUTTON_COMMAND_AUTO -> {
                Thread(Runnable{
                    try{
                        val client = socket
                        val outputStream = DataOutputStream(client.getOutputStream())
                        outputStream.write(COMMAND_AUTO.toByteArray())
                        client.close()
                    }
                    catch(exception: SocketException){
                        Log.d("Exception", exception.toString())
                    }
                }).start()
            }

            BUTTON_COMMAND_MANUAL -> {
                Thread(Runnable{
                    try{
                        val client = socket
                        val outputStream = DataOutputStream(client.getOutputStream())
                        outputStream.write(COMMAND_MANUAL.toByteArray())
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
                        val client = socket
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
                        val client = socket
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
                        val client = socket
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
                        val client = socket
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
                        val client = socket
                        val outputStream = DataOutputStream(client.getOutputStream())
                        outputStream.write(COMMAND_MOVE_LEFT.toByteArray())
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