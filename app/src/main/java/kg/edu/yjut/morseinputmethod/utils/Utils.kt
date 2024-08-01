package cn.tw.sar.easylauncher.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.text.format.DateFormat
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.util.ArrayList

fun timeFormat(
    time: String?
): String {
    // 转为 long
    val timeLong = time?.toLongOrNull()!!
    val hour = timeLong / 3600
    val minute = (timeLong % 3600) / 60
    val second = timeLong % 60
    return if (hour > 0) {
        String.format("%02d时%02d分%02d秒", hour, minute, second)
    } else if (minute > 0) {
        String.format("%02d分%02d秒", minute, second)
    } else {
        String.format("%02d秒", second)
    }
}
fun dataFormat(
    time: Long
): String {
    // 将时间辍转换为日期 2021-01-01 12:00:00
    return DateFormat.format("yyyy-MM-dd HH:mm:ss", time).toString()

}


fun isDarkMode(
    context: Context
): Boolean {
    val mode = context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
    return mode == android.content.res.Configuration.UI_MODE_NIGHT_YES

}


fun getDarkModeTextColor(
    context: Context
): Color {
    return if (isDarkMode(context)) {
        Color.White
    } else {
        Color.Black
    }
}

fun getUnDarkModeTextColor(
    context: Context
): Color {
    return if (!isDarkMode(context)) {
        Color.White
    } else {
        Color.Black
    }
}
@Composable
fun getDarkModeBackgroundColor(
    context: Context,
    level : Int
): Color {
    return if (isDarkMode(context)) {
        if (level == 0) {
            Color.Black
        } else if (level == 1) {
            Color.DarkGray

        } else if (level == 2) {
            MaterialTheme.colorScheme.onTertiaryContainer
        } else {
            Color.Black
        }
    } else {
        if (level == 0) {
            Color.White
        } else if (level == 1) {
            Color(0xFFDDDDDD)
        } else if (level == 2) {
            MaterialTheme.colorScheme.tertiaryContainer

        } else {
            Color.White
        }
    }
}


fun getYesOrNo(
    value: Boolean
): ImageVector {
    return if (value) {
        Icons.Filled.Check
    } else {
        Icons.Filled.Close
    }
}

fun getYesOrNoColor(
    value: Boolean
): Color {
    return if (value) {
        Color.Green
    } else {
        Color.Red
    }
}