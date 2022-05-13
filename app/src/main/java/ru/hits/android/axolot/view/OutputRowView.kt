package ru.hits.android.axolot.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TableRow
import ru.hits.android.axolot.databinding.OutputRowItemBinding

@SuppressLint("RtlHardcoded")
class OutputRowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private val binding = OutputRowItemBinding.inflate(LayoutInflater.from(context), this)
    var outputNode = true
    var description = true
    var descriptionText = "BlaBla"

    init {
        val paramsOutput = TableRow.LayoutParams()
        paramsOutput.gravity = Gravity.RIGHT

        initComponents()
        setDescription()
    }

    fun initComponents() {
        if (!outputNode) {
            binding.outputRowBlock.removeView(binding.outputNode)
        }

        if (!description) {
            binding.descriptionTextView.visibility = GONE
        }

        if (description) {
            binding.descriptionTextView.visibility = VISIBLE
        }

        setDescription()
    }

    private fun setDescription() {
        if (description) binding.descriptionTextView.text = descriptionText
    }

}