package com.example.kutirakone

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.kutirakone.ui.screens.LoginScreen
import com.example.kutirakone.viewmodel.ScrapViewModel

class LoginActivity : AppCompatActivity() {
    
    // Use activity viewModels to share the same instance if needed, 
    // but here we just need to set the ID before moving to MainActivity
    private val viewModel: ScrapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(
                        onLoginSuccess = { phone ->
                            // We don't really use the phone number yet, but in a real app 
                            // we would pass it to the ViewModel or SharedPreferences
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("USER_PHONE", phone)
                            startActivity(intent)
                            finish()
                        }
                    )
                }
            }
        }
    }
}
