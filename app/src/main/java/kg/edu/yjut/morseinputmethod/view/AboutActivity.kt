package kg.edu.yjut.morseinputmethod.view

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import cn.sar.tw.morseinputmethod.R
import kg.edu.yjut.morseinputmethod.utils.AppInfo
import kg.edu.yjut.morseinputmethod.view.ui.theme.MorseInputMethodTheme


class AboutActivity : ComponentActivity() {
    private var initPageInfoList = mutableStateListOf<SettingItem>()
    private var appVersion = mutableStateOf("")
    private var appCode = mutableStateOf("")
    private var appPackageName = mutableStateOf("")
    private var appLastTime = mutableStateOf("")
    private var appName = mutableStateOf("")
    private var appBuildType = mutableStateOf("")
    override fun onResume() {
        super.onResume()
        initPageInfoList.clear()

        initPageInfoList.add(
            SettingItem(
                title = resources.getString(R.string.privacy),
                description = resources.getString(R.string.open_source),
                type = 0,
                action = {
                    var intent = Intent(this@AboutActivity, WebViewActivity::class.java)
                    intent.putExtra("url","https://wxd2zrx.top/v1.html")
                    startActivity(intent)
                }
            )
        )
        initPageInfoList.add(
            SettingItem(
                title = resources.getString(R.string.privacy),
                description = resources.getString(R.string.privacy_desp),
                type = 0,
                action = {
                    var intent = Intent(this@AboutActivity, WebViewActivity::class.java)
                    intent.putExtra("url","https://wxd2zrx.top/mp.html")
                    startActivity(intent)
                }
            )
        )

        initPageInfoList.add(
            SettingItem(
                title = resources.getString(R.string.privacy),
                description = resources.getString(R.string.open_source_lib),
                type = 0,
                action = {
                    startActivity(Intent(this, OssLicensesMenuActivity::class.java))

                }
            )
        )
        appName.value = resources.getString(R.string.app_name)
        appVersion.value = AppInfo.getVerName(this@AboutActivity)
        appCode.value = AppInfo.getVersionCode(this@AboutActivity).toString()
        appPackageName.value = AppInfo.getPackageName(this@AboutActivity)
        appLastTime.value = AppInfo.getLastTime(this@AboutActivity)
        appBuildType.value = AppInfo.getBuildType(this@AboutActivity)



    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MorseInputMethodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .background(
                                color = getDarkModeBackgroundColor(
                                    context = this,
                                    level = 0
                                )
                            )
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(
                                rememberScrollState()
                            ),
                        verticalArrangement = Arrangement.spacedBy(10.dp),

                        ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    getDarkModeBackgroundColor(
                                        context = this@AboutActivity,
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
                                text = resources.getString(R.string.about_desp),
                                color = getDarkModeTextColor(this@AboutActivity),
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
                                                context = this@AboutActivity,
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
                                        color = getDarkModeTextColor(this@AboutActivity),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    Text(
                                        text = ">",
                                        color = getDarkModeTextColor(this@AboutActivity),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                }
                            } else if (item.type == 1) {
                                var swith = remember { mutableStateOf(false) }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 20.dp, end = 20.dp
                                        )
                                        .background(
                                            getDarkModeBackgroundColor(
                                                context = this@AboutActivity,
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
                                        color = getDarkModeTextColor(this@AboutActivity),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    Switch(checked = swith.value, onCheckedChange = {
                                        swith.value = !swith.value
                                        item.action()
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
                                                context = this@AboutActivity,
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
                                        color = getDarkModeTextColor(this@AboutActivity),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                    )

                                }

                            }

                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 20.dp, end = 20.dp
                                )
                                .background(
                                    getDarkModeBackgroundColor(
                                        context = this@AboutActivity,
                                        level = 0
                                    ),
                                    shape = RoundedCornerShape(15.dp)
                                )

                                .padding(
                                    20.dp
                                ),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = resources.getString(R.string.curr_version),
                                color = getDarkModeTextColor(this@AboutActivity),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                            )
                            Text(
                                text = appName.value,
                                color = getDarkModeTextColor(this@AboutActivity),
                                fontSize = 15.sp,
                            )
                            Text(
                                text = appVersion.value,
                                color = getDarkModeTextColor(this@AboutActivity),
                                fontSize = 15.sp,
                            )
                            Text(
                                text = resources.getString(R.string.curr_version_code) + ":"+ appVersion.value,
                                color = getDarkModeTextColor(this@AboutActivity),
                                fontSize = 15.sp,
                            )
                            Text(
                                text = resources.getString(R.string.build_type) + ":"+ appBuildType.value,
                                color = getDarkModeTextColor(this@AboutActivity),
                                fontSize = 15.sp,
                            )
                            Text(
                                text = resources.getString(R.string.last_update) + ":"+ appLastTime.value,
                                color = getDarkModeTextColor(this@AboutActivity),
                                fontSize = 15.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}
