package kg.edu.yjut.morseinputmethod.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kg.edu.yjut.morseinputmethod.R

class OpenSourceLicenseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_source_license)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        // 设置透明导航栏
        window.navigationBarColor = Color.TRANSPARENT

        // 隐藏标题栏
        supportActionBar?.title = "开放源代码许可"

    }
}