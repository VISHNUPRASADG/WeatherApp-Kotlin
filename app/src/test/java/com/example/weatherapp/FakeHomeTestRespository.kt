package com.example.weatherapp

import com.example.weatherapp.coreapi.model.Clouds
import com.example.weatherapp.coreapi.model.Coord
import com.example.weatherapp.coreapi.model.Main
import com.example.weatherapp.coreapi.model.Sys
import com.example.weatherapp.coreapi.model.Weather
import com.example.weatherapp.coreapi.model.WeatherData
import com.example.weatherapp.coreapi.model.Wind

class FakeHomeTestRespository {

    val coord = Coord(79.83,11.93)

    val weather:Array<Weather> =
        arrayOf(Weather(800,"Clear","clear sky","01d"))

    val main = Main(31.59,36.42,31.59,31.59,1011,
        61)

    val wind = Wind(3.8,146)

    val clouds = Clouds(10)

    val sys = Sys(99,99,"IN",1620260361,1620305713)

    val weatherData = WeatherData(coord,weather,"stations",main,10000,wind,clouds,
        1620272904,sys,19800,1259425,"Puducherry",200)


   fun getWeatherReport(): WeatherData {
        return weatherData
    }
}