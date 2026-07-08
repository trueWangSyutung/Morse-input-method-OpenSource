package kg.edu.yjut.morseinputmethod.view

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SpaceBar
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import kg.edu.yjut.morseinputmethod.viewmodel.MorseViewModel

@Composable
fun MorseKeyboardScreen(
    viewModel: MorseViewModel,
    onHideKeyboard: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val colorScheme = if (uiState.isDarkMode) darkColorScheme() else lightColorScheme()

    // 主题色：深色模式使用蓝色，浅色模式使用青绿色（与原型图保持一致）
    val accentColor = if (uiState.isDarkMode) Color(0xFF3B82F6) else Color(0xFF0D9488)
    val bgColor = if (uiState.isDarkMode) Color(0xFF121212) else Color(0xFFF3F4F6)
    val cardBg = if (uiState.isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (uiState.isDarkMode) Color(0xFFE5E7EB) else Color(0xFF1F2937)
    val secondaryText = if (uiState.isDarkMode) Color(0xFF4B5563) else Color(0xFF9CA3AF)
    val inactiveTab = if (uiState.isDarkMode) Color(0xFF4B5563) else Color(0xFF9CA3AF)

    MaterialTheme(colorScheme = colorScheme) {
        Surface(
            color = bgColor,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        WindowInsets.navigationBars.asPaddingValues()
                    )
            ) {
                // 顶部标题栏
                Header(
                    textColor = textColor,
                    onSettingsClick = {
                        context.startActivity(
                            Intent(context, MainPageActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                        )
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 转码预览区
                DisplayArea(
                    morseBuffer = uiState.morseBuffer,
                    textResult = uiState.textResult,
                    accentColor = accentColor,
                    cardBg = cardBg,
                    textColor = textColor,
                    secondaryText = secondaryText,
                    inactiveTab = inactiveTab
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 键盘区
                Box(modifier = Modifier.fillMaxWidth()) {
                    KeyboardArea(
                        viewModel = viewModel,
                        accentColor = accentColor,
                        cardBg = cardBg
                    )

                    // 禁用遮罩
                    InputDisabledOverlay(
                        isVisible = !uiState.inputEnabled,
                        cardBg = cardBg
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 底部功能键
                BottomFunctionKeys(
                    viewModel = viewModel,
                    inputEnabled = uiState.inputEnabled,
                    cardBg = cardBg,
                    textColor = textColor
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun Header(textColor: Color, onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "莫尔斯输入法",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Brightness4,
                contentDescription = "theme",
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "settings",
                tint = textColor,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onSettingsClick() }
            )
        }
    }
}

@Composable
private fun DisplayArea(
    morseBuffer: String,
    textResult: String,
    accentColor: Color,
    cardBg: Color,
    textColor: Color,
    secondaryText: Color,
    inactiveTab: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(140.dp)
            .background(color = cardBg.copy(alpha = 0.5f), shape = RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 莫尔斯缓冲区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = inactiveTab.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = morseBuffer.ifEmpty { "... --- ..." },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (morseBuffer.isEmpty()) secondaryText else accentColor,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 4.sp,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 虚线分隔
            DashedDivider(
                color = inactiveTab,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 文本结果区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = textResult.ifEmpty { "SOS" },
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (textResult.isEmpty()) secondaryText else textColor,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
private fun DashedDivider(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .height(1.dp)
    ) {
        val dashWidth = 6.dp.toPx()
        val dashGap = 4.dp.toPx()
        val strokeWidth = 1.dp.toPx()
        var x = 0f
        while (x < size.width) {
            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(x, size.height / 2),
                end = androidx.compose.ui.geometry.Offset(x + dashWidth, size.height / 2),
                strokeWidth = strokeWidth
            )
            x += dashWidth + dashGap
        }
    }
}

@Composable
private fun KeyboardArea(
    viewModel: MorseViewModel,
    accentColor: Color,
    cardBg: Color
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.keyingMode == 0) {
            ManualKeyMode(
                viewModel = viewModel,
                accentColor = accentColor,
                cardBg = cardBg
            )
        } else {
            AutoKeyMode(
                viewModel = viewModel,
                accentColor = accentColor,
                cardBg = cardBg
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 模式切换栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(
                    color = cardBg,
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (uiState.keyingMode == 0) "手动键" else "自动键",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (uiState.isDarkMode) Color.White else Color(0xFF1F2937)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable { viewModel.toggleKeyingMode() }
            ) {
                Text(
                    text = "切换",
                    fontSize = 12.sp,
                    color = accentColor,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "switch mode",
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ManualKeyMode(
    viewModel: MorseViewModel,
    accentColor: Color,
    cardBg: Color
) {
    val uiState by viewModel.uiState.collectAsState()

    val scale by animateFloatAsState(
        targetValue = if (uiState.pressedKey == 1) 0.95f else 1f,
        animationSpec = tween(100),
        label = "manualKeyScale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 拟物化发报键
        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(scale)
                .graphicsLayer {
                    if (uiState.pressedKey == 1) {
                        shadowElevation = 0f
                    } else {
                        shadowElevation = 12f
                        shape = CircleShape
                        clip = false
                    }
                }
                .clip(CircleShape)
                .background(cardBg)
                .pointerInput(uiState.inputEnabled) {
                    detectTapGestures(
                        onPress = {
                            if (uiState.inputEnabled) {
                                viewModel.startManualPress()
                                val success = tryAwaitRelease()
                                if (success) {
                                    viewModel.endManualPress()
                                }
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.RadioButtonChecked,
                contentDescription = "morse key",
                tint = accentColor,
                modifier = Modifier.size(40.dp)
            )
        }

        // 提示文字
        Text(
            text = "长按为划 (-) 短按为点 (.)",
            fontSize = 10.sp,
            color = if (uiState.isDarkMode) Color(0xFF4B5563) else Color(0xFF9CA3AF),
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 确认发送按钮
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(
                    color = accentColor,
                    shape = RoundedCornerShape(22.dp)
                )
                .clickable(enabled = uiState.inputEnabled) {
                    viewModel.confirmChar()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "确认发送",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun AutoKeyMode(
    viewModel: MorseViewModel,
    accentColor: Color,
    cardBg: Color
) {
    val uiState by viewModel.uiState.collectAsState()

    val dotScale by animateFloatAsState(
        targetValue = if (uiState.keyPressAnimation == 1) 0.95f else 1f,
        animationSpec = tween(100),
        label = "dotScale"
    )
    val dashScale by animateFloatAsState(
        targetValue = if (uiState.keyPressAnimation == 2) 0.95f else 1f,
        animationSpec = tween(100),
        label = "dashScale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 点键
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .scale(dotScale)
                        .graphicsLayer {
                            shadowElevation = 8f
                            shape = CircleShape
                            clip = false
                        }
                        .clip(CircleShape)
                        .background(cardBg)
                        .clickable(enabled = uiState.inputEnabled) {
                            viewModel.onDotClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444))
                    )
                }
                Text(
                    text = "点 (·)",
                    fontSize = 12.sp,
                    color = if (uiState.isDarkMode) Color(0xFF9CA3AF) else Color(0xFF4B5563),
                    fontWeight = FontWeight.Bold
                )
            }

            // 中间分隔符
            Text(
                text = "· −",
                fontSize = 24.sp,
                color = if (uiState.isDarkMode) Color(0xFF374151) else Color(0xFFE5E7EB),
                fontFamily = FontFamily.Monospace
            )

            // 划键
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .scale(dashScale)
                        .graphicsLayer {
                            shadowElevation = 8f
                            shape = CircleShape
                            clip = false
                        }
                        .clip(CircleShape)
                        .background(cardBg)
                        .clickable(enabled = uiState.inputEnabled) {
                            viewModel.onDashClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(accentColor)
                    )
                }
                Text(
                    text = "划 (−)",
                    fontSize = 12.sp,
                    color = if (uiState.isDarkMode) Color(0xFF9CA3AF) else Color(0xFF4B5563),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 确认发送按钮
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(
                    color = accentColor,
                    shape = RoundedCornerShape(22.dp)
                )
                .clickable(enabled = uiState.inputEnabled) {
                    viewModel.confirmChar()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "确认发送",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun InputDisabledOverlay(isVisible: Boolean, cardBg: Color) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(200))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(Color(0xB3000000), shape = RoundedCornerShape(20.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.RadioButtonChecked,
                    contentDescription = "disabled",
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "输入法已禁用",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "请在设置中开启",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun BottomFunctionKeys(
    viewModel: MorseViewModel,
    inputEnabled: Boolean,
    cardBg: Color,
    textColor: Color
) {
    val uiState by viewModel.uiState.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(56.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 退格键
        Box(
            modifier = Modifier
                .weight(1f)
                .graphicsLayer {
                    shadowElevation = 4f
                    shape = RoundedCornerShape(16.dp)
                    clip = false
                }
                .clip(RoundedCornerShape(16.dp))
                .background(cardBg)
                .clickable(enabled = inputEnabled) {
                    viewModel.onBackspace()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Backspace,
                contentDescription = "backspace",
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )
        }

        // 空格键
        Box(
            modifier = Modifier
                .weight(1f)
                .graphicsLayer {
                    shadowElevation = 4f
                    shape = RoundedCornerShape(16.dp)
                    clip = false
                }
                .clip(RoundedCornerShape(16.dp))
                .background(cardBg)
                .clickable(enabled = inputEnabled) {
                    viewModel.addSpace()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.SpaceBar,
                contentDescription = "space",
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )
        }

        // 清空键
        Box(
            modifier = Modifier
                .weight(1f)
                .graphicsLayer {
                    shadowElevation = 4f
                    shape = RoundedCornerShape(16.dp)
                    clip = false
                }
                .clip(RoundedCornerShape(16.dp))
                .background(cardBg)
                .clickable(enabled = inputEnabled) {
                    viewModel.clearInput()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Clear",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEF4444)
            )
        }
    }
}
