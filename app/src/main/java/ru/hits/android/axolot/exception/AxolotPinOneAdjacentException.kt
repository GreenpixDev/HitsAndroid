package ru.hits.android.axolot.exception

import ru.hits.android.axolot.blueprint.element.pin.PinToOne

open class AxolotPinOneAdjacentException @JvmOverloads constructor(
    val pin: PinToOne,
    message: String? = null,
    cause: Throwable? = null
) : AxolotPinException(message, cause) {

    constructor(pin: PinToOne, lazyMessage: () -> String) : this(pin, lazyMessage.invoke())

    constructor(pin: PinToOne, lazyMessage: () -> String, cause: Throwable?) : this(
        pin,
        lazyMessage.invoke(),
        cause
    )

}