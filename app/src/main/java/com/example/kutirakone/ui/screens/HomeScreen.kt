package com.example.kutirakone.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.kutirakone.R
import com.example.kutirakone.ui.components.ScrapCard
import com.example.kutirakone.viewmodel.ScrapViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ScrapViewModel,
    onUploadClick: () -> Unit,
    onBrowseClick: () -> Unit,
    onNearbyClick: () -> Unit,
    onRequestsClick: () -> Unit,
    onAiClick: () -> Unit
) {
    val scraps by viewModel.scraps.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            stringResource(R.string.app_title),
                            color = Color(0xFF2D5A27),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                },
                actions = {
                    var showLanguageMenu by remember { mutableStateOf(false) }
                    
                    IconButton(onClick = { showLanguageMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Change Language",
                            tint = Color(0xFF2D5A27)
                        )
                    }
                    
                    DropdownMenu(
                        expanded = showLanguageMenu,
                        onDismissRequest = { showLanguageMenu = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = { Text("English", color = Color.Black) }, 
                            onClick = { 
                                showLanguageMenu = false 
                                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("en")
                                AppCompatDelegate.setApplicationLocales(appLocale)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("हिंदी (Hindi)", color = Color.Black) }, 
                            onClick = { 
                                showLanguageMenu = false 
                                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("hi")
                                AppCompatDelegate.setApplicationLocales(appLocale)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("ಕನ್ನಡ (Kannada)", color = Color.Black) },
                            onClick = { 
                                showLanguageMenu = false 
                                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("kn")
                                AppCompatDelegate.setApplicationLocales(appLocale)
                            }
                        )
                    }

                    IconButton(onClick = {
                        onRequestsClick()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color(0xFF2D5A27)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAiClick,
                containerColor = Color(0xFF2D5A27),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = stringResource(R.string.ai_suggest_desc))
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAF8)),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Banner
            item(span = { GridItemSpan(2) }) {
                BannerCard()
            }

            item(span = { GridItemSpan(2) }) {
                Text(
                    stringResource(R.string.what_do),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color(0xFF333333)
                )
            }

            // Action Grid
            item { ActionCard(stringResource(R.string.upload_fabric), stringResource(R.string.add_your_fabric), Icons.Default.CloudUpload, onUploadClick) }
            item { ActionCard(stringResource(R.string.browse_listings), stringResource(R.string.find_fabrics), Icons.Default.Search, onBrowseClick) }
            item { ActionCard(stringResource(R.string.nearby_fabrics), stringResource(R.string.within_range), Icons.Default.MyLocation, onNearbyClick) }
            item { ActionCard(stringResource(R.string.my_requests), stringResource(R.string.buy_or_swap), Icons.Default.ListAlt, onRequestsClick) }

            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.recent_listings), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(
                        stringResource(R.string.see_all),
                        color = Color(0xFF4CAF50),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onBrowseClick() }
                    )
                }
            }

            items(scraps.take(10)) { scrap ->
                ScrapCard(scrap = scrap, onClick = {})
            }
            
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun BannerCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.share_fabric),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2D5A27)
                )
                Text(
                    stringResource(R.string.save_planet),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2D5A27)
                )
                Text(
                    stringResource(R.string.reduce_waste),
                    fontSize = 12.sp,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.Medium
                )
            }
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(80.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun ActionCard(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(32.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
            Text(subtitle, fontSize = 10.sp, color = Color(0xFF777777))
        }
    }
}