package kg.edu.yjut.morseinputmethod.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import com.google.android.material.materialswitch.MaterialSwitch
import kg.edu.yjut.morseinputmethod.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        // 设置透明状态栏
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
        // 设置透明导航栏
        window.navigationBarColor = Color.TRANSPARENT

        // 隐藏标题栏
        supportActionBar?.hide()

        var switch_sound = findViewById<Switch>(R.id.switch_sound)
        // 读取 shared preference 中的设置
        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        switch_sound.isChecked = sharedPref.getBoolean("sound", true)
        switch_sound.setOnCheckedChangeListener { buttonView, isChecked ->
            // 保存设置
            val editor = sharedPref.edit()
            editor.putBoolean("sound", isChecked)
            editor.apply()
        }

        var about_me = findViewById<LinearLayout>(R.id.about_me)
        about_me.setOnClickListener {
            startActivity(Intent(this, AboueMeActivity::class.java))
        }



    }
}