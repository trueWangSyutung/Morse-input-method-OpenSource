package kg.edu.yjut.morseinputmethod.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.tw.sar.easylauncher.utils.getDarkModeBackgroundColor
import cn.tw.sar.easylauncher.utils.getDarkModeTextColor
import cn.tw.sar.easylauncher.weight.LineBar
import cn.sar.tw.morseinputmethod.R
import kg.edu.yjut.morseinputmethod.utils.isCurrIME
import kg.edu.yjut.morseinputmethod.utils.isEnableIME
import kg.edu.yjut.morseinputmethod.view.ui.theme.MorseInputMethodTheme

data class InitPageInfo(
    val title: String,
    val description: String,
    val action: () -> Unit
)




class InitActivity : ComponentActivity() {
    private var initPageInfoList = mutableStateListOf<InitPageInfo>()

    override fun onResume() {
        super.onResume()
        val curr = listOf(
            InitPageInfo(
                title = resources.getString(R.string.step1),
                description = resources.getString(R.string.step1_desp),
                action = {
                    if (!isEnableIME(this@InitActivity)){
                        val intent = android.content.Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS)
                        startActivity(intent)
                    }
                }
            ),
            InitPageInfo(
                title = resources.getString(R.string.step2),
                description = resources.getString(R.string.step2_desp),
                action = {
                    if (!isCurrIME(this@InitActivity)){
                        val imeManager = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                        imeManager.showInputMethodPicker()
                    }
                }
            ),
            InitPageInfo(
                title = resources.getString(R.string.step3),
                description = resources.getString(R.string.step3_desp),
                action = {
                    val sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
                    sharedPreferences.edit().putBoolean("is_first", false).apply()
                    var intent = Intent(this@InitActivity, MainPageActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            ),
        )
        initPageInfoList.clear()
        initPageInfoList.addAll(curr)

    }
    private var currPageIndex = mutableStateOf(0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var settings = getSharedPreferences("settings", MODE_PRIVATE)
        var isFirstTime = settings.getBoolean("is_first", true)
        if (!isFirstTime) {
            var intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
            finish()
        }
        enableEdgeToEdge()
        setContent {
            MorseInputMethodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .background(
                                color = getDarkModeBackgroundColor(
                                    context = this@InitActivity,
                                    level = 0
                                )
                            )
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxWidth()
                                .fillMaxHeight(0.9f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(painter = painterResource(id = R.mipmap.logo),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .width(400.dp)
                                    .height(200.dp)
                                    .padding(20.dp).clip(RoundedCornerShape(25.dp)),

                            )
                            Text(
                                text = initPageInfoList[currPageIndex.value].title,
                                color = getDarkModeTextColor(this@InitActivity),
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                            TextButton(
                                onClick = {
                                    initPageInfoList[currPageIndex.value].action()
                                },
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .background(
                                        color = getDarkModeBackgroundColor(
                                            context = this@InitActivity,
                                            level = 1
                                        ),
                                        shape = RoundedCornerShape(9999.dp)
                                    ),
                            ) {
                                Text(
                                    text = initPageInfoList[currPageIndex.value].description,
                                    color = getDarkModeTextColor(this@InitActivity),
                                    fontSize = 15.sp,
                                    modifier = Modifier
                                        .padding(5.dp),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Normal
                                )
                            }

                        }
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxWidth()
                                .fillMaxHeight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LineBar(
                                maxPage = initPageInfoList.size,
                                page = currPageIndex.value + 1,
                                onRightEnd = {
                                    currPageIndex.value = (currPageIndex.value+ 1) % initPageInfoList.size
                                },
                                onLeftEnd = {
                                    if (currPageIndex.value != 0) {
                                        currPageIndex.value = initPageInfoList.size - 1
                                    }else{
                                        currPageIndex.value = initPageInfoList.size - 1
                                    }
                                },
                                onDotsClick = {
                                    currPageIndex.value = it - 1
                                },
                            )
                        }

                    }
                }
            }
        }
    }
}
