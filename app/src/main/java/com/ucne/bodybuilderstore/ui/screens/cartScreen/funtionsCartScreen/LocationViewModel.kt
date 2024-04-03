package com.ucne.bodybuilderstore.ui.screens.cartScreen.funtionsCartScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.bodybuilderstore.data.local.entity.Location
import com.ucne.bodybuilderstore.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: CartRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LocationState())
    val state = _state.asStateFlow()

    fun getLocationById(id: Int): Flow<Location?> {
        return locationRepository.getLocationById(id)
    }

    fun onEvent(event: LocationEvent) {
        when (event) {
            is LocationEvent.Id -> {
                _state.update {
                    it.copy(
                        location = it.location.copy(
                            id = event.id.toIntOrNull() ?: 0
                        )
                    )
                }
            }
            is LocationEvent.Address -> {
                _state.update {
                    it.copy(
                        location = it.location.copy(address = event.address)
                    )
                }
            }
            is LocationEvent.City -> {
                _state.update {
                    it.copy(
                        location = it.location.copy(city = event.city)
                    )
                }
            }
            is LocationEvent.State -> {
                _state.update {
                    it.copy(
                        location = it.location.copy(state = event.state)
                    )
                }
            }
            is LocationEvent.PostalCode -> {
                _state.update {
                    it.copy(
                        location = it.location.copy(postalCode = event.postalCode)
                    )
                }
            }
            is LocationEvent.Country -> {
                _state.update {
                    it.copy(
                        location = it.location.copy(country = event.country)
                    )
                }
            }
            is LocationEvent.GpsCoordinates -> {
                _state.update {
                    it.copy(
                        location = it.location.copy(gpsCoordinates = event.gpsCoordinates)
                    )
                }
            }
            is LocationEvent.AdditionalNotes -> {
                _state.update {
                    it.copy(
                        location = it.location.copy(additionalNotes = event.additionalNotes)
                    )
                }
            }

            LocationEvent.onSave -> {
                val address = state.value.location.address
                val city = _state.value.location.city
                val estado = state.value.location.state
                val postalCode = state.value.location.postalCode
                val country = state.value.location.country
                val gpsCoordinates = state.value.location.gpsCoordinates
                val additionalNotes = state.value.location.additionalNotes

                val location = Location(
                    address = address,
                    city = city,
                    state = estado,
                    postalCode = postalCode,
                    country = country,
                    gpsCoordinates = gpsCoordinates,
                    additionalNotes = additionalNotes
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
                        locationRepository.saveLocation(location)
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

            LocationEvent.onNew -> {
                _state.update {
                    it.copy(
                        successMessage = null,
                        error = null,
                        location = Location(),
                    )
                }
            }
        }
    }
}

data class LocationState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val location: Location = Location(),
    val successMessage: String? = null,
)

sealed interface LocationEvent {
    data class Id(val id: String) : LocationEvent
    data class Address(val address: String) : LocationEvent
    data class City(val city: String) : LocationEvent
    data class State(val state: String) : LocationEvent
    data class PostalCode(val postalCode: String) : LocationEvent
    data class Country(val country: String) : LocationEvent
    data class GpsCoordinates(val gpsCoordinates: String) : LocationEvent
    data class AdditionalNotes(val additionalNotes: String) : LocationEvent
    object onSave : LocationEvent
    object onNew : LocationEvent
}