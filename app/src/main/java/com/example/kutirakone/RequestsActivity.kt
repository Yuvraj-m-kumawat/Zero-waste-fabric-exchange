package com.example.kutirakone

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.kutirakone.ui.screens.RequestsScreen
import com.example.kutirakone.viewmodel.ScrapViewModel

class RequestsActivity : AppCompatActivity() {

    private val viewModel: ScrapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            RequestsScreen(
                viewModel = viewModel,
                onBackClick = { finish() }
            )
        }
    }
}