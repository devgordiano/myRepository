package com.devgordiano.myrepository.network

import com.devgordiano.myrepository.domain.Repository
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RepoService {

    @GET("/users/{user}/repos")
    fun getAllRepo(@Path("user") user: String): Call<List<Repository>>
}