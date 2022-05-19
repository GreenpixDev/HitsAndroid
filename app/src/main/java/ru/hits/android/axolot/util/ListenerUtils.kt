package ru.hits.android.axolot.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner

fun Spinner.addItemSelectedListener(
    listener: (
        parent: AdapterView<*>,
        view: View,
        position: Int,
        id: Long
    ) -> Unit
) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            return
        }

        override fun onItemSelected(
            parent: AdapterView<*>,
            view: View,
            position: Int,
            id: Long
        ) {
            listener.invoke(parent, view, position, id)
        }
    }
}

fun EditText.addTextChangedListener(
    listener: (
        title: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) -> Unit
) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(title: Editable) {}

        override fun beforeTextChanged(
            title: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(title: CharSequence, start: Int, before: Int, count: Int) {
            listener.invoke(title, start, before, count)
        }
    })
}

fun EditText.addBeforeTextChangedListener(
    listener: (
        title: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) -> Unit
) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(title: Editable) {}

        override fun beforeTextChanged(
            title: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
            listener.invoke(title, start, count, after)
        }

        override fun onTextChanged(title: CharSequence, start: Int, before: Int, count: Int) {
        }
    })
}

fun EditText.addAfterTextChangedListener(
    listener: (
        title: Editable
    ) -> Unit
) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(title: Editable) {
            listener.invoke(title)
        }

        override fun beforeTextChanged(
            title: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(title: CharSequence, start: Int, before: Int, count: Int) {
        }
    })
}