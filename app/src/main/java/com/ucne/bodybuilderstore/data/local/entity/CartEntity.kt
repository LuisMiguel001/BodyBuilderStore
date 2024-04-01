package com.ucne.bodybuilderstore.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "table_cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imagen: String = "",
    val nombre: String = "",
    val precio: Float = 0f,
    val cantidad: Int = 0,
    var existencia: Int = 0,
    val locationId: Int = 0,
    val paymentMethodId: Int = 0
)

@Entity(tableName = "table_location")
data class Location(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = "",
    val country: String = "",
    val gpsCoordinates: String = "",
    val additionalNotes: String = ""
)

@Entity(tableName = "table_payment_method")
data class PaymentMethod(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cardholderName: String = "",
    val cardNumber: String = "",
    val expirationDate: String = "",
    val cvv: String = "",
    val cardType: String = "",
    val billingAddress: String = "",
    val postalCode: String = "",
    val email: String = ""
)

class CartWithLocationAndPayment {
    @Embedded
    lateinit var cart: CartEntity

    @Relation(parentColumn = "locationId", entityColumn = "id")
    lateinit var location: Location

    @Relation(parentColumn = "paymentMethodId", entityColumn = "id")
    lateinit var paymentMethod: PaymentMethod
}
