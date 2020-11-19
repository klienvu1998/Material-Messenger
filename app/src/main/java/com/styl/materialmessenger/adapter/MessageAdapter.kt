package com.styl.materialmessenger.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.styl.materialmessenger.R
import com.styl.materialmessenger.entities.MessageEntity
import de.hdodenhof.circleimageview.CircleImageView

class MessageAdapter(
    val context: Context,
    val listMessage: ArrayList<MessageEntity>,
    val receiverImageUrl: String
): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    var isUserChatMultipleLine = false
    var countChat = 0

    companion object {
        const val MSG_TYPE_LEFT = 0
        const val MSG_TYPE_RIGHT = 1
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvShowMessage = itemView.findViewById<TextView>(R.id.tvShowMessage)
        val imgShowMessage = itemView.findViewById<CircleImageView>(R.id.imgShowMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.ViewHolder {
        if (viewType == MSG_TYPE_LEFT) {
            val v: View = LayoutInflater.from(context).inflate(R.layout.item_chat_receiver, parent, false)
            return ViewHolder(v)
        } else {
            val v: View = LayoutInflater.from(context).inflate(R.layout.item_chat_sender, parent, false)
            return ViewHolder(v)
        }
    }

    override fun getItemCount(): Int {
        return listMessage.size
    }

    override fun onBindViewHolder(holder: MessageAdapter.ViewHolder, position: Int) {
        val message = listMessage[position]
        holder.tvShowMessage.text = message.message
        if (isUserChatMultipleLine) {
            if (receiverImageUrl.isEmpty()) {
                holder.imgShowMessage.setImageResource(R.mipmap.ic_launcher_round)
            } else {
                Glide.with(context).load(receiverImageUrl).into(holder.imgShowMessage)
            }
        } else {
            holder.imgShowMessage.visibility = View.INVISIBLE
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (listMessage[position].senderUserId == firebaseUser?.uid) {
            countChat = 0
            return MSG_TYPE_RIGHT
        } else {
            if (countChat == 0) {
                isUserChatMultipleLine = true
                countChat++
            } else {
                isUserChatMultipleLine = false
            }
            return MSG_TYPE_LEFT
        }
    }
}