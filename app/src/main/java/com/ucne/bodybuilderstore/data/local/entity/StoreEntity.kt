package com.ucne.bodybuilderstore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("table_store")
data class StoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val detalle: String = "",
    val precio: Float = 0.0f,
    val imagen: String = "",
    val tipo: String = ""
)
