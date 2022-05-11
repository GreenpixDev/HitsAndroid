package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import ru.hits.android.axolot.R
import ru.hits.android.axolot.databinding.BlockItemBinding
import ru.hits.android.axolot.databinding.BlockRowItemBinding
import ru.hits.android.axolot.interpreter.type.Type

class BlockRowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
): ConstraintLayout(context, attrs) {

    private val binding = BlockRowItemBinding.inflate(LayoutInflater.from(context), this)
    var inputNode = true
    var outputNode = true

    var inputTypeField = true
    var descriptionField = true

    var description = "BlaBla"
    var expression = "BlaBla"

    var definiteInput = false
    var expressionField = true

    var typeBlock:Type = Type.INT

    init {
        //overrideData()
        initComponents()
        setDescription()
        setDefaultExpression()
        setInputType()
    }

    fun overrideData() {

    }

    private fun initComponents() {

        if (!inputNode) {
            binding.rowBlock.removeViewAt(0)
        }

        if (!inputTypeField) {
            binding.rowBlock.removeViewAt(1)
        }

        if (!descriptionField) {
            binding.rowBlock.removeViewAt(2)
        }

        if (!expressionField) {
            binding.rowBlock.removeViewAt(3)
        }

        if (!outputNode) {
            binding.rowBlock.removeViewAt(4)
        }
    }

    private fun setDefaultExpression() {
        if (descriptionField) binding.expressionTextView.text = expression
    }

    private fun setDescription() {
        if (descriptionField) binding.descriptionTextView.text = description
    }

    private fun setInputType() {
        if (!definiteInput) return

        val type = when (typeBlock) {
            Type.INT -> "Integer"
            Type.BOOLEAN -> "Boolean"
            Type.FLOAT -> "Double"
            Type.STRING -> "String"
            else -> "Function" //эта строчка бред, не воспринимайте всерьез
        }

        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add(type)

//        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
//            context, R.layout.simple_spinner_item, spinnerArray
//        )
//
//        binding.typeSpinner.adapter = adapter
    }
}