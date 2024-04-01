package com.ucne.bodybuilderstore.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor()
    :ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun signInWithEmailAndPssword(email: String, password: String, onLoginSuccess: () -> Unit)
    = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {task->
                    if (task.isSuccessful){
                        Log.d("BodyBuilder", "signInWithEmailAndPssword logueando!!")
                            onLoginSuccess()
                    }else{
                        Log.d("BodyBuilder", "signInWithEmailAndPssword: ${task.result.toString()}")
                    }
                }
        }
        catch (ex:Exception){
            Log.d("BodyBuilder", "signInWithEmailAndPssword: ${ex.message}")
        }
    }
}
