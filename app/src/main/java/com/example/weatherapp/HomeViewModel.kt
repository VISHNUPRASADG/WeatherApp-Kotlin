package com.example.weatherapp


import androidx.lifecycle.*
import com.example.weatherapp.coreapi.model.FakeResponseClass
import com.example.weatherapp.coreapi.model.WeatherData
import com.example.weatherapp.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    var homeRepository: HomeRepository = HomeRepository()
    var weatherData: MutableLiveData<WeatherData> = MutableLiveData()

    fun getWeatherData(url: String) = liveData(Dispatchers.IO) {
        val cityUrl = "weather?q=" + url + "&units=metric&appid=8118ed6ee68db2debfaaa5a44c832918"
        weatherData = MutableLiveData(homeRepository.getServicesApiCall(cityUrl))
        emit(homeRepository.getServicesApiCall(cityUrl))
    }

    //for testcase observe
    fun testCaseData() = weatherData

    // Fake api call for testing

    val fakeResponseData =  MutableLiveData<FakeResponseClass>()

    fun getFakeApiCall() {
        viewModelScope.launch {
            fakeResponseData.value = homeRepository.getFakeApiCall()
        }
    }

}


