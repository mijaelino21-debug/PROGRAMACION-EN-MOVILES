package com.example.clinicasalud.model

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val calificacion: Double
)

data class Cita(
    val medico: Medico,
    val fecha: String,
    val hora: String,
    var estado: String = "Confirmada"
)

val listaMedicos = listOf(
    Medico(1, "Dra. Ana Torres", "Cardiología", 4.8),
    Medico(2, "Dr. Luis Ramos", "Pediatría", 4.6),
    Medico(3, "Dra. Carla Vidal", "Dermatología", 4.9),
    Medico(4, "Dr. Jorge Peña", "Traumatología", 4.5)
)

val especialidades = listOf("Todas", "Cardiología", "Pediatría", "Dermatología", "Traumatología")