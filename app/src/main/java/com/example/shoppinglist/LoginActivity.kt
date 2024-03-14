package com.example.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.data.PreferencesViewModel
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //FirebaseApp.initializeApp()
        auth = FirebaseAuth.getInstance()
        setContent {
            ShoppingListTheme {
                val viewModel = PreferencesViewModel.getItemViewModel(application)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginPanel(auth, viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginPanel(auth: FirebaseAuth, vm: PreferencesViewModel, modifier: Modifier = Modifier) {
    var loginText by remember { mutableStateOf("") }
    var registerText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val c by vm.colorFlow.collectAsState(initial = "gray")
    val f by vm.fontSizeFlow.collectAsState(initial = "14")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        TextField(
            value = loginText,
            onValueChange = {
                loginText = it
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        TextField(
            value = registerText,
            onValueChange = {
                registerText = it
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        ActionButton(onClick = {
            auth.createUserWithEmailAndPassword(
                loginText,
                registerText
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Rejestracja się powiodła.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Nie udało się zarejestrować.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }, text = "Zarejestruj zaloguj")
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        ActionButton(onClick = {
            auth.signInWithEmailAndPassword(
                loginText,
                registerText
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    context.startActivity(Intent(context, MainActivity::class.java))
                } else {
                    Toast.makeText(
                        context,
                        "Nie udało się zalogować.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }, text = "Zaloguj")
    }
}