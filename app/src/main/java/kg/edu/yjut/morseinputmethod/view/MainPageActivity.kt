package kg.edu.yjut.morseinputmethod.view

import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.tw.sar.easylauncher.utils.getDarkModeBackgroundColor
import cn.tw.sar.easylauncher.utils.getDarkModeTextColor
import cn.sar.tw.morseinputmethod.R
import kg.edu.yjut.morseinputmethod.utils.isCurrIME
import kg.edu.yjut.morseinputmethod.utils.isEnableIME
import kg.edu.yjut.morseinputmethod.view.ui.theme.MorseInputMethodTheme


data class SettingItem(
    val title: String,
    val description: String,
    val type: Int = 0,  // 0 表示 菜单项 ， 1 表示 切换按钮， 2 表示 无意义
    val action: () -> Unit,
    val swithName : String? = ""
)

class MainPageActivity : ComponentActivity() {
    private var initPageInfoList = mutableStateListOf<SettingItem>()
    private var isChecked = mutableStateOf(false)
    override fun onResume() {
        super.onResume()
        initPageInfoList.clear()
        var isCurrIme = isCurrIME(this@MainPageActivity)
        var isActive = isEnableIME(this@MainPageActivity)

        if (!isActive) {
            initPageInfoList.add(
                SettingItem(
                    title = resources.getString(R.string.step1),
                    description = resources.getString(R.string.step1_desp),
                    type = 0,
                    action = {
                        if (!isEnableIME(this@MainPageActivity)){
                            val intent = android.content.Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS)
                            startActivity(intent)
                        }
                    }
                )
            )
        }
        if (!isCurrIme) {
            initPageInfoList.add(
                SettingItem(
                    title = resources.getString(R.string.step2),
                    description = resources.getString(R.string.step2_desp),
                    type = 0,
                    action = {
                        if (!isCurrIME(this@MainPageActivity)){
                            val imeManager = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                            imeManager.showInputMethodPicker()
                        }
                    }
                )
            )
        }


        initPageInfoList.add(
            SettingItem(
                title = resources.getString(R.string.help),
                description = resources.getString(R.string.help_desp),
                type = 0,
                action = {
                    var intent = Intent(this@MainPageActivity, HelpCenterActivity::class.java)
                    startActivity(intent)
                }
            )
        )


        initPageInfoList.add(
            SettingItem(
                title = resources.getString(R.string.help),
                description = resources.getString(R.string.sound_desp),
                type = 1,
                action = {

                },
                swithName = "sound"
            )
        )


        initPageInfoList.add(
            SettingItem(
                title = resources.getString(R.string.help),
                description = resources.getString(R.string.vibrate_desp),
                type = 1,
                action = {

                },
                swithName = "vibrate"
            )
        )

        initPageInfoList.add(
            SettingItem(
                title = resources.getString(R.string.about),
                description = resources.getString(R.string.about_desp),
                type = 0,
                action = {
                    var intent = Intent(this@MainPageActivity, AboutActivity::class.java)
                    startActivity(intent)
                }
            )
        )
        initPageInfoList.add(
            SettingItem(
                title = "help",
                description = resources.getString(R.string.callback),
                type = 0,
                action = {
                    var intent = Intent(this@MainPageActivity, WebViewActivity::class.java)
                    intent.putExtra("url","https://www.wjx.cn/vm/mZxCSow.aspx")
                    startActivity(intent)
                }
            )
        )

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)

        enableEdgeToEdge()
        setContent {
            MorseInputMethodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .background(
                                getDarkModeBackgroundColor(
                                    context = this@MainPageActivity,
                                    level = 0
                                )
                            )
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    getDarkModeBackgroundColor(
                                        context = this@MainPageActivity,
                                        level = 0
                                    )
                                )
                                .padding(innerPadding)
                                .padding(
                                    top = 100.dp, start = 40.dp
                                ),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = resources.getString(R.string.settings),
                                color = getDarkModeTextColor(this@MainPageActivity),
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )

                        }
                        for (item in initPageInfoList) {
                            if (item.type == 0) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 20.dp, end = 20.dp
                                        )
                                        .background(
                                            getDarkModeBackgroundColor(
                                                context = this@MainPageActivity,
                                                level = 0
                                            ),
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .clickable {
                                            item.action()
                                        }
                                        .padding(
                                            20.dp
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = item.description,
                                        color = getDarkModeTextColor(this@MainPageActivity),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    Text(
                                        text = ">",
                                        color = getDarkModeTextColor(this@MainPageActivity),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                }
                            } else if (item.type == 1) {

                                var swith = remember { mutableStateOf(
                                    sharedPref.getBoolean(item.swithName, false)
                                ) }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 20.dp, end = 20.dp
                                        )
                                        .background(
                                            getDarkModeBackgroundColor(
                                                context = this@MainPageActivity,
                                                level = 0
                                            ),
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .padding(
                                            20.dp
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = item.description,
                                        color = getDarkModeTextColor(this@MainPageActivity),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    Switch(checked = swith.value, onCheckedChange = {
                                        swith.value = !swith.value
                                        item.action()
                                        val editor = sharedPref.edit()
                                        editor.putBoolean(item.swithName, swith.value)
                                        editor.apply()
                                    })
                                }

                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 20.dp, end = 20.dp
                                        )
                                        .background(
                                            getDarkModeBackgroundColor(
                                                context = this@MainPageActivity,
                                                level = 1
                                            ),
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .padding(
                                            20.dp
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = item.description,
                                        color = getDarkModeTextColor(this@MainPageActivity),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                    )

                                }

                            }

                        }
                    }
                }
            }
        }
    }
}
