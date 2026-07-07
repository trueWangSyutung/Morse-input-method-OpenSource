package kg.edu.yjut.morseinputmethod.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.sar.tw.morseinputmethod.R

import kg.edu.yjut.morseinputmethod.utils.getDarkModeBackgroundColor
import kg.edu.yjut.morseinputmethod.utils.getDarkModeTextColor
import kg.edu.yjut.morseinputmethod.utils.getUnDarkModeTextColor
import kg.edu.yjut.morseinputmethod.utils.isCurrIME
import kg.edu.yjut.morseinputmethod.utils.isEnableIME
import kg.edu.yjut.morseinputmethod.view.ui.theme.MorseInputMethodTheme

class InitActivity : ComponentActivity() {
    private var currentStep = androidx.compose.runtime.mutableIntStateOf(0)

    override fun onResume() {
        super.onResume()
        updateStepBasedOnIMEStatus()
    }

    private fun updateStepBasedOnIMEStatus() {
        val isEnabled = isEnableIME(this)
        val isCurrent = isCurrIME(this)

        if (!isEnabled) {
            currentStep.value = 1
        } else if (!isCurrent) {
            currentStep.value = 2
        } else {
            currentStep.value = 3
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settings = getSharedPreferences("settings", MODE_PRIVATE)
        val isFirstTime = settings.getBoolean("is_first", true)
        if (!isFirstTime) {
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
            finish()
        }
        updateStepBasedOnIMEStatus()
        enableEdgeToEdge()
        setContent {
            MorseInputMethodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InitWizard(
                        currentStep = currentStep,
                        onComplete = {
                            settings.edit().putBoolean("is_first", false).apply()
                            val intent = Intent(this, MainPageActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        onEnableIME = {
                            val intent = Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS)
                            startActivity(intent)
                        },
                        onSwitchIME = {
                            val imeManager = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                            imeManager.showInputMethodPicker()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
private fun InitWizard(
    currentStep: androidx.compose.runtime.MutableIntState,
    onComplete: () -> Unit,
    onEnableIME: () -> Unit,
    onSwitchIME: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val textColor = getDarkModeTextColor(context)
    val textColorSecondary = getDarkModeTextColor(context).copy(alpha = 0.7f)
    val bgColor = getDarkModeBackgroundColor(context, 0)
    val accentColor = Color(0xFF3B82F6)

    val step = currentStep
    var showPopup by remember { mutableStateOf(false) }
    var popupType by remember { mutableStateOf("enable") }

    Box(modifier = modifier.fillMaxSize().background(bgColor)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = step.value == 0,
                    enter = fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 0.9f),
                    exit = fadeOut(tween(200)) + scaleOut(tween(200), targetScale = 0.9f)
                ) {
                    WelcomeStep(textColor = textColor, textColorSecondary = textColorSecondary, accentColor = accentColor)
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = step.value == 1,
                    enter = fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 0.9f),
                    exit = fadeOut(tween(200)) + scaleOut(tween(200), targetScale = 0.9f)
                ) {
                    EnableIMEStep(textColor = textColor, textColorSecondary = textColorSecondary, accentColor = accentColor)
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = step.value == 2,
                    enter = fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 0.9f),
                    exit = fadeOut(tween(200)) + scaleOut(tween(200), targetScale = 0.9f)
                ) {
                    SwitchIMEStep(textColor = textColor, textColorSecondary = textColorSecondary, accentColor = accentColor)
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = step.value == 3,
                    enter = fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 0.9f),
                    exit = fadeOut(tween(200)) + scaleOut(tween(200), targetScale = 0.9f)
                ) {
                    CompleteStep(textColor = textColor, textColorSecondary = textColorSecondary, accentColor = accentColor)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                StepIndicators(currentStep = step.value, totalSteps = 4, accentColor = accentColor)

                ActionButton(
                    currentStep = step.value,
                    totalSteps = 4,
                    textColor = textColor,
                    accentColor = accentColor,
                    onNext = {
                        when (step.value) {
                            0 -> currentStep.value = 1
                            1 -> {
                                popupType = "enable"
                                showPopup = true
                            }
                            2 -> {
                                popupType = "switch"
                                showPopup = true
                            }
                            3 -> onComplete()
                        }
                    },
                    onPrev = {
                        if (currentStep.value > 0) {
                            currentStep.value--
                        }
                    },
                    onEnableIME = {
                        showPopup = false
                        onEnableIME()
                    },
                    onSwitchIME = {
                        showPopup = false
                        onSwitchIME()
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = showPopup,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200))
        ) {
            SimPopup(
                type = popupType,
                textColor = textColor,
                textColorSecondary = textColorSecondary,
                accentColor = accentColor,
                bgColor = getDarkModeBackgroundColor(context, 1),
                onConfirm = {
                    showPopup = false
                    if (popupType == "enable") {
                        onEnableIME()
                    } else {
                        onSwitchIME()
                    }
                },
                onClose = { showPopup = false }
            )
        }
    }
}

@Composable
private fun WelcomeStep(textColor: Color, textColorSecondary: Color, accentColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                    imageVector = Icons.Filled.RadioButtonChecked,
                    contentDescription = "Radio Tower",
                    tint = accentColor,
                    modifier = Modifier.size(64.dp)
                )
        }

