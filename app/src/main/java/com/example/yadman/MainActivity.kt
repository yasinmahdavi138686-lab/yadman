package com.example.yadman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yadman.ui.YadmanApp
import com.example.yadman.ui.YadmanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: NotesViewModel = viewModel()
            YadmanTheme(darkTheme = vm.darkMode) {
                YadmanApp(vm)
            }
        }
    }
}
