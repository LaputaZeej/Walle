package com.bugu.walle.overlay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bugu.walle.R
import com.bugu.walle.extension.formatDate
import com.bugu.walle.log.Message
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.item_message.view.tv_msg

class MessageAdapter(private val dataList: MutableList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var level: MessageShowLevel = MessageShowLevel.DATE_NONE
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setNewData(list: List<Message>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MessageViewHolder) {
            holder.itemView.run {

                when (level) {
                    MessageShowLevel.ALL -> {
                        tv_title.visibility = View.VISIBLE
                        tv_tag.visibility = View.VISIBLE
                    }
                    MessageShowLevel.DATE_NONE -> {
                        tv_title.visibility = View.GONE
                        tv_tag.visibility = View.VISIBLE
                    }
                    MessageShowLevel.TAG_NONE -> {
                        tv_title.visibility = View.VISIBLE
                        tv_tag.visibility = View.GONE
                    }
                }
                val message = dataList[position]
                tv_title.text = "[${formatDate(message.time)}]"
                tv_tag.text = "[${message.tag}]"
                tv_msg.text = message.msg

            }
        }
    }

    private inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view)

}