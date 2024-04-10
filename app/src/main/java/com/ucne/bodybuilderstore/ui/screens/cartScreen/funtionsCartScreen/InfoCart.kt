package com.ucne.bodybuilderstore.ui.screens.cartScreen.funtionsCartScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ucne.bodybuilderstore.data.local.entity.Location
import com.ucne.bodybuilderstore.data.local.entity.PaymentMethod

@Composable
fun InformationCard(
    location: Location?,
    payMethod: PaymentMethod?,
    onLocation: @Composable () -> Unit,
    onPaymentMethod: @Composable () -> Unit,
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Location: ",
                    fontWeight = FontWeight.Medium
                )
                if (location != null) {
                    Text(text = location.address, fontWeight = FontWeight.Bold, color = Color.Black)
                } else {
                    Text(text = "No hay dirección")
                }
                Spacer(modifier = Modifier.weight(1f))
                onLocation()
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Info Credit Card: ",
                    fontWeight = FontWeight.Medium
                )
                if (payMethod != null) {
                    Text(text = payMethod.cardholderName, fontWeight = FontWeight.Bold, color = Color.Black)
                } else {
                    Text(text = "No hay Info")
                }
                Spacer(modifier = Modifier.weight(1f))
                onPaymentMethod()
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
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Total Items in Cart:", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(185.dp))
                Text(text = "$totalItemsInCart", maxLines = 1)
            }
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Total Products Count:", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(163.dp))
                Text(text = "$totalProductsCount", maxLines = 1)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Total:",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(256.dp))
                Text(
                    text = String.format("\$%.2f", totalPrice),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1
                )
            }
        }
    }
}
