package kg.edu.yjut.morseinputmethod.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.tw.sar.easylauncher.utils.getDarkModeBackgroundColor
import cn.tw.sar.easylauncher.utils.getDarkModeTextColor
import cn.sar.tw.morseinputmethod.R
import kg.edu.yjut.morseinputmethod.entity.MorseCode
import kg.edu.yjut.morseinputmethod.utils.SQLUtils
import kg.edu.yjut.morseinputmethod.view.ui.theme.MorseInputMethodTheme

class HelpChineseActivity : ComponentActivity() {
    var morseCodes = mutableStateListOf<MorseCode>()
    private lateinit var sqlUtils: SQLUtils
    override fun onResume() {
        super.onResume()
        sqlUtils = SQLUtils(this@HelpChineseActivity)

    }
    private var inputCode = mutableStateOf("")
    private var page = mutableStateOf(1)
    fun loadData(
        more: Boolean = false,

    ) {
        if (!more) {
            page.value = 1
            morseCodes.clear()
        }
        val list = sqlUtils.getChineseMorseByStr(inputCode.value,page.value)

        if (list.isNotEmpty()) {
            morseCodes.addAll(list)
        }else{
            Toast.makeText(this@HelpChineseActivity, "没有数据", Toast.LENGTH_SHORT).show()
        }
    }
    fun loadMoreData() {
        page.value++
        loadData(more = true)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MorseInputMethodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .background(
                                color = getDarkModeBackgroundColor(
                                    context = this,
                                    level = 0
                                )
                            )
                            .padding(innerPadding)

                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(0.3f)
                                .background(
                                    getDarkModeBackgroundColor(
                                        context = this@HelpChineseActivity,
                                        level = 0
                                    )
                                )
                                .padding(
                                    top = 100.dp, start = 20.dp
                                ),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = resources.getString(R.string.cn_code),
                                color = getDarkModeTextColor(this@HelpChineseActivity),
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        TextField(
                            value = inputCode.value, onValueChange = {
                                inputCode.value = it
                                loadData()
                            },
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(
                                    fraction = 1f
                                ).fillMaxHeight(0.1f)
                                .border(
                                    0.dp, MaterialTheme.colorScheme.onSurface,
                                    shape = MaterialTheme.shapes.extraLarge
                                )
                                .background(
                                    color = Color.Transparent,
                                    shape = MaterialTheme.shapes.extraLarge
                                ),
                            maxLines = 1,
                            placeholder = { Text(text =  resources.getString(R.string.input_code)) },
                            // 不显示下方的横线
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = MaterialTheme.shapes.extraLarge
                        )
                        Column(
                            modifier = Modifier.fillMaxHeight(1f).padding(start = 20.dp, end = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            AnimatedVisibility(visible = morseCodes.isNotEmpty()) {
                                LazyVerticalGrid(
                                    columns  = GridCells.Fixed(5),
                                    modifier = Modifier.padding(5.dp).fillMaxHeight(0.6f),
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalArrangement = Arrangement.spacedBy(5.dp)
                                ){
                                    items(morseCodes) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    getDarkModeBackgroundColor(
                                                        context = this@HelpChineseActivity,
                                                        level = 1
                                                    ),
                                                    shape = MaterialTheme.shapes.extraLarge
                                                ).padding(
                                                    top = 6.dp, bottom = 6.dp
                                                )
                                        ) {
                                            Text(
                                                text = it.word,
                                                color = getDarkModeTextColor(this@HelpChineseActivity),
                                                fontSize = 25.sp,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            Text(
                                                text = it.code,
                                                color = getDarkModeTextColor(this@HelpChineseActivity),
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Normal,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()

                                            )
                                        }
                                    }
                                }

                            }
                            AnimatedVisibility(visible = morseCodes.isNotEmpty()) {
                                TextButton(onClick = {
                                    loadMoreData()
                                },
                                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.3f)
                                ) {
                                    Text(
                                        text =  resources.getString(R.string.more_codes),
                                        color = getDarkModeTextColor(this@HelpChineseActivity),
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                            Text(
                                text =  resources.getString(R.string.powered_by),
                                color = getDarkModeTextColor(this@HelpChineseActivity),
                                modifier = Modifier.fillMaxWidth().fillMaxHeight(1f),
                                textAlign = TextAlign.Center,
                            )

                        }
                    }
                }
            }
        }
    }
}
