package com.styl.materialmessenger.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.styl.materialmessenger.R
import com.styl.materialmessenger.entities.UnreadMessageEntity
import com.styl.materialmessenger.modules.chat.ChatActivity
import de.hdodenhof.circleimageview.CircleImageView

class UnreadMessageAdapter(
    private val context: Context,
    private val listPeople: ArrayList<UnreadMessageEntity>
): RecyclerView.Adapter<UnreadMessageAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val iconPeople = itemView.findViewById<CircleImageView>(R.id.iconPeople)
        val tvNamePeople = itemView.findViewById<TextView>(R.id.tvNamePeople)
        val tvUreadMessage = itemView.findViewById<TextView>(R.id.tvUnreadMessage)
        val container = itemView.findViewById<ConstraintLayout>(R.id.unreadMessageContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.item_unread_message, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listPeople.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val peopleMessage = listPeople[position]
        if (peopleMessage.userEntity.imageUrl.isNullOrEmpty()) {
            holder.iconPeople.setImageResource(R.mipmap.ic_launcher)
        } else {
            Glide.with(context).load(peopleMessage.userEntity.imageUrl).into(holder.iconPeople)
        }
        holder.tvNamePeople.text = peopleMessage.userEntity.name
        holder.tvUreadMessage.text = peopleMessage.message
        holder.container.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId", peopleMessage.userEntity.id)
            intent.putExtra("imageReceiver", peopleMessage.userEntity.imageUrl)
            context.startActivity(intent)
        }
    }
}