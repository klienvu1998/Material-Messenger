package com.styl.materialmessenger.modules.unreadMessage.view

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.styl.materialmessenger.R
import com.styl.materialmessenger.adapter.UnreadMessageAdapter
import com.styl.materialmessenger.entities.MessageEntity
import com.styl.materialmessenger.entities.UnreadMessageEntity
import com.styl.materialmessenger.entities.UserEntity
import com.styl.materialmessenger.modules.BaseFragment
import com.styl.materialmessenger.modules.unreadMessage.UnreadMessageContract
import com.styl.materialmessenger.modules.unreadMessage.presenter.UnreadMessagePresenter
import java.util.*
import kotlin.collections.ArrayList

class UnreadMessageFragment: BaseFragment(), View.OnClickListener, UnreadMessageContract.View {

    var presenter: UnreadMessagePresenter? = null
    var unreadMessageAdapter: UnreadMessageAdapter? = null
    var unreadMessageList: ArrayList<UnreadMessageEntity>? = null
    var hashMessageUnread: Hashtable<String, String>? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        presenter = UnreadMessagePresenter(context, this)
        hashMessageUnread = Hashtable()
        unreadMessageList = ArrayList()
        initListView()
        getMessageReceiveBefore()
    }

    private fun initListView() {
        val recyclerView = v?.findViewById<RecyclerView>(R.id.listViewUnreadChat)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
        unreadMessageAdapter = context?.let { UnreadMessageAdapter(it, unreadMessageList ?: ArrayList()) }
        recyclerView?.adapter = unreadMessageAdapter
    }

    private fun getMessageReceiveBefore() {
        showLoading()
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats")
        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                dismissLoading()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                unreadMessageList?.clear()
                for (snapshot in dataSnapshot.children) {
                    val messageEntity = snapshot.getValue(MessageEntity::class.java)
                    val sender = messageEntity?.senderUserId
                    val receiver = messageEntity?.receiverUserId
                    val message = messageEntity?.message
                    if (sender != firebaseUser?.uid) {
                        if (hashMessageUnread?.containsKey(sender) == false) {
                            hashMessageUnread?.put(sender, message)
                        } else {
                            hashMessageUnread?.set(sender, message)
                        }
                    }
                }
                getInfoUserUnread()
            }
        })
    }

    private fun getInfoUserUnread() {
        val databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("Users")
        databaseReferenceUsers.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val userInfo = snapshot.getValue(UserEntity::class.java)
                    if (hashMessageUnread?.containsKey(userInfo?.id) == true) {
                        if (userInfo != null) {
                            unreadMessageList?.add(UnreadMessageEntity(hashMessageUnread?.get(userInfo.id) ?: "", userInfo))
                        }
                    }
                }
                unreadMessageAdapter?.notifyDataSetChanged()
                dismissLoading()
            }

        })
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_unread_message
    }

    override fun onClick(p0: View?) {

    }
}