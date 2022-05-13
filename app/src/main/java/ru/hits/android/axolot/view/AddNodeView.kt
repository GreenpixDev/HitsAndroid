package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import ru.hits.android.axolot.databinding.AddNodeItemBinding

class AddNodeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private val binding = AddNodeItemBinding.inflate(LayoutInflater.from(context), this)
    val params = LinearLayoutCompat.LayoutParams(
        LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT
    ).apply {
        gravity = Gravity.END
    }
    var addNode = true

    init {
        binding.addNode.layoutParams = params
        initComponents()
    }

    fun initComponents() {
        if (!addNode) {
            binding.addNode.removeView(binding.btnAddNode)
            binding.addNode.removeView(binding.textAddNode)
        }

    }

}