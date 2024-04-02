package com.ucne.bodybuilderstore.ui.screens.registroScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.bodybuilderstore.data.local.entity.StoreEntity
import com.ucne.bodybuilderstore.data.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun getProductosByType(type: String): Flow<List<StoreEntity>> {
        return storeRepository.getProductosByType(type)
    }

    fun getProductoById(id: Int): Flow<StoreEntity?> {
        return storeRepository.getProductoById(id)
    }

    fun toggleFavorite(producto: StoreEntity) {
        viewModelScope.launch {
            if (producto.isFavorite) {
                storeRepository.removeFromFavorites(producto.id)
            } else {
                storeRepository.markAsFavorite(producto.id)
            }
        }
    }

    fun getFavorites(): Flow<List<StoreEntity>> {
        return storeRepository.getFavorites()
    }

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

            is StoreEvent.TipoProducto -> {
                _state.update {
                    it.copy(
                        store = it.store.copy(tipo = event.tipo)
                    )
                }
            }

            /*is StoreEvent.Existencia -> {
                _state.update {
                    it.copy(
                        store = it.store.copy(existencia = event.existencia.toIntOrNull() ?: 0)
                    )
                }
            }*/

            StoreEvent.onSave -> {
                val nombre = state.value.store.nombre
                val descripcion = state.value.store.descripcion
                val detalle = state.value.store.detalle
                val precio = state.value.store.precio
                val imagen = state.value.store.imagen
                val tipo = state.value.store.tipo
                /*val existencia = state.value.store.existencia*/

                val emptyFields = mutableListOf<String>()
                if (nombre.isBlank()) {
                    emptyFields.add("Nombre")
                }
                if (descripcion.isBlank()) {
                    emptyFields.add("Descripción")
                }
                if (detalle.isBlank()) {
                    emptyFields.add("Detalle")
                }
                if (precio <= 0) {
                    emptyFields.add("Precio")
                }
                if (imagen.isBlank()) {
                    emptyFields.add("Imagen")
                }
                if (tipo.isBlank()) {
                    emptyFields.add("Tipo")
                }

                if (emptyFields.isNotEmpty()) {
                    _state.update {
                        it.copy(
                            error = "Llene los campos requeridos: ${emptyFields.joinToString(", ")}",
                            emptyFields = emptyFields
                        )
                    }
                    return
                }

                val store = StoreEntity(
                    nombre = nombre,
                    descripcion = descripcion,
                    detalle = detalle,
                    precio = precio,
                    imagen = imagen,
                    tipo = tipo,
                    /*existencia = existencia*/
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
                                succesMessage = "Se guardó correctamente✔"
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
                        store = StoreEntity(),
                        emptyFields = listOf()
                    )
                }
            }

            StoreEvent.onNew -> {
                _state.update {
                    it.copy(
                        succesMessage = "\tℹVacíoℹ",
                        store = StoreEntity(),
                        emptyFields = listOf()
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
    val emptyFields: List<String> = listOf()
)

sealed interface StoreEvent {
    data class Id(val id: String) : StoreEvent
    data class Nombre(val nombre: String) : StoreEvent
    data class Descripcion(val descripcion: String) : StoreEvent
    data class Detalle(val detalle: String) : StoreEvent
    data class Precio(val precio: String) : StoreEvent
    data class Imagen(val imagen: String) : StoreEvent
    data class TipoProducto(val tipo: String) : StoreEvent
    /*data class Existencia(val existencia: String) : StoreEvent*/
    data class Delete(val store: StoreEntity) : StoreEvent
    object onSave : StoreEvent
    object onNew : StoreEvent
}
