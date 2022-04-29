package ru.hits.android.axolot.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_second.view.*
import ru.hits.android.axolot.R
import ru.hits.android.axolot.databinding.BlockItemBinding


class BlockView(
    context: Context,
    attrs: AttributeSet?,
    defstyleAttr: Int,
    defStyleRes: Int
): ConstraintLayout(context, attrs, defstyleAttr, defStyleRes) {

    private val binding: BlockItemBinding

    constructor(context: Context, attrs: AttributeSet?, defstyleAttr: Int) : this(context, attrs, defstyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.block_item, this, true)
        binding = BlockItemBinding.bind(this)

        initializeAttributes(attrs, defstyleAttr, defStyleRes)
    }

    private fun initializeAttributes(attrs:AttributeSet?, defstyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.BlockView, defstyleAttr, defStyleRes)
        with(binding) {
            val blockTitle = typedArray.getString(R.styleable.BlockView_blockTitle)
            binding.title.text = blockTitle?: "BLOCK"

            val blockBackgroundColor = typedArray.getColor(R.styleable.BlockView_blockBackgroundColor, Color.BLUE)
            block.backgroundTintList = ColorStateList.valueOf(blockBackgroundColor)

            val blockPropertyText = typedArray.getString(R.styleable.BlockView_blockPropertyText)
            binding.property.text = blockPropertyText
        }

        typedArray.recycle()
    }
}
