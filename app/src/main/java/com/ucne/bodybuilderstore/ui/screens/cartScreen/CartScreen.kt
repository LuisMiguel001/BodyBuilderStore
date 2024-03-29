package com.ucne.bodybuilderstore.ui.screens.cartScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberImagePainter
import com.ucne.bodybuilderstore.R
import com.ucne.bodybuilderstore.data.local.entity.CartEntity
import kotlinx.coroutines.delay

@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    viewModelC: CartViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModelC.state.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsState(initial = emptyList())
    val totalItemsInCart = viewModel.getTotalItemsInCart()
    val totalProductsCount = viewModel.getTotalProductsCount()
    val totalPrice = viewModel.getTotalPrice()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row {
            IconButton(
                onClick = { navigateBack() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(30.dp)
                )
            }
            Spacer(modifier = Modifier.width(95.dp))
            Text(
                text = "My Cart",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            Text(
                text = "Your cart is empty",
                style = MaterialTheme.typography.titleMedium
            )
        } else {
            LazyColumn {
                items(cartItems + listOf(null)) { item ->
                    if (item != null) {
                        CartItemRow(
                            item = item,
                            onDeleteClicked = { viewModelC.removeCartItem(item.id) },
                            onIncreaseClicked = {
                                viewModelC.addToCart(
                                    item.imagen,
                                    item.nombre,
                                    item.precio,
                                    1
                                )
                            },
                            onDecreaseClicked = {
                                viewModelC.addToCart(
                                    item.imagen,
                                    item.nombre,
                                    item.precio,
                                    -1
                                )
                            }
                        )
                    } else {
                        Button(
                            onClick = { viewModel.clearCart() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Clear Cart",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Order Summary",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Card {
            OrderSummary(
                totalItemsInCart = totalItemsInCart,
                totalProductsCount = totalProductsCount,
                totalPrice = totalPrice
            )
        }
        Button(
            onClick = { /* Lógica para realizar el pedido */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Order Now",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
        }
    }
    state.MessageSucces?.let {
        MessageCard(message = it, color = Color.Green)
    }

    state.info?.let {
        MessageCard(message = it, color = Color.Red)
    }
}

@Composable
fun CartItemRow(
    item: CartEntity,
    onDeleteClicked: () -> Unit,
    onIncreaseClicked: () -> Unit,
    onDecreaseClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(data = item.imagen),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = item.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = String.format("$%.2f", item.precio),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Blue
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onDecreaseClicked() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = R.drawable.icons8_subtract_30
                        ),
                        contentDescription = "Decrease Quantity Button",
                        modifier = Modifier
                            .size(15.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = item.cantidad.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onIncreaseClicked() },
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { onIncreaseClicked() }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Increase Quantity",
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(
                onClick = onDeleteClicked
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Remove from Cart")
            }
        }
    }
}

@Composable
fun OrderSummary(
    totalItemsInCart: Int,
    totalProductsCount: Int,
    totalPrice: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 6.dp)
    ) {
        Row {
            Text(text = "Total Items in Cart:", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.width(185.dp))
            Text(text = "$totalItemsInCart")
        }
        Row {
            Text(text = "Total Products Count:", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.width(163.dp))
            Text(text = "$totalProductsCount")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .width(400.dp)
                    .background(color = Color.Black)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text(
                text = "Total:",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(256.dp))
            Text(
                text = String.format("$%.2f",totalPrice),
                fontWeight = FontWeight.Bold,
                color = Color.Black
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
