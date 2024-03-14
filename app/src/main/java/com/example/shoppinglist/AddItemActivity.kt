@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.shoppinglist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.shoppinglist.data.items.Item
import com.example.shoppinglist.data.items.ItemDBViewModel
import com.example.shoppinglist.data.PreferencesViewModel
import com.example.shoppinglist.ui.theme.ShoppingListTheme

class AddItemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                val dbViewModel = ItemDBViewModel(application)
                val viewModel = PreferencesViewModel.getItemViewModel(application)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AddListPanel(
                        dbvm = dbViewModel,
                        vm = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun AddListPanel(modifier: Modifier = Modifier, dbvm: ItemDBViewModel, vm: PreferencesViewModel){
    var inputTextName by remember { mutableStateOf("Default") }
    var inputTextNumber by remember { mutableStateOf("1") }
    var inputTextPrice by remember { mutableStateOf("1.00") }
    val context = LocalContext.current
    val c by vm.colorFlow.collectAsState(initial = "gray")
    val f by vm.fontSizeFlow.collectAsState(initial = "14")

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Utwórz nowy produkt",
            modifier = modifier
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        TextField(
            value = inputTextName,
            onValueChange = {
                inputTextName = it
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        TextField(
            value = inputTextNumber,
            onValueChange = {
                inputTextNumber = it
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        TextField(
            value = inputTextPrice,
            onValueChange = {
                inputTextPrice = it
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        ActionButton(onClick = {
            dbvm.insertItem(
                Item(
                    name = inputTextName, number = inputTextNumber.toInt(),
                    price = inputTextPrice.toFloat(), bought = false
                )
            )
            val intentProductListActivity = Intent(context, ProductListActivity::class.java)
            context.startActivity(intentProductListActivity)
        }, text = "Dodaj produkt")
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        NavButton(text = "Anuluj", context = context, cls = ProductListActivity::class.java)
    }
}