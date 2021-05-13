package com.example.weatherapp.roomDB

interface DatabaseHelper {

    suspend fun getLocationData(): List<LocationData>
    suspend fun insertLocationData(locationData: LocationData)
    suspend fun updateLocationData(locationData: LocationData)

}

