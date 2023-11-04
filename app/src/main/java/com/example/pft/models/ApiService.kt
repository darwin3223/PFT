package com.example.pft.models

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    fun loginUsuario(@Body user: UserLoginRequest): Call<UserResponse>

    @GET("eventos")
    fun getAllEventos(@Header("Authorization") token: String?): Call<List<Evento>>

    @GET("estadosSolicitud")
    fun getAllEstados(@Header("Authorization") token: String?): Call<List<EstadoSolicitud>>

    @POST("reclamos")
    fun createReclamo(@Header("Authorization") token: String?, @Body reclamo: Reclamo?): Call<Reclamo>

    @GET("reclamos")
    fun getAllReclamos(@Header("Authorization") token: String?): Call<List<Reclamo>>

    @PUT("reclamos")
    fun updateReclamo(@Header("Authorization") token: String?, @Body reclamo: Reclamo?): Call<Reclamo>

    @DELETE("reclamos/{id}")
    fun deleteReclamo(@Path("id") id: Long?,@Header("Authorization") token: String?): Call<Reclamo>
}