package ru.hits.android.axolot.util

class SuppliedThreadLocal<T>(private val supplier: () -> T) : ThreadLocal<T>() {

    override fun get(): T {
        var result: T? = super.get()

        if (result == null) {
            result = supplier.invoke()
            super.set(result)
        }

        return result!!
    }
}