package com.example.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.coreapi.model.Clouds
import com.example.weatherapp.coreapi.model.Coord
import com.example.weatherapp.coreapi.model.FakeResponseClass
import com.example.weatherapp.coreapi.model.FakeStatusResponseClass
import com.example.weatherapp.coreapi.model.Main
import com.example.weatherapp.coreapi.model.Sys
import com.example.weatherapp.coreapi.model.Weather
import com.example.weatherapp.coreapi.model.WeatherData
import com.example.weatherapp.coreapi.model.Wind
import com.google.common.truth.Truth.assertThat
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

//@RunWith(AndroidJUnit4::class)
//@RunWith(MockitoJUnitRunner::class)
@RunWith(JUnit4::class)
class HomeViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var homeViewModel: HomeViewModel

    @Mock
    private lateinit var homeRepository: HomeRepository

    @Mock
    private lateinit var fakeHomeTestRespository: FakeHomeTestRespository

    @Mock
    private lateinit var weatherService: WeatherService

    @Before
    fun setUp(){
        homeViewModel = HomeViewModel()
        homeRepository = HomeRepository()
        fakeHomeTestRespository = FakeHomeTestRespository()
    }


    // sample response
    val coord = Coord(79.83, 11.93)
    val weather: Array<Weather> =
        arrayOf(Weather(800, "Clear", "clear sky", "01d"))
    val main = Main(
        31.59, 36.42, 31.59, 31.59, 1011,
        61
    )
    val wind = Wind(3.8, 146)
    val clouds = Clouds(10)
    val sys = Sys(99, 99, "IN", 1620260361, 1620305713)
    val weatherData = WeatherData(
        coord, weather, "stations", main, 10000, wind, clouds,
        1620272904, sys, 19800, 1259425, "Puducherry", 200
    )

    fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
        val observer = OneTimeObserver(handler = onChangeHandler)
        observe(observer, observer)
    }

    @Test
    fun getWeatherData_returnSuccessData() {
        homeViewModel.getWeatherData(
            "weather?q=puducherry,in&units=metric&appid=8118ed6ee68db2debfaaa5a44c832918"
        )
        homeViewModel.testCaseData().observeOnce {
            assertEquals(weatherData,it)
        }
    }

    @Test
    fun getWeatherData_returnFailureData() {
        homeViewModel.getWeatherData(
            "weather?q=puducherry,in&units=metric&appid=8118ed6ee68db2debfaaa5a44c832918"
        )
        homeViewModel.testCaseData().observeOnce {
            assertEquals(it,null)
        }
    }

    //testcase for viewmodel success return
    @Test
    fun getWeatherData_returnSuccess() {
        // val returnWeatherData: WeatherData = fakeHomeTestRespository.getWeatherReport()
        val returnWeatherData: WeatherData? =
            homeViewModel.getWeatherData(
                "weather?q=puducherry,in&units=metric&appid=8118ed6ee68db2debfaaa5a44c832918"
            ).value
        assertEquals(returnWeatherData,weatherData)
    }

/*    //test api call
    @Test
    fun fakeApi_SuccessTest() {
        testCoroutineRule.runBlockingTest {
            val fakeSatusResponseClass = FakeStatusResponseClass("true")
            val fakeResponseClass = FakeResponseClass("200", fakeSatusResponseClass)

            Mockito.`when`(weatherService.getFakeResponse()).thenReturn(fakeResponseClass)
            homeViewModel.getFakeApiCall()
            Assert.assertEquals(fakeResponseClass, homeViewModel.fakeResponseData.value)
        }
    }

    @Test
    fun fakeApi_FailureTest() {
        testCoroutineRule.runBlockingTest {
            val fakeSatusResponseClass = FakeStatusResponseClass("false")
            val fakeResponseClass = FakeResponseClass("200", fakeSatusResponseClass)

            Mockito.`when`(weatherService.getFakeResponse()).thenReturn(fakeResponseClass)
            homeViewModel.getFakeApiCall()
            Assert.assertEquals(fakeResponseClass, homeViewModel.fakeResponseData.value)
        }
    }

    @Mock
    lateinit var observer: Observer<WeatherData>

    @Test
    fun getWeatherData_whenFetch_shouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            doReturn(weatherData)
                .`when`(apiHelper)
                .getWeatherReport("weather?q=puducherry,in&units=metric&appid=8118ed6ee68db2debfaaa5a44c832918")
            homeViewModel.getWeatherData("weather?q=puducherry,in&units=metric&appid=8118ed6ee68db2debfaaa5a44c832918")
                .observeForever(observer)
            verify(apiHelper).getWeatherReport("weather?q=puducherry,in&units=metric&appid=8118ed6ee68db2debfaaa5a44c832918")
            verify(observer).onChanged(weatherData)
            homeViewModel.getWeatherData("weather?q=puducherry,in&units=metric&appid=8118ed6ee68db2debfaaa5a44c832918")
                .removeObserver(observer)
        }
}

  */
}
