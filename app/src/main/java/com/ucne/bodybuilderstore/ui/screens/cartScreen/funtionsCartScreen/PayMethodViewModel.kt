package com.ucne.bodybuilderstore.ui.screens.cartScreen.funtionsCartScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.bodybuilderstore.data.local.entity.Location
import com.ucne.bodybuilderstore.data.local.entity.PaymentMethod
import com.ucne.bodybuilderstore.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentMethodViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PaymentMethodState())
    val state = _state.asStateFlow()

    fun getPaymentMethodById(id: Int): Flow<PaymentMethod?> {
        return cartRepository.getPaymentMethodById(id)
    }

    fun onEvent(event: PaymentMethodEvent) {
        when (event) {
            is PaymentMethodEvent.CardholderName -> {
                _state.update {
                    it.copy(
                        paymentMethod = it.paymentMethod.copy(
                            cardholderName = event.cardholderName
                        )
                    )
                }
            }
            is PaymentMethodEvent.CardNumber -> {
                _state.update {
                    it.copy(
                        paymentMethod = it.paymentMethod.copy(
                            cardNumber = event.cardNumber
                        )
                    )
                }
            }
            is PaymentMethodEvent.ExpirationDate -> {
                _state.update {
                    it.copy(
                        paymentMethod = it.paymentMethod.copy(
                            expirationDate = event.expirationDate
                        )
                    )
                }
            }
            is PaymentMethodEvent.Cvv -> {
                _state.update {
                    it.copy(
                        paymentMethod = it.paymentMethod.copy(
                            cvv = event.cvv
                        )
                    )
                }
            }
            is PaymentMethodEvent.CardType -> {
                _state.update {
                    it.copy(
                        paymentMethod = it.paymentMethod.copy(
                            cardType = event.cardType
                        )
                    )
                }
            }
            is PaymentMethodEvent.BillingAddress -> {
                _state.update {
                    it.copy(
                        paymentMethod = it.paymentMethod.copy(
                            billingAddress = event.billingAddress
                        )
                    )
                }
            }
            is PaymentMethodEvent.PostalCode -> {
                _state.update {
                    it.copy(
                        paymentMethod = it.paymentMethod.copy(
                            postalCode = event.postalCode
                        )
                    )
                }
            }
            is PaymentMethodEvent.Email -> {
                _state.update {
                    it.copy(
                        paymentMethod = it.paymentMethod.copy(
                            email = event.email
                        )
                    )
                }
            }

            PaymentMethodEvent.onSave -> {
                val cardHolder = state.value.paymentMethod.cardholderName
                val cardNumber = _state.value.paymentMethod.cardNumber
                val expirationDate = state.value.paymentMethod.expirationDate
                val cvv = state.value.paymentMethod.cvv
                val cardType = state.value.paymentMethod.cardType
                val billingAddress = state.value.paymentMethod.billingAddress
                val postalCode = state.value.paymentMethod.postalCode
                val email = state.value.paymentMethod.email

                val pay = PaymentMethod(
                    cardholderName = cardHolder,
                    cardNumber = cardNumber,
                    expirationDate = expirationDate,
                    cvv = cvv,
                    cardType = cardType,
                    billingAddress = billingAddress,
                    postalCode = postalCode,
                    email = email
                )

                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        successMessage = null
                    )
                }

                viewModelScope.launch {
                    try {
                        cartRepository.savePaymentMethod(pay)
                        _state.update {
                            it.copy(
                                isLoading = false,
                                successMessage = "Se guardó correctamente"
                            )
                        }
                    } catch (e: Exception) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "Error al guardar: ${e.message}"
                            )
                        }
                    }
                }
            }
        }
    }
}

data class PaymentMethodState(
    val isLoading: Boolean = false,
    val paymentMethod: PaymentMethod = PaymentMethod(),
    val successMessage: String? = null,
    val error: String? = null,
)

sealed interface PaymentMethodEvent {
    data class CardholderName(val cardholderName: String) : PaymentMethodEvent
    data class CardNumber(val cardNumber: String) : PaymentMethodEvent
    data class ExpirationDate(val expirationDate: String) : PaymentMethodEvent
    data class Cvv(val cvv: String) : PaymentMethodEvent
    data class CardType(val cardType: String) : PaymentMethodEvent
    data class BillingAddress(val billingAddress: String) : PaymentMethodEvent
    data class PostalCode(val postalCode: String) : PaymentMethodEvent
    data class Email(val email: String) : PaymentMethodEvent
    object onSave : PaymentMethodEvent
}
