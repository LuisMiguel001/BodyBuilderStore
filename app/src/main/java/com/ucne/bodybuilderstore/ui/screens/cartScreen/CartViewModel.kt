package com.ucne.bodybuilderstore.ui.screens.cartScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.bodybuilderstore.data.local.entity.CartEntity
import com.ucne.bodybuilderstore.data.local.entity.StoreEntity
import com.ucne.bodybuilderstore.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    val cartItems: Flow<List<CartEntity>> = cartRepository.getAllCartItems()

    fun addToCart(imagen: String, nombre: String, precio: Float, cantidad: Int) {
        viewModelScope.launch {
            cartRepository.addToCart(imagen, nombre, precio, cantidad)
        }
    }

    fun getAllCartItems(): Flow<List<CartEntity>> {
        return cartRepository.getAllCartItems()
    }

    fun removeCartItem(itemId: Int) {
        viewModelScope.launch {
            cartRepository.removeCartItem(itemId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }
}
