package com.example.weatherapp.coreapi.model

import java.io.Serializable

data class FakeResponseClass(

    val status:String,
    val body: FakeStatusResponseClass

): Serializable

data class FakeStatusResponseClass(

    val success: String

): Serializable