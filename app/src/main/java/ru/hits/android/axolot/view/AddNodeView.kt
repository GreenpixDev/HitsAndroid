package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import ru.hits.android.axolot.databinding.AddNodeItemBinding

class AddNodeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private val binding = AddNodeItemBinding.inflate(LayoutInflater.from(context), this)
    var addNode = false

    init {
        initComponents()
    }

    fun initComponents() {
        if (!addNode) {
            removeView(binding.btnAddNode)
            removeView(binding.textAddNode)
        }

    }

}