        Text(
            text = "欢迎使用莫尔斯输入法",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Text(
            text = "复古电码 指尖传承",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = accentColor
        )

        Text(
            text = "本应用是一款基于莫尔斯电码的输入法，让您在指尖重现经典电报通信体验。",
            fontSize = 14.sp,
            color = textColorSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
private fun EnableIMEStep(textColor: Color, textColorSecondary: Color, accentColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(accentColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = accentColor,
                    modifier = Modifier.size(40.dp)
                )
        }

        Text(
            text = "启用输入法",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Text(
            text = "请前往系统设置，启用「莫尔斯输入法」以开始使用。",
            fontSize = 14.sp,
            color = textColorSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
private fun SwitchIMEStep(textColor: Color, textColorSecondary: Color, accentColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(accentColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                    imageVector = Icons.Filled.Keyboard,
                    contentDescription = "Keyboard",
                    tint = accentColor,
                    modifier = Modifier.size(40.dp)
                )
        }

        Text(
            text = "设为默认输入法",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Text(
            text = "请在输入法切换菜单中选择「莫尔斯输入法」，设置为当前使用的输入法。",
            fontSize = 14.sp,
            color = textColorSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
private fun CompleteStep(textColor: Color, textColorSecondary: Color, accentColor: Color) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "准备就绪",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )

        val scale by animateFloatAsState(
            targetValue = 1f,
            animationSpec = androidx.compose.animation.core.tween(1000, 500),
            label = "checkScale"
        )

        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = "Check",
            tint = Color(0xFF22C55E),
            modifier = Modifier
                .size(96.dp)
                .scale(scale)
        )

        Text(
            text = "所有设置已完成，现在开始体验莫尔斯电码输入！",
            fontSize = 14.sp,
            color = textColorSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FeatureCard(
                icon = "📡",
                title = "拟物发报键",
                description = "真实电报击键感",
                textColor = textColor,
                textColorSecondary = textColorSecondary,
                bgColor = getDarkModeBackgroundColor(context, 1)
            )
            FeatureCard(
                icon = "🔄",
                title = "实时转码",
                description = "莫尔斯码即时翻译",
                textColor = textColor,
                textColorSecondary = textColorSecondary,
                bgColor = getDarkModeBackgroundColor(context, 1)
            )
            FeatureCard(
                icon = "🎮",
                title = "学习训练",
                description = "趣味掌握电码表",
                textColor = textColor,
                textColorSecondary = textColorSecondary,
                bgColor = getDarkModeBackgroundColor(context, 1)
            )
        }
    }
}

@Composable
private fun FeatureCard(icon: String, title: String, description: String, textColor: Color, textColorSecondary: Color, bgColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor),
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
                color = textColorSecondary
            )
        }
    }
}

@Composable
private fun StepIndicators(currentStep: Int, totalSteps: Int, accentColor: Color) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalSteps) { step ->
            val isActive = step == currentStep
            val color by animateColorAsState(
                targetValue = if (isActive) accentColor else accentColor.copy(alpha = 0.3f),
                label = "indicatorColor"
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Composable
private fun ActionButton(
    currentStep: Int,
    totalSteps: Int,
    textColor: Color,
    accentColor: Color,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onEnableIME: () -> Unit,
    onSwitchIME: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = androidx.compose.animation.core.tween(100),
        label = "buttonScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(accentColor)
            .scale(scale)
            .clickable {
                isPressed = true
                onNext()
                isPressed = false
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (currentStep) {
                0 -> "开始设置"
                1 -> "前往系统设置"
                2 -> "切换输入法"
                3 -> "开始使用"
                else -> "下一步"
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun SimPopup(type: String, textColor: Color, textColorSecondary: Color, accentColor: Color, bgColor: Color, onConfirm: () -> Unit, onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000))
            .clickable { onClose() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(bgColor)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (type == "enable") "启用输入法" else "切换输入法",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Text(
                text = if (type == "enable") {
                    "请在系统「设置 → 语言与输入法 → 管理键盘」中开启「莫尔斯输入法」"
                } else {
                    "请在输入法切换菜单中选择「莫尔斯输入法」"
                },
                fontSize = 14.sp,
                color = textColorSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(accentColor)
                    .clickable { onConfirm() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "我已启用",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}