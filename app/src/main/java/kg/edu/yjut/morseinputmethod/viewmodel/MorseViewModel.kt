package kg.edu.yjut.morseinputmethod.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.view.inputmethod.InputConnection
import kg.edu.yjut.morseinputmethod.helper.AssetsDatabaseManager
import kg.edu.yjut.morseinputmethod.entity.MorseCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class KeyboardUiState(
    val isDarkMode: Boolean = false,
    val inputString: String = "",
    val inputMode: Int = 0,
    val chineseInput: String = "",
    val isMore: Boolean = false,
    val showCandidate: Boolean = false,
    val isSound: Boolean = true,
    val isVibrator: Boolean = false,
    val candidates: List<MorseCode> = emptyList(),
    val moreCandidates: List<MorseCode> = emptyList(),
    val keyPressAnimation: Int = 0,
    val keyingMode: Int = 0,
    val pressedKey: Int = 0,
    val morseBuffer: String = "",
    val textResult: String = "",
    val inputEnabled: Boolean = true
)

class MorseViewModel(private val context: Context) {

    private val _uiState = MutableStateFlow(KeyboardUiState())
    val uiState: StateFlow<KeyboardUiState> = _uiState.asStateFlow()

    private val morseCodeMap = HashMap<String, String>()
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: android.os.Vibrator? = null
    private val viewModelScope = CoroutineScope(Dispatchers.Main + Job())

    private var pressStartTime: Long = 0
    private var lastInputTime: Long = 0
    private var autoSpaceJob: Job? = null

    var inputConnection: InputConnection? = null

    init {
        initMorseCode()
        loadSettings()
        initDarkMode()
        initVibrator()
        AssetsDatabaseManager.initManager(context)
    }

    private fun initDarkMode() {
        val isDark = context.resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES
        _uiState.update { it.copy(isDarkMode = isDark) }
    }

    @Suppress("DEPRECATION")
    private fun initVibrator() {
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
    }

    private fun initMorseCode() {
        morseCodeMap["·-"] = "a"
        morseCodeMap["-···"] = "b"
        morseCodeMap["-·-·"] = "c"
        morseCodeMap["-··"] = "d"
        morseCodeMap["·"] = "e"
        morseCodeMap["··-·"] = "f"
        morseCodeMap["--·"] = "g"
        morseCodeMap["····"] = "h"
        morseCodeMap["··"] = "i"
        morseCodeMap["·---"] = "j"
        morseCodeMap["-·-"] = "k"
        morseCodeMap["·-··"] = "l"
        morseCodeMap["--"] = "m"
        morseCodeMap["-·"] = "n"
        morseCodeMap["---"] = "o"
        morseCodeMap["·--·"] = "p"
        morseCodeMap["--·-"] = "q"
        morseCodeMap["·-·"] = "r"
        morseCodeMap["···"] = "s"
        morseCodeMap["-"] = "t"
        morseCodeMap["··-"] = "u"
        morseCodeMap["···-"] = "v"
        morseCodeMap["·--"] = "w"
        morseCodeMap["-··-"] = "x"
        morseCodeMap["-·--"] = "y"
        morseCodeMap["--··"] = "z"
        morseCodeMap["-----"] = "0"
        morseCodeMap["·----"] = "1"
        morseCodeMap["··---"] = "2"
        morseCodeMap["···--"] = "3"
        morseCodeMap["····-"] = "4"
        morseCodeMap["·····"] = "5"
        morseCodeMap["-····"] = "6"
        morseCodeMap["--···"] = "7"
        morseCodeMap["---··"] = "8"
        morseCodeMap["----·"] = "9"
        morseCodeMap["·-·-·-"] = "."
        morseCodeMap["--··--"] = ","
        morseCodeMap["··--··"] = "?"
        morseCodeMap["-·-·-·"] = "!"
        morseCodeMap["---···"] = ":"
        morseCodeMap["-·-·-"] = ";"
        morseCodeMap["-···-"] = "="
        morseCodeMap["-····-"] = "-"
        morseCodeMap["··--·-"] = "_"
        morseCodeMap["·-··-·"] = "\""
        morseCodeMap["·--·-·"] = "("
        morseCodeMap["-·--·-"] = ")"
        morseCodeMap["···-··"] = "$"
        morseCodeMap["·-···"] = "&"
        morseCodeMap["·--·-·"] = "@"
    }

    private fun loadSettings() {
        val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        _uiState.update { 
            it.copy(
                isSound = sharedPref.getBoolean("sound", true),
                isVibrator = sharedPref.getBoolean("vibrator", false),
                keyingMode = sharedPref.getInt("keyingMode", 0),
                inputEnabled = sharedPref.getBoolean("enabled", true)
            )
        }
    }

    fun startManualPress() {
        pressStartTime = System.currentTimeMillis()
        lastInputTime = System.currentTimeMillis()
        _uiState.update { it.copy(pressedKey = 1) }
        cancelAutoSpace()
    }

    fun endManualPress() {
        val duration = System.currentTimeMillis() - pressStartTime
        pressStartTime = 0
        _uiState.update { it.copy(pressedKey = 0) }

        if (duration < 300) {
            inputMorse("·")
        } else {
            inputMorse("-")
        }
        scheduleAutoSpace()
    }

    fun onDotClick() {
        inputMorse("·")
        triggerKeyPressAnimation(1)
        scheduleAutoSpace()
    }

    fun onDashClick() {
        inputMorse("-")
        triggerKeyPressAnimation(2)
        scheduleAutoSpace()
    }

