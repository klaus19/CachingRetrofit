package com.example.cachingretrofit.api

import com.example.cachingretrofit.JokeResponse
import retrofit2.http.GET
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Path

interface ApiService {


    @GET("{path}")
    fun getRandomJokes(@Path("path") path: String?): Observable<JokeResponse>
}