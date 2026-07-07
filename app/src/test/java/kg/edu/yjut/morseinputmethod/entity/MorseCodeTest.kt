package kg.edu.yjut.morseinputmethod.entity

import org.junit.Assert.*
import org.junit.Test

class MorseCodeTest {

    @Test
    fun `morse code data class creation`() {
        val morseCode = MorseCode(code = "·-", word = "a")
        assertEquals("·-", morseCode.code)
        assertEquals("a", morseCode.word)
    }

    @Test
    fun `morse code equality`() {
        val code1 = MorseCode(code = "·-", word = "a")
        val code2 = MorseCode(code = "·-", word = "a")
        val code3 = MorseCode(code = "-·", word = "n")
        
        assertEquals(code1, code2)
        assertNotEquals(code1, code3)
    }

    @Test
    fun `morse code copy`() {
        val original = MorseCode(code = "·-", word = "a")
        val copied = original.copy(word = "A")
        
        assertEquals("·-", copied.code)
        assertEquals("A", copied.word)
        assertEquals("a", original.word)
    }

    @Test
    fun `morse code toString`() {
        val morseCode = MorseCode(code = "·-", word = "a")
        val stringRepresentation = morseCode.toString()
        assertTrue(stringRepresentation.contains("·-"))
        assertTrue(stringRepresentation.contains("a"))
    }
}