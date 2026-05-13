package com.example.kutirakone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kutirakone.R
import com.example.kutirakone.model.Request
import com.example.kutirakone.ui.components.RequestCard
import com.example.kutirakone.viewmodel.ScrapViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsScreen(
    viewModel: ScrapViewModel,
    onBackClick: () -> Unit
) {
    val mySentRequests by viewModel.mySentRequests.collectAsState()
    val receivedRequests by viewModel.receivedRequests.collectAsState()
    
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Sent Requests", "Received Interest")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.my_requests), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
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
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color(0xFF2D5A27),
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFF2D5A27)
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            val displayList = if (selectedTab == 0) mySentRequests else receivedRequests

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (displayList.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                            Text(
                                if (selectedTab == 0) "You haven't sent any requests yet." 
                                else "No one has requested your fabric yet.",
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    items(displayList) { request ->
                        if (selectedTab == 1) {
                            ReceivedRequestCard(
                                request = request,
                                onAccept = { viewModel.updateRequestStatus(request.id, "Accepted") },
                                onDecline = { viewModel.updateRequestStatus(request.id, "Declined") }
                            )
                        } else {
                            RequestCard(request = request)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReceivedRequestCard(
    request: Request,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(request.scrapId, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF2D5A27))
                Surface(
                    color = when(request.status) {
                        "Accepted" -> Color(0xFFE8F5E9)
                        "Declined" -> Color(0xFFFFEBEE)
                        else -> Color(0xFFFFF3E0)
                    },
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                ) {
                    Text(
                        request.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = when(request.status) {
                            "Accepted" -> Color(0xFF388E3C)
                            "Declined" -> Color(0xFFD32F2F)
                            else -> Color(0xFFF57C00)
                        }
                    )
                }
            }
            
            Text("From: ${request.requesterId}", fontSize = 14.sp, color = Color.Gray)
            Text("Type: ${request.type}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            
            if (request.status == "Pending") {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onAccept,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D5A27)),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Accept")
                    }
                    OutlinedButton(
                        onClick = onDecline,
                        modifier = Modifier.weight(1f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Decline")
                    }
                }
            }
        }
    }
}
