package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import ru.hits.android.axolot.R
import ru.hits.android.axolot.databinding.BlockRowItemBinding
import java.lang.reflect.Type

class BlockRowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
): ConstraintLayout(context, attrs) {

    private  var binding: BlockRowItemBinding
    var inputField = true
    var definiteInput = false

    var inputTypeField = true
    var descriptionField = true
    var expressionField = true
    var outputField = true

    var description = "BlaBla"
    var expression = "BlaBla"
    var typeBlock = BlockView.Companion.TypeBlock.INTEGER

    init {
        LayoutInflater.from(context).inflate(R.layout.block_item, this, true)
        binding = BlockRowItemBinding.bind(this)

        overrideData()
        initComponents()
        setDescription()
        setDefaultExpression()
        setInputType()
    }

    fun overrideData() {

    }

    private fun initComponents() {
        if (!outputField) {
            binding.rowBlock.removeViewAt(4)
        }
        if (!expressionField) {
            binding.rowBlock.removeViewAt(3)
        }
        if (!descriptionField) {
            binding.rowBlock.removeViewAt(2)
        }
        if (!inputTypeField) {
            binding.rowBlock.removeViewAt(1)
        }
        if (!inputField) {
            binding.rowBlock.removeViewAt(0)
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
            BlockView.Companion.TypeBlock.INTEGER -> "Boolean"
            BlockView.Companion.TypeBlock.STRING -> "Double"
            BlockView.Companion.TypeBlock.BOOLEAN -> "String"
            BlockView.Companion.TypeBlock.FUNCTION -> "Function"
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