package kg.edu.yjut.morseinputmethod.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.rotate
import androidx.compose.material.icons.filled.Search
import cn.sar.tw.morseinputmethod.R
import kg.edu.yjut.morseinputmethod.utils.getDarkModeBackgroundColor
import kg.edu.yjut.morseinputmethod.utils.getDarkModeTextColor
import kg.edu.yjut.morseinputmethod.utils.isCurrIME
import kg.edu.yjut.morseinputmethod.utils.isEnableIME
import kg.edu.yjut.morseinputmethod.view.ui.theme.MorseInputMethodTheme

class MainPageActivity : ComponentActivity() {

    val currentPage = mutableIntStateOf(0)
    private val isSubPage = mutableStateOf(false)
    private val subPageTitle = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MorseInputMethodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(getDarkModeBackgroundColor(this@MainPageActivity, 0))
                    ) {
                        if (!isSubPage.value) {
                            TopBar(showBack = false, title = getPageTitle(currentPage.value))
                        } else {
                            TopBar(showBack = true, title = subPageTitle.value) {
                                isSubPage.value = false
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            if (!isSubPage.value && currentPage.value == 0) {
                                InputPage(this@MainPageActivity)
                            }

                            if (!isSubPage.value && currentPage.value == 1) {
                                LearningPage()
                            }

                            if (!isSubPage.value && currentPage.value == 2) {
                                PhrasesPage(this@MainPageActivity)
                            }

                            if (!isSubPage.value && currentPage.value == 3) {
                                SettingsPage(this@MainPageActivity) { title ->
                                    subPageTitle.value = title
                                    isSubPage.value = true
                                }
                            }

                            if (isSubPage.value && subPageTitle.value == "帮助中心") {
                                HelpCenterPage(this@MainPageActivity)
                            }

                            if (isSubPage.value && subPageTitle.value == "关于我们") {
                                AboutPage(this@MainPageActivity)
                            }

                            if (isSubPage.value && subPageTitle.value == "反馈") {
                                FeedbackPage(this@MainPageActivity)
                            }
                        }

                        if (!isSubPage.value) {
                            BottomNavigation(currentPage.value) { page ->
                                currentPage.value = page
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getPageTitle(page: Int): String {
        return when (page) {
            0 -> "莫尔斯输入法"
            1 -> "学习训练中心"
            2 -> "常用短语库"
            3 -> "设置"
            else -> ""
        }
    }
}

@Composable
private fun TopBar(showBack: Boolean, title: String, onBack: (() -> Unit)? = null) {
    val context = LocalContext.current
    val textColor = getDarkModeTextColor(context)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .padding(WindowInsets.statusBars.asPaddingValues()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (showBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                contentDescription = "back",
                tint = textColor,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack?.invoke() }
            )
        } else {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        if (!showBack) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "settings",
                    tint = textColor,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            (context as MainPageActivity).currentPage.value = 3
                        }
                )
            }
        } else {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.width(24.dp))
        }
    }
}

@Composable
private fun BottomNavigation(currentPage: Int, onPageChange: (Int) -> Unit) {
    val context = LocalContext.current
    val accentColor = if (context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES) Color(0xFF3B82F6) else Color(0xFF0D9488)
    val inactiveColor = if (context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES) Color(0xFF6B7280) else Color(0xFF9CA3AF)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(getDarkModeBackgroundColor(context, 0))
            .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(1.dp)
                    .background(inactiveColor.copy(alpha = 0.2f))
                    .clip(RoundedCornerShape(0.5.dp))
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val navItems = listOf(
                Triple(Icons.Filled.Keyboard, "输入", 0),
                Triple(Icons.Filled.Book, "学习", 1),
                Triple(Icons.Filled.FormatQuote, "短语", 2),
                Triple(Icons.Filled.Person, "设置", 3)
            )

            navItems.forEach { (icon, text, index) ->
                val isActive = currentPage == index
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clickable { onPageChange(index) }
                        .padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = text,
                        tint = if (isActive) accentColor else inactiveColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = text,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) accentColor else inactiveColor
                    )
                }
            }
        }
    }
}

