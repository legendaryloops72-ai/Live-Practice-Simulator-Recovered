package com.livepractice.simulator.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.livepractice.simulator.ui.theme.*

@Composable
fun OnboardingFlow(
    onFinishOnboarding: () -> Unit
) {
    var currentStep by remember { mutableStateOf(1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SophisticatedDarkBackground)
    ) {
        when (currentStep) {
            1 -> OnboardingStep1(onNext = { currentStep = 2 })
            2 -> OnboardingStep2(onNext = { currentStep = 3 })
            3 -> OnboardingStep3(onNext = { currentStep = 4 })
            4 -> OnboardingStep4(onNext = { currentStep = 5 })
            5 -> OnboardingStep5(onFinish = onFinishOnboarding)
        }
    }
}

@Composable
fun OnboardingStep1(onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "جاهز تحس إنك",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "مشهور",
            color = Color(0xFFE53935),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "21,745",
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "يشاهدون",
            color = Color(0xFFE53935),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "كل المعجبين والهدايا من صنع الذكاء الاصطناعي ولأغراض الترفيه فقط.",
            color = SophisticatedTextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Chat preview cards container
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = SophisticatedSurface.copy(alpha = 0.5f),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFE53935).copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FanCommentCard("✨ شكلّك يلمع اليوم")
                FanCommentCard("🔥 هذا أفضل بث في التطبيق")
                FanCommentCard("❤️ وين كنت؟! اشتقنا لك")
                FanCommentCard("😍 يا سلام! أخيراً صرت لايف")
                FanCommentCard("💕 من الآن أنت ستريمري المفضّل")
                FanCommentCard("🙏 ممكن تسلم عليّ")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
            shape = RoundedCornerShape(28.dp)
        ) {
            Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "جرّبها الآن", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FanCommentCard(text: String) {
    Surface(
        color = SophisticatedCard,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            color = SophisticatedText,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}

@Composable
fun OnboardingStep2(onNext: () -> Unit) {
    var selectedGender by remember { mutableStateOf("ذكر") }
    var selectedAge by remember { mutableStateOf("24-18") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(SophisticatedCard, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = SophisticatedPrimary, modifier = Modifier.size(36.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "احكِ لنا قليلاً",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "عن نفسك",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "هذا يساعدنا على تخصيص تجربتك.",
            color = SophisticatedTextSecondary,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "الجنس", color = SophisticatedTextSecondary, fontSize = 14.sp, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SelectionChip(text = "ذكر", isSelected = selectedGender == "ذكر", modifier = Modifier.weight(1f)) { selectedGender = "ذكر" }
            SelectionChip(text = "أنثى", isSelected = selectedGender == "أنثى", modifier = Modifier.weight(1f)) { selectedGender = "أنثى" }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SelectionChip(text = "غير ثنائي", isSelected = selectedGender == "غير ثنائي", modifier = Modifier.weight(1f)) { selectedGender = "غير ثنائي" }
            SelectionChip(text = "أفضل عدم الإجابة", isSelected = selectedGender == "أفضل عدم الإجابة", modifier = Modifier.weight(1f)) { selectedGender = "أفضل عدم الإجابة" }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "العمر", color = SophisticatedTextSecondary, fontSize = 14.sp, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(8.dp))

        val ages = listOf("أقل من 18", "24-18", "34-25", "44-35", "+45")
        ages.chunked(2).forEach { rowAges ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowAges.forEach { age ->
                    SelectionChip(text = age, isSelected = selectedAge == age, modifier = Modifier.weight(1f)) { selectedAge = age }
                }
                if (rowAges.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(text = "متابعة", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SelectionChip(text: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        color = if (isSelected) SophisticatedCard else SophisticatedSurface,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, if (isSelected) Color(0xFFE53935) else Color.White.copy(alpha = 0.1f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = if (isSelected) Color.White else SophisticatedTextSecondary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun OnboardingStep3(onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(text = "جاهز تقابل", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text(text = "معجبيك؟", color = Color(0xFFE53935), fontSize = 32.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "❤️ معجبوك متحمسون للقائك", color = SophisticatedText, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(20.dp))

        // Fan message preview
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = SophisticatedCard,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "sophia_rae", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "🦋", fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "❤️ 8.2K  OMG you're going live?! I'm so ready 😍", color = SophisticatedText, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = SophisticatedCard,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "jake.stream", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "🔥", fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "❤️ 4.5K  First!! Your biggest fan right here 🙌", color = SophisticatedText, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Chat, contentDescription = null, tint = SophisticatedPrimary)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = "دردش مع المعجبين", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = "معجبوك بالذكاء الاصطناعي يتفاعلون مع كلامك مباشرة.", color = SophisticatedTextSecondary, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = Color(0xFFFFD700))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = "اطلب تبرعات", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = "يرسل المعجبون هدايا وتبرعات أثناء بثك.", color = SophisticatedTextSecondary, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(text = "التالي", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun OnboardingStep4(onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(text = "ساعدنا على النمو", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.Center) {
            repeat(5) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(36.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "الجميع يحب Parallel Live!", color = Color(0xFFE53935), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = "أظهر حبك بتركه تقييم لنا على Play Store", color = SophisticatedTextSecondary, fontSize = 13.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = SophisticatedCard,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFE53935).copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row {
                    repeat(5) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "هذا التطبيق جنوني، بثوثي المزيفة تبدو حقيقية جدًا 😭 المعجبون يردّون فعلًا!",
                    color = SophisticatedText,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "- @sophia_rae", color = SophisticatedTextSecondary, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(text = "Rate", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = onNext) {
            Text(text = "تخطي", color = SophisticatedTextSecondary, fontSize = 14.sp)
        }
    }
}

@Composable
fun OnboardingStep5(onFinish: () -> Unit) {
    // Screen 5: Main Dashboard Setup before starting live
    var selectedTab by remember { mutableStateOf("أساسي") }
    var mood by remember { mutableStateOf("حماس") }
    var showProfileDialog by remember { mutableStateOf(false) }
    var showStoreDialog by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("username") }
    var coinBalance by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SophisticatedDarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Top App Bar Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = "PARALLEL LIVE",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(6.dp).background(Color(0xFFE53935), CircleShape))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        onClick = { showStoreDialog = true },
                        color = SophisticatedCard,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "$coinBalance", color = Color(0xFFFFD700), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "🪙", fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        onClick = { showStoreDialog = true },
                        color = SophisticatedCard,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, SophisticatedPrimary.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Diamond, contentDescription = null, tint = SophisticatedPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Pro", color = SophisticatedPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "دعم العملاء: support@bigbraintech.ai",
                color = SophisticatedTextSecondary,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("أساسي", "الجمهور", "التعليقات", "المحفوظات").forEach { tab ->
                    Surface(
                        onClick = { selectedTab = tab },
                        modifier = Modifier.height(36.dp),
                        color = if (selectedTab == tab) SophisticatedCard else Color.Transparent,
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, if (selectedTab == tab) Color.White.copy(alpha = 0.2f) else Color.Transparent)
                    ) {
                        Box(modifier = Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = tab,
                                color = if (selectedTab == tab) Color.White else SophisticatedTextSecondary,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Edit Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = SophisticatedCard,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { showProfileDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SophisticatedButton),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(text = "تعديل", color = Color.White, fontSize = 13.sp)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = userName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(SophisticatedSurface, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = SophisticatedTextSecondary, modifier = Modifier.size(28.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Info banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = SophisticatedCard,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "بث المباشر يمكن أن يصل إلى 150 مشاهد",
                        color = Color(0xFFE53935),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ابث باستمرار لتبقي معجبيك متفاعلين!",
                        color = SophisticatedTextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "حدد مزاج الجمهور أثناء جلسة البث",
                color = SophisticatedTextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mood grid (2 columns)
            val moods = listOf(
                Triple("حماس", "المعجبون يشجعونك بلا توقف", "🔥"),
                Triple("كاره", "تصلك تعليقات لئيمة ووقحة", "😠"),
                Triple("غزل", "المعجبون يغازلونك", "😉"),
                Triple("سعيد", "تعليقات إيجابية ومبهجة", "😊"),
                Triple("فضولي", "المعجبون يطرحون عليك أسئلة", "❓"),
                Triple("مضحك", "نكت وتعليقات مرحة", "😂")
            )

            moods.chunked(2).forEach { rowMoods ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowMoods.forEach { (mName, mDesc, mEmoji) ->
                        val isSelected = (mood == mName)
                        Surface(
                            onClick = { mood = mName },
                            modifier = Modifier
                                .weight(1f)
                                .height(90.dp),
                            color = SophisticatedCard,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, if (isSelected) Color(0xFFE53935) else Color.White.copy(alpha = 0.05f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "$mEmoji $mName", color = if (isSelected) Color(0xFFE53935) else Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = mDesc, color = SophisticatedTextSecondary, fontSize = 10.sp, textAlign = TextAlign.Center, maxLines = 2)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(60.dp))
        }

        // Bottom Start Button and share bonus banner
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color.Transparent, SophisticatedDarkBackground, SophisticatedDarkBackground)))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Surface(
                    onClick = onFinish,
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape,
                    color = Color(0xFFE53935),
                    shadowElevation = 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Start", tint = Color.White, modifier = Modifier.size(40.dp))
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "ابدأ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
