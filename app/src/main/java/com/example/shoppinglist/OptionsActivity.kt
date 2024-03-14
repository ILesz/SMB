package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.data.PreferencesViewModel
import com.example.shoppinglist.ui.theme.ShoppingListTheme

class OptionsActivity :ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = PreferencesViewModel.getItemViewModel(application)
            ShoppingListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AccountScreen(vm = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(modifier: Modifier = Modifier, vm: PreferencesViewModel){
    val context = LocalContext.current
    val f = MyElements.fontSize
    val c by vm.colorFlow.collectAsState(initial = "gray")
    //val f by vm.fontSizeFlow.collectAsState(initial = "14")
    var inputColor by remember { mutableStateOf(c) }
    var inputFontSize by remember { mutableStateOf("14") }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        NavButton(text = "Wstecz do ekranu głównego", context = context, cls = MainActivity::class.java)
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        Text(
            text = "Wpisz kolor motywu ($c)",
            modifier = modifier,
            fontSize = f
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        TextField(value = inputColor, onValueChange = {inputColor = it})
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        TextField(value = inputFontSize, onValueChange = {inputFontSize = it})
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        ActionButton(onClick = {
            vm.saveColorToPreferences(inputColor)
            vm.saveFontSizeToPreferences(inputFontSize)
        }, text = "Zmień motyw")
    }
}