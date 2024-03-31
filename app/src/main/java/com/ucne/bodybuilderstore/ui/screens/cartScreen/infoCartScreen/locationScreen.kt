package com.ucne.bodybuilderstore.ui.screens.cartScreen.infoCartScreen

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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.bodybuilderstore.data.local.entity.Location
import com.ucne.bodybuilderstore.ui.screens.cartScreen.CartViewModel
import com.ucne.bodybuilderstore.ui.screens.registroScreen.StoreEvent

@Composable
fun LocationForm(
    location: Location,
    onDismiss: () -> Unit,
    viewModel: LocationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val _state = state.location

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
                    text = "Location Form",
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
                value = _state.address,
                onValueChange = { viewModel.onEvent(LocationEvent.Address(it)) },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = _state.city,
                onValueChange = { viewModel.onEvent(LocationEvent.City(it)) },
                label = { Text("Ciudad") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = _state.state,
                onValueChange = { viewModel.onEvent(LocationEvent.State(it)) },
                label = { Text("Estado/Provincia") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = _state.postalCode,
                onValueChange = { viewModel.onEvent(LocationEvent.PostalCode(it)) },
                label = { Text("Código Postal") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = _state.country,
                onValueChange = { viewModel.onEvent(LocationEvent.Country(it)) },
                label = { Text("País") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = _state.gpsCoordinates,
                onValueChange = { viewModel.onEvent(LocationEvent.GpsCoordinates(it)) },
                label = { Text("Coordenadas GPS") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = _state.additionalNotes,
                onValueChange = { viewModel.onEvent(LocationEvent.AdditionalNotes(it)) },
                label = { Text("Referencias adicionales") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {viewModel.onEvent(LocationEvent.onSave)},
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
