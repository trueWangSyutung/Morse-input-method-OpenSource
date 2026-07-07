package kg.edu.yjut.morseinputmethod.utils

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE

fun isEnableIME(
    context: Context,
):Boolean {
    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
    val list = imm.enabledInputMethodList
    val targetId = context.packageName + "/kg.edu.yjut.morseinputmethod.services.MorseInputService"
    for (inputMethodInfo in list) {
        if (inputMethodInfo.id == targetId) {
            return true
        }
    }
    return false
}

fun isCurrIME(
    context: Context,
):Boolean {
    val targetId = context.packageName + "/kg.edu.yjut.morseinputmethod.services.MorseInputService"
    
    val defaultInputMethod = android.provider.Settings.Secure.getString(
        context.contentResolver, 
        android.provider.Settings.Secure.DEFAULT_INPUT_METHOD
    )
    if (defaultInputMethod == targetId) {
        return true
    }

    return false
}