package kg.edu.yjut.morseinputmethod.services

import android.annotation.SuppressLint
import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kg.edu.yjut.morseinputmethod.R
import kg.edu.yjut.morseinputmethod.helper.AssetsDatabaseManager
import java.util.Locale


class MorseInputService : InputMethodService()
{
    var isDarkMode = false
    var dots: ImageButton? = null
    var lines: ImageButton? = null
    var inputString: String = ""
    var code = HashMap<String,String>()
    var layout: LinearLayout? = null
    var backBtn: ImageButton? = null
    var lauBtn: ImageButton ? = null
    var imeBtn : ImageButton ? = null
    var imputMode = 0 // 0 表示英文输入法，1 表示中文
    var chineseInput = ""

    var tool_bar: LinearLayout? = null
    var houxuan: LinearLayout? = null

    var more_houxuan: LinearLayout? = null

    var down : ImageButton? = null
    var isMore = false
    var keyboardView: View? = null

    var settings: ImageButton? = null
    var help : ImageButton? = null
    var isSound = true

    @SuppressLint("UseCompatLoadingForDrawables")
    fun initMorseCode(){
        // 初始化莫尔斯代码 表 a-z，0-9，, . ? ! : ; = - _ " ( ) $ & @
        code["·-"] = "a"
        code["-···"] = "b"
           code["-·-·"] = "c"
        code["-··"] = "d"
        code["·"] = "e"
        code["··-·"] = "f"
        code["--·"] = "g"
        code["····"] = "h"
        code["··"] = "i"
        code["·---"] = "j"
        code["-·-"] = "k"
        code["·-··"] = "l"
        code["--"] = "m"
        code["-·"] = "n"
        code["---"] = "o"
        code["·--·"] = "p"
        code["--·-"] = "q"
        code["·-·"] = "r"
        code["···"] = "s"
        code["-"] = "t"
        code["··-"] = "u"
        code["···-"] = "v"
        code["·--"] = "w"
        code["-··-"] = "x"
        code["-·--"] = "y"
        code["--··"] = "z"
        code["-----"] = "0"
        code["·----"] = "1"
        code["··---"] = "2"
        code["···--"] = "3"
        code["····-"] = "4"
        code["·····"] = "5"
        code["-····"] = "6"
        code["--···"] = "7"
        code["---··"] = "8"
        code["----·"] = "9"
        code["·-·-·-"] = "."
        code["--··--"] = ","
        code["··--··"] = "?"
        code["-·-·-·"] = "!"
        code["---···"] = ":"
        code["-·-·-"] = ";"
        code["-···-"] = "="
        code["-····-"] = "-"
        code["··--·-"] = "_"
        code["·-··-·"] = "\""
        code["·--·-·"] = "("
        code["-·--·-"] = ")"
        code["···-··"] = "$"
        code["·-···"] = "&"
        code["·--·-·"] = "@"


    }

