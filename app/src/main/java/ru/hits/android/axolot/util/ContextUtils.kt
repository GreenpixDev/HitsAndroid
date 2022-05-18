package ru.hits.android.axolot.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

@ColorInt
fun Context.getThemeColor(@AttrRes attribute: Int) = TypedValue().let {
    theme.resolveAttribute(attribute, it, true)
    it.data
}

@ColorInt
fun Context.getThemeColor(key: String): Int {
    return try {
        getThemeColor(resources.getIdentifier(key, "attr", packageName))
    } catch (e: Resources.NotFoundException) {
        Color.GRAY
    }
}

fun Context.getLocalizedString(key: String): String {
    return try {
        resources.getString(resources.getIdentifier(key, "string", packageName))
    } catch (e: Resources.NotFoundException) {
        key
    }
}