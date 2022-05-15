package ru.hits.android.axolot.util

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent

/**
 * Find the [View] at the provided [x] and [y] coordinates within the [ViewGroup].
 */
fun ViewGroup.findViewAt(position: Vec2): View? {
    for (i in 0 until childCount) {
        val child = getChildAt(i)

        if (child is ViewGroup) {
            val childPos = Vec2f(child.x, child.y)
            val foundView = child.findViewAt(position - childPos)
            if (foundView != null && foundView.isShown) {
                return foundView
            }
        } else {
            val rect = Rect()

            child.getHitRect(rect)

            if (rect.contains(position.x.toInt(), position.y.toInt())) {
                return child
            }
        }
    }
    return null
}

fun View.getPositionRelative(view: ViewParent): Vec2f {
    var position = Vec2f(x, y)
    var current = this

    while (current.parent != null && current.parent is View && current.parent.parent != view) {
        current = current.parent as View
        position += Vec2f(current.x, current.y)
    }

    return position
}