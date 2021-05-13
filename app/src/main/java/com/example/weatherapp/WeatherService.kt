package com.example.weatherapp

import com.example.weatherapp.coreapi.model.FakeResponseClass
import com.example.weatherapp.coreapi.model.FakeStatusResponseClass
import com.example.weatherapp.coreapi.model.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


interface WeatherService {
    @GET
    suspend fun getWeatherReport(@Url url: String?): WeatherData


    // fake api call
    suspend fun getFakeResponse(): FakeResponseClass

}