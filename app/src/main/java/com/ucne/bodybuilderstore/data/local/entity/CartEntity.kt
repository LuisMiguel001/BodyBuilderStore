package com.ucne.bodybuilderstore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imagen: String,
    val nombre: String,
    val precio: Float,
    val cantidad: Int,
)
