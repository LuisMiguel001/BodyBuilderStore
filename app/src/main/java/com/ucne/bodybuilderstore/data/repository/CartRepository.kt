package com.ucne.bodybuilderstore.data.repository

import com.ucne.bodybuilderstore.data.local.dao.CartDao
import com.ucne.bodybuilderstore.data.local.entity.CartEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {
    suspend fun addToCart(imagen: String, nombre: String, precio: Float, cantidad: Int) {
        val item = CartEntity(imagen = imagen, nombre = nombre, precio = precio, cantidad = cantidad)
        cartDao.insertOrUpdate(item)
    }

    fun getAllCartItems(): Flow<List<CartEntity>> {
        return cartDao.getAllItems()
    }

    suspend fun removeCartItem(itemId: Int) {
        cartDao.deleteById(itemId)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    suspend fun getCartItemByName(nombre: String): CartEntity? {
        return cartDao.getCartItemByName(nombre)
    }

    suspend fun updateCartItemQuantity(itemId: Int, newQuantity: Int) {
        cartDao.updateCartItemQuantity(itemId, newQuantity)
    }
}
