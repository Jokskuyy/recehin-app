// File: app/src/main/kotlin/com/example/recehin/data/network/api/ApiConfig.kt
package com.example.recehin.data.network.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    // Alamat IP 10.0.2.2 adalah alias untuk localhost dari emulator Android
    private const val BASE_URL = "http://10.0.2.2:3000/"

    fun getApiService(): ApiService {
        // Kita kembali ke versi sederhana ini, yang sudah cukup untuk debugging
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}