package ru.hits.android.axolot

import android.content.ClipData
import android.content.ClipDescription
import android.view.View
import android.os.Build
import android.os.Bundle
import android.content.Intent
import android.view.DragEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import ru.hits.android.axolot.databinding.ActivityBlueprintBinding
import ru.hits.android.axolot.view.BlockView

class BlueprintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlueprintBinding
    var menuIsVisible = true
    private var blocksList = mutableListOf<BlockView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlueprintBinding.inflate(layoutInflater)
        setContentView(binding.blueprintLayout)

        //скрывание и показ меню
        binding.showMenu.setOnClickListener() {
            if (menuIsVisible) {
                binding.menu.visibility = View.GONE
                menuIsVisible = false
            } else {
                binding.menu.visibility = View.VISIBLE
                menuIsVisible = true
            }
        }

        //переключение на страницу с настройками
        binding.ToPageSettings.setOnClickListener() {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        //добавление блока
        binding.addVar.setOnClickListener() {
            binding.codeField.addView(BlockView(this))
            blocksList.add(BlockView(this))
        }

        //перещение блоков добавленных первоначально в xml
        attachViewDragListener()
        binding.blueprintLayout.setOnDragListener(blockDragListener)
    }

    private fun attachViewDragListener() {
        binding.varBlock.setOnLongClickListener {view: View ->

            val clipText = "This is out Clipdata text"
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

    private val blockDragListener = View.OnDragListener { view, dragEvent->
        val draggableItem = dragEvent.localState as View

        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                true
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                binding.varBlock.alpha = 0.3f
                true
            }

            DragEvent.ACTION_DRAG_LOCATION -> {
                true
            }

            DragEvent.ACTION_DRAG_EXITED -> {
                binding.varBlock.alpha = 1.0f
                draggableItem.visibility = View.VISIBLE
                view.invalidate()
                true
            }

            DragEvent.ACTION_DROP -> {
                binding.varBlock.alpha = 1.0f

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
}