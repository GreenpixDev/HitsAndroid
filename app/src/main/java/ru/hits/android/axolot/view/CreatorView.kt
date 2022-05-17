package ru.hits.android.axolot.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import ru.hits.android.axolot.custom_spinner.SpinnerCustomAdapter
import ru.hits.android.axolot.custom_spinner.Types
import ru.hits.android.axolot.databinding.CreatorItemBinding

open class CreatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0,
    defstyleRes: Int = 0
) : ConstraintLayout(context, attrs, defstyleAttr, defstyleRes) {

    private val binding = CreatorItemBinding.inflate(LayoutInflater.from(context), this)

    var nameDescription = true
    var typeExpression = true
    var edit = true

    companion object {
        @JvmStatic
        val KEY_TITLE = "title"
    }

    private fun initLayoutParams() {
        val params = LinearLayoutCompat.LayoutParams(
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }

        binding.rowForMenu.layoutParams = params
    }

    fun initComponents() {
        if (!nameDescription) binding.rowForMenu.removeView(binding.name)
        if (!typeExpression) binding.rowForMenu.removeView(binding.typeVariable)
        if (!edit) binding.rowForMenu.removeView(binding.btnEdit)
    }

    private fun setCustomSpinner() {
        val adapter = SpinnerCustomAdapter(context, Types.list!!)
        binding.typeVariable.adapter = adapter
    }

//    private fun setupListSimple() {
//        val imageType = ImageView(this)
//
//        val data  = listOf() {
//            mapOf(
//                BlueprintActivity.KEY_TITLE to "First title 111"
//            )
//
//            mapOf(
//                BlueprintActivity.KEY_TITLE to "First title 222"
//            )
//
//            mapOf(
//                BlueprintActivity.KEY_TITLE to "First title 333"
//            )
//        }
//
//        val adapter = SimpleAdapter(
//            this,
//            data,
//            R.layout.simple_spinner_item,
//            arrayOf(BlueprintActivity.KEY_TITLE),
//            intArrayOf(R.id.text1)
//        )
//        binding.type.adapter = adapter
//
//        binding.type.onItemSelectedListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//            val selectedItemTitle = data[position]
//
//            val dialog = AlertDialog.Builder(this)
//                .setTitle(selectesItemTitle)
//                .setMessage(getString(R.array.typeBlock, selectedItemDescription))
//                .setPositiveButton("Ok") { dialog, which -> }
//                .create()
//            dialog.show()
//        }
//    }

    init {
        initLayoutParams()
        setCustomSpinner()
    }
}