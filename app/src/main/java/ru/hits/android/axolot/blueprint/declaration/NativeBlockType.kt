package ru.hits.android.axolot.blueprint.declaration

import android.content.Context
import ru.hits.android.axolot.R
import ru.hits.android.axolot.blueprint.declaration.pin.DeclaredPin
import ru.hits.android.axolot.util.getLocalizedString
import ru.hits.android.axolot.util.getThemeColor

/**
 * Нативная декларация блока
 */
open class NativeBlockType(
    override val simpleName: String,
    override val declaredPins: List<DeclaredPin>
) : BlockType {

    constructor(simpleName: String, vararg pins: DeclaredPin)
            : this(simpleName, pins.toList())

    override val fullName: String
        get() = "native.$simpleName"

    override fun getDisplayName(context: Context): String {
        return context.getLocalizedString(fullName)
    }

    override fun getDisplayColor(context: Context): Int {
        val blockTitleToColor = mapOf(
            Regex("^native\\.main$") to R.attr.colorBlockHeaderMain,
            Regex("^native\\..+\\.boolean.*") to R.attr.colorVariableBoolean,
            Regex("^native\\..+\\.int.*") to R.attr.colorVariableInt,
            Regex("^native\\..+\\.float.*") to R.attr.colorVariableFloat,
            Regex("^native\\..+\\.string.*") to R.attr.colorVariableString
        )
        for (it in blockTitleToColor) {
            if (it.key matches fullName) {
                return context.getThemeColor(it.value)
            }
        }
        return 0
    }
}