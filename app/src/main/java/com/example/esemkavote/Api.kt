package com.example.esemkavote

import java.io.*
import java.net.HttpURLConnection
import java.net.URL

object Api {

    fun get(url: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return connection.inputStream.bufferedReader().readText()
    }

    fun post(url: String, data: Map<String, String>): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json")

        val body = data.map { "${it.key}=${it.value}" }.joinToString("&")

        connection.outputStream.use {
            it.write(body.toByteArray())
        }

        return connection.inputStream.bufferedReader().readText()
    }
}