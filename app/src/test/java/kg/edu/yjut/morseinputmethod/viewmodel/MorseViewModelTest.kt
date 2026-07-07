package kg.edu.yjut.morseinputmethod.viewmodel

import kg.edu.yjut.morseinputmethod.entity.MorseCode
import org.junit.Assert.*
import org.junit.Test

class MorseViewModelTest {

    private val morseCodeMap = HashMap<String, String>().apply {
        put("·-", "a")
        put("-···", "b")
        put("-·-·", "c")
        put("-··", "d")
        put("·", "e")
        put("··-·", "f")
        put("--·", "g")
        put("····", "h")
        put("··", "i")
        put("·---", "j")
        put("-·-", "k")
        put("·-··", "l")
        put("--", "m")
        put("-·", "n")
        put("---", "o")
        put("·--·", "p")
        put("--·-", "q")
        put("·-·", "r")
        put("···", "s")
        put("-", "t")
        put("··-", "u")
        put("···-", "v")
        put("·--", "w")
        put("-··-", "x")
        put("-·--", "y")
        put("--··", "z")
        put("-----", "0")
        put("·----", "1")
        put("··---", "2")
        put("···--", "3")
        put("····-", "4")
        put("·····", "5")
        put("-····", "6")
        put("--···", "7")
        put("---··", "8")
        put("----·", "9")
    }

    @Test
    fun `morse code mapping for letters`() {
        assertEquals("a", morseCodeMap["·-"])
        assertEquals("b", morseCodeMap["-···"])
        assertEquals("c", morseCodeMap["-·-·"])
        assertEquals("d", morseCodeMap["-··"])
        assertEquals("e", morseCodeMap["·"])
        assertEquals("f", morseCodeMap["··-·"])
        assertEquals("g", morseCodeMap["--·"])
        assertEquals("h", morseCodeMap["····"])
        assertEquals("i", morseCodeMap["··"])
        assertEquals("j", morseCodeMap["·---"])
        assertEquals("k", morseCodeMap["-·-"])
        assertEquals("l", morseCodeMap["·-··"])
        assertEquals("m", morseCodeMap["--"])
        assertEquals("n", morseCodeMap["-·"])
        assertEquals("o", morseCodeMap["---"])
        assertEquals("p", morseCodeMap["·--·"])
        assertEquals("q", morseCodeMap["--·-"])
        assertEquals("r", morseCodeMap["·-·"])
        assertEquals("s", morseCodeMap["···"])
        assertEquals("t", morseCodeMap["-"])
        assertEquals("u", morseCodeMap["··-"])
        assertEquals("v", morseCodeMap["···-"])
        assertEquals("w", morseCodeMap["·--"])
        assertEquals("x", morseCodeMap["-··-"])
        assertEquals("y", morseCodeMap["-·--"])
        assertEquals("z", morseCodeMap["--··"])
    }

    @Test
    fun `morse code mapping for numbers`() {
        assertEquals("0", morseCodeMap["-----"])
        assertEquals("1", morseCodeMap["·----"])
        assertEquals("2", morseCodeMap["··---"])
        assertEquals("3", morseCodeMap["···--"])
        assertEquals("4", morseCodeMap["····-"])
        assertEquals("5", morseCodeMap["·····"])
        assertEquals("6", morseCodeMap["-····"])
        assertEquals("7", morseCodeMap["--···"])
        assertEquals("8", morseCodeMap["---··"])
        assertEquals("9", morseCodeMap["----·"])
    }

    @Test
    fun `invalid morse code returns null`() {
        assertNull(morseCodeMap["invalid"])
        assertNull(morseCodeMap[""])
        assertNull(morseCodeMap["······"])
    }

    @Test
    fun `keyboard ui state default values`() {
        val state = KeyboardUiState()
        assertFalse(state.isDarkMode)
        assertEquals("", state.inputString)
        assertEquals(0, state.inputMode)
        assertEquals("", state.chineseInput)
        assertFalse(state.isMore)
        assertFalse(state.showCandidate)
        assertTrue(state.isSound)
        assertFalse(state.isVibrator)
        assertEquals(emptyList<MorseCode>(), state.candidates)
        assertEquals(emptyList<MorseCode>(), state.moreCandidates)
        assertEquals(0, state.keyPressAnimation)
    }

    @Test
    fun `keyboard ui state copy updates values`() {
        val state = KeyboardUiState()
        val updated = state.copy(
            inputString = "·-",
            inputMode = 1,
            showCandidate = true
        )
        assertEquals("·-", updated.inputString)
        assertEquals(1, updated.inputMode)
        assertTrue(updated.showCandidate)
        assertEquals("", state.inputString)
    }
}