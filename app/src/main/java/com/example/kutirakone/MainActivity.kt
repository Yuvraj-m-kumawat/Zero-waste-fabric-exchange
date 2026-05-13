package com.example.kutirakone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.kutirakone.ui.screens.HomeScreen
import com.example.kutirakone.viewmodel.ScrapViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: ScrapViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            // Permissions granted
        } else {
            Toast.makeText(this, "Location permission needed for nearby fabrics", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val userPhone = intent.getStringExtra("USER_PHONE") ?: "User_Demo"
        viewModel.setUserId(userPhone)
        
        checkPermissions()
        
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(
                        viewModel = viewModel,
                        onUploadClick = {
                            startActivity(Intent(this, UploadFabricActivity::class.java))
                        },
                        onBrowseClick = {
                            val intent = Intent(this, ListingsActivity::class.java)
                            intent.putExtra("filter", "all")
                            startActivity(intent)
                        },
                        onNearbyClick = {
                            val intent = Intent(this, ListingsActivity::class.java)
                            intent.putExtra("filter", "nearby")
                            startActivity(intent)
                        },
                        onRequestsClick = {
                            startActivity(Intent(this, RequestsActivity::class.java))
                        },
                        onAiClick = {
                            startActivity(Intent(this, AiSuggestActivity::class.java))
                        }
                    )
                }
            }
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }
}
