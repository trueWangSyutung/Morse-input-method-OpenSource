package kg.edu.yjut.morseinputmethod.view

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import cn.sar.tw.morseinputmethod.R
import kg.edu.yjut.morseinputmethod.view.ui.theme.MorseInputMethodTheme

class WebViewActivity : ComponentActivity() {
    fun Activity.listenForKeyboardVisibilityChange(callback: (Boolean) -> Unit) {
        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            private val rect = Rect()
            private var isKeyboardVisible = false

            override fun onGlobalLayout() {
                rootView.getWindowVisibleDisplayFrame(rect)
                val screenHeight = rootView.rootView.height
                val keyboardHeight = screenHeight - rect.bottom

                val isVisible = keyboardHeight > screenHeight * 0.15 // 判断键盘高度是否超过屏幕高度的 15%

                if (isVisible != isKeyboardVisible) {
                    isKeyboardVisible = isVisible
                    callback(isVisible)
                }
            }
        })
    }
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun imeVisible(): State<Boolean> {
        val isImeVisible = remember { mutableStateOf(false) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            isImeVisible.value = WindowInsets.isImeVisible
        }else {
            val view = LocalView.current
            DisposableEffect(view) {
                val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
                    val rect = Rect()
                    view.getWindowVisibleDisplayFrame(rect)
                    val screenHeight = view.rootView.height
                    val keypadHeight = screenHeight - rect.bottom
                    isImeVisible.value = keypadHeight > screenHeight * 0.15
                }
                view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)
                onDispose {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
                }
            }
        }

        return isImeVisible
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var url = intent.getStringExtra("url")
        // 打开网页

        enableEdgeToEdge()
        setContent {

            MorseInputMethodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        val imeVisible = imeVisible().value


                        AndroidView(factory = {
                            WebView(this@WebViewActivity).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                webViewClient = WebViewClient()
                                settings.javaScriptEnabled = true
                                settings.javaScriptCanOpenWindowsAutomatically = true
                                settings.domStorageEnabled = true
                                settings.setSupportZoom(true)
                                if (url != null) {
                                    loadUrl(url)
                                }
                            }

                                              },
                            modifier = Modifier.fillMaxSize().padding(
                                bottom = if (!imeVisible) 0.dp else 300.dp
                            ),
                            update = {

                            }
                        )

                        
                    }
                }
            }
        }
    }
}
