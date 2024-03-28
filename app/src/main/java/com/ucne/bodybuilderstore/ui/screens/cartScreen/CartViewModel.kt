package com.ucne.bodybuilderstore.ui.screens.cartScreen

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.bodybuilderstore.R
import com.ucne.bodybuilderstore.data.local.entity.CartEntity
import com.ucne.bodybuilderstore.data.repository.CartRepository
import com.ucne.bodybuilderstore.ui.screens.registroScreen.StoreState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartEntity>>(emptyList())
    val cartItems: StateFlow<List<CartEntity>> = _cartItems

    private val _state = MutableStateFlow(StateCart())
    val state = _state.asStateFlow()

    init {
        fetchCartItems()
    }

    private fun fetchCartItems() {
        viewModelScope.launch {
            cartRepository.getAllCartItems().collect {
                _cartItems.value = it
            }
        }
    }
    fun addToCart(imagen: String, nombre: String, precio: Float, cantidad: Int) {
        viewModelScope.launch {
            val existingItem = cartRepository.getCartItemByName(nombre)
            if (existingItem != null) {
                cartRepository.updateCartItemQuantity(existingItem.id, existingItem.cantidad + cantidad)
            } else {
                cartRepository.addToCart(imagen, nombre, precio, cantidad)
            }

            _state.update {
                it.copy(
                    MessageSucces = "Se agrego al carrito"
                )
            }
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
            _state.update {
                it.copy(
                    info = "Se limpio el carrito"
                )
            }
        }
    }

    fun getTotalItemsInCart(): Int {
        return _cartItems.value.size
    }

    fun getTotalProductsCount(): Int {
        return _cartItems.value.sumBy { it.cantidad }
    }

    fun getTotalPrice(): Float {
        return _cartItems.value.sumByDouble { it.precio.toDouble() * it.cantidad }.toFloat()
    }
}

data class StateCart(
    val info: String? = null,
    val MessageSucces: String? = null
)

