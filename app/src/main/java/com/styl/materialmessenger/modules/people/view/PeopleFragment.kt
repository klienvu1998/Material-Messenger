package com.styl.materialmessenger.modules.people.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.styl.materialmessenger.R
import com.styl.materialmessenger.adapter.PeopleAdapter
import com.styl.materialmessenger.entities.UserEntity
import com.styl.materialmessenger.modules.BaseFragment

class PeopleFragment: BaseFragment() {

    companion object {
        val TAG = PeopleFragment::class.java.simpleName
    }

    private lateinit var listPeople: ArrayList<UserEntity>
    private var adapter: PeopleAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        listPeople = ArrayList()
        val listViewPeople = v?.findViewById<RecyclerView>(R.id.listViewPeople)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listViewPeople?.layoutManager = layoutManager

        readPeople(listViewPeople)
    }

    private fun readPeople(listViewPeople: RecyclerView?) {
        showLoading()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                showLog(TAG, error.message)
                dismissLoading()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                v?.findViewById<ProgressBar>(R.id.loadPeople)?.visibility = View.VISIBLE
                listPeople.clear()
                for (data in snapshot.children) {
                    val peopleEntity = data.getValue(UserEntity::class.java)
                    if (!peopleEntity?.id.equals(firebaseUser?.uid)) {
                        peopleEntity?.let { listPeople.add(it) }
                    }
                }
                adapter = context?.let { PeopleAdapter(it, listPeople) }
                listViewPeople?.adapter = adapter
                v?.findViewById<ProgressBar>(R.id.loadPeople)?.visibility = View.GONE
                dismissLoading()
            }

        })
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_people
    }
}