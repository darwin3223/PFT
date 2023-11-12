package com.example.pft.models

data class Evento (
    val idEvento: Long,
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
    val listaSemestres: List<Semestre>
)

data class Semestre (
    val idSemestre: Long,
    val nombre: String
)
