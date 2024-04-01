package com.ucne.bodybuilderstore.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
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

    fun signInWithEmailAndPssword(email: String, password: String, onLoginSuccess: () -> Unit) =
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("BodyBuilder", "signInWithEmailAndPssword logueando!!")
                            onLoginSuccess()
                        } else {
                            Log.d(
                                "BodyBuilder",
                                "signInWithEmailAndPssword: ${task.result.toString()}"
                            )
                        }
                    }
            } catch (ex: Exception) {
                Log.d("BodyBuilder", "signInWithEmailAndPssword: ${ex.message}")
            }
        }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onCreateSucces: () -> Unit
    ) {
        if (_loading.value == false) {
            _loading.value == true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName =
                            task.result.user?.email?.split("@")?.get(0)
                        createUser(displayName)
                        onCreateSucces()
                    } else {
                        Log.d(
                            "BodyBuilder",
                            "createWithEmailAndPssword: ${task.result.toString()}}"
                        )
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
}
