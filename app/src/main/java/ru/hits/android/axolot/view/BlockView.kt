package ru.hits.android.axolot.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import ru.hits.android.axolot.R
import ru.hits.android.axolot.blueprint.declaration.BlockType
import ru.hits.android.axolot.databinding.BlockItemBinding
import ru.hits.android.axolot.interpreter.type.Type


@SuppressLint("ViewConstructor")
class BlockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
): ConstraintLayout(context, attrs, defstyleAttr, defstyleRes) {

    private val binding = BlockItemBinding.inflate(LayoutInflater.from(context), this)
    lateinit var typeBlock: BlockType
    lateinit var typeVar: Type

    //TODO: сделать 2 массива inputNode и outputNode

    fun addInput() {

    }

    fun addOutput() {

    }

    fun removeInput() {

    }

    fun removeOutput() {

    }

    fun connectInput() {

    }

    fun connectOutput() {

    }

    fun disConnectInput() {

    }

    fun disConnectOutput() {

    }

    @SuppressLint("Recycle")
    private fun initializeAttributes(attrs: AttributeSet?, defstyleAttr: Int, defstyleRes: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.BlockView, defstyleAttr, defstyleRes)

        with(binding) {
//            val titleBlock = typedArray.getString(R.styleable.BlockView_blockTitle)
//            block.title.text = titleBlock
            //val bgBlock = typedArray.getColor(R.styleable.BlockView_bgBlock, Color.WHITE)
            //block.backgroundTintList = ColorStateList.valueOf(bgBlock)
        }

        typedArray.recycle()
    }

    init {
        //создание атрибутов
        initializeAttributes(attrs, defstyleAttr, defstyleRes)

    }
}