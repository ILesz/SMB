package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglist.data.items.Item
import com.example.shoppinglist.data.items.ItemDBViewModel
import com.example.shoppinglist.data.PreferencesViewModel
import com.example.shoppinglist.ui.theme.ShoppingListTheme

class ProductListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                val viewModel = PreferencesViewModel.getItemViewModel(application)
                val dbViewModel = ItemDBViewModel(application)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ListModificationScreen(
                        vm = viewModel,
                        dbvm = dbViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun ListModificationScreen(modifier: Modifier = Modifier, vm: PreferencesViewModel, dbvm: ItemDBViewModel){
    var context = LocalContext.current
    //val listItems by dbvm.items.collectAsState(emptyList())
    val listItems by dbvm.items.collectAsState(emptyMap<Long, Item>())

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        NavButton(text = "Wstecz do ekranu głównego", context = context, cls = MainActivity::class.java)
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        NavButton(text = "Dodaj produkt", context = context, cls = AddItemActivity::class.java)
        Spacer(modifier = Modifier.requiredHeight(10.dp))
        LazyColumn(
            modifier = Modifier.requiredHeight(400.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(listItems.toList()) { item ->
                Row(){
                    var checkedStateElement by remember { mutableStateOf(true) }
                    checkedStateElement = item.second.bought
                    Checkbox(
                        checked = checkedStateElement,
                        onCheckedChange = {
                            checkedStateElement = it
                            item.second.bought = it
                            dbvm.updateItem(item.second)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MyElements.color,
                            uncheckedColor = Color.White,
                            checkmarkColor = Color.White
                    ))
                    TextLabel(text = "${item.second.name} x${item.second.number} %.2f PLN ".format(item.second.price))
                    DeleteButton(onClick = {
                            dbvm.deleteItem(item.second)
                        },
                        text = "X")
                }
            }
        }
    }
}