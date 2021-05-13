package com.example.weatherapp.roomDB

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {

    override suspend fun getLocationData(): List<LocationData> = appDatabase.locationDao().getLocationData()

    override suspend fun insertLocationData(locationData: LocationData) = appDatabase.locationDao().insertAll(locationData)

    override suspend fun updateLocationData(locationData: LocationData) = appDatabase.locationDao().updateLocationData(locationData)

}