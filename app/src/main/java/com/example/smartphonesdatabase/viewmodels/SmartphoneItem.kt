package com.example.smartphonesdatabase.viewmodels

import com.example.smartphonesdatabase.R
import com.example.smartphonesdatabase.models.Smartphone
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.smartphones_list_row.view.*


class SmartphoneItem(val smartphone: Smartphone): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.brand_textview.text = smartphone.brand
        viewHolder.itemView.model_textview.text = smartphone.model
    }

    override fun getLayout(): Int {
        return R.layout.smartphones_list_row
    }
}