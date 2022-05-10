package ru.hits.android.axolot.view

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnDragListener
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_blueprint.*
import kotlinx.android.synthetic.main.activity_blueprint.view.*
import kotlinx.android.synthetic.main.activity_information.view.*
import kotlinx.android.synthetic.main.block_item.view.*
import ru.hits.android.axolot.R
import ru.hits.android.axolot.databinding.ActivityBlueprintBinding
import ru.hits.android.axolot.databinding.BlockItemBinding


@SuppressLint("ViewConstructor")
class BlockView @JvmOverloads constructor(
    isWhat: String?,
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
): ConstraintLayout(context, attrs, defstyleAttr, defstyleRes) {

    private val binding = BlockItemBinding.inflate(LayoutInflater.from(context), this)
    //var rows = listOf(binding.blockField.getChildAt(1) as BlockRowView)

    companion object{
        enum class TypeBlock { INTEGER, STRING, BOOLEAN, FUNCTION }

    }

    init {
        //создание атрибутов
        initializeAttributes(attrs, defstyleAttr, defstyleRes)

        initNode(isWhat)
    }

    @SuppressLint("Recycle")
    private fun initializeAttributes(attrs: AttributeSet?, defstyleAttr: Int, defstyleRes: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.BlockView, defstyleAttr, defstyleRes)

        with(binding) {
            val bgBlock = typedArray.getColor(R.styleable.BlockView_bgBlock, Color.WHITE)
            block.backgroundTintList = ColorStateList.valueOf(bgBlock)
        }

        typedArray.recycle()
    }

    @SuppressLint("ResourceType")
    private fun initNode(isWhat: String?) {
        when(isWhat) {
            "isVariable" -> addNode(1, 1)
            "isCycle" -> {
                addNode(1, 1)
                addNode(2, 1)
            }
            "isCondition" -> {
                addNode(1, 2)
                addNode(2, 2)
                addNode(3, 1)
            }
        }
    }

    private fun addNode(level: Int, count: Int) {
        val node = ImageButton(context)
        val params = ConstraintLayout.LayoutParams(
            block.width - 40,
            block.height - 40
        )

        when(level) {
            1 -> {
                var k = 0
                while (k < count){
                    binding.level1.addView(node, params)
                    k++
                }
            }

            2 -> {
                var k = 0
                while (k < count){
                    binding.level2.addView(node, params)
                    //как-то добавить несколько node в один row
                    k++
                }
            }

            3 -> {
                var k = 0
                while (k < count){
                    binding.level3.addView(node, params)

                    k++
                }
            }
        }

        //задать параметры расположения
        node.setImageResource(R.drawable.button_shape_circle_stroke)
    }

//    private fun addSetRows(){
//        for (row in rows){
//            binding.blockField.addView(row)
//        }
//    }

//    private fun attachBlockDragListener(){
//        binding.block.setOnLongClickListener {view: View ->
//
//            val clipText = "This is out ClipData text"
//            val item = ClipData.Item(clipText)
//
//            val dataToDrag = ClipData (clipText, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item)
//
//            val blockShadow = View.DragShadowBuilder(view)
//
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//                //support pre-Nougat versions
//                @Suppress("DEPRECATION")
//                view.startDragAndDrop(dataToDrag, blockShadow, view, 0)
//            } else {
//                //supports Nougat and beyond
//                view.startDragAndDrop(dataToDrag, blockShadow, view, 0)
//            }
//
//            view.visibility = View.INVISIBLE
//
//            true
//        }
//    }
}