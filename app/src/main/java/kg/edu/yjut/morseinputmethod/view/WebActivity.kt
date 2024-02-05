package kg.edu.yjut.morseinputmethod.view

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import kg.edu.yjut.morseinputmethod.R

class WebActivity : AppCompatActivity() {
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        // 设置透明导航栏
        window.navigationBarColor = Color.TRANSPARENT
        // 隐藏标题栏
        // 读取传入的参数 url
        var url = intent.getStringExtra("url")
        // 打开网页
        var web = findViewById<WebView>(R.id.web)
        if (url != null) {
            // 启用 JavaScript
            web.settings.javaScriptEnabled = true
            web.loadUrl(url)
            supportActionBar?.title = web.title
        }

    }
}