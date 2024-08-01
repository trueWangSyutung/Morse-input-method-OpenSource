package kg.edu.yjut.morseinputmethod.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import cn.tw.sar.easylauncher.utils.getDarkModeBackgroundColor
import cn.tw.sar.easylauncher.utils.getDarkModeTextColor
import cn.sar.tw.morseinputmethod.R
import kg.edu.yjut.morseinputmethod.view.ui.theme.MorseInputMethodTheme

class HelpEnglishMorseCode : ComponentActivity() {
    var morseCodeEnglish = HashMap<String, String>()
    var morseCodeNumber = HashMap<String, String>()
    var morseCodePunctuation = HashMap<String, String>()

    fun init(){
        // 将 a-Z 与 .- 的对应关系存入 morseCodeEnglish
        morseCodeEnglish["a"] = ".-"
        morseCodeEnglish["b"] = "-..."
        morseCodeEnglish["c"] = "-.-."
        morseCodeEnglish["d"] = "-.."
        morseCodeEnglish["e"] = "."
        morseCodeEnglish["f"] = "..-."
        morseCodeEnglish["g"] = "--."
        morseCodeEnglish["h"] = "...."
        morseCodeEnglish["i"] = ".."
        morseCodeEnglish["j"] = ".---"
        morseCodeEnglish["k"] = "-.-"
        morseCodeEnglish["l"] = ".-.."
        morseCodeEnglish["m"] = "--"
        morseCodeEnglish["n"] = "-."
        morseCodeEnglish["o"] = "---"
        morseCodeEnglish["p"] = ".--."
        morseCodeEnglish["q"] = "--.-"
        morseCodeEnglish["r"] = ".-."
        morseCodeEnglish["s"] = "..."
        morseCodeEnglish["t"] = "-"
        morseCodeEnglish["u"] = "..-"
        morseCodeEnglish["v"] = "...-"
        morseCodeEnglish["w"] = ".--"
        morseCodeEnglish["x"] = "-..-"
        morseCodeEnglish["y"] = "-.--"
        morseCodeEnglish["z"] = "--.."
        // 将 0-9 与 .- 的对应关系存入 morseCodeNumber
        morseCodeNumber["0"] = "-----"
        morseCodeNumber["1"] = ".----"
        morseCodeNumber["2"] = "..---"
        morseCodeNumber["3"] = "...--"
        morseCodeNumber["4"] = "....-"
        morseCodeNumber["5"] = "....."
        morseCodeNumber["6"] = "-...."
        morseCodeNumber["7"] = "--..."
        morseCodeNumber["8"] = "---.."
        morseCodeNumber["9"] = "----."
        // 将 . , ? ! 与 .- 的对应关系存入 morseCodePunctuation
        morseCodePunctuation["."] = ".-.-.-"
        morseCodePunctuation[","] = "--..--"
        morseCodePunctuation["?"] = "..--.."
        morseCodePunctuation["!"] = "-.-.--"

    }

    override fun onResume() {
        super.onResume()
        init()
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
                            .padding(innerPadding).verticalScroll(
                                rememberScrollState()
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    getDarkModeBackgroundColor(
                                        context = this@HelpEnglishMorseCode,
                                        level = 0
                                    )
                                )
                                .padding(innerPadding)
                                .padding(
                                    top = 100.dp, start = 20.dp
                                ),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = resources.getString(R.string.en_code),
                                color = getDarkModeTextColor(this@HelpEnglishMorseCode),
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Column(
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.Start
                        ){
                            for (i in 0 until morseCodeEnglish.size step 2) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(0.5f),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = morseCodeEnglish.keys.elementAt(i),
                                            color = getDarkModeTextColor(this@HelpEnglishMorseCode),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Medium,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier.fillMaxWidth(0.5f)
                                        )
                                        Text(
                                            text = morseCodeEnglish.values.elementAt(i),
                                            color = getDarkModeTextColor(this@HelpEnglishMorseCode),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Medium,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    if (i + 1 < morseCodeEnglish.size) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(1f),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Text(
                                                text = morseCodeEnglish.keys.elementAt(i + 1),
                                                color = getDarkModeTextColor(this@HelpEnglishMorseCode),
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Medium,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier.fillMaxWidth(0.5f)
                                            )
                                            Text(
                                                text = morseCodeEnglish.values.elementAt(i + 1),
                                                color = getDarkModeTextColor(this@HelpEnglishMorseCode),
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Medium,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }


                                    }

                                }


                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().height(2.dp).background(
                                    color = getDarkModeBackgroundColor(
                                        this@HelpEnglishMorseCode,1
                                    ),
                                    shape = RoundedCornerShape(15.dp)
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {

                            }
                            for (i in 0 until morseCodeNumber.size step 2) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(0.5f),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = morseCodeEnglish.keys.elementAt(i),
                                            color = getDarkModeTextColor(this@HelpEnglishMorseCode),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Medium,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier.fillMaxWidth(0.5f)
                                        )
                                        Text(
                                            text = morseCodeEnglish.values.elementAt(i),
                                            color = getDarkModeTextColor(this@HelpEnglishMorseCode),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Medium,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    if (i + 1 < morseCodeEnglish.size) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(1f),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Text(
                                                text = morseCodeEnglish.keys.elementAt(i + 1),
                                                color = getDarkModeTextColor(this@HelpEnglishMorseCode),
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Medium,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier.fillMaxWidth(0.5f)
                                            )
                                            Text(
                                                text = morseCodeEnglish.values.elementAt(i + 1),
                                                color = getDarkModeTextColor(this@HelpEnglishMorseCode),
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Medium,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }


                                    }

                                }


                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().height(2.dp).background(
                                    color = getDarkModeBackgroundColor(
                                        this@HelpEnglishMorseCode,1
                                    ),
                                    shape = RoundedCornerShape(15.dp)
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {

                            }
                            for (i in 0 until morseCodePunctuation.size step 2) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(0.5f),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = morseCodeEnglish.keys.elementAt(i),
                                            color = getDarkModeTextColor(this@HelpEnglishMorseCode),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Medium,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier.fillMaxWidth(0.5f)
                                        )
                                        Text(
                                            text = morseCodeEnglish.values.elementAt(i),
                                            color = getDarkModeTextColor(this@HelpEnglishMorseCode),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Medium,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    if (i + 1 < morseCodeEnglish.size) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(1f),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Text(
                                                text = morseCodeEnglish.keys.elementAt(i + 1),
                                                color = getDarkModeTextColor(this@HelpEnglishMorseCode),
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Medium,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier.fillMaxWidth(0.5f)
                                            )
                                            Text(
                                                text = morseCodeEnglish.values.elementAt(i + 1),
                                                color = getDarkModeTextColor(this@HelpEnglishMorseCode),
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Medium,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier.fillMaxWidth()
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
    }
}
