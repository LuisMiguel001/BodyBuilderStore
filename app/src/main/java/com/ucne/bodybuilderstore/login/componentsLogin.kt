package com.ucne.bodybuilderstore.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserForm(
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { email, pwd -> }
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val valido = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() &&
                password.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailInput(
            emailState = email
        )
        PasswordInput(
            passwordState = password,
            labelId = "Password",
            passwordVisible = passwordVisible
        )
        SubmitButton(
            textId = if (isCreateAccount) "Crear cuenta" else "Login",
            inputValido = valido
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    inputValido: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        elevation = ButtonDefaults.elevatedButtonElevation(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp),
        shape = RoundedCornerShape(8.dp),
        enabled = inputValido
    ) {
        Text(
            text = textId,
            color = MaterialTheme.colorScheme.inverseSurface,
            modifier = Modifier
                .padding(5.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisible: MutableState<Boolean>
) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = labelId, color = MaterialTheme.colorScheme.onSecondary) },
        singleLine = true,
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            cursorColor = MaterialTheme.colorScheme.onSecondary,
            unfocusedTextColor = MaterialTheme.colorScheme.onSecondary,
            focusedTextColor = MaterialTheme.colorScheme.onSecondary
        ),
        trailingIcon = {
            if (passwordState.value.isNotBlank()) {
                PasswoedVsibleIcon(passwordVisible)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
    )
}

@Composable
fun PasswoedVsibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    val image =
        if (passwordVisible.value)
            Icons.Default.Visibility
        else
            Icons.Default.VisibilityOff
    IconButton(onClick = {
        passwordVisible.value = !passwordVisible.value
    }) {
        Icon(
            imageVector = image,
            contentDescription = ""
        )
    }
}

@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId: String = "Email"
) {
    InputFieLd(
        valueState = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputFieLd(
    valueState: MutableState<String>,
    labelId: String,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId, color = MaterialTheme.colorScheme.onSecondary) },
        singleLine = isSingleLine,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            cursorColor = MaterialTheme.colorScheme.onSecondary,
            unfocusedTextColor = MaterialTheme.colorScheme.onSecondary,
            focusedTextColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )
}
