package com.peopleperfectae.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.peopleperfectae.BR
import com.peopleperfectae.R
import com.peopleperfectae.data.SharedData

class GenericExpandableListAdapter<T, U>(
    private val headerLayout: Int,
    private val childLayout: Int,
    private val listDataHeader: List<T>,
    private val listChildData: HashMap<T, List<U>>
) : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): Any? = listDataHeader[groupPosition]

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    override fun hasStableIds(): Boolean = false

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val itemBinding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent?.context), headerLayout, parent, false
        )

        val obj = getGroup(groupPosition)
        itemBinding.setVariable(BR.obj, obj)
        itemBinding.executePendingBindings()

        val imgDropDown = itemBinding.root.findViewById<ImageView>(R.id.expandable_dropdown_arrow)
        val itemContainer = itemBinding.root.findViewById<RelativeLayout>(R.id.container)


        if (isExpanded) {
            imgDropDown.setImageResource(R.drawable.ic_down_arrow)
        } else {
            imgDropDown.setImageResource(R.drawable.ic_forward_arrow)
        }

        when (groupPosition) {
            0 -> {
                imgDropDown.visibility = View.INVISIBLE
                itemContainer.setOnClickListener { SharedData.drawerLayout?.closeDrawers() }
            }
            //TODO remove in future
            2, 4, 5 -> {
                itemContainer.setOnClickListener {
                    Toast.makeText(
                        convertView?.context,
                        "You are not able to access to this module.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        return itemBinding.root

    }

    override fun getChildrenCount(groupPosition: Int): Int =
        listChildData[listDataHeader[groupPosition]]?.size ?: 0

    override fun getChild(groupPosition: Int, childPosition: Int): Any? =
        listChildData[listDataHeader[groupPosition]]?.get(childPosition)

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val itemBinding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent?.context), childLayout, parent, false
        )

        val obj = getChild(groupPosition, childPosition)
        itemBinding.setVariable(BR.obj, obj)
        itemBinding.executePendingBindings()

        return itemBinding.root
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun getGroupCount(): Int = listDataHeader.size

}