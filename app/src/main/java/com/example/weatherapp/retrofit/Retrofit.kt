package com.example.weatherapp.retrofit

import com.example.weatherapp.WeatherService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/*class RetrofitInstance {
    companion object {
        val BASE_URL: String = "https://api.openweathermap.org/data/2.5/"

        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()

        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}

 */

object ApiManager {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private fun retrofitService(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
//            .addInterceptor(MockInterceptor())
            .addInterceptor(logging)
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())

            .client(httpClient.build())
            .baseUrl(BASE_URL)
            .build()
    }

    val weatherServiceApi: WeatherService by lazy {
        retrofitService().create(WeatherService::class.java)
    }
    val weatherReportApi: WeatherService by lazy {
        retrofitService().create(WeatherService::class.java)
    }
}