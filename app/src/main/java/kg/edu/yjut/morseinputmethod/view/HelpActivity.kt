package kg.edu.yjut.morseinputmethod.view

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import kg.edu.yjut.morseinputmethod.R

class HelpActivity : AppCompatActivity() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        // 设置透明状态栏
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
        // 设置透明导航栏
        window.navigationBarColor = Color.TRANSPARENT

        // 隐藏标题栏
        supportActionBar?.hide()

        var morses = findViewById<LinearLayout>(R.id.morses)
        var morsesNumber = findViewById<LinearLayout>(R.id.morsesNumber)
        var morsesPunctuation = findViewById<LinearLayout>(R.id.morsesBD)
        init()
        var index = 0
        var linearLayout = LinearLayout(this)
        for (i in morseCodeEnglish){
            // 每两个 为一行
            if (index % 2 == 0){
                linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                // margin 5
                var layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(5, 5, 5, 5)
                linearLayout.layoutParams = layoutParams


                morses.addView(linearLayout)
            }

            // 读取 code.xml 布局
            var morse = layoutInflater.inflate(R.layout.code, null)
            // 设置字母
            morse.findViewById<TextView>(R.id.text_code).text = i.key
            // 设置摩斯码
            morse.findViewById<TextView>(R.id.code_text).text = i.value
            // 设置 layout_wight
            var newParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            morse.layoutParams = newParams
            linearLayout.addView(morse)
            index++
        }
        index = 0
        for (i in morseCodeNumber){
            if (index % 2 == 0){
                linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                var layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(5, 5, 5, 5)
                linearLayout.layoutParams = layoutParams
                morsesNumber.addView(linearLayout)
            }
            var morse = layoutInflater.inflate(R.layout.code, null)
            morse.findViewById<TextView>(R.id.text_code).text = i.key
            morse.findViewById<TextView>(R.id.code_text).text = i.value
            var newParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            morse.layoutParams = newParams
            linearLayout.addView(morse)
            index++
        }
        index = 0
        for (i in morseCodePunctuation){
            println(i.key + " " + i.value)
            if (index % 2 == 0){
                linearLayout = LinearLayout(this)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                var layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(5, 5, 5, 5)
                linearLayout.layoutParams = layoutParams
                morsesPunctuation.addView(linearLayout)
            }
            var morse = layoutInflater.inflate(R.layout.code, null)
            morse.findViewById<TextView>(R.id.text_code).text = i.key
            morse.findViewById<TextView>(R.id.code_text).text = i.value
            var newParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            morse.layoutParams = newParams
            linearLayout.addView(morse)
            index++
        }

    }
}