package cn.tw.sar.easylauncher.weight

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
@Preview
fun MyDialog(
    onDismissRequest: () -> Unit = {},
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    onAgreeRequest : () -> Unit = {},
    showDialog: Boolean = false,
    fontColor: Color = Color.Black,
    subBackgroundColor: Color = Color.White,
    content: @Composable () -> Unit = {},
) {
    AnimatedVisibility(visible = showDialog) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()

                    .background(
                        color = subBackgroundColor.copy(alpha = 0.3f)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .clip(
                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                        .background(
                            subBackgroundColor,
                        )
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var positionEnanle = remember {
                        mutableStateOf(false)
                    }
                    var isDrawing = remember {
                        mutableStateOf(false)
                    }
                    var mdragAmount = remember {
                        mutableStateOf(0f)
                    }
                    Row(
                        modifier = Modifier
                            .width(50.dp)
                            .height(10.dp).padding(
                                bottom = 3.dp
                            )
                            .pointerInput(Unit) {

                                detectVerticalDragGestures(
                                    onDragStart = {
                                        isDrawing.value = true
                                    },
                                    onDragEnd = {
                                        isDrawing.value = false
                                        Log.d("dragAmount", "dragAmount: $mdragAmount")
                                        if (mdragAmount.value > 5) {
                                            onDismissRequest()
                                        }
                                    },
                                    onVerticalDrag = { change, dragAmount ->
                                            if (change.positionChanged()) {
                                                mdragAmount.value = dragAmount
                                            }
                                    }

                                )
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Spacer(modifier = modifier
                            .width(80.dp)
                            .height(20.dp)
                            .background(
                                Color.LightGray, shape = RoundedCornerShape(999.dp)
                            ))
                    }
                    AnimatedVisibility(
                        visible = !positionEnanle.value,
                        exit = fadeOut()
                    ) {
                        content()
                    }
                }

            }

        }
    }

}

