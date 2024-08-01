package cn.tw.sar.easylauncher.weight

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.sar.tw.morseinputmethod.R

@Preview(showBackground = true)
@Composable
fun LineBar(
    maxPage: Int = 5,
    page: Int = 1,
    onLeftEnd: () -> Unit = {
        println("end")
    },
    onRightEnd: () -> Unit = {
        println("end")
    },
    onDotsClick: (Int) -> Unit = {
        println("click")
    }
){
    var targetOffsetX = remember { mutableStateOf(0f) }//Activity的最终偏移目标
    val activityOffset = remember { Animatable(0f) }//Activity的偏移，动画
    var dragging by remember { mutableStateOf(false) }//是否正在被滑动
    // 显示下方的滚动条
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    //跟踪滑动偏移
                    targetOffsetX.value += delta
                },
                onDragStarted = {
                    dragging = true
                },

                onDragStopped = {
                    //滑动停止时，根据偏移目标，判断是否需要切换页面
                    // 判断偏移量
                    if (targetOffsetX.value > 0) {
                        onLeftEnd()

                        targetOffsetX.value = 0f

                    } else if (targetOffsetX.value < 0) {
                        onRightEnd()
                        targetOffsetX.value = 0f
                    } else {
                        // 判断点击的元素


                    }

                },
                startDragImmediately = true


            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..maxPage) {
            if (i == page) {
                Image(
                    painter = painterResource(id = R.drawable.dots),
                    contentDescription = null,
                    modifier = Modifier
                        .width(10.dp)
                        .height(10.dp)
                        .padding(2.dp)
                        .clickable {
                            onDotsClick(i)
                        }
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.dots_un),
                    contentDescription = null,
                    modifier = Modifier
                        .width(10.dp)
                        .height(10.dp)
                        .padding(2.dp)
                        .clickable {
                            onDotsClick(i)
                        }
                )
            }
        }


    }
}



