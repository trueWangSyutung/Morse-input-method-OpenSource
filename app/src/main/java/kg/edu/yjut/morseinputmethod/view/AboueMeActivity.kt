package kg.edu.yjut.morseinputmethod.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import kg.edu.yjut.morseinputmethod.R
import kg.edu.yjut.morseinputmethod.utils.AppInfo
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

class AboueMeActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboue_me)
        // 设置透明状态栏
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
        // 设置透明导航栏
        window.navigationBarColor = Color.TRANSPARENT

        // 隐藏标题栏
        supportActionBar?.hide()

        var app_version = findViewById<TextView>(R.id.app_version)
        // 读取 编译时的版本号
        app_version.text = "v" + AppInfo.getVerName(this) +"(" +AppInfo.getVersionCode(this) +")";


        var privacy_policy = findViewById<LinearLayout>(R.id.privacy_policy)
        var user_agreement = findViewById<LinearLayout>(R.id.user_agreement)
        privacy_policy.setOnClickListener {
            // 跳转到 WebActivity, 传入参数，打开隐私政策
            var intent = Intent(this, WebActivity::class.java)
            intent.putExtra("url", "https://www.yjut.edu.kg/index.php/4.html")
            startActivity(intent)

        }
        user_agreement.setOnClickListener {
            // 打开用户协议
            var intent = Intent(this, WebActivity::class.java)
            intent.putExtra("url", "https://www.yjut.edu.kg/index.php/5.html")
            startActivity(intent)
        }

        var open_source = findViewById<LinearLayout>(R.id.open_source)
        open_source.setOnClickListener {
            // 打开开源协议
            var intent = Intent(this, OpenSourceLicenseActivity::class.java)
            startActivity(intent)
        }

        var github_repo = findViewById<LinearLayout>(R.id.github_repo)
        github_repo.setOnClickListener {
            // 打开开源协议
            var intent = Intent(this, WebActivity::class.java)
            intent.putExtra(
                "url",
                "https://github.com/trueWangSyutung/Morse-input-method-OpenSource"
            )
            startActivity(intent)
        }


    }
}