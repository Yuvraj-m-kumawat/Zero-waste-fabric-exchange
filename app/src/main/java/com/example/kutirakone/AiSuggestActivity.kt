package com.example.kutirakone

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.kutirakone.ui.screens.AiSuggestScreen

class AiSuggestActivity : AppCompatActivity() {

    private val GEMINI_API_KEY = "YOUR_GEMINI_API_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            AiSuggestScreen(
                apiKey = GEMINI_API_KEY,
                onBackClick = { finish() }
            )
        }
    }
}