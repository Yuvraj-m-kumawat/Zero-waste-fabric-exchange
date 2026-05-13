package com.example.kutirakone.ui.screens

import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kutirakone.R
import com.example.kutirakone.model.Scrap
import com.example.kutirakone.ui.components.ScrapCardHorizontal
import com.example.kutirakone.viewmodel.ScrapViewModel
import com.google.android.gms.location.LocationServices
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingsScreen(
    viewModel: ScrapViewModel,
    filterType: String = "all",
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scraps by viewModel.scraps.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var isMapView by remember { mutableStateOf(false) }
    
    // Request Dialog State
    var showRequestDialog by remember { mutableStateOf(false) }
    var selectedScrapForRequest by remember { mutableStateOf<Scrap?>(null) }
    
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var userLocation by remember { mutableStateOf<Location?>(null) }

    LaunchedEffect(Unit) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) userLocation = location
            }
        } catch (e: SecurityException) {
            android.util.Log.e("ListingsScreen", "Location permission denied")
        }
    }

    val categories = listOf("All", "Cotton", "Silk", "Wool", "Denim", "Linen", "Velvet")

    val filteredScraps = scraps.map { scrap ->
        var displayDist = scrap.distance
        if (userLocation != null && scrap.latitude != 0.0) {
            val results = FloatArray(1)
            Location.distanceBetween(
                userLocation!!.latitude, userLocation!!.longitude,
                scrap.latitude, scrap.longitude,
                results
            )
            val km = results[0] / 1000
            displayDist = String.format(Locale.US, "%.1f km", km)
        } else if (displayDist.isEmpty()) {
            displayDist = "Unknown dist"
        }
        scrap.copy(distance = displayDist)
    }.filter { scrap ->
        val matchesSearch = scrap.material.contains(searchQuery, ignoreCase = true) || 
                          scrap.description.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "All" || scrap.material.contains(selectedCategory, ignoreCase = true)
        
        val matchesFilter = if (filterType == "nearby") {
            val distValue = scrap.distance.filter { it.isDigit() || it == '.' }.toDoubleOrNull() ?: 99.0
            distValue < 50.0 
        } else true
        
        matchesSearch && matchesCategory && matchesFilter
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (filterType == "nearby") "Nearby Fabric Exchange" else "All Fabric Listings", 
                        fontWeight = FontWeight.Bold 
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { isMapView = !isMapView }) {
                        Icon(
                            imageVector = if (isMapView) Icons.Default.ViewModule else Icons.Default.Map,
                            contentDescription = "Toggle View",
                            tint = Color(0xFF2D5A27)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        if (showRequestDialog && selectedScrapForRequest != null) {
            AlertDialog(
                onDismissRequest = { showRequestDialog = false },
                title = { Text("Exchange Request") },
                text = { Text("Would you like to Buy this ${selectedScrapForRequest?.material} or propose a Swap?") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.sendRequest(selectedScrapForRequest!!, "Buy") { success ->
                                if (success) {
                                    Toast.makeText(context, "Buy request sent!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Failed to send request. Check internet.", Toast.LENGTH_SHORT).show()
                                }
                                showRequestDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D5A27))
                    ) {
                        Text("BUY")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            viewModel.sendRequest(selectedScrapForRequest!!, "Swap") { success ->
                                if (success) {
                                    Toast.makeText(context, "Swap request proposed!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Failed to send request.", Toast.LENGTH_SHORT).show()
                                }
                                showRequestDialog = false
                            }
                        }
                    ) {
                        Text("SWAP", color = Color(0xFF2D5A27))
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAF8))
        ) {
            if (isMapView) {
                // Mock Map View
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = Color(0xFFE8F5E9),
                            modifier = Modifier.size(120.dp)
                        ) {
                            Icon(Icons.Default.LocationOn, null, modifier = Modifier.padding(30.dp), tint = Color(0xFF2D5A27))
                        }
                        Text("Hyper-Local Map View", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
                        Text("Found ${filteredScraps.size} listings in your area", color = Color.Gray)
                        Button(onClick = { isMapView = false }, modifier = Modifier.padding(top = 20.dp)) {
                            Text("Show as List")
                        }
                    }
                }
            } else {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    placeholder = { Text("Search by material or description...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White),
                    singleLine = true
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2D5A27),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                if (isLoading && filteredScraps.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF2D5A27))
                    }
                } else if (filteredScraps.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No listings found matching your criteria", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredScraps) { scrap ->
                            ScrapCardHorizontal(
                                scrap = scrap,
                                onClick = {
                                    selectedScrapForRequest = scrap
                                    showRequestDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
