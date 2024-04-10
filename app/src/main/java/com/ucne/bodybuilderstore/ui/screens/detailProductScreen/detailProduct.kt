package com.ucne.bodybuilderstore.ui.screens.detailProductScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.ucne.bodybuilderstore.R
import com.ucne.bodybuilderstore.data.local.entity.StoreEntity
import com.ucne.bodybuilderstore.ui.screens.cartScreen.CartViewModel
import com.ucne.bodybuilderstore.ui.screens.registroScreen.ProductViewModel
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailsScreen(
    productId: Int,
    viewModel: ProductViewModel = hiltViewModel(),
    viewModelC: CartViewModel = hiltViewModel(),
    navController: NavController,
) {
    val producto by viewModel.getProductoById(productId).collectAsState(initial = null)
    val painter: Painter = rememberImagePainter(data = producto?.imagen)
    var productAddedToCart by remember { mutableStateOf(false) }
    val state by viewModelC.state.collectAsStateWithLifecycle()
    val productosSimilares by viewModel.getProductosByType(producto?.tipo ?: "").collectAsState(initial = emptyList())
    val myGreen = Color(android.graphics.Color.parseColor("#04764B"))
    var isFavorite by remember { mutableStateOf(producto?.isFavorite ?: false) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            IconButton(
                onClick = {navController.navigate("suplemento") }
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "home",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(288.dp))
            Row{
                Image(
                    painter = rememberImagePainter(
                        data = R.drawable.icons8_share_48,
                    ),
                    contentDescription = "Decrease Quantity Button",
                    modifier = Modifier
                        .size(30.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = rememberImagePainter(
                        data = R.drawable.icons8_save_24,
                    ),
                    contentDescription = "Decrease Quantity Button",
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                    producto?.let { viewModel.toggleFavorite(it) }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "favorite",
                    tint = if (isFavorite) Color.Red else Color.Gray,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding()
                    .clip(shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .fillMaxHeight(0.08f)
                            .padding(1.dp),
                        colors = CardDefaults.cardColors(myGreen)
                    ) {
                        Text(
                            text = producto?.nombre ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 18.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "$${producto?.precio}",
                        style = MaterialTheme.typography.titleMedium,
                        color = myGreen
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Descripción:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = producto?.descripcion ?: "",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Detalle:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = producto?.detalle ?: "",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Button(
                    onClick = {
                        producto?.let {
                            viewModelC.addToCart(
                                imagen = it.imagen,
                                nombre = it.nombre,
                                precio = it.precio,
                                cantidad = 1,
                                locationId = it.id,
                                payId = it.id,
                            )
                        }
                        productAddedToCart = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 20.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = myGreen,
                        contentColor = Color.White
                    ),
                    enabled = !productAddedToCart
                ) {
                    Text(
                        text = "Add to Cart",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Productos relacionados",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp, end = 13.dp)
        ) {
            items(productosSimilares) { producto ->
                SuggestedProductCard(
                    producto = producto,
                    onClick = { navController.navigate("detalle/${producto.id}") }
                )
            }
        }
        Spacer(modifier = Modifier.height(25.dp))
    }
    state.MessageSucces?.let {
        MessageCard(message = it, color = Color.Gray)
    }
}

@Composable
fun SuggestedProductCard(
    producto: StoreEntity,
    onClick: () -> Unit
) {
    val painter: Painter = rememberImagePainter(data = producto.imagen)
    val myGreen = Color(android.graphics.Color.parseColor("#04764B"))

    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .padding(start = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .width(150.dp)
                .padding(8.dp)
                .clickable { onClick() }
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = String.format("%.2f", producto.precio),
                style = MaterialTheme.typography.titleSmall,
                color = myGreen
            )
        }
    }
}

@Composable
fun MessageCard(
    message: String,
    color: Color,
    durationMillis: Long = 3000
) {
    var showMessage by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(durationMillis)
        showMessage = false
    }

    if (showMessage) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(16.dp),
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = color
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
