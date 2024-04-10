package com.ucne.bodybuilderstore.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.ucne.bodybuilderstore.login.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    //Admin User: admin@gmail.com-@Dmin123
    fun signInWithEmailAndPssword(
        email: String,
        password: String,
        onLoginSuccess: () -> Unit,
        onLoginFailed: (String) -> Unit
    ) = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("BodyBuilder", "signInWithEmailAndPssword logueando!!")
                        onLoginSuccess()
                    } else {
                        Log.d("BodyBuilder", "signInWithEmailAndPssword: ${task.exception}")
                        onLoginFailed("Credenciales incorrectas. Por favor, inténtalo de nuevo.")
                    }
                }
        } catch (ex: Exception) {
            Log.d("BodyBuilder", "signInWithEmailAndPssword: ${ex.message}")
            onLoginFailed("Ha ocurrido un error. Por favor, inténtalo de nuevo.")
        }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onCreateSuccess: () -> Unit,
        onCreateFailed: (String) -> Unit
    ) {
        if (!isEmailValid(email)) {
            onCreateFailed("El formato de correo electrónico no es válido.")
            return
        }

        if (!isPasswordValid(password)) {
            onCreateFailed("La contraseña no cumple con los requisitos de seguridad.")
            return
        }

        if (_loading.value == false) {
            _loading.value == true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName =
                            task.result.user?.email?.split("@")?.get(0)
                        createUser(displayName)
                        onCreateSuccess()
                    } else {
                        Log.d(
                            "BodyBuilder",
                            "createUserWithEmailAndPassword: ${task.exception}"
                        )
                        val errorMessage = when(task.exception?.message) {
                            "The email address is already in use by another account." ->
                                "La dirección de correo electrónico ya está en uso."
                            else ->
                                "Error al crear la cuenta. Por favor, inténtalo de nuevo."
                        }
                        onCreateFailed(errorMessage)
                    }
                    _loading.value == false
                }
        }
    }


    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid

        val user = User(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            id = null
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("BodyBuilder", "Creado ${it.id}")
            }.addOnFailureListener{
                Log.d("BodyBuilder", "Error! ${it}")
            }
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        val minLength = 6
        if (password.length < minLength) {
            return false
        }

        val specialCharacters = setOf('!', '@', '#', '$', '%', '^', '&', '*')
        val containsSpecialCharacter = password.any { it in specialCharacters }
        if (!containsSpecialCharacter) {
            return false
        }

        val containsUpperCase = password.any { it.isUpperCase() }
        val containsLowerCase = password.any { it.isLowerCase() }
        if (!containsUpperCase || !containsLowerCase) {
            return false
        }

        return true
    }
}
