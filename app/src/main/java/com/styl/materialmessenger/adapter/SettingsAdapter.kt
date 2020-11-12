package com.styl.materialmessenger.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.styl.materialmessenger.R
import com.styl.materialmessenger.entities.SettingEntity

class SettingsAdapter(val context: Context?): RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    companion object {
        val listSetting: List<SettingEntity> = arrayListOf(
            SettingEntity(R.drawable.ic_logout, "Logout")
        )
    }

    interface SettingListener{
        fun onItemClickListener(settingEntity: SettingEntity)
    }

    private var settingListener: SettingListener? = null

    fun setSettingListener(listener: SettingListener) {
        this.settingListener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSetting = itemView.findViewById<TextView>(R.id.tvSetting)
        val imgSetting = itemView.findViewById<ImageView>(R.id.imgIconSetting)
        val itemSetting = itemView.findViewById<LinearLayout>(R.id.itemSetting)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listSetting.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val setting = listSetting[position]
        holder.imgSetting.setImageDrawable(setting.image?.let { context?.getDrawable(it) })
        holder.tvSetting.text = setting.text
        holder.itemSetting.setOnClickListener {
            settingListener?.onItemClickListener(setting)
        }
    }
}