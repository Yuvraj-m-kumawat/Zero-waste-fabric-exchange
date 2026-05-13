package com.example.kutirakone.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.kutirakone.R

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }
    var isVerifying by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = "KUTIRA-KONE",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D5A27),
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Zero Waste Fabric Exchange",
            fontSize = 12.sp,
            color = Color(0xFF4CAF50)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "ENTER YOUR PHONE NUMBER",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            color = Color(0xFF333333)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .height(56.dp)
                    .width(70.dp),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDDDDDD)),
                color = Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("+91", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { if (it.length <= 10 && it.all { c -> c.isDigit() }) phoneNumber = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Mobile Number (10 digits)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(8.dp),
                enabled = !isOtpSent && !isVerifying,
                singleLine = true
            )
        }

        Button(
            onClick = { 
                isVerifying = true
                scope.launch {
                    kotlinx.coroutines.delay(1000)
                    isOtpSent = true 
                    isVerifying = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D5A27)),
            enabled = phoneNumber.length == 10 && !isVerifying && !isOtpSent
        ) {
            if (isVerifying && !isOtpSent) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text("SEND OTP")
        }

        if (isOtpSent) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "ENTER 6-DIGIT OTP",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = Color(0xFF333333)
            )

            OutlinedTextField(
                value = otp,
                onValueChange = { if (it.length <= 6 && it.all { c -> c.isDigit() }) otp = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                placeholder = { Text("123456") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(8.dp),
                enabled = !isVerifying,
                singleLine = true
            )

            Button(
                onClick = {
                    isVerifying = true
                    scope.launch {
                        kotlinx.coroutines.delay(1000)
                        onLoginSuccess(phoneNumber)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D5A27)),
                enabled = otp.length == 6 && !isVerifying
            ) {
                if (isVerifying) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else {
                    Icon(Icons.Default.Lock, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("VERIFY & LOGIN")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "By continuing, you agree to our Terms and Conditions",
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF777777),
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}
