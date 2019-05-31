
import org.junit.Test
import kotlin.test.assertEquals

class CleanStringTest {


    @Test
    fun cleanString(){
        val input = """
        Here's a
        long
        description
        """.trimIndent()

        val expected = "Here's a\\nlong\\ndescription"

        assertEquals(expected, cleanNewLines(input))
    }

    @Test
    fun cleanMultiLine(){
        val input = """
        Here's a


        long
        description
        """.trimIndent()

        val expected = "Here's a\\n\\n\\nlong\\ndescription"

        assertEquals(expected, cleanNewLines(input))
    }

    @Test
    fun otherChars(){
        val input = "Here's a \nlong \rdescription"

        val expected = "Here's a \\nlong \\ndescription"

        assertEquals(expected, cleanNewLines(input))
    }

}