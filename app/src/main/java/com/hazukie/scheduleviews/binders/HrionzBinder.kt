package com.hazukie.scheduleviews.binders

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.hazukie.scheduleviews.R
import com.hazukie.scheduleviews.models.HrionzModel

class HrionzBinder: ItemViewBinder<HrionzModel, HrionzBinder.ViewHolder>() {
    lateinit var onDel:(v:View,item:HrionzModel)->Unit
    lateinit var onExpande:(v:TextView)->Unit
    lateinit var onOpen:(scheuid:String,timeuid:String)->Unit
    lateinit var onEdit:(item:HrionzModel)->Unit


    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.recy_horion_card, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: HrionzModel) {
        holder.fileName.text=item.schename
        holder.detailBtn.text=item.timename
        holder.fileDescription.text=Html.fromHtml(item.descript)

        holder.deleteBtn.setOnClickListener { onDel.invoke(it, item) }
        holder.itemView.setOnClickListener{onOpen.invoke(item.scheuid,item.timeuid)}
        holder.settingBtn.setOnClickListener{onEdit.invoke(item)}
        holder.detailBtn.setOnClickListener { onExpande(holder.fileDescription) }
    }

    override fun onBindViewHolder(holder: ViewHolder, item: HrionzModel, payloads: List<Any>) {
        super.onBindViewHolder(holder, item, payloads)
        if (!payloads.isEmpty()) {
            val isLoad = payloads[0].toString()
            if (isLoad == "updating") {
                holder.fileName.text=item.schename
                holder.detailBtn.text=item.timename
                holder.fileDescription.text=Html.fromHtml(item.descript)
            }
        }
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val fileName :TextView= itemView.findViewById(R.id.recy_horion_card_title)
        val fileDescription :TextView= itemView.findViewById(R.id.recy_horion_card_detail)
        val detailBtn :Button= itemView.findViewById(R.id.recy_horion_card_detailBtn)
        val deleteBtn :ImageView= itemView.findViewById(R.id.recy_horion_card_delete)
        val settingBtn :Button= itemView.findViewById(R.id.recy_horion_card_setting)
    }

}