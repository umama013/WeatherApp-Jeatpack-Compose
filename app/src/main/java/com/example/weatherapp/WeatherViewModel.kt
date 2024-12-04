package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.RetrofitInstance
import kotlinx.coroutines.launch
import java.lang.Exception

class WeatherViewModel : ViewModel() {

  private val weatherApi = RetrofitInstance.weatherApi
  private val _weatherResponse = MutableLiveData<NetworkResponse<WeatherModel>>()
  val weatherResponse: LiveData<NetworkResponse<WeatherModel>> = _weatherResponse

  fun getData(city: String) {

    _weatherResponse.value = NetworkResponse.Loading
    viewModelScope.launch {
      try {
        val response = weatherApi.getWeather(Constants.api, city)

        if (response.isSuccessful) {
          response.body()?.let {
            _weatherResponse.value = NetworkResponse.Success(it)
          }
        } else {
          _weatherResponse.value = NetworkResponse.Error("Fail to load")
        }
      } catch (e: Exception) {
        _weatherResponse.value = NetworkResponse.Error("Fail to load")
      }
    }

  }
}