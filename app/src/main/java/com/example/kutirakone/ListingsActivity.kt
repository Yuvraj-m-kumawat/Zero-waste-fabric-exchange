package com.example.kutirakone

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.kutirakone.ui.screens.ListingsScreen
import com.example.kutirakone.viewmodel.ScrapViewModel

class ListingsActivity : AppCompatActivity() {

    private val viewModel: ScrapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val filter = intent.getStringExtra("filter") ?: "all"

        setContent {
            ListingsScreen(
                viewModel = viewModel,
                filterType = filter,
                onBackClick = { finish() }
            )
        }
    }
}
