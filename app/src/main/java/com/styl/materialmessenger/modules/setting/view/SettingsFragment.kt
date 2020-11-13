package com.styl.materialmessenger.modules.setting.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.styl.materialmessenger.R
import com.styl.materialmessenger.adapter.SettingsAdapter
import com.styl.materialmessenger.entities.SettingEntity
import com.styl.materialmessenger.modules.BaseFragment
import com.styl.materialmessenger.modules.dialog.MessageDialogFragment
import com.styl.materialmessenger.modules.home.view.HomeActivity
import com.styl.materialmessenger.modules.login.LoginActivity

class SettingsFragment: BaseFragment(), SettingsAdapter.SettingListener {

    private var settingsAdapter: SettingsAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        settingsAdapter = SettingsAdapter(context)
        val recyclerView = v?.findViewById<RecyclerView>(R.id.listViewSettings)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = settingsAdapter
        settingsAdapter?.setSettingListener(this)
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_setting
    }

    override fun onItemClickListener(settingEntity: SettingEntity) {
        when(settingEntity.text) {
            "Logout" -> {
                val dialog = MessageDialogFragment.newInstance("", "Do you want to logout?")
                dialog.setMultiChoice(context?.resources?.getString(R.string.btn_no), context?.resources?.getString(R.string.btn_yes))
                dialog.listener = object : MessageDialogFragment.MessageDialogListener{
                    override fun onNegativeClickListener(dialogFragment: DialogFragment) {
                        dialog.dismiss()
                    }

                    override fun onPositiveClickListener(dialogFragment: DialogFragment) {
                        firebaseAuth.signOut()
                        val intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)
                        activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        activity?.finish()
                    }

                    override fun onNeutralClickListener(dialogFragment: DialogFragment) {
                    }
                }
                val fragmentManager = (context as? HomeActivity)?.supportFragmentManager
                if(fragmentManager != null){
                    dialog.show(fragmentManager, MessageDialogFragment::class.java.simpleName)
                }
            }
        }
    }
}