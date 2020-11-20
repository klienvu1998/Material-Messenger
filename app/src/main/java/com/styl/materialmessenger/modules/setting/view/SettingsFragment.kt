package com.styl.materialmessenger.modules.setting.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.TextureView
import android.view.View
import android.widget.TextView
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
import de.hdodenhof.circleimageview.CircleImageView

class SettingsFragment: BaseFragment(), SettingsAdapter.SettingListener, View.OnClickListener {

    private var settingsAdapter: SettingsAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        settingsAdapter = SettingsAdapter(context)
        val recyclerView = v?.findViewById<RecyclerView>(R.id.listViewSettings)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = settingsAdapter
        settingsAdapter?.setSettingListener(this)
        v?.findViewById<CircleImageView>(R.id.imgUser).setOnClickListener(this)
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

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgUser -> {
                selectImage()
            }
        }
    }

    private fun selectImage() {
        val buider = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val dialogView: View? = inflater?.inflate(R.layout.dialog_change_image, null)
        buider.setView(v)
        val dialog = buider.create()
        if (dialog?.window != null) {
            dialog.window?.setGravity(Gravity.CENTER)
        }
        dialogView?.findViewById<TextView>(R.id.btnCaptureImage)?.setOnClickListener {

        }

        dialogView?.findViewById<TextView>(R.id.btnChooseImageGallery)?.setOnClickListener {

        }

        dialogView?.findViewById<TextView>(R.id.btnClearImage)?.setOnClickListener {

        }

        dialogView?.findViewById<TextView>(R.id.btnCancelChangeImage)?.setOnClickListener {
            dialog.dismiss()
        }
    }
}