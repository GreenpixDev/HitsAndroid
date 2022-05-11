package ru.hits.android.axolot.benchmark

import org.junit.Test

class CollectionTest {

    companion object {
        const val SIZE = 1_000_000
        const val TESTS = 10_000_000
    }

    @Test
    fun mapGetTest() {
        val map = hashMapOf<Any, Any>()

        for (i in 1..SIZE) {
            map[i] = Any()
        }

        val timestamp = System.currentTimeMillis()
        for (i in 1..TESTS) {
            val result = map[i % SIZE]
        }
        println("Calc mapGet for ${System.currentTimeMillis() - timestamp} ms")
    }

    @Test
    fun listGetTest() {
        val list = arrayListOf<Any>()

        for (i in 1..SIZE) {
            list.add(Any())
        }

        val timestamp = System.currentTimeMillis()
        for (i in 1..TESTS) {
            val result = list[i % SIZE]
        }
        println("Calc listGet for ${System.currentTimeMillis() - timestamp} ms")
    }

    @Test
    fun mapPutTest() {
        val map = hashMapOf<Any, Any>()

        val timestamp = System.currentTimeMillis()
        for (i in 1..SIZE) {
            map[i] = Any()
        }
        println("Calc mapPut for ${System.currentTimeMillis() - timestamp} ms")
    }

    @Test
    fun listAddTest() {
        val list = arrayListOf<Any>()

        val timestamp = System.currentTimeMillis()
        for (i in 1..SIZE) {
            list.add(Any())
        }
        println("Calc listAdd for ${System.currentTimeMillis() - timestamp} ms")
    }
}