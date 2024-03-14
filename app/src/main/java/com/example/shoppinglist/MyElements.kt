package com.example.shoppinglist

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglist.data.PreferencesViewModel
import com.example.shoppinglist.data.shops.Shop

class MyElements(private val viewModel: PreferencesViewModel) {
    val vm = viewModel

    companion object {
        private lateinit var instance: MyElements
        fun getInstance(): MyElements {
            return instance
        }

        val colorMap = mapOf(
            "gray" to Color.DarkGray,
            "green" to Color.Green,
            "red" to Color.Red,
            "blue" to Color.Blue
        )
        var color: Color = Color.DarkGray
        var fontSize: TextUnit = "14".toInt().sp
    }

    init {
        if (instance == null)
            instance = this
    }
}

@Composable
fun RefreshStyle() {
    val color by MyElements.getInstance().vm.colorFlow.collectAsState(initial = "gray")
    val fontSize by MyElements.getInstance().vm.fontSizeFlow.collectAsState(initial = "14")
    var c = MyElements.colorMap[color]
    if (c != null)
        MyElements.color = c
    if (fontSize != null && !fontSize.isEmpty())
        MyElements.fontSize = fontSize.toInt().sp
}

@Composable
fun NavButton(text: String, context: Context, cls: Class<*>) {
    ActionButton(onClick = {
        val intent = Intent(context, cls)
        context.startActivity(intent)
    }, text = text)
}

@Composable
fun ActionButton(onClick: () -> Unit, text: String) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .requiredWidth(300.dp)
            .requiredHeight(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MyElements.color,
            contentColor = Color.White
        )
    ) {
        Text(text = text, fontSize = MyElements.fontSize)
    }
}

@Composable
fun DeleteButton(onClick: () -> Unit, text: String) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MyElements.color,
            contentColor = Color.White
        )
    )
    {
        Text(text = text, fontSize = MyElements.fontSize)
    }
}

@Composable
fun TextLabel(text: String) {
    Text(text = text, fontSize = MyElements.fontSize)
}
