package com.example.weatherapp.roomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface LocationDao {

    @Query("SELECT * FROM locationdata")
     suspend fun getLocationData(): List<LocationData>

    @Insert
     suspend fun insertAll(locationEntity: LocationData)

    @Delete
     suspend fun delete(locationEntity: List<LocationData>)

    @Update
    suspend fun updateLocationData(locationEntity: LocationData)

}