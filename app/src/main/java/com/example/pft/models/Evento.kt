package com.example.pft.models

data class Evento (
    val idEvento: Int,
    val titulo: String,
    val informacion: String,
    val tipoEvento: String,
    val fechaInicio: Long,
    val fechaFin: Long,
    val modalidad: String,
    val localizacion: String,
    val creditos: Int,
    val estadoEvento: String,
    val itr: Itr,
    val listaSemestres: List<Any>  // You can replace Any with the actual data type if needed
)