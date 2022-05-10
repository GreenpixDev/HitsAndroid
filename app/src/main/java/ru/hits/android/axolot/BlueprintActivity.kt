package ru.hits.android.axolot

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.view.DragEvent
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.View
import android.os.Bundle
import android.content.Intent
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_blueprint.*
import ru.hits.android.axolot.databinding.ActivityBlueprintBinding
import ru.hits.android.axolot.view.BlockView

class Point (
    var x: Float,
    var y: Float )

class BlueprintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlueprintBinding

    private lateinit var dragView: View
    private var location = Point(0f, 0f)

    private var menuIsVisible = true
    private var blocksList = mutableListOf<View>()
    private var counterVar = 1
    private var counterCyc = 1
    private var counterCon = 1

    @SuppressLint("ResourceType")
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
        binding.addVariable.setOnClickListener() {
            val blockView= BlockView( "isVariable",this )

            addBlockToStack(blockView)
            counterVar++
        }

        //перещение блоков
        attachBlockDragListener()
        binding.codeField.setOnDragListener(dragListener())
    }

    private fun attachBlockDragListener() = View.OnLongClickListener {view: View ->
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

    private fun dragListener() = View.OnDragListener { view, event ->
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                dragView.visibility = INVISIBLE
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                if (view == binding.codeField) {
                    location.x = event.x
                    location.y = event.y
                }
            }
            DragEvent.ACTION_DROP -> {
                when (view) {
                    dragView.parent -> {
                        dragView.translationZ = 30f
                        dragView.x = (location.x - dragView.width / 2)
                        dragView.y = location.y - dragView.height / 2
                    }

                    binding.codeField -> {
                        binding.codeField.removeView(dragView)
                        binding.codeField.addView(dragView)
                        dragView.translationZ = 30f
                        dragView.x = location.x - dragView.width / 2
                        dragView.y = location.y - dragView.height / 2
                    }
                }
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                dragView.visibility = VISIBLE
            }
        }
        true
    }

    private fun addBlockToStack(view: View) {
        //TODO: подправить генерирацию блоков нужного размера
        val params = ConstraintLayout.LayoutParams(
            codeField.width - 40,
            codeField.height - 40
        )
        val currentId = "1${counterVar}"

        codeField.addView(view, params)
        blocksList.add(view)

        view.x += 20
        view.y += 20
        view.id = currentId.toInt()
        view.setOnLongClickListener(attachBlockDragListener())
        view.translationZ = 30f
    }

//    fun addTextView(stringArray : Array<String>) {
//        for (textViewName in stringArray){
//            val textView = TextView(this)
//            textView.setTextAppearance(R.style.TextViewStyleForMenu)
//            textView.text = textViewName.toString()
//
//            if (stringArray.toString() == "variables") {
//                binding.scrollViewMenu.linearLayoutMenu.linearLayoutVariablesContainer.addView(textView)
//            } else if (stringArray.toString() == "cycles") {
//                binding.scrollViewMenu.linearLayoutMenu.linearLayoutCyclesContainer.addView(textView)
//            } else if (stringArray.toString() == "conditions") {
//                binding.scrollViewMenu.linearLayoutMenu.linearLayoutConditionsContainer.addView(textView)
//            }
//        }
//    }

//    @SuppressLint("ClickableViewAccessibility")
//    private fun longClickListener() = View.OnLongClickListener { view ->
//        val data = ClipData.newPlainText("label", "smth text")
//        val shadowBuilder = View.DragShadowBuilder(view)
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//            @Suppress("DEPRECATION")
//            view.startDrag(data, shadowBuilder, view, 0)
//        } else {
//            view.startDragAndDrop(data, shadowBuilder, view, 0)
//        }
//        dragView = view as View
//        true
//    }

//    private fun blockDragListener() = OnDragListener { view, dragEvent->
//        val draggableItem = dragEvent.localState as View
//
//        when (dragEvent.action) {
//            DragEvent.ACTION_DRAG_ENTERED -> {
//                binding.block.alpha = 0.3f
//                true
//            }
//
//            DragEvent.ACTION_DRAG_EXITED -> {
//                binding.block.alpha = 1.0f
//                draggableItem.visibility = View.VISIBLE
//
//                view.invalidate()
//                true
//            }
//
//            DragEvent.ACTION_DROP -> {
//                val item = dragEvent.clipData.getItemAt(0)
//
//                binding.block.alpha = 1.0f
//
//                draggableItem.x = dragEvent.x - (draggableItem.width/2)
//                draggableItem.y = dragEvent.y - (draggableItem.height/2)
//
//                val parent = draggableItem.parent as ConstraintLayout
//                parent.removeView(draggableItem)
//
//                val dropArea = view as ConstraintLayout
//                dropArea.addView(draggableItem)
//
//                true
//            }
//
//            DragEvent.ACTION_DRAG_ENDED -> {
//                draggableItem.visibility = View.VISIBLE
//                view.invalidate()
//                true
//            }
//
//            else -> {
//                false
//            }
//        }
//    }
}
