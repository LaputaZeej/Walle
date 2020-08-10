package com.bugu.walle.overlay

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bugu.walle.R
import com.bugu.walle.extension.formatDate
import com.bugu.walle.log.LogLevelEnum
import com.bugu.walle.log.Message
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.item_message.view.tv_msg

class MessageAdapter(private val dataList: MutableList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var tagMode: TagMode = TagMode.DATE_NONE
    var level: LogLevelEnum = LogLevelEnum.VERBOSE
    var messageFilter: MessageFilter = { true }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setNewData(list: List<Message>) {
        dataList.clear()
        dataList.addAll(list.filter(messageFilter).filter {
            it.level >= this.level
        })
        notifyDataSetChanged()
    }

    fun addData(message :Message) {
        if(messageFilter.invoke(message) && message.level>=this.level){
            dataList.add(message)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MessageViewHolder) {
            holder.itemView.run {

                when (tagMode) {
                    TagMode.ALL -> {
                        tv_title.visibility = View.VISIBLE
                        tv_tag.visibility = View.VISIBLE
                    }
                    TagMode.DATE_NONE -> {
                        tv_title.visibility = View.GONE
                        tv_tag.visibility = View.VISIBLE
                    }
                    TagMode.TAG_NONE -> {
                        tv_title.visibility = View.VISIBLE
                        tv_tag.visibility = View.GONE
                    }
                }
                val message = dataList[position]
                tv_title.text = "[${formatDate(message.time)}]"
                tv_tag.text = "[${message.tag}]"
                tv_msg.text = message.msg
                tv_msg.setTextColor(context.resources.getColor(message.level.color))

            }
        }
    }

    private inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view)

}