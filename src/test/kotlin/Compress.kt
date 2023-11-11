import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestCompress {
    @Test
    fun oneSymbolCompressTest() {
        assertEquals(compress("A"), "A±1")
        assertEquals(compress("1"), "1±1")
    }
    @Test
    fun sameSymbolsCompressTest() {
        assertEquals(compress("aaaaa"), "a±5")
        assertEquals(compress("33"), "3±2")
    }
    @Test
    fun completeCompressTest() {
        assertEquals(compress("AAAAABBB#####"), "A±5B±3#±5")
        assertEquals(compress("AA6AAABBB#####T"), "A±26±1A±3B±3#±5T±1")
        assertEquals(compress("abcdaabbccdd1111111111"), "a±1b±1c±1d±1a±2b±2c±2d±21±10")
    }
    @Test
    fun badInputCompressTest() {
        assertThrows<BadFormatException>{compress("±")}
        assertThrows<BadFormatException>{compress("aab±3cc")}
    }
    @Test
    fun decompressSimpleTest() {
        assertEquals(decompress("x±4"), "xxxx")
        assertEquals(decompress("a±21±3a±1b±1c±2"), "aa111abcc")
        assertEquals(decompress("A±5B±3#±5"), "AAAAABBB#####")
    }
    @Test
    fun decompress2DigitTest() {
        assertEquals(decompress("a±111±2"), "aaaaaaaaaaa11")
        assertEquals(decompress("&±10±1"), "&0")
        assertEquals(decompress("T±999±999"), "T".repeat(99) + "9".repeat(999))
    }
    @Test
    fun badInputDecompressTest() {
        assertThrows<BadFormatException>{decompress("a±2b±±1")}
        assertThrows<BadFormatException>{decompress("ab±2")}
        assertThrows<BadFormatException>{decompress("a±3b±2cd±1")}
        assertThrows<BadFormatException>{decompress("a±1x±0")}
        assertThrows<BadFormatException>{decompress("a±1x±-2")}
    }
    private fun compressDecompressComparison(originalString: String) {
        assertEquals(decompress(compress(originalString)), originalString)
    }
    @Test
    fun compressDecompressTest() {
        compressDecompressComparison("AA6AAABBB#####T")
        compressDecompressComparison("bbcaa32ee1saa")
        compressDecompressComparison("aaaaaaaaaaaaaaa")
        compressDecompressComparison("1".repeat(111))
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        for (i in 1 ..< 100) {
            compressDecompressComparison((1..10000)
                .map { allowedChars.random() }
                .joinToString(""))
        }
    }
}