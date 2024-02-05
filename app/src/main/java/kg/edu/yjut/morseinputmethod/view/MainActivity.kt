package kg.edu.yjut.morseinputmethod.view

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kg.edu.yjut.morseinputmethod.R

class MainActivity : AppCompatActivity() {
    var isDarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 设置透明状态栏
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
        // 设置透明导航栏
        window.navigationBarColor = Color.TRANSPARENT

        // 隐藏标题栏
        supportActionBar?.hide()

        // 检测是否是深色模式
        val mode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        isDarked = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES

        var qiyong = findViewById<LinearLayout>(R.id.qiyong)
        var active = findViewById<LinearLayout>(R.id.active)
        // 检查当前输入法是否是本输入法
        // 如果是，显示“已启用”
        // 如果不是，显示“启用”v
        var isNowEnable = false
        var isCurrentEnable = false
        // 读取当前输入法
        val imm = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        val list = imm.enabledInputMethodList
        for (inputMethodInfo in list) {
            if (inputMethodInfo.id == packageName + "/.services.MorseInputService") {
                isNowEnable = true
                break
            }
        }
        // 获取当前输入法
        var currentInputMethod = android.provider.Settings.Secure.getString(contentResolver, android.provider.Settings.Secure.DEFAULT_INPUT_METHOD)
        if (currentInputMethod == packageName + "/.services.MorseInputService") {
            isCurrentEnable = true
        }
        println("currentInputMethod: $currentInputMethod")
        println("isNowEnable: $isNowEnable")
        println("list: $list")
        if (isNowEnable) {
            active.visibility = View.GONE
        } else {
            qiyong.visibility = View.VISIBLE
        }
        if (isCurrentEnable) {
            qiyong.visibility = View.GONE
        } else {
            qiyong.visibility = View.VISIBLE
        }

        active.setOnClickListener {
            // 跳转到启用页面
            // 切换输入法
            val intent = android.content.Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS)
            startActivity(intent)

        }

        qiyong.setOnClickListener {
            // 跳转到启用页面
            // 切换输入法
            val imeManager = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
            imeManager.showInputMethodPicker()
            // 重启

        }

        var settings = findViewById<LinearLayout>(R.id.settings_home)
        var help = findViewById<LinearLayout>(R.id.help_home)
        var about = findViewById<LinearLayout>(R.id.about_home)
        settings.setOnClickListener {
            // 跳转到设置页面
            val intent = android.content.Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        help.setOnClickListener {
            // 跳转到帮助页面
            val intent = android.content.Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }
        about.setOnClickListener {
            // 跳转到关于页面
            val intent = android.content.Intent(this, AboueMeActivity::class.java)
            startActivity(intent)
        }





    }

}