package com.ucne.bodybuilderstore.ui.screens.cartScreen.funtionsCartScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.bodybuilderstore.data.local.entity.PaymentMethod


@Composable
fun PaymentMethodForm(
    initialPaymentMethod: PaymentMethod,
    onDismiss: () -> Unit,
    viewModel: PaymentMethodViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val _state = state.paymentMethod

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        Column(
            modifier = Modifier
                .clip(ShapeDefaults.Small)
                .background(Color.White)
                .clip(MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Info Credit Card",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp
                )
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp),
                    shape = MaterialTheme.shapes.small,
                    contentPadding = PaddingValues(0.dp),
                    elevation = null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text("X", fontSize = 24.sp)
                }
            }
            Divider(color = Color.DarkGray, thickness = 1.dp)

            OutlinedTextField(
                value = _state.cardholderName,
                onValueChange = { viewModel.onEvent(PaymentMethodEvent.CardholderName(it)) },
                label = { Text("Cardholder Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = _state.cardNumber,
                onValueChange = { viewModel.onEvent(PaymentMethodEvent.CardNumber(it))
                },
                label = { Text("Card Number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = _state.expirationDate,
                onValueChange = { viewModel.onEvent(PaymentMethodEvent.ExpirationDate(it)) },
                label = { Text("Expiration Date") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = _state.cvv,
                onValueChange = { viewModel.onEvent(PaymentMethodEvent.Cvv(it)) },
                label = { Text("CVV/CVC") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = _state.cardType,
                onValueChange = { viewModel.onEvent(PaymentMethodEvent.CardType(it)) },
                label = { Text("Card Type") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = _state.billingAddress,
                onValueChange = { viewModel.onEvent(PaymentMethodEvent.BillingAddress(it)) },
                label = { Text("Billing Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = _state.postalCode,
                onValueChange = { viewModel.onEvent(PaymentMethodEvent.PostalCode(it))},
                label = { Text("Postal Code") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = _state.email,
                onValueChange = { viewModel.onEvent(PaymentMethodEvent.Email(it))},
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.onEvent(PaymentMethodEvent.Save)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.Blue
                ),
                shape = ShapeDefaults.Small
            ) {
                Row {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Save")
                    Text("Save")
                }
            }
        }
    }
}
