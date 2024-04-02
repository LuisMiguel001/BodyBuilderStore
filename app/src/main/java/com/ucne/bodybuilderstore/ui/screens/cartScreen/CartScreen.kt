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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberImagePainter
import com.ucne.bodybuilderstore.R
import com.ucne.bodybuilderstore.data.local.entity.CartEntity
import com.ucne.bodybuilderstore.data.local.entity.Location
import com.ucne.bodybuilderstore.ui.screens.cartScreen.funtionsCartScreen.InformationCard
import com.ucne.bodybuilderstore.ui.screens.cartScreen.funtionsCartScreen.LocationForm
import com.ucne.bodybuilderstore.ui.screens.cartScreen.funtionsCartScreen.LocationViewModel
import com.ucne.bodybuilderstore.ui.screens.cartScreen.funtionsCartScreen.OrderSummary
import com.ucne.bodybuilderstore.ui.screens.cartScreen.funtionsCartScreen.PaymentMethodForm
import com.ucne.bodybuilderstore.ui.screens.cartScreen.funtionsCartScreen.SwipeToDeleteContainer
import kotlinx.coroutines.delay

@Composable
fun CartScreen(
    viewModelC: CartViewModel = hiltViewModel(),
    viewModelL: LocationViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    val state by viewModelC.state.collectAsStateWithLifecycle()
    val cartItems by viewModelC.cartItems.collectAsState(initial = emptyList())
    val totalItemsInCart = viewModelC.getTotalItemsInCart()
    val totalProductsCount = viewModelC.getTotalProductsCount()
    val totalPrice = viewModelC.getTotalPrice()
    var isLocationFormVisible by remember { mutableStateOf(false) }
    var isPaymentMethodFormVisible by remember { mutableStateOf(false) }
    val cartLocationId = cartItems.firstOrNull()?.locationId ?: 0
    val cartLocation = viewModelL.getLocationById(cartLocationId).collectAsState(initial = null).value

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
                    modifier = Modifier.size(30.dp)
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tu Carrito de BodyBuilder Store está vacío",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        } else {
            LazyColumn {
                items(cartItems) { item ->
                    SwipeToDeleteContainer(
                        item = item,
                        onDelete = { deletedItem -> viewModelC.removeCartItem(deletedItem.id) },
                        onClick = { /* handle item click */ },
                        content = { item ->
                            CartItemRow(
                                item = item,
                                onIncreaseClicked = {
                                    viewModelC.addToCart(
                                        item.imagen,
                                        item.nombre,
                                        item.precio,
                                        1,
                                        item.locationId,
                                        item.paymentMethodId
                                    )
                                },
                                onDecreaseClicked = {
                                    viewModelC.addToCart(
                                        item.imagen,
                                        item.nombre,
                                        item.precio,
                                        -1,
                                        item.locationId,
                                        item.paymentMethodId
                                    )
                                }
                            )
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModelC.clearAll() },
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
                    Spacer(modifier = Modifier.height(16.dp))

                    if (isLocationFormVisible) {
                        LocationForm(
                            location = Location(),
                            onDismiss = { isLocationFormVisible = false }
                        )
                    }

                    if (isPaymentMethodFormVisible) {
                        PaymentMethodForm(
                            onDismiss = { isPaymentMethodFormVisible = false }
                        )
                    }

                    Text(
                        text = "Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    InformationCard(
                        location = cartLocation,
                        onLocation = {
                            IconButton(
                                onClick = {
                                    isLocationFormVisible = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "More details"
                                )
                            }
                        },
                        onPaymentMethod = {
                            IconButton(
                                onClick = {
                                    isPaymentMethodFormVisible = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "More details"
                                )
                            }
                        },
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {viewModelC.OrderNow()},
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
            }
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
                        .clickable {
                            if (item.cantidad > 1) {
                                onDecreaseClicked()
                            }
                        },
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
