package com.livepractice.simulator.ui

import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import coil.compose.rememberAsyncImagePainter
import com.livepractice.simulator.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.graphics.ColorMatrix as AndroidColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.os.Build
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.random.Random

data class CameraFilter(
    val id: String,
    val name: String,
    val emoji: String,
    val floatArray: FloatArray,
    val tintColor: Color
)

val cameraFilters = listOf(
    CameraFilter(
        "normal", "Original", "✨",
        floatArrayOf(
            1f, 0f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        ),
        Color.Transparent
    ),
    CameraFilter(
        "sunset", "Warm Sunset", "🌅",
        floatArrayOf(
            1.2f, 0.1f, 0.0f, 0f, 15f,
            0.0f, 1.0f, 0.0f, 0f, 8f,
            0.0f, 0.0f, 0.8f, 0f, -10f,
            0.0f, 0.0f, 0.0f, 1f, 0f
        ),
        Color(0xFFFFB300).copy(alpha = 0.12f)
    ),
    CameraFilter(
        "cyberpunk", "Cyberpunk", "🌆",
        floatArrayOf(
            0.9f, 0.2f, 0.4f, 0f, 20f,
            0.1f, 1.1f, 0.1f, 0f, 0f,
            0.2f, 0.1f, 1.6f, 0f, 30f,
            0.0f, 0.0f, 0.0f, 1f, 0f
        ),
        Color(0xFFE91E63).copy(alpha = 0.12f)
    ),
    CameraFilter(
        "sepia", "Vintage", "🎞️",
        floatArrayOf(
            0.393f, 0.769f, 0.189f, 0f, 0f,
            0.349f, 0.686f, 0.168f, 0f, 0f,
            0.272f, 0.534f, 0.131f, 0f, 0f,
            0.0f, 0.0f, 0.0f, 1f, 0f
        ),
        Color(0xFF795548).copy(alpha = 0.15f)
    ),
    CameraFilter(
        "noir", "Noir", "🎬",
        floatArrayOf(
            0.33f, 0.59f, 0.11f, 0f, 0f,
            0.33f, 0.59f, 0.11f, 0f, 0f,
            0.33f, 0.59f, 0.11f, 0f, 0f,
            0.0f, 0.0f, 0.0f, 1f, 0f
        ),
        Color.DarkGray.copy(alpha = 0.2f)
    ),
    CameraFilter(
        "cinematic", "Cinematic", "🧊",
        floatArrayOf(
            1.0f, 0.0f, 0.0f, 0f, -15f,
            0.0f, 1.1f, 0.1f, 0f, 5f,
            0.1f, 0.1f, 1.4f, 0f, 25f,
            0.0f, 0.0f, 0.0f, 1f, 0f
        ),
        Color(0xFF00BCD4).copy(alpha = 0.12f)
    )
)

data class UserProfile(
    val username: String,
    val isFemale: Boolean,
    val avatarEmoji: String,
    val colors: List<Color>
)

val sampleUserProfiles = listOf(
    UserProfile("sophia_rae", true, "👩🏻", listOf(Color(0xFFFF4081), Color(0xFFE91E63))),
    UserProfile("jake.stream", false, "👨🏼", listOf(Color(0xFF00B0FF), Color(0xFF2979FF))),
    UserProfile("emma_99", true, "👱‍♀️", listOf(Color(0xFFFF80AB), Color(0xFFFF4081))),
    UserProfile("alex_pro", false, "🧑🏽‍💻", listOf(Color(0xFF00E676), Color(0xFF00C853))),
    UserProfile("liam_beats", false, "👨🏽", listOf(Color(0xFF7C4DFF), Color(0xFF651FFF))),
    UserProfile("chloe_x", true, "👧🏼", listOf(Color(0xFFFF6D00), Color(0xFFFF9100))),
    UserProfile("noah_vibe", false, "👦🏻", listOf(Color(0xFF00B0FF), Color(0xFF00E5FF))),
    UserProfile("olivia_star", true, "👩🏼", listOf(Color(0xFFE040FB), Color(0xFFAA00FF))),
    UserProfile("lucas_92", false, "👨🏻", listOf(Color(0xFFFFAB40), Color(0xFFFF6D00))),
    UserProfile("mia_spark", true, "👩🏽", listOf(Color(0xFFFF1744), Color(0xFFD50000))),
    UserProfile("ethan_cool", false, "👨🏾", listOf(Color(0xFF1DE9B6), Color(0xFF00BFA5))),
    UserProfile("ava_rose", true, "👱🏻‍♀️", listOf(Color(0xFFFF80AB), Color(0xFFF50057))),
    UserProfile("mason_fx", false, "🧑🏼", listOf(Color(0xFF40C4FF), Color(0xFF00B0FF))),
    UserProfile("isabella_h", true, "👩🏻‍🰱", listOf(Color(0xFFEA80FC), Color(0xFFD500F9))),
    UserProfile("logan_99", false, "👦🏼", listOf(Color(0xFF69F0AE), Color(0xFF00E676))),
    UserProfile("amelia_art", true, "👩🏼‍🎨", listOf(Color(0xFFFF5252), Color(0xFFFF1744))),
    UserProfile("elijah_m", false, "👨🏼‍🎓", listOf(Color(0xFF448AFF), Color(0xFF2979FF))),
    UserProfile("harper_01", true, "👩🏽‍🎤", listOf(Color(0xFFE040FB), Color(0xFF7C4DFF))),
    UserProfile("james_99", false, "👨🏽‍💼", listOf(Color(0xFFFFD700), Color(0xFFFFAB40))),
    UserProfile("charlotte_k", true, "👩🏻‍⚕️", listOf(Color(0xFF64FFDA), Color(0xFF1DE9B6)))
)

