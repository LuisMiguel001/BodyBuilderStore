package com.ucne.bodybuilderstore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("table_store")
data class StoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nombre: String,
    val decripción: String,
    val detalle: String,
    val precio: Float,
    val imagen: String
)
