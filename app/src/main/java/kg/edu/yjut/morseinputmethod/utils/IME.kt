package kg.edu.yjut.morseinputmethod.utils

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE

fun isEnableIME(
    context: Context,
):Boolean {
    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
    val list = imm.enabledInputMethodList
    val packageName = "kg.edu.yjut.morseinputmethod"
    Log.d("TAG", "isEnableIME: $context.packageName")
    for (inputMethodInfo in list) {
        if (inputMethodInfo.id == context.packageName + "/kg.edu.yjut.morseinputmethod.services.MorseInputService") {
            return true
            break
        }
    }
    return false
}

fun isCurrIME(
    context: Context,
):Boolean {
    val packageName = "kg.edu.yjut.morseinputmethod"

    var currentInputMethod = android.provider.Settings.Secure.getString(context.contentResolver, android.provider.Settings.Secure.DEFAULT_INPUT_METHOD)
    if (currentInputMethod == context.packageName + "/kg.edu.yjut.morseinputmethod.services.MorseInputService") {
        return true
    }
    return false
}