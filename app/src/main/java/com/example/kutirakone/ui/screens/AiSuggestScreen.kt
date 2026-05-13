package com.example.kutirakone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kutirakone.R
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiSuggestScreen(
    apiKey: String,
    onBackClick: () -> Unit
) {
    val fallbackSuggestions = listOf(
        "Denim Tote Bag" to "Upcycle your old denim scraps into a durable and stylish tote bag! Perfect for carrying groceries or books.",
        "Cotton Face Masks" to "Create reusable and breathable face masks for your family using clean cotton scraps.",
        "Patchwork Silk Scarf" to "Patch together small silk pieces into a vibrant, multi-colored scarf.",
        "Braided Fabric Rug" to "Braid thick cotton strips from old clothes to make a soft, colorful rug for your home.",
        "Wall Hanging Art" to "Create beautiful wall art using different fabric textures and colors.",
        "Eco-friendly Pot Covers" to "Wrap your plant pots in colorful fabric scraps to give them a fresh, rustic look.",
        "Quilted Cushion Covers" to "Use small pieces of various fabrics to sew a patchwork quilted cushion cover.",
        "Fabric Bunting" to "Cut triangles from colorful scraps and sew them to a string for festive home decorations.",
        "Stuffed Toy Stuffing" to "Shred very small scraps into tiny pieces to use as eco-friendly stuffing for toys or pillows.",
        "Cleaning Rags" to "Cut old cotton T-shirt scraps into squares for high-quality, reusable cleaning cloths."
    )
    
    // Always start with 5 random fallback suggestions
    var suggestionsList by remember { mutableStateOf(fallbackSuggestions.shuffled().take(5)) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun generateNewSuggestions() {
        isLoading = true
        
        // If API key is not valid, just reshuffle fallback
        if (apiKey == "YOUR_GEMINI_API_KEY" || apiKey.isEmpty()) {
            scope.launch {
                kotlinx.coroutines.delay(1000)
                suggestionsList = fallbackSuggestions.shuffled().take(5)
                isLoading = false
            }
            return
        }
        
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey
        )

        scope.launch {
            try {
                val prompt = "List 5 unique and practical ways to upcycle fabric scraps specifically for rural Indian households. Format each as 'Title: Description' on a new line."
                val response = generativeModel.generateContent(prompt)
                val text = response.text ?: ""
                val lines = text.split("\n").filter { it.contains(":") }
                
                if (lines.size >= 3) {
                    val newSuggestions = lines.map { line ->
                        val parts = line.split(":", limit = 2)
                        parts[0].trim().removePrefix("- ").removePrefix("1. ").trim() to parts[1].trim()
                    }
                    // Ensure we have exactly 5 or more
                    suggestionsList = if (newSuggestions.size < 5) {
                        (newSuggestions + fallbackSuggestions.shuffled()).take(5)
                    } else {
                        newSuggestions.take(5)
                    }
                } else {
                    suggestionsList = fallbackSuggestions.shuffled().take(5)
                }
            } catch (e: Exception) {
                android.util.Log.e("AiSuggestScreen", "AI Error: ${e.message}")
                suggestionsList = fallbackSuggestions.shuffled().take(5)
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smart Upcycling AI", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAF8))
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFF1976D2))
                    Text(
                        "Showing ${suggestionsList.size} creative ideas to reuse your fabric.",
                        modifier = Modifier.padding(start = 12.dp),
                        color = Color(0xFF1976D2),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFF2D5A27))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Thinking of new ideas...", color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(suggestionsList) { (title, desc) ->
                        SuggestionItem(title, desc)
                    }
                }
            }

            Button(
                onClick = { generateNewSuggestions() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D5A27)),
                enabled = !isLoading
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("GET MORE IDEAS", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SuggestionItem(title: String, desc: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFE8F5E9),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp),
                    tint = Color(0xFF2D5A27)
                )
            }
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF2D5A27))
                Text(desc, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}
