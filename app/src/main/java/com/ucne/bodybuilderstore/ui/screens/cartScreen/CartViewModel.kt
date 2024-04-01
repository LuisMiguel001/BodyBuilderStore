package com.ucne.bodybuilderstore.ui.screens.cartScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.bodybuilderstore.data.local.entity.CartEntity
import com.ucne.bodybuilderstore.data.local.entity.StoreEntity
import com.ucne.bodybuilderstore.data.repository.CartRepository
import com.ucne.bodybuilderstore.data.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartEntity>>(emptyList())
    val cartItems: StateFlow<List<CartEntity>> = _cartItems
    val cartItem = mutableStateListOf<StoreEntity>()
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

    fun addToCart(imagen: String, nombre: String, precio: Float, cantidad: Int, locationId: Int,payId: Int, existencia: Int) {
        viewModelScope.launch {
            val existingItem = cartRepository.getCartItemByName(nombre)
            if (existingItem != null) {
                cartRepository.updateCartItemQuantity(existingItem.id, existingItem.cantidad + cantidad)
            } else {
                cartRepository.addToCart(imagen, nombre, precio, cantidad, locationId = 1, payId, existencia)
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

    fun clearAll() {
        viewModelScope.launch {
            cartRepository.clearAll()
            _state.update {
                it.copy(
                    info = "Se limpio el carrito"
                )
            }
        }
    }


    fun OrderNow() {
        viewModelScope.launch {
            cartRepository.clearAll()
            _state.update {
                it.copy(
                    MessageSucces = "Su orden se realizo correctemente.✔"
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
    val MessageSucces: String? = null,
    val cart: CartEntity = CartEntity()
)
