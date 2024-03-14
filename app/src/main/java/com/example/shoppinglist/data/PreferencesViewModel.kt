package com.example.shoppinglist.data

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PreferencesViewModel(private val application: Application) : AndroidViewModel(application) {
    private val android.content.Context.dataStore by preferencesDataStore("appPref")

    companion object {
        private var instance: PreferencesViewModel? = null

        fun getItemViewModel(application: Application): PreferencesViewModel {
            if(instance !=null){
                return instance as PreferencesViewModel
            }
            instance = PreferencesViewModel(application = application)
            return instance as PreferencesViewModel
        }

    }

    private val colorKey = stringPreferencesKey("color")
    private val fontSizeKey = stringPreferencesKey("fontSize")

    val colorFlow: Flow<String> = application.dataStore.data.map {
        it[colorKey] ?: ""
    }

    val fontSizeFlow: Flow<String> = application.dataStore.data.map {
        it[fontSizeKey] ?: ""
    }

    fun saveColorToPreferences(color: String){
        viewModelScope.launch {
            application.dataStore.edit {
                it[colorKey] = color
            }
        }
    }

    fun saveFontSizeToPreferences(fontSize: String){
        viewModelScope.launch {
            application.dataStore.edit {
                it[fontSizeKey] = fontSize
            }
        }
    }

}