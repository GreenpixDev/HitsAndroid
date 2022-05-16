package ru.hits.android.axolot.util

import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent

/**
 * Находит [View] по координатам [position] относительно [ViewGroup].
 */
fun ViewGroup.findViewAt(position: Vec2, filter: (View) -> Boolean = { true }): View? {
    for (i in 0 until childCount) {
        val child = getChildAt(i)

        if (child is ViewGroup) {
            val childPos = Vec2f(child.x, child.y)
            val foundView = child.findViewAt(position - childPos)
            if (foundView != null && foundView.isShown) {
                return foundView
            }
        } else if (filter.invoke(child)) {
            val rect = Rect()

            child.getHitRect(rect)

            if (rect.contains(position.x.toInt(), position.y.toInt())) {
                return child
            }
        }
    }
    return null
}

/**
 * Находит координаты [Vec2f] у [view] относительно [ViewParent].
 */
fun ViewParent.findRelativePosition(view: View): Vec2f {
    var position = Vec2f(view.x, view.y)
    var current = view

    while (current.parent != null && current.parent is View && current.parent != this) {
        current = current.parent as View
        position += Vec2f(current.x, current.y)
    }

    return position
}

/**
 * Получить координаты [Vec2f] у [View] относительно родителя [View].
 */
var View.position: Vec2f
    get() = Vec2f(x, y)
    set(value) {
        x = value.x
        y = value.y
    }

/**
 * Получить координаты центра [Vec2f] у [View] относительно самой [View].
 */
val View.center: Vec2f
    get() = Vec2f(width, height) / 2

/**
 * Получить сырые координаты [Vec2f] курсора относительно экрана телефона
 */
@Deprecated(
    "Используйте всегда position, rawPosition работает не всегда корректно",
    ReplaceWith("position")
)
val MotionEvent.rawPosition: Vec2f
    get() = Vec2f(rawX, rawY)

/**
 * Получить координаты [Vec2f] курсора относительно вьюшки, по которой кликнул пользователь
 */
val MotionEvent.position: Vec2f
    get() = Vec2f(x, y)