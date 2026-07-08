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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kg.edu.yjut.morseinputmethod.utils.getDarkModeBackgroundColor
import kg.edu.yjut.morseinputmethod.utils.getDarkModeTextColor
import cn.sar.tw.morseinputmethod.R
import kg.edu.yjut.morseinputmethod.view.ui.theme.MorseInputMethodTheme

class HelpCenterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MorseInputMethodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .background(color = getDarkModeBackgroundColor(context = this, level = 0))
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .background(getDarkModeBackgroundColor(context = this@HelpCenterActivity, level = 0))
                                .padding(innerPadding)
                                .padding(top = 100.dp, start = 20.dp),
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

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp)
                                .background(getDarkModeBackgroundColor(context = this@HelpCenterActivity, level = 1), shape = RoundedCornerShape(15.dp))
                                .clickable {
                                    val intent = Intent(this@HelpCenterActivity, HelpEnglishMorseCode::class.java)
                                    startActivity(intent)
                                }
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = resources.getString(R.string.en_code),
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

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp)
                                .background(getDarkModeBackgroundColor(context = this@HelpCenterActivity, level = 1), shape = RoundedCornerShape(15.dp))
                                .clickable {
                                    val intent = Intent(this@HelpCenterActivity, HelpChineseActivity::class.java)
                                    startActivity(intent)
                                }
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = resources.getString(R.string.cn_code),
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
                    }
                }
            }
        }
    }
}
