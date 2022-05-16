package ru.hits.android.axolot.util

import android.content.Context
import android.util.DisplayMetrics

/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 *
 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent px equivalent to dp depending on device density
 *
 * TODO Лена перепишет, она молодец, за код стайл шарит
 */
fun Context.convertDpToPixel(dp: Float): Float {
    return dp * (resources
        .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

/**
 * This method converts device specific pixels to density independent pixels.
 *
 * @param px A value in px (pixels) unit. Which we need to convert into db
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent dp equivalent to px value
 *
 * TODO Лена перепишет, она молодец, за код стайл шарит
 */
fun Context.convertPixelsToDp(px: Float): Float {
    return px / (resources
        .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}