package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.data.PreferencesViewModel
import com.example.shoppinglist.data.shops.Shop
import com.example.shoppinglist.data.shops.ShopDBViewModel
import com.example.shoppinglist.ui.theme.ShoppingListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                // A surface container using the 'background' color from the theme
                /*
                val dbViewModel = ShopDBViewModel(application)
                val allShops by dbViewModel.shops.collectAsState(emptyMap<String, Shop>())
                ShoppingAppWidget.allShops = allShops.toList()*/
                val viewModel = PreferencesViewModel.getItemViewModel(application)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainMenu(vm = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainMenu(modifier: Modifier = Modifier, vm: PreferencesViewModel){
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Spacer(modifier = Modifier.requiredHeight(15.dp))
        NavButton(text = "Lista zakupów", context = context, cls = ProductListActivity::class.java)
        Spacer(modifier = Modifier.requiredHeight(15.dp))
        NavButton(text = "Ustawienia", context = context, cls = OptionsActivity::class.java)
        Spacer(modifier = Modifier.requiredHeight(15.dp))
        NavButton(text = "Zaloguj", context = context, cls = LoginActivity::class.java)
        Spacer(modifier = Modifier.requiredHeight(15.dp))
        NavButton(text = "Ulubione sklepy", context = context, cls = FavShopsActivity::class.java)
    }


}

