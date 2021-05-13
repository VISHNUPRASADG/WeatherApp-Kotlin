package com.example.weatherapp


import com.example.weatherapp.coreapi.model.WeatherData
import com.example.weatherapp.retrofit.RetrofitClient


class HomeRepository {

    val call = RetrofitClient.weatherService

    suspend fun getServicesApiCall(cityUrl: String?) = call.getWeatherReport(cityUrl)

    suspend fun getFakeApiCall() = call.getFakeResponse()

}
