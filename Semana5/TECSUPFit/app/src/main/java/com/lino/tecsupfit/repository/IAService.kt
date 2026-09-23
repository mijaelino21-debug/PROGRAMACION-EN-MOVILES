package com.lino.tecsupfit.repository

import com.lino.tecsupfit.model.IARecomendacion

class IAService {
    fun obtenerConsejoDiario(): String {
        return "Para optimizar el rendimiento, mantén una hidratación constante y descansa 8 horas."
    }

    fun obtenerRecomendaciones(nivel: String): List<IARecomendacion> {
        return listOf(
            IARecomendacion("1", "Hiit Cardio Pro", "Intensidad alta con intervalos", nivel),
            IARecomendacion("2", "Fuerza Funcional", "Fortalecimiento del core", nivel)
        )
    }
}