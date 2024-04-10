package com.ucne.bodybuilderstore.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ucne.bodybuilderstore.R

@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val errorMessage = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.background(Color.LightGray)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(bottomEnd = 32.dp, bottomStart = 32.dp))
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable._2646230),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
        }

        Surface(
            color = Color.LightGray,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Column {
                    Text(
                        text = "Registrarte",
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp
                    )
                    Text(
                        text = "Es rápido y fácil.",
                        fontWeight = FontWeight.Light,
                        fontSize = 20.sp
                    )
                }
                UserForm(isCreateAccount = true) { email, password ->
                    Log.d("BuildingMaster", "Creando cuenta con $email y $password")
                    viewModel.createUserWithEmailAndPassword(email, password, {
                        navController.navigate("suplemento")
                    }, { error ->
                        errorMessage.value = error
                    })
                }

                if (errorMessage.value.isNotBlank()) {
                    Text(
                        text = errorMessage.value,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color.Red
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Ya tienes cuenta?")
                    Text(
                        text = "Iniciar sesión",
                        modifier = Modifier
                            .clickable { navController.navigate("login") }
                            .padding(start = 5.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
