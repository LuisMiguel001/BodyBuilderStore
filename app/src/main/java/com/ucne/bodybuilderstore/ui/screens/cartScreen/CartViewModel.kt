package com.ucne.bodybuilderstore.ui.screens.cartScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.bodybuilderstore.data.local.entity.CartEntity
import com.ucne.bodybuilderstore.data.local.entity.Location
import com.ucne.bodybuilderstore.data.local.entity.StoreEntity
import com.ucne.bodybuilderstore.data.repository.CartRepository
import com.ucne.bodybuilderstore.data.repository.StoreRepository
import com.ucne.bodybuilderstore.ui.screens.cartScreen.funtionsCartScreen.LocationState
import com.ucne.bodybuilderstore.ui.screens.cartScreen.funtionsCartScreen.PaymentMethodState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
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

    private val _state = MutableStateFlow(StateCart())
    val state = _state.asStateFlow()

    private val _locationState = MutableStateFlow(LocationState())
    val locationState: StateFlow<LocationState> = _locationState.asStateFlow()

    private val _payState = MutableStateFlow(PaymentMethodState())
    val payState: StateFlow<PaymentMethodState> = _payState.asStateFlow()

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

    fun addToCart(imagen: String, nombre: String, precio: Float, cantidad: Int, locationId: Int,payId: Int,) {
        viewModelScope.launch {
            val existingItem = cartRepository.getCartItemByName(nombre)
            if (existingItem != null) {
                cartRepository.updateCartItemQuantity(existingItem.id, existingItem.cantidad + cantidad)
            } else {
                cartRepository.addToCart(imagen, nombre, precio, cantidad, locationId = 1, payId)
            }

            _state.update {
                it.copy(
                    MessageSucces = "\t\t\t\t✔Se agrego al carrito✔"
                )
            }
        }
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
                    info = "\t\t\t\tℹSe limpio el carritoℹ"
                )
            }
        }
    }

    fun OrderNow() {
        viewModelScope.launch {
            val locationId = locationState.value.location.id
            val paymentMethodId = payState.value.paymentMethod.id

            val locationCount = cartRepository.getLocationById(locationId).firstOrNull()
            val paymentMethodCount = cartRepository.getPaymentMethodById(paymentMethodId).firstOrNull()

            if (locationCount != null && paymentMethodCount != null) {
                cartRepository.clearAll()
                _state.update {
                    it.copy(
                        MessageSucces = "\t\t\t\t✔Orden Exitosa✔"
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        info = "❌No se puede realizar el pedido, asegúrate de tener la ubicación y la info credit card configuradas❌"
                    )
                }
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
