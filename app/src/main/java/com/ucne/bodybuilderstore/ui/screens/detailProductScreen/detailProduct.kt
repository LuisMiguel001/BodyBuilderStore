package com.ucne.bodybuilderstore.ui.screens.detailProductScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberImagePainter
import com.ucne.bodybuilderstore.R
import com.ucne.bodybuilderstore.ui.screens.cartScreen.CartViewModel
import com.ucne.bodybuilderstore.ui.screens.registroScreen.ProductViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailsScreen(
    productId: Int,
    viewModel: ProductViewModel = hiltViewModel(),
    viewModelC: CartViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val producto by viewModel.getProductoById(productId).collectAsState(initial = null)
    val painter: Painter = rememberImagePainter(data = producto?.imagen)
    val myGreen = Color(android.graphics.Color.parseColor("#00A42E"))
    var productAddedToCart by remember { mutableStateOf(false) }
    val state by viewModelC.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            IconButton(
                onClick = { navigateBack() }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back",
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
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(MaterialTheme.shapes.medium)
        )

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
                        colors = CardDefaults.cardColors(Color.Blue)
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
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("Unidades Disponibles: ")
                                }
                                withStyle(style = SpanStyle(color = Color.Blue)) {
                                    append("${producto?.existencia}")
                                }
                            },
                            style = MaterialTheme.typography.titleMedium,
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
                                cantidad = 1
                            )
                        }
                        productAddedToCart = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 20.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
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
    }
    state.MessageSucces?.let {
        MessageCard(message = it, color = Color.Blue)
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
