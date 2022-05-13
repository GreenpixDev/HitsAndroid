package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.block_row_item.view.*
import ru.hits.android.axolot.databinding.BlockRowItemBinding
import ru.hits.android.axolot.interpreter.type.Type


class BlockRowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
): ConstraintLayout(context, attrs) {

    private val binding = BlockRowItemBinding.inflate(LayoutInflater.from(context), this)
    var inputNode = true
    var outputNode = true

    var inputType = true
    var description = true
    var expression = true

    var descriptionText = "BlaBla"
    var expressionText = "BlaBla"

    var definiteInput = false


    var typeBlock: Type = Type.INT

    init {
        initComponents()
        setDescription()
        setDefaultExpression()
        setInputType()
    }

    fun overrideData() {
    }

    fun initComponents() {

        if (!inputNode) {
            binding.rowBlock.removeView(binding.rowBlock.inputNode)
            //binding.rowBlock.removeViewAt(0)
        }

        if (!inputType) {
            binding.rowBlock.removeView(binding.rowBlock.typeSpinner)
            //binding.rowBlock.removeViewAt(1)
        }

        if (!description) {
            binding.rowBlock.removeView(binding.rowBlock.descriptionTextView)
            //binding.rowBlock.removeViewAt(2)
        }

        if (!expression) {
            binding.rowBlock.removeView(binding.rowBlock.expressionTextView)
            //binding.rowBlock.removeViewAt(3)
        }

        if (!outputNode) {
            binding.rowBlock.removeView(binding.rowBlock.outputNode)
            //binding.rowBlock.removeViewAt(4)
        }
    }

    private fun setDefaultExpression() {
        if (expression) binding.expressionTextView.text = expressionText
    }

    private fun setDescription() {
        if (description) binding.descriptionTextView.text = descriptionText
    }

    fun setInputType() {
        if (!definiteInput) return

        val type = when (typeBlock) {
            Type.INT -> "Integer"
            Type.BOOLEAN -> "Boolean"
            Type.FLOAT -> "Double"
            Type.STRING -> "String"
            else -> "Something" //эта строчка бред, не воспринимайте всерьез
        }

        binding.typeSpinner

        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add(type)

        // Настраиваем адаптер
        // Настраиваем адаптер
//        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
//            this, ,
//            R.layout.simple_spinner_item
//        )
//        adapter.setDropDownViewResource(R.layout.)

        // Вызываем адаптер
        //binding.typeSpinner.adapter = adapter
    }
}