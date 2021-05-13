package com.example.weatherapp.coreapi.model


data class WeatherData (
    val coord: Coord,
    val weather: Array<Weather>,
    val base: String,
    val main: Main,
    val visiblity: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Long,
    val id: Long,
    val name: String,
    val cod: Int
        )

data class Coord (
    var lon: Double,
    var lat: Double
        )

data class Weather (
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
        )

data class Main (
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
        )

data class Wind (
    val speed: Double,
    val deg: Int
        )

data class Clouds (
    val all: Int
        )

data class Sys (
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
     val sunset: Long
        )