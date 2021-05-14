package com.styl.materialmessenger.modules.chat

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.styl.materialmessenger.R
import com.styl.materialmessenger.adapter.MessageAdapter
import com.styl.materialmessenger.entities.MessageEntity
import com.styl.materialmessenger.entities.UserEntity
import com.styl.materialmessenger.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity: BaseActivity(), View.OnClickListener {

    private var currentChatUserId: String = ""
    private var imgUrlReceiver: String = ""
    private var listMessage: ArrayList<MessageEntity>? = null
    private var messageAdapter: MessageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        currentChatUserId = intent.getStringExtra("userId").toString()
        imgUrlReceiver = intent.getStringExtra("imageReceiver").toString()

        setSupportActionBar(toolbarChatMessage)
        setUpListView()
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbarChatMessage.setNavigationOnClickListener {
            finish()
        }
        btnSendMessage.setOnClickListener(this)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentChatUserId)
        databaseReference?.addValueEventListener(object  : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserEntity::class.java)
                profileNameChatMessage.text = user?.name
                if (user?.imageUrl.isNullOrEmpty()) {
                    profileImageChatMessage.setImageResource(R.mipmap.ic_launcher)
                } else {
                    Glide.with(this@ChatActivity).load(user?.imageUrl).into(profileImageChatMessage)
                }
                firebaseUser?.uid?.let { readMessage(it, currentChatUserId) }
            }

        })
    }

    private fun setUpListView() {
        listMessage = ArrayList()
        messageAdapter = MessageAdapter(this, listMessage!!, imgUrlReceiver)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        listViewMessage.layoutManager = linearLayoutManager
        listViewMessage.adapter = messageAdapter
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.btnSendMessage -> {
                val message = edtMessage.text.toString()
                if (message.isEmpty()) {
                    showToast("Please, input message !!")
                } else {
                    firebaseUser?.uid?.let { sendMessage(it, currentChatUserId, message) }
                }
            }
            R.id.edtMessage -> {
                listMessage?.size?.minus(1)?.let { listViewMessage.scrollToPosition(it) }
            }
        }
    }

    private fun sendMessage(senderUserId: String, receiverUserId: String, message: String) {
        databaseReference = FirebaseDatabase.getInstance().reference

        val sendData = HashMap<String, Any>()
        sendData["senderUserId"] = senderUserId
        sendData["receiverUserId"] = receiverUserId
        sendData["message"] = message

        databaseReference?.child("Chats")?.push()?.setValue(sendData)
    }

    private fun readMessage(senderUserId: String, receiverUserId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats")
        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listMessage?.clear()
                for (data in snapshot.children) {
                    val messageEntity = data.getValue(MessageEntity::class.java)
                    if ((messageEntity?.receiverUserId.equals(senderUserId) && messageEntity?.senderUserId.equals(receiverUserId)) ||
                        (messageEntity?.receiverUserId.equals(receiverUserId) && messageEntity?.senderUserId.equals(senderUserId))) {
                        messageEntity?.let { listMessage?.add(it) }
                    }
                }
                messageAdapter?.notifyDataSetChanged()
                listMessage?.size?.minus(1)?.let { listViewMessage.scrollToPosition(it) }
            }

        })
    }

}