package ru.hits.android.axolot.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import ru.hits.android.axolot.R
import ru.hits.android.axolot.databinding.BlockItemBinding


@SuppressLint("ViewConstructor")
class BlockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
): ConstraintLayout(context, attrs, defstyleAttr, defstyleRes) {

    private val binding: BlockItemBinding

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.block_item, this, true)
        binding = BlockItemBinding.bind(this)

        initializeAttributes(attrs, defstyleAttr, defstyleRes)
    }

    @SuppressLint("Recycle")
    private fun initializeAttributes(attrs: AttributeSet?, defstyleAttr: Int, defstyleRes: Int) {
        if (attrs == null) return

        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.BlockView, defstyleAttr, defstyleRes)

//        with(binding) {
//            val blockTitle = typedArray.getString(R.styleable.BlockView_blockTitle)
//            binding.title.text = blockTitle ?: "BLOCK"
//
//            val blockPropertyText = typedArray.getString(R.styleable.BlockView_blockPropertyText)
//            binding.property.text = blockPropertyText

//            val blockConnection = typedArray.getBoolean(R.styleable.BlockView_btnConnection, false)
//            if (blockConnection) {
//                binding.node.backgroundTintList = ColorStateList.createFromXml(resources, XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
//            } else {
//                binding.node.backgroundTintList = ColorStateList.createFromXml("@drawable/button_shape_circle2")
//            }

        //}

        typedArray.recycle()
    }
}
