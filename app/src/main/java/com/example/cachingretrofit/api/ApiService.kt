package com.example.cachingretrofit.api

import retrofit2.http.GET
import com.example.cachingretrofit.JokeResponse
import io.reactivex.Observable
import retrofit2.http.Path

interface ApiService {
    @GET("{path}")
    fun getRandomJokes(@Path("path") path: String?): Observable<JokeResponse?>?
}