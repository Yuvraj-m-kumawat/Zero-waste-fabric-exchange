package com.example.kutirakone.ui.screens

import android.content.Context
import android.location.Location
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kutirakone.R
import com.example.kutirakone.model.Scrap
import com.example.kutirakone.viewmodel.ScrapViewModel
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadFabricScreen(
    viewModel: ScrapViewModel,
    onBackClick: () -> Unit,
    onUploadSuccess: () -> Unit
) {
    val context = LocalContext.current
    var materialType by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("meter") }
    var condition by remember { mutableStateOf("New") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    
    val isLoading by viewModel.isLoading.collectAsState()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) imageUri = uri
    }

    var expandedMaterial by remember { mutableStateOf(false) }
    var expandedUnit by remember { mutableStateOf(false) }
    var expandedCondition by remember { mutableStateOf(false) }

    val materials = listOf("Cotton", "Denim", "Silk", "Wool", "Polyester", "Linen", "Velvet", "Chiffon")
    val units = listOf("meters", "kilograms", "pieces", "yards")
    val conditions = listOf("New", "Excellent", "Good", "Fair", "Used")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Fabric Listing", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Fabric Images", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable { launcher.launch("image/*") },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEEEEE))
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(48.dp))
                            Text("Tap to select image", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Material Type
            ExposedDropdownMenuBox(
                expanded = expandedMaterial,
                onExpandedChange = { expandedMaterial = !expandedMaterial }
            ) {
                OutlinedTextField(
                    value = materialType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Material Type *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMaterial) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedMaterial,
                    onDismissRequest = { expandedMaterial = false }
                ) {
                    materials.forEach { material ->
                        DropdownMenuItem(
                            text = { Text(material) },
                            onClick = {
                                materialType = material
                                expandedMaterial = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = size,
                    onValueChange = { if (it.all { c -> c.isDigit() }) size = it },
                    label = { Text("Size/Qty") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                    placeholder = { Text("Numbers only") }
                )
                Spacer(modifier = Modifier.width(12.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedUnit,
                    onExpandedChange = { expandedUnit = !expandedUnit },
                    modifier = Modifier.width(140.dp)
                ) {
                    OutlinedTextField(
                        value = unit,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Unit") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUnit) },
                        modifier = Modifier.menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedUnit,
                        onDismissRequest = { expandedUnit = false }
                    ) {
                        units.forEach { u ->
                            DropdownMenuItem(
                                text = { Text(u) },
                                onClick = {
                                    unit = u
                                    expandedUnit = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { if (it.isEmpty() || it.all { c -> c.isDigit() || c == '.' }) price = it },
                label = { Text("Price (₹) *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                prefix = { Text("₹") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            fun startUpload(lat: Double, lon: Double) {
                val scrap = Scrap(
                    material = materialType,
                    price = price,
                    unit = unit,
                    size = size,
                    condition = condition,
                    description = description,
                    userName = "Demo User",
                    timestamp = System.currentTimeMillis(),
                    latitude = lat,
                    longitude = lon
                )
                viewModel.uploadScrap(scrap, imageUri) { success ->
                    if (success) {
                        onUploadSuccess()
                    } else {
                        Toast.makeText(context, "Upload Failed! Please check: \n1. Internet Connection\n2. Firebase Rules (allow read, write)\n3. Storage enabled in Console", Toast.LENGTH_LONG).show()
                    }
                }
            }

            Button(
                onClick = {
                    if (imageUri == null || materialType.isEmpty() || price.isEmpty()) {
                        Toast.makeText(context, "Please fill all required fields (*)", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    try {
                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            startUpload(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
                        }.addOnFailureListener {
                            startUpload(0.0, 0.0)
                        }
                    } catch (e: SecurityException) {
                        startUpload(0.0, 0.0)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D5A27)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("UPLOAD NOW", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