data class ChatMessage(
    val id: Int, 
    val username: String, 
    val message: String, 
    val isGift: Boolean = false, 
    val giftName: String? = null, 
    val avatarEmoji: String = "👤",
    val isFemale: Boolean = false,
    val avatarColors: List<Color> = listOf(Color(0xFFFF4081), Color(0xFFE91E63))
)
data class FloatingHeart(val id: Long, val xOffset: Float, val color: Color)
data class GiftAlert(val id: Long, val username: String, val giftName: String, val giftEmoji: String, val count: Int)
data class ScreenParticle(val id: Long, val emoji: String, val startX: Float, val startY: Float, val targetOffsetX: Float, val targetOffsetY: Float)
data class GiftItem(val name: String, val emoji: String, val coinPrice: Int, val description: String)
data class EngagementPoint(val timeLabel: String, val excitement: Float, val happiness: Float)

val availableGifts = listOf(
    GiftItem("Roses", "🌹", 10, "Classic 100x Roses"),
    GiftItem("Diamonds", "💎", 50, "Sparkling Gemstones"),
    GiftItem("Galaxy", "🌌", 200, "Stunning Cosmic Galaxy"),
    GiftItem("Lion", "🦁", 500, "Majestic Royal Lion"),
    GiftItem("Universe", "🚀", 1000, "Interstellar Universe Rocket")
)

val avatarEmojis = listOf("🦊", "🐼", "🦄", "🦁", "🐯", "🐰", "🐨", "🐸", "🐱", "🐶", "🐵", "🐥", "🦋", "🌸", "⭐", "🔥", "💎", "👑")

val massiveEnglishComments = listOf(
    // Greetings & Vibes
    "OMG you're finally live!!", "Hey from New York!", "Love your vibes!", "Looking stunning today ✨", "Where are you streaming from?",
    "Hello from London! 🇬🇧", "You always make my day better", "So happy you went live tonight", "What time do you usually stream?", "Such great energy here 🔥",
    "Welcome back legend!", "Good evening from California!", "Your setup looks amazing", "Best stream to end the night with", "So glad I caught the notification",
    "Hello from Tokyo! 🇯🇵", "Such wholesome energy here", "Big fan from Sydney!", "You look so happy today", "Sending good vibes from Paris 🇫🇷",
    
    // Questions & Curiosity
    "Can you do a room tour?", "What camera are you using?", "Did you see my DM?", "Say hi to Sarah please!", "What's your plan for tomorrow?",
    "How long have you been streaming?", "What microphone do you use?", "Can you speak Spanish?", "What's your favorite food?", "Are you doing a Q&A today?",
    "What's your dream trip?", "Can you play a song?", "What games do you play?", "How did you start streaming?", "What's your favorite movie?",
    "Do you have a Discord server?", "What time do you stream tomorrow?", "Can you collaborate with other streamers?", "What's your favorite color?", "Are you touring soon?",
    
    // Support, Gifts, Diamonds, Roses
    "Sent 100x Roses 🌹🌹", "Take my diamonds 💎", "Top streamer of the week!", "Sent a Universe Gift 🚀🪐",
    "Sent a Lion Gift 🦁👑", "You deserve 1M followers", "Sent 50x Hearts 💕", "Sent a Galaxy Gift 🌌", "Absolute legend",
    "Sent 10x Coffee ☕", "Sent a Treasure Chest 💰", "Sent 500x Coins 🪙", "Legendary stream", "Sent a Phoenix Gift 🔥",
    "Sent a Sports Car 🏎️", "Sent a Yacht 🛥️", "Sent a Castle 🏰", "Sent a Diamond Ring 💍", "Sent a Firework 🎆",
    "Sent a Magic Lamp 🪔", "Sent a Private Jet ✈️", "Sent 1000x Roses 🌹🌹", "Sent a Golden Crown 👑", "Sent a Starburst ✨",
    
    // Memes & Random Chat
    "Bro is glowing today", "First time catching the stream on time 🙌", "W stream", "Drop the link for your outfit!", "Spam the hearts guys ❤️❤️❤️",
    "Absolute vibe", "This stream cures depression", "W in the chat boys", "Bro cooked with this outfit", "Sheesh look at that energy",
    "Main character energy right here", "We love to see it", "Legendary moment captured", "Bro is majestic", "Top tier content",
    "No way you actually noticed my comment!", "Chat is moving so fast", "Let's get those likes up!", "Best community on the app", "Absolute cinema 🎬"
)

