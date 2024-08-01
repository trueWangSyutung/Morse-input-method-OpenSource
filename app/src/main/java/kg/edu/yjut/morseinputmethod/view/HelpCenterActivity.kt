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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.tw.sar.easylauncher.utils.getDarkModeBackgroundColor
import cn.tw.sar.easylauncher.utils.getDarkModeTextColor
import cn.sar.tw.morseinputmethod.R
import kg.edu.yjut.morseinputmethod.utils.isCurrIME
import kg.edu.yjut.morseinputmethod.utils.isEnableIME
import kg.edu.yjut.morseinputmethod.view.ui.theme.MorseInputMethodTheme

class HelpCenterActivity : ComponentActivity() {
    private var initPageInfoList = mutableStateListOf<SettingItem>()

    override fun onResume() {
        super.onResume()
        initPageInfoList.clear()

        initPageInfoList.add(
            SettingItem(
                title = "en_code",
                description = resources.getString(R.string.en_code),
                type = 0,
                action = {
                    var intent = Intent(this@HelpCenterActivity, HelpEnglishMorseCode::class.java)
                    startActivity(intent)
                }
            )
        )
        initPageInfoList.add(
            SettingItem(
                title = "cn_code",
                description = resources.getString(R.string.cn_code),
                type = 0,
                action = {
                    var intent = Intent(this@HelpCenterActivity, HelpChineseActivity::class.java)
                    startActivity(intent)
                }
            )
        )


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
                                color = getDarkModeBackgroundColor(context = this,
                                    level = 0)
                            ).fillMaxSize()
                            .padding(innerPadding).verticalScroll(
                                rememberScrollState()
                            ),
                        verticalArrangement = Arrangement.spacedBy(10.dp),

                        ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    getDarkModeBackgroundColor(
                                        context = this@HelpCenterActivity,
                                        level = 0
                                    )
                                )
                                .padding(innerPadding).padding(
                                    top = 100.dp, start = 20.dp
                                ),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = resources.getString(R.string.help_desp),
                                color = getDarkModeTextColor(this@HelpCenterActivity),
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
                                                context = this@HelpCenterActivity,
                                                level = 1
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
                                        color = getDarkModeTextColor(this@HelpCenterActivity),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    Text(
                                        text = ">",
                                        color = getDarkModeTextColor(this@HelpCenterActivity),
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
                                                context = this@HelpCenterActivity,
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
                                        color = getDarkModeTextColor(this@HelpCenterActivity),
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
                                                context = this@HelpCenterActivity,
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
                                        color = getDarkModeTextColor(this@HelpCenterActivity),
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
