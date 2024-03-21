package com.ucne.bodybuilderstore.ui.screens.registroScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.bodybuilderstore.data.local.entity.StoreEntity
import com.ucne.bodybuilderstore.data.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val storeRepository: StoreRepository
) : ViewModel() {
    private val _state = MutableStateFlow(StoreState())
    val state = _state.asStateFlow()
    val stores: Flow<List<StoreEntity>> = storeRepository.getProducto()

    fun onEvent(event: StoreEvent) {
        when (event) {
            is StoreEvent.Id -> {
                _state.update {
                    it.copy(
                        store = it.store.copy(
                            id = event.id.toIntOrNull() ?: 0
                        )
                    )
                }
            }

            is StoreEvent.Nombre -> {
                _state.update {
                    it.copy(
                        store = it.store.copy(nombre = event.nombre)
                    )
                }
            }

            is StoreEvent.Descripcion -> {
                _state.update {
                    it.copy(
                        store = it.store.copy(descripcion = event.descripcion)
                    )
                }
            }

            is StoreEvent.Detalle -> {
                _state.update {
                    it.copy(
                        store = it.store.copy(detalle = event.detalle)
                    )
                }
            }

            is StoreEvent.Precio -> {
                _state.update {
                    it.copy(
                        store = it.store.copy(precio = event.precio.toFloatOrNull() ?: 0f)
                    )
                }
            }

            is StoreEvent.Imagen -> {
                _state.update {
                    it.copy(
                        store = it.store.copy(imagen = event.imagen)
                    )
                }
            }

            StoreEvent.onSave -> {
                val nombre = state.value.store.nombre
                val descripcion = state.value.store.descripcion
                val detalle = state.value.store.detalle
                val precio = state.value.store.precio
                val imagen = state.value.store.imagen

                if (nombre.isBlank() || descripcion.isBlank() || detalle.isBlank() || imagen.isBlank()) {
                    _state.update {
                        it.copy(
                            error = "Por favor, complete todos los campos."
                        )
                    }
                    return
                }

                val store = StoreEntity(
                    nombre = nombre,
                    descripcion = descripcion,
                    detalle = detalle,
                    precio = precio,
                    imagen = imagen
                )

                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        succesMessage = null
                    )
                }

                viewModelScope.launch {
                    try {
                        storeRepository.upsert(store)
                        _state.update {
                            it.copy(
                                isLoading = false,
                                succesMessage = "Se guardó correctamente"
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

                _state.update {
                    it.copy(
                        store = StoreEntity()
                    )
                }
            }

            StoreEvent.onNew -> {
                _state.update {
                    it.copy(
                        succesMessage = null,
                        error = null,
                        store = StoreEntity()
                    )
                }
            }

            is StoreEvent.Delete -> {
                viewModelScope.launch {
                    storeRepository.delete(event.store)
                }
            }
        }
    }
}

data class StoreState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val store: StoreEntity = StoreEntity(),
    val succesMessage: String? = null,
)

sealed interface StoreEvent {
    data class Id(val id: String) : StoreEvent
    data class Nombre(val nombre: String) : StoreEvent
    data class Descripcion(val descripcion: String) : StoreEvent
    data class Detalle(val detalle: String) : StoreEvent
    data class Precio(val precio: String) : StoreEvent
    data class Imagen(val imagen: String) : StoreEvent
    data class Delete(val store: StoreEntity) : StoreEvent
    object onSave : StoreEvent
    object onNew : StoreEvent
}