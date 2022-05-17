package ru.hits.android.axolot.blueprint.element.pin

import ru.hits.android.axolot.blueprint.element.AxolotOwner
import java.io.Serializable

interface Pin : Serializable {

    val name: String

    val owner: AxolotOwner

}