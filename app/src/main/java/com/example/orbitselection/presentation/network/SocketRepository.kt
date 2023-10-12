package com.example.orbitselection.presentation.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

object SocketRepository {
    private const val length = 17
    private const val port = 8999
    private var socketUDP = DatagramSocket()

    suspend fun sendUDP(x: String, y: String, action: String, ip: String) {
        val buffer = x.toByteArray() + y.toByteArray() + action.toByteArray()
        return withContext(Dispatchers.IO) {
            val message = DatagramPacket(buffer, length, InetAddress.getByName(ip), port)
            socketUDP.send(message)
            println(x)
            println(y)
        }
    }

    fun close() {
        socketUDP.close()
    }
}