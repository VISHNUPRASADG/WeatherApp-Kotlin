package com.example.weatherapp

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.weatherapp.GPS.GpsTracker
import com.example.weatherapp.coreapi.model.WeatherData
import com.example.weatherapp.databinding.ActivityHomeBinding
import com.example.weatherapp.roomDB.DatabaseBuilder
import com.example.weatherapp.roomDB.DatabaseHelper
import com.example.weatherapp.roomDB.DatabaseHelperImpl
import com.example.weatherapp.roomDB.LocationData
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.runBlocking


class HomeActivity : AppCompatActivity() {

    private lateinit var activityHomeBinding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    var url: String? = null

    lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_home)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        activityHomeBinding.viewModel = homeViewModel
         dbHelper = DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))

        activityHomeBinding.loader.visibility = View.VISIBLE
        getLocation()
        observeStatus()
        startWorkManager()

    }

    private fun startWorkManager() {
        val constraints = Constraints.Builder().setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.UNMETERED).build()
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 30, TimeUnit.MINUTES)
                .build()
        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(periodicWorkRequest)
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id).observeForever {
            if (it != null) {
                Log.d("periodicWorkRequest", "Status changed to ${it.state.isFinished}")
            }
        }
    }

     fun getLocation() {
         val gpsTracker = GpsTracker(this)
        if(gpsTracker.canGetLocation()){
            Log.d("Location", gpsTracker.longitude.toString())
            Log.d("Location", gpsTracker.latitude.toString())

            val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
            val currentDateAndTime: String = simpleDateFormat.format(Date())
            Log.d("Date",currentDateAndTime)

            //Adding location in RoomDB if not null
            if(gpsTracker.latitude?.equals(null) == false) {
            runBlocking {

                val locationData = LocationData(null,gpsTracker.getLat(),gpsTracker.getLong(),currentDateAndTime)
                Log.d("LocationCheck",locationData.toString())
                dbHelper.insertLocationData(locationData)
                Log.d("LocationDBCheck",dbHelper.getLocationData().toString())
            }
                //calling method to get current address (city & country)
                geoCoder()
            } else {
                getLocation()
            }
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    fun geoCoder() {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address> = runBlocking {
                var position: Int = dbHelper.getLocationData().size - 1
                geocoder.getFromLocation(
                    dbHelper.getLocationData()[position].latitude!!,
                    dbHelper.getLocationData()[position].longitude!!,
                    1
                )
            }
            if (!addresses.isNullOrEmpty()) {
                Log.d("Location", addresses.get(0).countryName)
                Log.d("Location", addresses.get(0).locality.toLowerCase())
                Log.d("Location", addresses.get(0).countryCode.toLowerCase())
                var cityCode: String = addresses.get(0).locality.toLowerCase()
                var countryCode: String = addresses.get(0).countryCode.toLowerCase()
                url = cityCode +","+ countryCode
                Log.d("LocationUrl",url.toString())
                Toast.makeText(this, url?.toUpperCase(),Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this,"Can't Fetch your current location",Toast.LENGTH_SHORT).show()
        }
    }

    fun updateUI(weatherData: WeatherData){
        activityHomeBinding.tvLocation.text = weatherData.name.toUpperCase()+" ,"+ weatherData.sys.country
        activityHomeBinding.tvUpdatedAt.text =
                SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                    Date(weatherData.dt * 1000)
                )
        activityHomeBinding.tvTemp.text = weatherData.main.temp.toString()+ "\u2103"
        activityHomeBinding.humidity.text = weatherData.main.humidity.toString()
        activityHomeBinding.tvWind.text = weatherData.wind.speed.toString()+" Km/hr"
        activityHomeBinding.tvSunrise.text = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
            Date(weatherData.sys.sunrise * 1000)
        )
        activityHomeBinding.tvSunset.text = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
            Date(weatherData.sys.sunset * 1000)
        )
        activityHomeBinding.pressure.text = weatherData.main.pressure.toString()
        activityHomeBinding.tvVisiblity.text = weatherData.cod.toString()
        activityHomeBinding.tvStatus.text = weatherData.weather[0].description.toUpperCase()
        activityHomeBinding.tvTempMin.text = "Min Temp: "+weatherData.main.temp_min.toString()+"\u2103"
        activityHomeBinding.tvTempMax.text = "Max Temp: "+weatherData.main.temp_max.toString()+"\u2103"

    }

    private fun observeStatus() {
        homeViewModel.apply {
            url?.let {
                getWeatherData(it).observe(this@HomeActivity, {
                    activityHomeBinding.loader.visibility = View.GONE
                    updateUI(it)
                })
            }
            }
    }

}