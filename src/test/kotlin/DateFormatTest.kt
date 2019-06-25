
import org.junit.Test
import kotlin.test.assertEquals

class DateFormatTest {


    @Test
    fun formatEmptyDate(){
        assertEquals("", formatDate(""))
    }

    @Test
    fun formatDate(){
        val input = "2019-06-24T16:37:37Z".trimIndent()
        val expected = "2019-06-24 16:37:37"

        assertEquals(expected, formatDate(input))
    }

}