val sampleUsernames = listOf(
    "sophia_rae", "jake.stream", "emma_99", "alex_pro", "liam_beats", 
    "chloe_x", "noah_vibe", "olivia_star", "lucas_92", "mia_spark",
    "ethan_cool", "ava_rose", "mason_fx", "isabella_h", "logan_99",
    "amelia_art", "elijah_m", "harper_01", "james_99", "charlotte_k",
    "benjamin_l", "evelyn_p", "daniel_z", "abigail_w", "jackson_b",
    "grace_g", "lucas_p", "chloe_m", "leo_s", "mia_k"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun LiveSimulatorScreen(
    onEndStream: () -> Unit,
    onBoostStream: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    var viewerCount by remember { mutableStateOf(0) }
    var likesCount by remember { mutableStateOf(0) }
    var coinsEarned by remember { mutableStateOf(0) }
    var isMuted by remember { mutableStateOf(false) }
    var isCameraOn by remember { mutableStateOf(true) }
    var cameraFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_FRONT) }
    var showGiftBottomSheet by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var showAnalyticsModal by remember { mutableStateOf(false) }
    var showPreBroadcastDialog by remember { mutableStateOf(true) }
    var selectedFilter by remember { mutableStateOf(cameraFilters[0]) }

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    val chatMessages = remember { mutableStateListOf<ChatMessage>() }

    val floatingHearts = remember { mutableStateListOf<FloatingHeart>() }
    val activeGiftAlerts = remember { mutableStateListOf<GiftAlert>() }
    val screenParticles = remember { mutableStateListOf<ScreenParticle>() }
    var centerPopupGift by remember { mutableStateOf<GiftAlert?>(null) }

    // Track recently used comments to avoid repetition and ensure variety
    val recentComments = remember { mutableListOf<String>() }

    // Engagement trends history data points over time starting from 0m
    val engagementHistory = remember {
        mutableStateListOf(
            EngagementPoint("0m", 50f, 50f)
        )
    }

    // Periodic task to record engagement metrics over time
    LaunchedEffect(Unit) {
        var minuteCount = 0
        while (true) {
            delay(8000)
            minuteCount++
            val newExcitement = (55f + Random.nextFloat() * 40f).coerceIn(30f, 100f)
            val newHappiness = (60f + Random.nextFloat() * 35f).coerceIn(40f, 100f)
            engagementHistory.add(EngagementPoint("${minuteCount}m", newExcitement, newHappiness))
            if (engagementHistory.size > 12) {
                engagementHistory.removeAt(0)
            }
        }
    }

    // Helper to trigger screen-filling particles and center pop-up animation
    fun triggerGiftAnimation(username: String, giftName: String, giftEmoji: String) {
        val alert = GiftAlert(System.currentTimeMillis(), username, giftName, giftEmoji, Random.nextInt(1, 15))
        activeGiftAlerts.add(alert)
        centerPopupGift = alert

        scope.launch {
            val newParticles = (1..20).map {
                ScreenParticle(
                    id = System.currentTimeMillis() + it,
                    emoji = giftEmoji,
                    startX = Random.nextFloat() * 300f - 150f,
                    startY = Random.nextFloat() * 200f - 100f,
                    targetOffsetX = Random.nextFloat() * 600f - 300f,
                    targetOffsetY = -Random.nextFloat() * 800f - 200f
                )
            }
            screenParticles.addAll(newParticles)

            delay(3000)
            activeGiftAlerts.remove(alert)
            if (centerPopupGift?.id == alert.id) {
                centerPopupGift = null
            }
            delay(1000)
            screenParticles.removeAll(newParticles)
        }
    }

    // Natural comment stream flow & Automated Gift events
    LaunchedEffect(Unit) {
        while (true) {
            val delayMillis = Random.nextLong(700, 1500)
            delay(delayMillis)

            viewerCount = (viewerCount + Random.nextInt(1, 6)).coerceAtMost(35000)

            val profile = sampleUserProfiles.random()
            val randomName = profile.username
            
            // Pick a comment that hasn't been recently used to ensure variety (non-repetitive)
            var randomComment = massiveEnglishComments.random()
            var attempts = 0
            while (recentComments.contains(randomComment) && attempts < 15) {
                randomComment = massiveEnglishComments.random()
                attempts++
            }
            recentComments.add(randomComment)
            if (recentComments.size > 35) {
                recentComments.removeAt(0)
            }

            val isGift = randomComment.contains("Sent") || randomComment.contains("diamonds") || randomComment.contains("Roses")

            var giftName: String? = null
            var giftEmoji = "🎁"
            if (isGift) {
                coinsEarned += Random.nextInt(30, 200)
                likesCount += Random.nextInt(80, 250)
                giftName = when {
                    randomComment.contains("Roses") -> "100x Roses 🌹"
                    randomComment.contains("diamonds") -> "Diamonds 💎"
                    randomComment.contains("Universe") -> "Universe 🚀"
                    randomComment.contains("Lion") -> "Lion 🦁"
                    randomComment.contains("Galaxy") -> "Galaxy 🌌"
                    else -> "Special Gift 👑"
                }
                giftEmoji = when {
                    randomComment.contains("Roses") -> "🌹"
                    randomComment.contains("diamonds") -> "💎"
                    randomComment.contains("Universe") -> "🚀"
                    randomComment.contains("Lion") -> "🦁"
                    randomComment.contains("Galaxy") -> "🌌"
                    else -> "👑"
                }

                triggerGiftAnimation(randomName, giftName, giftEmoji)
            } else {
                likesCount += Random.nextInt(1, 5)
            }

            chatMessages.add(
                ChatMessage(
                    id = chatMessages.size + 1,
                    username = randomName,
                    message = randomComment,
                    isGift = isGift,
                    giftName = giftName,
                    avatarEmoji = profile.avatarEmoji,
                    isFemale = profile.isFemale,
                    avatarColors = profile.colors
                )
            )
            if (chatMessages.size > 80) {
                chatMessages.removeAt(0)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SophisticatedDarkBackground)
            .graphicsLayer {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val colorMatrix = AndroidColorMatrix(selectedFilter.floatArray)
                    val colorFilter = ColorMatrixColorFilter(colorMatrix)
                    renderEffect = RenderEffect.createColorFilterEffect(colorFilter).asComposeRenderEffect()
                }
            }
    ) {
        // Real Camera Preview Background or Fallback
        if (cameraPermissionState.status.isGranted && isCameraOn) {
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }
                            val cameraSelector = CameraSelector.Builder()
                                .requireLensFacing(cameraFacing)
                                .build()
                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }, ContextCompat.getMainExecutor(ctx))
                        previewView
                    },
                    update = { view ->
                        val ctx = view.context
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(view.surfaceProvider)
                            }
                            val cameraSelector = CameraSelector.Builder()
                                .requireLensFacing(cameraFacing)
                                .build()
                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }, ContextCompat.getMainExecutor(ctx))
                    }
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                SophisticatedSurface,
                                SophisticatedDarkBackground,
                                Color(0xFF0A0A0A)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(SophisticatedCard)
                            .border(3.dp, SophisticatedPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Host Avatar",
                            tint = SophisticatedPrimary,
                            modifier = Modifier.size(72.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (!cameraPermissionState.status.isGranted) "Camera permission required for live feed" else "Camera is turned off",
                        color = SophisticatedTextSecondary,
                        fontSize = 14.sp
                    )
                    if (!cameraPermissionState.status.isGranted) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { cameraPermissionState.launchPermissionRequest() },
                            colors = ButtonDefaults.buttonColors(containerColor = SophisticatedPrimary)
                        ) {
                            Text("Grant Permission")
                        }
                    }
                }
            }
        }

        // Filter Tint Overlay
        if (selectedFilter.tintColor != Color.Transparent) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(selectedFilter.tintColor)
            )
        }
    }

    // Overlay UI & Chat & Controls
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFFE53935),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "LIVE",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(6.dp).background(Color(0xFFE53935), CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "$viewerCount viewers", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Analytics Trends Dashboard Button (📈)
                    IconButton(
                        onClick = { showAnalyticsModal = true },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(Icons.Default.TrendingUp, contentDescription = "Analytics Dashboard", tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    if (cameraPermissionState.status.isGranted && isCameraOn) {
                        IconButton(
                            onClick = {
                                cameraFacing = if (cameraFacing == CameraSelector.LENS_FACING_FRONT) {
                                    CameraSelector.LENS_FACING_BACK
                                } else {
                                    CameraSelector.LENS_FACING_FRONT
                                }
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(Icons.Default.Cameraswitch, contentDescription = "Switch Camera", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    // Coins Badge
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "$coinsEarned", color = Color(0xFFFFD700), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "🪙", fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // End Stream Button
                    IconButton(
                        onClick = onEndStream,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE53935), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "End", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // TikTok-Style Gift Alert Banners
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                activeGiftAlerts.take(2).forEach { alert ->
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                        exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                                .height(56.dp),
                            color = Color.Black.copy(alpha = 0.75f),
                            shape = RoundedCornerShape(28.dp),
                            border = BorderStroke(1.5.dp, Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFFFF2B6D), Color(0xFF9C27B0))))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            Brush.sweepGradient(listOf(Color(0xFFFFD700), Color(0xFFFF2B6D), Color(0xFF9C27B0))),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(SophisticatedCard, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "👑", fontSize = 18.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = alert.username,
                                        color = Color(0xFFFFD700),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "sent ${alert.giftName}",
                                        color = Color.White,
                                        fontSize = 11.sp
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color(0xFFFFD700).copy(alpha = 0.2f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = alert.giftEmoji, fontSize = 22.sp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Chat Stream List with TikTok-Style Avatars & Gift Highlights
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    reverseLayout = true
                ) {
                    items(chatMessages.reversed()) { msg ->
                        Surface(
                            color = if (msg.isGift) Color(0xFF9C27B0).copy(alpha = 0.85f) else Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(vertical = 4.dp),
                            border = BorderStroke(
                                1.dp,
                                if (msg.isGift) Color(0xFFFFD700) else Color.White.copy(alpha = 0.12f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            color = Color(0xFFFFD700).copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(4.dp),
                                            border = BorderStroke(0.5.dp, Color(0xFFFFD700))
                                        ) {
                                            Text(
                                                text = "Lv.${Random.nextInt(5, 35)}",
                                                color = Color(0xFFFFD700),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = msg.username,
                                            color = if (msg.isGift) Color(0xFFFFD700) else SophisticatedTextSecondary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = msg.message,
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = if (msg.isGift) FontWeight.Bold else FontWeight.Normal
                                    )
                                }

                                if (msg.isGift) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        color = Color(0xFFFFD700),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = "GIFT 🎁",
                                            color = Color.Black,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Actions Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Say something to your fans...", color = SophisticatedTextSecondary, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Gift Button (🎁) to open Gift Tray
                IconButton(
                    onClick = { showGiftBottomSheet = true },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFFFF2B6D))),
                            CircleShape
                        )
                ) {
                    Text(text = "🎁", fontSize = 22.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Filter Button (🪄)
                IconButton(
                    onClick = { showFilterSheet = true },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f), CircleShape)
                ) {
                    Text(text = "🪄", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Mute Button
                IconButton(
                    onClick = { isMuted = !isMuted },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Camera Toggle Button
                IconButton(
                    onClick = { isCameraOn = !isCameraOn },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isCameraOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
                        contentDescription = "Camera",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Heart / Like Button
                IconButton(
                    onClick = {
                        likesCount += 10
                        floatingHearts.add(
                            FloatingHeart(
                                id = System.currentTimeMillis(),
                                xOffset = Random.nextFloat() * 50 - 25,
                                color = listOf(Color(0xFFE53935), Color(0xFFFFD700), Color(0xFF2196F3), Color(0xFFE91E63)).random()
                            )
                        )
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFE53935), CircleShape)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = "Like", tint = Color.White)
                }
            }
        }



        // Screen-Filling Particles System
        Box(modifier = Modifier.fillMaxSize()) {
            screenParticles.forEach { particle ->
                var progress by remember { mutableStateOf(0f) }
                LaunchedEffect(particle.id) {
                    animate(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 2500, easing = FastOutSlowInEasing)
                    ) { value, _ ->
                        progress = value
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(
                            x = (particle.startX + (particle.targetOffsetX - particle.startX) * progress).dp,
                            y = (particle.startY + (particle.targetOffsetY - particle.startY) * progress).dp
                        )
                        .alpha(1f - progress)
                        .scale(0.8f + progress * 0.8f)
                ) {
                    Text(text = particle.emoji, fontSize = (28 + (progress * 16)).sp)
                }
            }
        }

        // Floating Hearts Animations
        Box(modifier = Modifier.fillMaxSize()) {
            floatingHearts.forEach { heart ->
                var offsetY by remember { mutableStateOf(0f) }
                var alpha by remember { mutableStateOf(1f) }

                LaunchedEffect(heart.id) {
                    val duration = 2000L
                    val startTime = System.currentTimeMillis()
                    while (System.currentTimeMillis() - startTime < duration) {
                        val elapsed = System.currentTimeMillis() - startTime
                        val progress = elapsed.toFloat() / duration
                        offsetY = -progress * 400f
                        alpha = 1f - progress
                        delay(16)
                    }
                    floatingHearts.remove(heart)
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 32.dp, bottom = 120.dp)
                        .offset(x = heart.xOffset.dp, y = offsetY.dp)
                        .alpha(alpha)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = heart.color,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        // TikTok-Style Gift Selection Bottom Sheet
        if (showGiftBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showGiftBottomSheet = false },
                containerColor = SophisticatedCard,
                contentColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Send Virtual Gift 🎁",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "$coinsEarned 🪙", fontWeight = FontWeight.Bold, color = Color(0xFFFFD700), fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.height(260.dp)
                    ) {
                        items(availableGifts) { gift ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color.White.copy(alpha = 0.08f),
                                                Color.White.copy(alpha = 0.02f)
                                            )
                                        )
                                    )
                                    .clickable {
                                        coinsEarned += gift.coinPrice
                                        likesCount += 100
                                        showGiftBottomSheet = false
                                        triggerGiftAnimation("You (Host)", gift.name, gift.emoji)
                                        chatMessages.add(
                                            ChatMessage(
                                                id = chatMessages.size + 1,
                                                username = "You",
                                                message = "Sent ${gift.name} ${gift.emoji}",
                                                isGift = true,
                                                giftName = gift.name,
                                                avatarEmoji = "👑"
                                            )
                                        )
                                    }
                                    .padding(14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(52.dp)
                                            .background(
                                                Brush.radialGradient(
                                                    listOf(Color(0xFFFFD700).copy(alpha = 0.35f), Color.Transparent)
                                                ),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = gift.emoji, fontSize = 38.sp)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = gift.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = "${gift.coinPrice} 🪙", color = Color(0xFFFFD700), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Filter Selection Bottom Sheet
        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                containerColor = SophisticatedCard,
                contentColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🪄", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Camera Aesthetic Filters",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD700)
                            )
                        }
                        IconButton(onClick = { showFilterSheet = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Apply real-time ColorMatrix aesthetic looks to your live stream preview",
                        fontSize = 12.sp,
                        color = SophisticatedTextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.height(260.dp)
                    ) {
                        items(cameraFilters) { filter ->
                            val isSelected = selectedFilter.id == filter.id
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedFilter = filter
                                        showFilterSheet = false
                                    },
                                color = if (isSelected) SophisticatedPrimary.copy(alpha = 0.25f) else SophisticatedSurface,
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) Color(0xFFFFD700) else Color.White.copy(alpha = 0.15f)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = filter.emoji, fontSize = 32.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = filter.name,
                                        color = if (isSelected) Color(0xFFFFD700) else Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Engagement Trends Dashboard Modal (Canvas Chart)
        if (showAnalyticsModal) {
            ModalBottomSheet(
                onDismissRequest = { showAnalyticsModal = false },
                containerColor = SophisticatedCard,
                contentColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Engagement & Vibe Trends",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        IconButton(onClick = { showAnalyticsModal = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Live analytics tracking viewer excitement & happiness over time",
                        fontSize = 12.sp,
                        color = SophisticatedTextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Summary metric cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = SophisticatedSurface,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFFF2B6D).copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "Peak Excitement 🔥", fontSize = 11.sp, color = SophisticatedTextSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${engagementHistory.maxOfOrNull { it.excitement }?.toInt() ?: 92}%",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF2B6D)
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            color = SophisticatedSurface,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "Avg Happiness 😊", fontSize = 11.sp, color = SophisticatedTextSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${(engagementHistory.map { it.happiness }.average()).toInt()}%",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD700)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Canvas Chart Card
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        color = SophisticatedSurface,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Live Trends Chart", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(8.dp).background(Color(0xFFFF2B6D), CircleShape))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Excitement", fontSize = 11.sp, color = SophisticatedTextSecondary)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Box(modifier = Modifier.size(8.dp).background(Color(0xFFFFD700), CircleShape))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Happiness", fontSize = 11.sp, color = SophisticatedTextSecondary)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Compose Canvas Multi-Line Chart
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                            ) {
                                val width = size.width
                                val height = size.height
                                val points = engagementHistory

                                if (points.size >= 2) {
                                    val stepX = width / (points.size - 1)
                                    val maxVal = 100f

                                    // Draw background horizontal grid lines
                                    for (i in 0..3) {
                                        val y = height * (i / 3f)
                                        drawLine(
                                            color = Color.White.copy(alpha = 0.1f),
                                            start = Offset(0f, y),
                                            end = Offset(width, y),
                                            strokeWidth = 1.dp.toPx()
                                        )
                                    }

                                    // Paths for Excitement & Happiness
                                    val excitementPath = Path()
                                    val happinessPath = Path()

                                    points.forEachIndexed { index, pt ->
                                        val x = index * stepX
                                        val yExc = height - (pt.excitement / maxVal) * height
                                        val yHap = height - (pt.happiness / maxVal) * height

                                        if (index == 0) {
                                            excitementPath.moveTo(x, yExc)
                                            happinessPath.moveTo(x, yHap)
                                        } else {
                                            excitementPath.lineTo(x, yExc)
                                            happinessPath.lineTo(x, yHap)
                                        }
                                    }

                                    // Draw Excitement Line & Points
                                    drawPath(
                                        path = excitementPath,
                                        color = Color(0xFFFF2B6D),
                                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                                    )

                                    // Draw Happiness Line & Points
                                    drawPath(
                                        path = happinessPath,
                                        color = Color(0xFFFFD700),
                                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                                    )

                                    // Draw circles on coordinates
                                    points.forEachIndexed { index, pt ->
                                        val x = index * stepX
                                        val yExc = height - (pt.excitement / maxVal) * height
                                        val yHap = height - (pt.happiness / maxVal) * height

                                        drawCircle(color = Color(0xFFFF2B6D), radius = 4.dp.toPx(), center = Offset(x, yExc))
                                        drawCircle(color = Color(0xFFFFD700), radius = 4.dp.toPx(), center = Offset(x, yHap))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFFF2B6D).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFFF2B6D).copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🔥", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = "Stream Vibe: Highly Electric & Engaging", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "Viewers are actively chatting and sending high-value gifts.", color = SophisticatedTextSecondary, fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // TikTok-Style Compact Gift Pop-up Banner above Chat/Comments Area
        centerPopupGift?.let { gift ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, bottom = 100.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                var visible by remember { mutableStateOf(false) }

                LaunchedEffect(gift.id) {
                    visible = true
                    delay(2500)
                    visible = false
                }

                AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(250, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(200))
                      + scaleIn(initialScale = 0.8f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
                    exit = slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(250, easing = FastOutSlowInEasing)
                    ) + fadeOut(animationSpec = tween(200))
                      + scaleOut(targetScale = 0.9f, animationSpec = tween(200))
                ) {
                    Surface(
                        modifier = Modifier
                            .widthIn(max = 280.dp)
                            .height(56.dp),
                        color = Color.Black.copy(alpha = 0.82f),
                        shape = RoundedCornerShape(28.dp),
                        border = BorderStroke(1.5.dp, Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFFFF2B6D), Color(0xFF9C27B0))))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        Brush.sweepGradient(listOf(Color(0xFFFFD700), Color(0xFFFF2B6D), Color(0xFF9C27B0))),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color(0xFF140722), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = gift.giftEmoji, fontSize = 20.sp)
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = gift.username,
                                    color = Color(0xFFFFD700),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Sent ${gift.giftName} 3D!",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Pre-Broadcast Ad / Premium Dialog Overlay
        if (showPreBroadcastDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.88f))
                    .zIndex(100f),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight(),
                    color = Color(0xFF140722),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.5.dp, Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFFFF2B6D))))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ابدأ البث مع Premium",
                            color = Color(0xFFFFD700),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "مع Premium ينتقل معجبوك إلى مستوى آخر",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FeatureRow(icon = "🧠", text = "معجبون أذكياء بالذكاء الاصطناعي يرونك ويتذكرونك")
                            FeatureRow(icon = "👀", text = "حتى 100,000 مشاهد مباشر")
                            FeatureRow(icon = "🎁", text = "هدايا وتبرعات وشارة توثيق")
                            FeatureRow(icon = "💎", text = "بدون إعلانات نهائياً")
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Button 1: Try Premium
                        Button(
                            onClick = { showPreBroadcastDialog = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFFFF2B6D))),
                                        RoundedCornerShape(25.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "جرّب Premium مجاناً!",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Button 2: Watch Ad to start broadcast ("شاهد إعلاناً لبدء البث")
                        OutlinedButton(
                            onClick = { showPreBroadcastDialog = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(25.dp),
                            border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.4f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                        ) {
                            Text(
                                text = "شاهد إعلاناً لبدء البث",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }

data class TikTokGift3DItem(
    val id: Int,
    val name: String,
    val coinValue: Int,
    val imageUrl: String
)

fun GiftItem(id: Int, name: String, coinValue: Int, imageUrl: String) = TikTokGift3DItem(id, name, coinValue, imageUrl)

val tikTok3DGiftsList = listOf(
    TikTokGift3DItem(1, "Rose", 1, "https://lh3.googleusercontent.com/pw/AP1GczN1wzFv9Jv8wJzJv9Jv8wJzJv9Jv8wJzJv9Jv8wJzJv9Jv8wJzJv8=s100-w100-h100"),
    TikTokGift3DItem(2, "Finger Heart", 5, "https://lh3.googleusercontent.com/pw/AP1GczO1wzFv9Jv8wJzJv9Jv8wJzJv9Jv8wJzJv9Jv8wJzJv9Jv8wJzJv8=s100-w100-h100"),
    TikTokGift3DItem(3, "TikTok", 1, "https://lh3.googleusercontent.com/pw/AP1GczP1wzFv9Jv8wJzJv8wJzJv9Jv8wJzJv9Jv8wJzJv8=s100-w100-h100"),
    GiftItem(4, "Ice Cream Cone", 1, "https://lh3.googleusercontent.com/pw/AP1GczQ1wzFv9Jv8wJzJv8wJzJv9Jv8wJzJv9Jv8wJzJv8=s100-w100-h100"),
    GiftItem(5, "Pixel Glasses", 1, "https://lh3.googleusercontent.com/pw/AP1GczR1wzFv9Jv8wJzJv8wJzJv9Jv8wJzJv8wJzJv8=s100-w100-h100"),
    GiftItem(6, "Dino", 1, "https://lh3.googleusercontent.com/pw/AP1GczS1wzFv9Jv8wJzJv8wJzJv9Jv8wJzJv8wJzJv8=s100-w100-h100"),
    GiftItem(7, "Corgi", 99, "https://lh3.googleusercontent.com/pw/AP1GczT1wzFv9Jv8wJzJv8wJzJv9Jv8wJzJv8wJzJv8=s200-w200-h200"),
    GiftItem(8, "Lion", 299, "https://lh3.googleusercontent.com/pw/AP1GczU1wzFv9Jv8wJzJv8wJzJv9Jv8wJzJv8wJzJv8=s200-w200-h200"),
    GiftItem(9, "Galaxy", 999, "https://lh3.googleusercontent.com/pw/AP1GczV1wzFv9Jv8wJzJv8wJzJv9Jv8wJzJv8wJzJv8=s300-w300-h300")
)

@Composable
fun CleanTransparent3DGift(
    giftUrl: String,
    onAnimationEnd: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(1800)
        visible = false
        delay(300)
        onAnimationEnd()
    }

    Box(
        modifier = Modifier
            .size(80.dp)
            .wrapContentSize(Alignment.Center)
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(250, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(200)) 
              + scaleIn(initialScale = 0.4f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
            exit = slideOutVertically(
                targetOffsetY = { -it / 2 },
                animationSpec = tween(250, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(200)) 
              + scaleOut(targetScale = 0.7f, animationSpec = tween(200))
        ) {
            androidx.compose.foundation.Image(
                painter = rememberAsyncImagePainter(model = giftUrl),
                contentDescription = "TikTok 3D Gift Item",
                modifier = Modifier
                    .fillMaxSize()
                    .scale(1.15f)
            )
        }
    }
}

@Composable
fun FeatureRow(icon: String, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

