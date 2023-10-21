package com.example.pft.models

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun loginUsuario(@Body user: UserLoginRequest): Call<UserResponse>
}