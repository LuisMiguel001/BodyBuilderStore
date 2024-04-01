package com.ucne.bodybuilderstore.data.repository

import com.ucne.bodybuilderstore.data.local.dao.CartDao
import com.ucne.bodybuilderstore.data.local.entity.CartEntity
import com.ucne.bodybuilderstore.data.local.entity.Location
import com.ucne.bodybuilderstore.data.local.entity.PaymentMethod
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {
    suspend fun addToCart(imagen: String, nombre: String, precio: Float, cantidad: Int, locationId: Int, payId: Int, existencia: Int) {
        val item = CartEntity(imagen = imagen, nombre = nombre, precio = precio, cantidad = cantidad, locationId = 1, paymentMethodId = payId, existencia = existencia)
        cartDao.insertOrUpdate(item)
    }

    fun getAllCartItems(): Flow<List<CartEntity>> {
        return cartDao.getAllItems()
    }

    suspend fun removeCartItem(itemId: Int) {
        cartDao.deleteById(itemId)
    }

    suspend fun clearAll() {
        cartDao.clearAll()
    }

    suspend fun getCartItemByName(nombre: String): CartEntity? {
        return cartDao.getCartItemByName(nombre)
    }

    suspend fun updateCartItemQuantity(itemId: Int, newQuantity: Int) {
        cartDao.updateCartItemQuantity(itemId, newQuantity)
    }

    suspend fun saveLocation(location: Location) {
        cartDao.insertOrUpdateLocation(location)
    }

    fun getLocationById(id: Int): Flow<Location?> {
        return cartDao.getLocationById(id)
    }

    suspend fun savePaymentMethod(paymentMethod: PaymentMethod) {
        cartDao.insertOrUpdatePaymentMethod(paymentMethod)
    }

    suspend fun getPaymentMethodById(paymentMethodId: Int): PaymentMethod? {
        return cartDao.getPaymentMethodById(paymentMethodId)
    }
}
