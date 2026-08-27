package com.livepractice.simulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.livepractice.simulator.ui.theme.*
import com.livepractice.simulator.ui.LiveSimulatorScreen
import com.livepractice.simulator.ui.OnboardingFlow
import com.livepractice.simulator.ui.OnboardingStep5

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Load Ads
        AdManager.loadInterstitial(this)
        AdManager.loadRewarded(this)

        setContent {
            LivePracticeSimulatorTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "onboarding",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("onboarding") {
                            OnboardingFlow(
                                onFinishOnboarding = {
                                    navController.navigate("dashboard") {
                                        popUpTo("onboarding") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("dashboard") {
                            OnboardingStep5(
                                onFinish = { navController.navigate("live") }
                            )
                        }
                        composable("live") {
                            LiveSimulatorScreen(
                                onEndStream = {
                                    if (AdManager.interstitialAd != null) {
                                        AdManager.interstitialAd?.show(this@MainActivity)
                                    }
                                    navController.popBackStack()
                                },
                                onBoostStream = {
                                    if (AdManager.rewardedAd != null) {
                                        AdManager.rewardedAd?.show(this@MainActivity) { reward ->
                                            // Handle reward
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(onStartLive: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SophisticatedDarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LiveTv,
                contentDescription = null,
                tint = SophisticatedPrimary,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Live Practice Simulator",
                color = SophisticatedText,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "محاكاة البث المباشر المتقدمة مع تفاعل الجمهور والإعلانات الذكية ومساعد الذكاء الاصطناعي",
                color = SophisticatedTextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onStartLive,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SophisticatedPrimary,
                    contentColor = SophisticatedOnPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "بدء محاكاة البث المباشر الآن",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

