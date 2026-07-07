package kg.edu.yjut.morseinputmethod.view

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.SpaceBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kg.edu.yjut.morseinputmethod.entity.MorseCode
import kg.edu.yjut.morseinputmethod.viewmodel.MorseViewModel

@Composable
fun MorseKeyboardScreen(
    viewModel: MorseViewModel,
    @Suppress("UNUSED_PARAMETER") onHideKeyboard: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val colorScheme = if (uiState.isDarkMode) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        Surface(
            color = if (uiState.isDarkMode) Color(0xFF121212) else Color(0xFFF3F4F6),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Header(context = context)

                Spacer(modifier = Modifier.height(8.dp))

                DisplayArea(uiState = uiState)

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.weight(1f)) {
                    KeyboardArea(viewModel = viewModel)

                    InputDisabledOverlay(isVisible = !uiState.inputEnabled)
                }

                Spacer(modifier = Modifier.height(8.dp))

                BottomFunctionKeys(viewModel = viewModel, inputEnabled = uiState.inputEnabled)
            }
        }
    }
}

@Composable
private fun Header(context: android.content.Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "莫尔斯输入法",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Circle,
                contentDescription = "theme",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {}
            )
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "settings",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        context.startActivity(
                            Intent(context, MainPageActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                        )
                    }
            )
        }
    }
}

@Composable
private fun DisplayArea(uiState: kg.edu.yjut.morseinputmethod.viewmodel.KeyboardUiState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(120.dp)
            .background(
                color = if (uiState.isDarkMode) Color(0xFF1E1E1E) else Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = uiState.morseBuffer,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (uiState.isDarkMode) Color(0xFF3B82F6) else Color(0xFF0D9488),
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    letterSpacing = 8.sp,
                    maxLines = 2
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        color = if (uiState.isDarkMode) Color(0xFF4B5563) else Color(0xFFD1D5DB),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(0.5.dp)
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = uiState.textResult.ifEmpty { "输入莫尔斯码..." },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (uiState.isDarkMode) Color.White else Color(0xFF1F2937),
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
private fun KeyboardArea(viewModel: MorseViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (uiState.keyingMode == 0) {
            ManualKeyMode(viewModel = viewModel)
        } else {
            AutoKeyMode(viewModel = viewModel)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(
                    color = if (uiState.isDarkMode) Color(0xFF1E1E1E) else Color(0xFFE5E7EB),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (uiState.keyingMode == 0) "手动键" else "自动键",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (uiState.isDarkMode) Color.White else Color(0xFF1F2937)
            )
            Icon(
                imageVector = Icons.Filled.Circle,
                contentDescription = "mode",
                tint = if (uiState.isDarkMode) Color(0xFF3B82F6) else Color(0xFF0D9488),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { viewModel.toggleKeyingMode() }
            )
        }
    }
}

@Composable
private fun ManualKeyMode(viewModel: MorseViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (uiState.pressedKey == 1) 0.95f else 1f,
        animationSpec = tween(100),
        label = "manualKeyScale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(130.dp)
                .scale(scale)
                .clip(RoundedCornerShape(65.dp))
                .background(
                    color = if (uiState.isDarkMode) Color(0xFF1E1E1E) else Color.White
                )
                .graphicsLayer {
                    if (uiState.pressedKey == 1) {
                        shadowElevation = 0f
                    } else {
                        shadowElevation = 8f
                    }
                }
                .clickable(enabled = uiState.inputEnabled) { }
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
                    imageVector = Icons.Filled.Circle,
                    contentDescription = "morse key",
                    tint = if (uiState.isDarkMode) Color(0xFF3B82F6) else Color(0xFF0D9488),
                    modifier = Modifier.size(48.dp)
                )
        }

        Text(
            text = "长按为划 (-) 短按为点 (.)",
            fontSize = 10.sp,
            color = if (uiState.isDarkMode) Color(0xFF4B5563) else Color(0xFF9CA3AF),
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .height(44.dp)
                .padding(horizontal = 48.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    color = if (uiState.isDarkMode) Color(0xFF3B82F6) else Color(0xFF0D9488)
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
private fun AutoKeyMode(viewModel: MorseViewModel) {
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
            horizontalArrangement = Arrangement.spacedBy(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .scale(dotScale)
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            color = if (uiState.isDarkMode) Color(0xFF2D1B1B) else Color(0xFFFEF2F2)
                        )
                        .clickable(enabled = uiState.inputEnabled) {
                            viewModel.onDotClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(8.dp))
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

            Text(
                text = "· −",
                fontSize = 24.sp,
                color = if (uiState.isDarkMode) Color(0xFF374151) else Color(0xFFE5E7EB),
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .scale(dashScale)
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            color = if (uiState.isDarkMode) Color(0xFF1B2D3D) else Color(0xFFEFF6FF)
                        )
                        .clickable(enabled = uiState.inputEnabled) {
                            viewModel.onDashClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF3B82F6))
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

        Box(
            modifier = Modifier
                .height(44.dp)
                .padding(horizontal = 48.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    color = if (uiState.isDarkMode) Color(0xFF3B82F6) else Color(0xFF0D9488)
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
private fun InputDisabledOverlay(isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(200))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(Color(0xAA000000))
                .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Circle,
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
private fun BottomFunctionKeys(viewModel: MorseViewModel, inputEnabled: Boolean) {
    val uiState by viewModel.uiState.collectAsState()
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(56.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    color = if (uiState.isDarkMode) Color(0xFF1E1E1E) else Color.White
                )
                .clickable(enabled = inputEnabled) {
                    viewModel.onBackspace()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Backspace,
                contentDescription = "backspace",
                tint = if (uiState.isDarkMode) Color.White else Color(0xFF1F2937),
                modifier = Modifier.size(24.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    color = if (uiState.isDarkMode) Color(0xFF1E1E1E) else Color.White
                )
                .clickable(enabled = inputEnabled) {
                    viewModel.addSpace()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                    imageVector = Icons.Outlined.SpaceBar,
                    contentDescription = "space",
                    tint = if (uiState.isDarkMode) Color.White else Color(0xFF1F2937),
                    modifier = Modifier.size(24.dp)
                )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    color = if (uiState.isDarkMode) Color(0xFF1E1E1E) else Color.White
                )
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