@Composable
private fun InputPage(context: android.content.Context) {
    val isCurrIme = isCurrIME(context)
    val isActive = isEnableIME(context)
    val textColor = getDarkModeTextColor(context)
    val secondaryText = textColor.copy(alpha = 0.7f)
    val accentColor = if (context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES) Color(0xFF3B82F6) else Color(0xFF0D9488)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(RoundedCornerShape(64.dp))
                .background(accentColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Keyboard,
                contentDescription = "keyboard",
                tint = accentColor,
                modifier = Modifier.size(64.dp)
            )
        }

        Text(
            text = "复古电码 指尖传承",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = accentColor
        )

        Text(
            text = "本应用是一款基于莫尔斯电码的输入法，让您在指尖重现经典电报通信体验。",
            fontSize = 14.sp,
            color = secondaryText,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (!isActive) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(accentColor)
                    .clickable {
                        val intent = Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS)
                        (context as MainPageActivity).startActivity(intent)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "启用输入法",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        } else if (!isCurrIme) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(accentColor)
                    .clickable {
                        val imeManager = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                        imeManager.showInputMethodPicker()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "切换为当前输入法",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF22C55E)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "active",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "输入法已启用",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FeatureCard("📡", "拟物发报键", "真实电报击键感", context)
            FeatureCard("🔄", "实时转码", "莫尔斯码即时翻译", context)
            FeatureCard("🎮", "学习训练", "趣味掌握电码表", context)
        }
    }
}

@Composable
private fun FeatureCard(icon: String, title: String, description: String, context: android.content.Context) {
    val textColor = getDarkModeTextColor(context)
    val secondaryText = textColor.copy(alpha = 0.7f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(getDarkModeBackgroundColor(context, 1)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, fontSize = 24.sp)
        }
        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = description,
                fontSize = 10.sp,
                color = secondaryText
            )
        }
    }
}

