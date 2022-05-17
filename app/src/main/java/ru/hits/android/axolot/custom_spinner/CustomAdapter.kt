package ru.hits.android.axolot.custom_spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.spinner_item.view.*
import ru.hits.android.axolot.R

class SpinnerCustomAdapter(
    context: Context,
    typeList: List<Type>
) : ArrayAdapter<Type>(context, 0, typeList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent, isDropdownView = false)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent, isDropdownView = true)
    }

    private fun initView(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
        isDropdownView: Boolean
    ): View {

        val type = getItem(position)

        val view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        view.typeImage.setImageResource(type!!.image)
        view.typeName.text = type.name

        view.typeName.visibility = if (!isDropdownView) View.GONE else View.VISIBLE

        return view
    }
}