    @SuppressLint("Recycle", "Range")
    fun getChineseMorse(code: String){
        // 清空候选框
        layout!!.removeAllViews()
        // 初始化，只需要调用一次
        // 初始化，只需要调用一次
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        val mg = AssetsDatabaseManager.getManager()
        // 通过管理对象获取数据库
        val db1 = mg.getDatabase("mdm.db")
        // 对数据库进行查询 , 查询以 code 开头的 12 条数据
        var cursor = db1.rawQuery("select * from mdm where key like '$code%' limit 5", null)


        println("cursor: " + cursor)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                var chinese = cursor.getString(cursor.getColumnIndex("value"))
                var code = cursor.getString(cursor.getColumnIndex("key"))
                // 添加到layout
                addViewToLayout(chinese, code)
            }
        }






    }

    @SuppressLint("Range")
    fun getMoreChinese(code:String){
        // 清空候选框
        more_houxuan!!.removeAllViews()
        val mg = AssetsDatabaseManager.getManager()
        // 通过管理对象获取数据库
        val db1 = mg.getDatabase("mdm.db")
        // 对数据库进行查询 , 查询以 code 开头的
        var cursor = db1.rawQuery("select * from mdm where key like '$code%'", null)
        var index = 0
        var linearLayout = LinearLayout(this)
        if (cursor != null) {
            while (cursor.moveToNext()) {

                var chinese = cursor.getString(cursor.getColumnIndex("value"))
                var code = cursor.getString(cursor.getColumnIndex("key"))
                // 每 6 个一行
                if (index % 6 == 0) {
                    linearLayout = LinearLayout(this)
                    linearLayout.orientation = LinearLayout.HORIZONTAL
                    // 设置 宽度  高度
                    linearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, numToDp(80))

                    more_houxuan!!.addView(linearLayout)
                }
                //
                addMySubViewToLayout(chinese, linearLayout, code)
                index += 1
            }
        }


    }

    fun addMySubViewToLayout(result : String, currLayout: LinearLayout ,num: String){
        // 将 字符串 显示到候选框, 加载布局 preview.xml
        var preview = layoutInflater.inflate(R.layout.preview, null)
        var textView = preview?.findViewById<TextView>(R.id.text_preview)
        var code = preview?.findViewById<TextView>(R.id.text_code)
        textView = textView!!
        code = code!!

        if (isDarkMode) {
            textView.setTextColor(resources.getColor(R.color.button_text_dark))
            code.setTextColor(resources.getColor(R.color.button_text_dark))
        }else {
            textView.setTextColor(resources.getColor(R.color.button_text_light))
            code.setTextColor(resources.getColor(R.color.button_text_light))
        }
        textView.textSize = numToDp(5).toFloat()
        textView.text = result
        code.text = num



        preview.setOnClickListener {
            // 点击候选框的文字，将文字输出
            val ic = currentInputConnection
            ic.commitText(result, 1)
            // 清空候选框
            cleanLayout()
            // 隐藏候选框
            hideHouxuan()
            // 隐藏 more_houxuan
            keyboardView!!.findViewById<LinearLayout>(R.id.more_bar).visibility = View.GONE
            keyboardView!!.findViewById<LinearLayout>(R.id.keyBar).visibility = View.VISIBLE

            // 清空输入框
            inputString = ""

        }
        // 设置 layout_weight
        val layoutParams = LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)


        // 添加到 LinearLayout
        currLayout.addView(preview)
    }
    override fun onCreateInputView(): View {
        // 创建输入法的视图
        // 这里我们使用了一个自定义的键盘布局
        AssetsDatabaseManager.initManager(this)

        keyboardView = layoutInflater.inflate(R.layout.newkeyboard, null)
        keyboardView = keyboardView!!

        initMorseCode()
        println(code)
        // 检测是否为深色模式
        isDarkMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES
        var dots = keyboardView!!.findViewById<ImageButton>(R.id.dots)
        var lines = keyboardView!!.findViewById<ImageButton>(R.id.lines)
        layout = keyboardView!!.findViewById<LinearLayout>(R.id.pinyin_container)
        layout!!.removeAllViews()

        backBtn = keyboardView!!.findViewById(R.id.back)!!
        lauBtn = keyboardView!!.findViewById(R.id.lau)!!
        imeBtn = keyboardView!!.findViewById(R.id.ime)!!
        tool_bar = keyboardView!!.findViewById(R.id.tool_bar)
        houxuan = keyboardView!!.findViewById(R.id.houxuan)
        more_houxuan = keyboardView!!.findViewById(R.id.more_houxuan)
        down = keyboardView!!.findViewById(R.id.down)

        settings = keyboardView!!.findViewById(R.id.settings)
        help = keyboardView!!.findViewById(R.id.help)

        // 读取 shared preference 中的设置
        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        isSound = sharedPref.getBoolean("sound", true)

        settings!!.setOnClickListener {
            // 打开设置页面
            // 打开设置页面
            val intent = android.content.Intent(this,
                kg.edu.yjut.morseinputmethod.view.SettingsActivity::class.java
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent)
        }
        help!!.setOnClickListener {
            // 打开帮助页面
            val intent = android.content.Intent(this,
                kg.edu.yjut.morseinputmethod.view.HelpActivity::class.java
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent)
        }

        if (isDarkMode) {
            // 如果是深色模式，我们需要修改键盘的背景颜色
            keyboardView!!.findViewById<LinearLayout>(R.id.houxuan).setBackgroundResource(R.drawable.key_background_dark)
            keyboardView!!.findViewById<LinearLayout>(R.id.appBackground).setBackgroundColor(resources.getColor(R.color.key_background_dark))
            tool_bar!!.setBackgroundResource(R.drawable.background_dark)
            houxuan!!.setBackgroundResource(R.drawable.background_dark)
            keyboardView!!.findViewById<LinearLayout>(R.id.more_bar).setBackgroundResource(R.drawable.background_dark)
            keyboardView!!.findViewById<LinearLayout>(R.id.keyBar).setBackgroundResource(R.drawable.background_dark)
            keyboardView!!.findViewById<LinearLayout>(R.id.lau_container).setBackgroundResource(R.drawable.newtext_background_dark)
            // 修改键盘的按键颜色

            dots.setBackgroundResource(R.drawable.key_background_dark)
            lines.setBackgroundResource(R.drawable.key_background_dark)
            backBtn!!.setBackgroundResource(R.mipmap.back_dark)
            lauBtn!!.setBackgroundResource(R.mipmap.english_dark)
            imeBtn!!.setBackgroundResource(R.mipmap.ime_dark)
            down!!.setBackgroundResource(R.mipmap.down_dark)
            settings!!.setBackgroundResource(R.mipmap.settings_dark)
            help!!.setBackgroundResource(R.mipmap.help_dark)

        } else {

        }

        down!!.setOnClickListener {
            // 显示更多候选框
            if (isMore) {
                keyboardView!!.findViewById<LinearLayout>(R.id.more_bar).visibility = View.GONE
                keyboardView!!.findViewById<LinearLayout>(R.id.keyBar).visibility = View.VISIBLE
                isMore = false
            }else{
                keyboardView!!.findViewById<LinearLayout>(R.id.more_bar).visibility = View.VISIBLE
                keyboardView!!.findViewById<LinearLayout>(R.id.keyBar).visibility = View.GONE
                getMoreChinese(chineseInput)
                isMore = true
            }
        }
        backBtn!!.setOnClickListener {
            // 删除一个字符
            val ic = currentInputConnection
            ic.deleteSurroundingText(1, 0)
        }
        imeBtn!!.setOnClickListener {
            // 切换输入法
            val imeManager = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
            imeManager.showInputMethodPicker()
        }
        lauBtn!!.setOnClickListener {
            // 切换输入法
            // 清空候选框
            layout!!.removeAllViews()
            hideHouxuan()
            // 清空临时变量
            inputString = ""
            chineseInput = ""



            if (imputMode == 0) {
                imputMode = 1
                if (isDarkMode) {
                    lauBtn!!.setBackgroundResource(R.mipmap.chinses_dark)
                }else{
                    lauBtn!!.setBackgroundResource(R.mipmap.chinses_light)
                }
            }else{
                imputMode = 0
                if (isDarkMode) {
                    lauBtn!!.setBackgroundResource(R.mipmap.english_dark)
                }else{
                    lauBtn!!.setBackgroundResource(R.mipmap.english_light)
                }
            }
        }
        dots.setOnClickListener {
            // 处理按键的点击事件
            // 这里我们可以处理按键的点击事件
            // 比如，我们可以在这里处理摩尔斯码的输入

            // · 点
            println("您输入了" + "·")
            var input = inputString
            var morseCode = input + "·"
            var result = toStringByMorseCode(morseCode)

            // 播放音效 assets/di.mp3
            // 读取 shared preference 中的设置
            isSound = sharedPref.getBoolean("sound", true)

            if (isSound) {
                val sound = assets.openFd("di.mp3")
                val mediaPlayer = android.media.MediaPlayer()
                mediaPlayer.setDataSource(sound.fileDescriptor, sound.startOffset, sound.length)
                mediaPlayer.prepare()
                mediaPlayer.start()
            }

            println("imputMode: " + imputMode)
            if (imputMode == 0) {
                // 英文输入法

                if (result!="" && result!=null) {
                    // 将 字符串 显示到候选框
                    layout!!.removeAllViews()

                    if (input.length > 6) {
                        inputString = ""
                    }else{
                        inputString = morseCode

                    }
                    addViewToLayout(result, morseCode)
                    // 如果 是 a-z 则再添加 对应的大写字母
                    if (result in "a".."z") {
                        addViewToLayout(result.toUpperCase(Locale.ROOT), morseCode)
                    }

                    // 如果长度超过 6，清空
                    if (input.length > 6) {
                        inputString = ""
                    }else{
                        inputString = morseCode

                    }

                } else {
                    if (input.length > 6) {
                        inputString = ""
                    }else{
                        inputString = morseCode

                    }
                }
            }
            else if (imputMode == 1) {
                // 中文输入法
               if (result!="" && result!=null) {
                   // 如果 result 仅是 数字，则查询对应的中文

                     if (result in "0".."9") {
                         inputString = morseCode

                         chineseInput += result
                            getChineseMorse(chineseInput)
                         inputString = ""

                     }else {
                         // 将 字符串 直接 输出

                            // 清空输入框



                     }
               }else{
                     if (input.length > 6) {
                          inputString = ""
                     }else{
                          inputString = morseCode

                     }
               }
            }

        }

        lines.setOnClickListener {
            // 处理按键的点击事件
            // 这里我们可以处理按键的点击事件
            // 比如，我们可以在这里处理摩尔斯码的输入

            println("您输入了" + "-")
            var input = inputString.toString()
            var morseCode = input + "-"
            var result = toStringByMorseCode(morseCode)
            println("imputMode: " + imputMode)
            // 播放音效 assets/dah.mp3
            isSound = sharedPref.getBoolean("sound", true)


            if (isSound) {
                val sound = assets.openFd("da.mp3")
                val mediaPlayer = android.media.MediaPlayer()
                mediaPlayer.setDataSource(sound.fileDescriptor, sound.startOffset, sound.length)
                mediaPlayer.prepare()
                mediaPlayer.start()
            }

            if (imputMode == 0) {
                // 英文输入法

                if (result!="" && result!=null) {
                    // 将 字符串 显示到候选框
                    layout!!.removeAllViews()

                    addViewToLayout(result, morseCode)
                    // 如果 是 a-z 则再添加 对应的大写字母
                    if (result in "a".."z") {
                        addViewToLayout(result.toUpperCase(Locale.ROOT), morseCode)
                    }

                    // 如果长度超过 6，清空
                    if (input.length > 6) {
                        inputString = ""
                        hideHouxuan()

                    }else{
                        inputString = morseCode

                    }

                } else {
                    if (input.length > 6) {
                        inputString = ""
                        hideHouxuan()
                    }else{
                        inputString = morseCode

                    }
                }
            }
            else if (imputMode == 1) {
                // 中文输入法
                if (result!="" && result!=null) {
                    // 如果 result 仅是 数字，则查询对应的中文
                    if (input.length > 6) {
                        inputString = ""
                        hideHouxuan()
                    }else{
                        inputString = morseCode

                    }
                    if (result in "0".."9") {
                        chineseInput += result
                        getChineseMorse(chineseInput)
                        // 清空input
                        inputString = ""


                    }else {
                        // 将 字符串 直接 输出

                        // 清空输入框



                    }
                }else{
                    if (input.length > 6) {
                        inputString = ""
                        hideHouxuan()
                    }else{
                        inputString = morseCode

                    }
                }
            }
        }


        return keyboardView!!
    }

    fun numToDp(num: Int): Int {
        // 将 px 转换为 dp
        val scale = resources.displayMetrics.density
        return (num * scale + 0.5f).toInt()
    }
    fun cleanLayout(){
        // 清空候选框
        layout!!.removeAllViews()
        // 弹出提示
        Toast.makeText(this, "输入错误", Toast.LENGTH_SHORT).show()
        hideHouxuan()
    }
    fun showHouxuan(){
        // 显示候选框
        houxuan!!.visibility = View.VISIBLE
        tool_bar!!.visibility = View.GONE
    }
    fun hideHouxuan(){
        // 隐藏候选框
        houxuan!!.visibility = View.GONE
        tool_bar!!.visibility = View.VISIBLE
    }
    @SuppressLint("SetTextI18n")
    fun addViewToLayout(result : String, code: String){
        // 设置候选框的显示
        showHouxuan()
        // 将 字符串 显示到候选框, 加载布局 preview.xml
        var preview = layoutInflater.inflate(R.layout.preview, null)
        var textView = preview?.findViewById<TextView>(R.id.text_preview)
        textView = textView!!
        var codeView = preview?.findViewById<TextView>(R.id.text_code)
        codeView = codeView!!

        if (isDarkMode) {
            textView.setTextColor(resources.getColor(R.color.button_text_dark))
            codeView.setTextColor(resources.getColor(R.color.button_text_dark))
        }else {
            textView.setTextColor(resources.getColor(R.color.button_text_light))
            codeView.setTextColor(resources.getColor(R.color.button_text_light))
        }
        textView.text = result
        codeView.text = code
        preview.setOnClickListener {
            // 点击候选框的文字，将文字输出
            val ic = currentInputConnection
            ic.commitText(result, 1)
            // 清空候选框
            cleanLayout()
            // 隐藏候选框
            hideHouxuan()
            // 清空输入框
            inputString = ""

        }
        // 添加到 LinearLayout
        val parent = layout!!.parent as ViewGroup

        layout!!.addView(preview)
    }
    fun toStringByMorseCode(morseCode: String): String {
        // 根据莫尔斯码转换为字符串
        println("morseCode: " + morseCode)
        println("code[morseCode]: " + code[morseCode])

        return code[morseCode] ?: ""

    }
    override fun onStartInput(attribute: EditorInfo?, restarting: Boolean) {



        super.onStartInput(attribute, restarting)
        // 处理输入法的启动逻辑



    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // 处理按键按下事件
        when (keyCode) {
            KeyEvent.KEYCODE_A -> {
                // 处理按键 A 的按下事件
            }
            KeyEvent.KEYCODE_B -> {
                // 处理按键 B 的按下事件
            }
        }
        return super.onKeyDown(keyCode, event)
    }


}

