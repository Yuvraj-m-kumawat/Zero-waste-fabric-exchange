package com.example.kutirakone

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.kutirakone.ui.screens.UploadFabricScreen
import com.example.kutirakone.viewmodel.ScrapViewModel

class UploadFabricActivity : AppCompatActivity() {

    private val viewModel: ScrapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            UploadFabricScreen(
                viewModel = viewModel,
                onBackClick = { finish() },
                onUploadSuccess = {
                    Toast.makeText(this, "Listing Uploaded Successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            )
        }
    }
}