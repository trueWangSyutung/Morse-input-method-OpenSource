package kg.edu.yjut.morseinputmethod.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import kg.edu.yjut.morseinputmethod.R

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        // 读取传入的参数 url
        var url = intent.getStringExtra("url")
        // 打开网页
        var web = findViewById<WebView>(R.id.web)
        if (url != null) {
            // 启用 JavaScript
            web.settings.javaScriptEnabled = true
            web.loadUrl(url)

        }

    }
}