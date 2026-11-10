package com.tecsup.catalogo.data.local

import androidx.room.Entity

@Entity(
    tableName = "favoritos",
    primaryKeys = ["usuarioId", "hotelId"]
)
data class FavoritoEntity(
    val usuarioId: String,
    val hotelId: Int
)