@Composable
private fun LearningPage() {
    val context = LocalContext.current
    val textColor = getDarkModeTextColor(context)
    val accentColor = if (context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES) Color(0xFF3B82F6) else Color(0xFF0D9488)
    var currentTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            val tabs = listOf("分级课程", "小游戏")
            tabs.forEachIndexed { index, tab ->
                val isActive = currentTab == index
                Text(
                    text = tab,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) accentColor else textColor.copy(alpha = 0.5f),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickable { currentTab = index }
                )
                if (isActive) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(accentColor)
                            .align(Alignment.Bottom)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(Color.Transparent)
                            .align(Alignment.Bottom)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (currentTab == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listOf(
                    Pair("初级 (字母 A-Z)", 85),
                    Pair("中级 (数字 + 标点)", 30),
                    Pair("高级 (常用单词)", 0)
                )) { (title, progress) ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(getDarkModeBackgroundColor(context, 1))
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                                if (progress > 0) {
                                    Text(
                                        text = "$progress%",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = accentColor
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Filled.Lock,
                                        contentDescription = "locked",
                                        tint = textColor.copy(alpha = 0.5f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(textColor.copy(alpha = 0.1f))
                                    .clip(RoundedCornerShape(2.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(if (progress > 0) accentColor else textColor.copy(alpha = 0.3f))
                                        .width((progress * 3.6).dp)
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(getDarkModeBackgroundColor(context, 1))
                        .clickable {},
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "⚡", fontSize = 48.sp)
                        Text(
                            text = "极速听辨",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(getDarkModeBackgroundColor(context, 1))
                        .clickable {},
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "🎯", fontSize = 48.sp)
                        Text(
                            text = "精准发送",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PhrasesPage(context: android.content.Context) {
    val textColor = getDarkModeTextColor(context)
    val accentColor = if (context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES) Color(0xFF3B82F6) else Color(0xFF0D9488)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(getDarkModeBackgroundColor(context, 1))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "search",
                            tint = textColor.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                Text(
                    text = "搜索短语...",
                    fontSize = 14.sp,
                    color = textColor.copy(alpha = 0.5f)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val categories = listOf("全部", "紧急", "问候", "航海")
            categories.forEach { category ->
                val isActive = category == "全部"
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(if (isActive) accentColor else getDarkModeBackgroundColor(context, 1))
                        .clickable {},
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) Color.White else textColor
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listOf(
                Triple("SOS (救命)", "... --- ...", "... --- ..."),
                Triple("Hello (你好)", ".... . .-.. .-.. ---", ".... . .-.. .-.. ---"),
                Triple("MAYDAY (求救)", "-- .- -.-- -.. .- -.--", "-- .- -.-- -.. .- -.--"),
                Triple("CQ (呼叫)", "-.-. --.-", "-.-. --.-"),
                Triple("73 (美好祝福)", "--... ...--", "--... ...--")
            )) { (title, morse, code) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(getDarkModeBackgroundColor(context, 1))
                        .clickable {},
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Text(
                                text = morse,
                                fontSize = 12.sp,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                color = accentColor
                            )
                        }
                        Icon(
                            imageVector = Icons.Outlined.ArrowRight,
                            contentDescription = "arrow",
                            tint = textColor.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsPage(context: MainPageActivity, onNavigate: (String) -> Unit) {
    val textColor = getDarkModeTextColor(context)
    val sharedPref = context.getSharedPreferences("settings", android.content.Context.MODE_PRIVATE)
    val isSound = remember { mutableStateOf(sharedPref.getBoolean("sound", true)) }
    val isVibrate = remember { mutableStateOf(sharedPref.getBoolean("vibrator", false)) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(listOf(
            Pair("帮助中心", { onNavigate("帮助中心") }),
            Pair("启用输入法", {
                val intent = Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS)
                context.startActivity(intent)
            }),
            Pair("按键模式", {
            })
        )) { (title, action) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(getDarkModeBackgroundColor(context, 1))
                    .clickable { action() }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Icon(
                        imageVector = Icons.Outlined.ArrowRight,
                        contentDescription = "arrow",
                        tint = textColor.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(getDarkModeBackgroundColor(context, 1))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "按键音",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Switch(
                        checked = isSound.value,
                        onCheckedChange = {
                            isSound.value = it
                            sharedPref.edit().putBoolean("sound", it).apply()
                        }
                    )
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(getDarkModeBackgroundColor(context, 1))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "按键震动",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Switch(
                        checked = isVibrate.value,
                        onCheckedChange = {
                            isVibrate.value = it
                            sharedPref.edit().putBoolean("vibrator", it).apply()
                        }
                    )
                }
            }
        }

        items(listOf(
            Pair("关于我们", { onNavigate("关于我们") }),
            Pair("反馈", { onNavigate("反馈") })
        )) { (title, action) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(getDarkModeBackgroundColor(context, 1))
                    .clickable { action() }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Icon(
                        imageVector = Icons.Outlined.ArrowRight,
                        contentDescription = "arrow",
                        tint = textColor.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HelpCenterPage(context: android.content.Context) {
    val textColor = getDarkModeTextColor(context)
    val accentColor = if (context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES) Color(0xFF3B82F6) else Color(0xFF0D9488)
    var expandedEn by remember { mutableStateOf(false) }
    var expandedCn by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(getDarkModeBackgroundColor(context, 1))
                    .clickable { expandedEn = !expandedEn },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "英文莫尔斯码",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Icon(
                        imageVector = if (expandedEn) Icons.AutoMirrored.Filled.ArrowLeft else Icons.Outlined.ArrowRight,
                        contentDescription = "arrow",
                        tint = accentColor,
                        modifier = Modifier.size(20.dp).rotate(if (expandedEn) 90f else 0f)
                    )
                }
            }
        }

        if (expandedEn) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "A .-", fontSize = 12.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, color = textColor)
                            Text(text = "B -...", fontSize = 12.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, color = textColor)
                            Text(text = "C -.-.", fontSize = 12.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, color = textColor)
                            Text(text = "D -..", fontSize = 12.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, color = textColor)
                        }
                        Column {
                            Text(text = "E .", fontSize = 12.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, color = textColor)
                            Text(text = "F ..-.", fontSize = 12.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, color = textColor)
                            Text(text = "G --.", fontSize = 12.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, color = textColor)
                            Text(text = "H ....", fontSize = 12.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, color = textColor)
                        }
                    }
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(getDarkModeBackgroundColor(context, 1))
                    .clickable { expandedCn = !expandedCn },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "中文莫尔斯码",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Icon(
                        imageVector = if (expandedCn) Icons.AutoMirrored.Filled.ArrowLeft else Icons.Outlined.ArrowRight,
                        contentDescription = "arrow",
                        tint = accentColor,
                        modifier = Modifier.size(20.dp).rotate(if (expandedCn) 90f else 0f)
                    )
                }
            }
        }

        if (expandedCn) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.1f))
                ) {
                    Text(
                        text = "中文莫尔斯码通常通过四位区位码转译，或使用特定拼音对应码表。",
                        fontSize = 12.sp,
                        color = textColor.copy(alpha = 0.7f),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun AboutPage(context: android.content.Context) {
    val textColor = getDarkModeTextColor(context)

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(listOf("开源许可", "隐私权限", "开源库列表")) { title ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(getDarkModeBackgroundColor(context, 1))
                    .clickable {}
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Icon(
                        imageVector = Icons.Outlined.ArrowRight,
                        contentDescription = "arrow",
                        tint = textColor.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(48.dp))
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(getDarkModeBackgroundColor(context, 1)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Keyboard,
                        contentDescription = "logo",
                        tint = if (context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES) Color(0xFF3B82F6) else Color(0xFF0D9488),
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = "莫尔斯输入法",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Text(
                    text = "版本号：1.2.0 Beta Preview 1",
                    fontSize = 12.sp,
                    color = textColor.copy(alpha = 0.5f)
                )
                Text(
                    text = "构建类型：release",
                    fontSize = 10.sp,
                    color = textColor.copy(alpha = 0.4f)
                )
            }
        }
    }
}

@Composable
private fun FeedbackPage(context: android.content.Context) {
    val textColor = getDarkModeTextColor(context)
    val accentColor = if (context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES) Color(0xFF3B82F6) else Color(0xFF0D9488)
    var selectedModule by remember { mutableStateOf("界面显示") }
    var feedbackContent by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "问题所在板块 *",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                listOf("界面显示", "功能问题").forEach { module ->
                    val isSelected = selectedModule == module
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) accentColor.copy(alpha = 0.1f) else Color.Transparent)
                            .clickable { selectedModule = module },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isSelected) accentColor else textColor.copy(alpha = 0.3f))
                            )
                            Text(
                                text = module,
                                fontSize = 14.sp,
                                color = if (isSelected) accentColor else textColor
                            )
                        }
                    }
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "反馈内容 *",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.1f))
                    .padding(16.dp)
            ) {
                Text(
                    text = feedbackContent.ifEmpty { "请详细描述您遇到的问题..." },
                    fontSize = 14.sp,
                    color = if (feedbackContent.isEmpty()) textColor.copy(alpha = 0.5f) else textColor
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(accentColor)
                .clickable {
                    (context as MainPageActivity).currentPage.value = 0
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "提交反馈",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Text(
            text = "您的意见对我们非常重要",
            fontSize = 10.sp,
            color = textColor.copy(alpha = 0.4f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
