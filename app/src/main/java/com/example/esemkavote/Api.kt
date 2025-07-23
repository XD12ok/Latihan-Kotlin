package com.example.esemkavote

import org.json.JSONObject
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

    fun test(url: String, json: JSONObject): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        return try {
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")

            // Kirim JSON ke server
            val writer = OutputStreamWriter(connection.outputStream)
            writer.write(json.toString())
            writer.flush()
            writer.close()

            // Baca respon dari server
            val inputStream = if (connection.responseCode in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }

            inputStream.bufferedReader().use { it.readText() }

        } catch (e: Exception) {
            "err: ${e.message}"
        } finally {
            connection.disconnect()
        }
    }


    fun testes(url: String, email: String, pass: String): String {
        val json = JSONObject().apply {
            put("email", email)
            put("password", pass)
        }
        return test(url, json)
    }

}