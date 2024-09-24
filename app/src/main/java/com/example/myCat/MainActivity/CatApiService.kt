package com.example.myCat.MainActivity

import retrofit2.Call
import retrofit2.http.GET

interface CatApiService {
    @GET("breeds")
    fun getBreeds(): Call<List<CatBreed>>
}