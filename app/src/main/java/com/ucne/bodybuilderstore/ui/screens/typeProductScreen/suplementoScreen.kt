package com.ucne.bodybuilderstore.ui.screens.typeProductScreen

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.ucne.bodybuilderstore.data.local.entity.StoreEntity
import com.ucne.bodybuilderstore.ui.screens.registroScreen.ProductViewModel
import com.ucne.bodybuilderstore.ui.screens.registroScreen.StoreEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    navController: NavController,
) {
    val productos by viewModel.getProductosByType("Suplemento")
        .collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }
    val myGreen = Color(android.graphics.Color.parseColor("#04764B"))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Surface(
            color = myGreen,
            shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp),
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                placeholder = { Text("Search") },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.LightGray,
                    cursorColor = Color.LightGray,
                    disabledLeadingIconColor = Color.White,
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                }
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(8.dp)
        ) {
            val filteredProducts = productos.filter {
                it.nombre.contains(searchQuery, ignoreCase = true)
            }
            items(filteredProducts) { producto ->
                SuplemetoCard(
                    producto = producto,
                    onDeleteClick = { viewModel.onEvent(StoreEvent.Delete(producto)) },
                    onFavoriteClick = { viewModel.toggleFavorite(producto) },
                    onClick = { navController.navigate("detalle/${producto.id}") },
                    onEdit = {
                        navController.navigate("edit/${producto.id}")
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(1000.dp))
    }
}

@Composable
fun SuplemetoCard(
    producto: StoreEntity,
    onDeleteClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit,
    onEdit: () -> Unit
) {
    val painter: Painter = rememberImagePainter(data = producto.imagen)
    val myGreen = Color(android.graphics.Color.parseColor("#04764B"))
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userEmail = currentUser?.email ?: ""
    val isAdmin = userEmail == "admin@gmail.com"

    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable { onClick() }
        ) {
            Box(
                modifier = Modifier.aspectRatio(1f)
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(175.dp)
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium)
                )
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (producto.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        tint = if (producto.isFavorite) Color.Red else Color.Gray,
                        contentDescription = "Favorito",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
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
                            .background(Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = producto.nombre,
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = String.format("%.2f", producto.precio),
                                style = MaterialTheme.typography.titleSmall,
                                color = myGreen
                            )
                        }
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        ) {
                            IconButton(
                                onClick = { onEdit() },
                                modifier = Modifier
                                    .size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    tint = Color.Gray,
                                    contentDescription = "Editar"
                                )
                            }
                            if (isAdmin) {
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
}