    private fun inputMorse(symbol: String) {
        playSound(if (symbol == "·") "di.mp3" else "da.mp3")
        vibrate(if (symbol == "·") 100 else 500)
        
        val currentBuffer = _uiState.value.morseBuffer
        val newBuffer = currentBuffer + symbol
        
        _uiState.update { it.copy(morseBuffer = newBuffer) }
        updateDisplay()
    }

    private fun updateDisplay() {
        val buffer = _uiState.value.morseBuffer
        val words = buffer.split(" ")
        val decoded = words.map { w -> morseCodeMap[w] ?: if (w.isEmpty()) "" else "?" }.joinToString("")
        _uiState.update { it.copy(textResult = decoded) }
    }

    private fun scheduleAutoSpace() {
        cancelAutoSpace()
        autoSpaceJob = viewModelScope.launch {
            delay(1000)
            addSpace()
        }
    }

    private fun cancelAutoSpace() {
        autoSpaceJob?.cancel()
        autoSpaceJob = null
    }

    fun addSpace() {
        val currentBuffer = _uiState.value.morseBuffer
        if (currentBuffer.isNotEmpty() && !currentBuffer.endsWith(" ")) {
            _uiState.update { it.copy(morseBuffer = currentBuffer + " ") }
            updateDisplay()
        }
    }

    fun confirmChar() {
        val buffer = _uiState.value.morseBuffer
        if (buffer.isEmpty()) return
        
        inputConnection?.commitText(_uiState.value.textResult, 1)
        clearInput()
    }

    private fun triggerKeyPressAnimation(keyType: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(keyPressAnimation = keyType) }
            delay(150)
            _uiState.update { it.copy(keyPressAnimation = 0) }
        }
    }

    fun onBackspace() {
        val buffer = _uiState.value.morseBuffer
        if (buffer.isEmpty()) return
        
        val newBuffer = buffer.dropLast(1)
        _uiState.update { it.copy(morseBuffer = newBuffer) }
        updateDisplay()
    }

    fun clearInput() {
        _uiState.update { 
            it.copy(
                morseBuffer = "",
                textResult = "",
                inputString = "",
                chineseInput = "",
                candidates = emptyList(),
                moreCandidates = emptyList(),
                showCandidate = false,
                isMore = false
            )
        }
        cancelAutoSpace()
    }

    fun toggleInputMode() {
        clearInput()
        val newMode = if (_uiState.value.inputMode == 0) 1 else 0
        _uiState.update { it.copy(inputMode = newMode) }
    }

    fun toggleKeyingMode() {
        val newMode = if (_uiState.value.keyingMode == 0) 1 else 0
        _uiState.update { it.copy(keyingMode = newMode) }
        
        val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        sharedPref.edit().putInt("keyingMode", newMode).apply()
        
        clearInput()
    }

    fun toggleMore() {
        val newIsMore = !_uiState.value.isMore
        _uiState.update { it.copy(isMore = newIsMore) }
        if (newIsMore) {
            queryMoreChineseCandidates(_uiState.value.chineseInput)
        }
    }

    private fun queryChineseCandidates(code: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val mg = AssetsDatabaseManager.getManager()
                val db = mg.getDatabase("mdm.db")
                val cursor = db.rawQuery("select * from mdm where key like '$code%' limit 5", null)
                val list = mutableListOf<MorseCode>()
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        val chinese = cursor.getString(cursor.getColumnIndexOrThrow("value"))
                        val key = cursor.getString(cursor.getColumnIndexOrThrow("key"))
                        list.add(MorseCode(key, chinese))
                    }
                    cursor.close()
                }
                withContext(Dispatchers.Main) {
                    _uiState.update { 
                        it.copy(
                            candidates = list,
                            showCandidate = list.isNotEmpty()
                        )
                    }
                }
            }
        }
    }

    fun queryMoreChineseCandidates(code: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val mg = AssetsDatabaseManager.getManager()
                val db = mg.getDatabase("mdm.db")
                val cursor = db.rawQuery("select * from mdm where key like '$code%'", null)
                val list = mutableListOf<MorseCode>()
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        val chinese = cursor.getString(cursor.getColumnIndexOrThrow("value"))
                        val key = cursor.getString(cursor.getColumnIndexOrThrow("key"))
                        list.add(MorseCode(key, chinese))
                    }
                    cursor.close()
                }
                withContext(Dispatchers.Main) {
                    _uiState.update { it.copy(moreCandidates = list) }
                }
            }
        }
    }

    fun onCandidateClick(candidate: MorseCode) {
        inputConnection?.commitText(candidate.word, 1)
        clearInput()
    }

    fun toggleInputEnabled() {
        val newEnabled = !_uiState.value.inputEnabled
        _uiState.update { it.copy(inputEnabled = newEnabled) }
        
        val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("enabled", newEnabled).apply()
    }

    private fun playSound(assetName: String) {
        if (!_uiState.value.isSound) return
        try {
            mediaPlayer?.release()
            val afd = context.assets.openFd(assetName)
            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Suppress("DEPRECATION")
    private fun vibrate(duration: Long) {
        if (!_uiState.value.isVibrator) return
        try {
            vibrator?.vibrate(duration)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateSettings(sound: Boolean, vibrator: Boolean) {
        val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        sharedPref.edit().apply {
            putBoolean("sound", sound)
            putBoolean("vibrator", vibrator)
            apply()
        }
        _uiState.update { it.copy(isSound = sound, isVibrator = vibrator) }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        vibrator?.cancel()
        cancelAutoSpace()
    }
}