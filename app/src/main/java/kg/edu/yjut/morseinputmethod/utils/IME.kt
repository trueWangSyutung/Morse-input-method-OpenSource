package kg.edu.yjut.morseinputmethod.utils

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodSubtype
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE

fun isEnableIME(
    context: Context,
):Boolean {
    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
    val list = imm.enabledInputMethodList
    val targetId = "cn.sar.tw.morseinputmethod/kg.edu.yjut.morseinputmethod.services.MorseInputService"
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
    val targetId = "cn.sar.tw.morseinputmethod/kg.edu.yjut.morseinputmethod.services.MorseInputService"
    
    val defaultInputMethod = android.provider.Settings.Secure.getString(
        context.contentResolver, 
        android.provider.Settings.Secure.DEFAULT_INPUT_METHOD
    )
    if (defaultInputMethod == targetId) {
        return true
    }

    try {
        val imm = context.getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        val method = android.view.inputmethod.InputMethodManager::class.java.getMethod("getCurrentInputMethodId")
        val currentInputMethodId = method.invoke(imm) as String
        if (currentInputMethodId == targetId) {
            return true
        }
    } catch (e: Exception) {
    }

    try {
        val imm = context.getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        val method = android.view.inputmethod.InputMethodManager::class.java.getMethod("getEnabledInputMethodList")
        val list = method.invoke(imm) as List<Any>
        for (item in list) {
            val idField = item.javaClass.getDeclaredField("id")
            idField.isAccessible = true
            val id = idField.get(item) as String
            if (id == targetId) {
                val isDefaultField = item.javaClass.getDeclaredField("isDefault")
                isDefaultField.isAccessible = true
                if (isDefaultField.getBoolean(item)) {
                    return true
                }
            }
        }
    } catch (e: Exception) {
    }

    return false
}