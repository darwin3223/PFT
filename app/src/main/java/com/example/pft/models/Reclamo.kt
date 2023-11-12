package com.example.pft.models

data class Reclamo(
    val idReclamo: Long?,
    val titulo: String?,
    val tipoReclamo: String?,
    val detalle: String?,
    val idSemestre: Long?,
    val idEstado: Long?,
    val idEstudiante: Long?,
    val idEvento: Long?
)

data class ReclamoCompleto(
    val idReclamo: Long?,
    val titulo: String?,
    val tipoReclamo: String?,
    val detalle: String?,
    val semestre: Semestre,
    val estado: EstadoSolicitud?,
    val estudiante: Estudiante?,
    val evento: Evento
)
