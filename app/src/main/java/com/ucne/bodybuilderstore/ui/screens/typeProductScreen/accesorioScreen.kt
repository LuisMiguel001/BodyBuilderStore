package com.ucne.bodybuilderstore.ui.screens.typeProductScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.ucne.bodybuilderstore.data.local.entity.StoreEntity
import com.ucne.bodybuilderstore.ui.screens.registroScreen.ProductViewModel
import com.ucne.bodybuilderstore.ui.screens.registroScreen.StoreEvent

@Composable
fun accesorioScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    navController: NavController
) {
    val productos by viewModel.getProductosByType("Accesorio").collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(120.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(8.dp)
        ) {
            items(productos) { producto ->
                AccesorioCard(producto = producto,
                    onDeleteClick = {viewModel.onEvent(StoreEvent.Delete(producto))},
                    onFavoriteClick = { viewModel.toggleFavorite(producto) },
                    onClick = { navController.navigate("detalle/${producto.id}") })
            }
        }
        Spacer(modifier = Modifier.height(1000.dp))
    }
}

@Composable
fun AccesorioCard(
    producto: StoreEntity,
    onDeleteClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    val painter: Painter = rememberImagePainter(data = producto.imagen)

    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable { onClick() }
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxSize()
                            .background(androidx.compose.ui.graphics.Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = producto.nombre,
                                style = MaterialTheme.typography.titleSmall,
                                color = androidx.compose.ui.graphics.Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = String.format("%.2f", producto.precio),
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.Blue
                            )
                        }
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        ) {
                            IconButton(
                                onClick = onFavoriteClick,
                                modifier = Modifier
                                    .size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (producto.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    tint = if (producto.isFavorite) Color.Red else Color.Gray,
                                    contentDescription = "Favorito"
                                )
                            }
                            IconButton(
                                onClick = onDeleteClick,
                                modifier = Modifier
                                    .size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    tint = Color.Red,
                                    contentDescription = "Eliminar"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}