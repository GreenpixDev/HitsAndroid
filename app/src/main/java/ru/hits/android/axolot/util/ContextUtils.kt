package ru.hits.android.axolot.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

@ColorInt
fun Context.getThemeColor(@AttrRes attribute: Int) = TypedValue().let {
    theme.resolveAttribute(attribute, it, true)
    it.data
}

fun Context.getLocalizedString(key: String): String {
    return try {
        resources.getString(resources.getIdentifier(key, "string", packageName))
    } catch (e: Resources.NotFoundException) {
        key
    }
}