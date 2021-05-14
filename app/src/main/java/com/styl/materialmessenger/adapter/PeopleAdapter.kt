package com.styl.materialmessenger.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.styl.materialmessenger.R
import com.styl.materialmessenger.entities.UserEntity
import com.styl.materialmessenger.modules.chat.ChatActivity
import de.hdodenhof.circleimageview.CircleImageView

class PeopleAdapter(
    private val context: Context,
    private val listPeople: ArrayList<UserEntity>
): RecyclerView.Adapter<PeopleAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<CircleImageView>(R.id.iconPeople)
        val tvName = itemView.findViewById<TextView>(R.id.tvNamePeople)
        val itemContainer = itemView.findViewById<LinearLayout>(R.id.peopleItemContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleAdapter.ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_people, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listPeople.size
    }

    override fun onBindViewHolder(holder: PeopleAdapter.ViewHolder, position: Int) {
        val currentPeople = listPeople[position]
        if (currentPeople.imageUrl.isNullOrEmpty()) {
            holder.image.setImageResource(R.mipmap.ic_launcher)
        } else {
            Glide.with(context).load(currentPeople.imageUrl).into(holder.image)
        }
        holder.tvName.text = currentPeople.name
        holder.itemContainer.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId", currentPeople.id)
            intent.putExtra("imageReceiver", currentPeople.imageUrl)
            context.startActivity(intent)
        }

    }
}