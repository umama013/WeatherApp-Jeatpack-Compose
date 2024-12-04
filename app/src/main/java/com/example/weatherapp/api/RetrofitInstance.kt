package com.example.weatherapp.api

import com.example.weatherapp.WeatherApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.Calendar.getInstance

object RetrofitInstance {

  private const val baseUrl = "https://api.weatherapi.com"

  private fun getRetrofitInstance(): Retrofit {
    return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  val weatherApi : WeatherApi = getRetrofitInstance().create(WeatherApi::class.java)
}