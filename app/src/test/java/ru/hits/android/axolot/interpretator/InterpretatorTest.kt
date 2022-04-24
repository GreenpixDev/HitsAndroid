package ru.hits.android.axolot.interpretator

import org.junit.Test
import java.io.BufferedReader
import java.io.File

class InterpretatorTest {
    @Test
    fun firstTest()
    {
        val code: Array<String>
        val bufferedReader: BufferedReader = File("/home/cyberian/Documents/data/first").bufferedReader()
        val inputString = bufferedReader.use { it.readText() }
        code = inputString.split("\n").toTypedArray()
        println(code)

        val interpretator = Interpretator()
        interpretator.init()
        interpretator.setCode(code)
        interpretator.run()
    }
}