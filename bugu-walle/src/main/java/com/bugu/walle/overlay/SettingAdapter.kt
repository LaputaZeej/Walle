package com.bugu.walle.overlay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bugu.walle.R
import kotlinx.android.synthetic.main.item_setting.view.*

class SettingAdapter<T>(private val dataList: MutableList<SettingItem<T>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mOnItemClickListener: OnItemClickListener<SettingItem<T>>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false)
        return ItemViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setNewData(list: List<SettingItem<T>>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SettingAdapter<*>.ItemViewHolder) {
            holder.itemView.run {
                val settingItem = dataList[position]
                tv_text.text = settingItem.text
                tv_text.setOnClickListener {
                    mOnItemClickListener?.invoke(it, settingItem)
                }
            }
        }
    }

    private inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

}