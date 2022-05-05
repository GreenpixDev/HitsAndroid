package ru.hits.android.axolot.view

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_blueprint.view.*
import ru.hits.android.axolot.R
import ru.hits.android.axolot.databinding.ActivityBlueprintBinding
import ru.hits.android.axolot.databinding.BlockItemBinding


@SuppressLint("ViewConstructor")
class BlockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
): ConstraintLayout(context, attrs, defstyleAttr, defstyleRes) {

    private val binding: BlockItemBinding
    private lateinit var bin: ActivityBlueprintBinding

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.block_item, this, true)
        binding = BlockItemBinding.bind(this)

        //создание атрибутов
        initializeAttributes(attrs, defstyleAttr, defstyleRes)

        //перемещение
        changeMove()
    }

    private fun changeMove() {
        //переменная для drag and drop
        val blockDragListener = OnDragListener { view, dragEvent->
            val draggableItem = dragEvent.localState as View

            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    true
                }

                DragEvent.ACTION_DRAG_ENTERED -> {
                    binding.block.alpha = 0.3f
                    true
                }

                DragEvent.ACTION_DRAG_LOCATION -> {
                    true
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    binding.block.alpha = 1.0f
                    //draggableItem.visibility = View.VISIBLE

                    view.invalidate()
                    true
                }

                DragEvent.ACTION_DROP -> {
                    binding.block.alpha = 1.0f

                    draggableItem.x = dragEvent.x - (draggableItem.width/2)
                    draggableItem.y = dragEvent.y - (draggableItem.height/2)

                    val parent = draggableItem.parent as ConstraintLayout

                    parent.removeView(draggableItem)

                    val dropArea = view as ConstraintLayout

                    dropArea.addView(draggableItem)

                    if (dragEvent.clipDescription.hasMimeType((ClipDescription.MIMETYPE_TEXT_PLAIN))) {
                        val draggedData = dragEvent.clipData.getItemAt(0).text
                    }

                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    draggableItem.visibility = View.VISIBLE

                    view.invalidate()
                    true
                }

                else -> {
                    false
                }
            }
        }

        attachViewDragListener()
        bin = ActivityBlueprintBinding.inflate(LayoutInflater.from(context))
        binding.block.setOnDragListener(blockDragListener)
    }

    @SuppressLint("Recycle")
    private fun initializeAttributes(attrs: AttributeSet?, defstyleAttr: Int, defstyleRes: Int) {
        if (attrs == null) return

        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.BlockView, defstyleAttr, defstyleRes)

        with(binding) {
            val blockTitle = typedArray.getString(R.styleable.BlockView_blockTitle)
            setTitleBlockText(blockTitle)

            val bgBlock = typedArray.getColor(R.styleable.BlockView_bgBlock, Color.WHITE)
            block.backgroundTintList = ColorStateList.valueOf(bgBlock)

        }

        typedArray.recycle()
    }

    //присваивание названия
    private fun setTitleBlockText(title:String?) {
        binding.title.text = title ?: "block"
    }


    private fun attachViewDragListener(){
        binding.block.setOnLongClickListener {view: View ->

            val clipText = "This is out ClipData text"
            val item = ClipData.Item(clipText)

            val dataToDrag = ClipData (clipText, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item)

            val blockShadow = View.DragShadowBuilder(view)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                //support pre-Nougat versions
                @Suppress("DEPRECATION")
                view.startDragAndDrop(dataToDrag, blockShadow, view, 0)
            } else {
                //supports Nougat and beyond
                view.startDragAndDrop(dataToDrag, blockShadow, view, 0)
            }

            view.visibility = View.INVISIBLE

            true
        }
    }
}

//data class Node (
//    val name: String,
//    val neighbour: BlockView
//)