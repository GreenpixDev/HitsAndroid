package ru.hits.android.axolot.exception

open class AxolotException @JvmOverloads constructor(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause) {

    constructor(lazyMessage: () -> String) : this(lazyMessage.invoke())

    constructor(lazyMessage: () -> String, cause: Throwable?) : this(lazyMessage.invoke(), cause)

}