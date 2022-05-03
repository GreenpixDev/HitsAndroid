package ru.hits.android.axolot.benchmark

import org.junit.Test
import kotlin.random.Random

class ArrayTest {

    companion object {
        const val SIZE = 1_000
        const val RANDOM = 10_000
    }

    @Test
    fun bubbleSortTest() {
        val array = Array(SIZE) { Random.nextInt(RANDOM) }

        val timestamp = System.currentTimeMillis()
        for (i in 2..array.size) {
            for (j in 0..array.size - i) {
                if (array[j] > array[j + 1]) {
                    val tmp = array[j]
                    array[j] = array[j + 1]
                    array[j + 1] = tmp
                }
            }
        }
        println("Calc bubbleSort for ${System.currentTimeMillis() - timestamp} ms")
    }
}