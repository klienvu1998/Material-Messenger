package com.styl.materialmessenger.modules.setting.view

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.styl.materialmessenger.R
import com.styl.materialmessenger.adapter.SettingsAdapter
import com.styl.materialmessenger.entities.PeopleEntity
import com.styl.materialmessenger.entities.SettingEntity
import com.styl.materialmessenger.modules.BaseFragment
import com.styl.materialmessenger.modules.dialog.MessageDialogFragment
import com.styl.materialmessenger.modules.home.view.HomeActivity
import com.styl.materialmessenger.modules.login.LoginActivity
import com.styl.materialmessenger.utils.ImageProcessor
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_chat_message.*
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingsFragment: BaseFragment(), SettingsAdapter.SettingListener, View.OnClickListener {

    companion object {
        const val PERMISSION_READ_REQUEST_CODE = 1000
        const val RESULT_CHOOSE_IMAGE = 1001
        const val RESULT_TAKE_PHOTO = 1002
    }

    private var settingsAdapter: SettingsAdapter? = null
    private var outputUri: Uri? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        settingsAdapter = SettingsAdapter(context)
        val recyclerView = v?.findViewById<RecyclerView>(R.id.listViewSettings)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = settingsAdapter
        settingsAdapter?.setSettingListener(this)
        v?.findViewById<CircleImageView>(R.id.imgSettingUserAvatar)?.setOnClickListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseReference =
            firebaseUser?.uid?.let { FirebaseDatabase.getInstance().getReference("Users").child(it) }
        databaseReference?.addValueEventListener(object  : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(PeopleEntity::class.java)
                tvSettingUserName.text = user?.name
            }
        })
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
            R.id.imgSettingUserAvatar -> {
                selectImage()
            }
        }
    }

    private fun selectImage() {
        val buider = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val dialogView: View? = inflater?.inflate(R.layout.dialog_change_image, null)
        buider.setView(dialogView)
        val dialog = buider.create()
        if (dialog?.window != null) {
            dialog.window?.setGravity(Gravity.CENTER)
        }

        dialogView?.findViewById<TextView>(R.id.btnCaptureImage)?.setOnClickListener {
            if (activity?.let { it1 -> ContextCompat.checkSelfPermission(it1, Manifest.permission.CAMERA) } != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA),
                        PERMISSION_READ_REQUEST_CODE
                    )
                }
            } else {
                outputUri = ImageProcessor.createImageUri(context)
                if (outputUri != null) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
                    startActivityForResult(cameraIntent, RESULT_TAKE_PHOTO)
                }
            }
        }

        dialogView?.findViewById<TextView>(R.id.btnChooseImageGallery)?.setOnClickListener {
            dialog.dismiss()
            if (activity?.let { it1 -> ContextCompat.checkSelfPermission(it1, Manifest.permission.READ_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_READ_REQUEST_CODE
                    )
                }
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                startActivityForResult(intent, RESULT_CHOOSE_IMAGE)
            }
        }

        dialogView?.findViewById<TextView>(R.id.btnClearImage)?.setOnClickListener {
            imgSettingUserAvatar.setImageResource(R.mipmap.ic_launcher_round)
        }

        dialogView?.findViewById<TextView>(R.id.btnCancelChangeImage)?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        outputUri = ImageProcessor.requestPermissionsResult(requestCode, grantResults[0], this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ImageProcessor.activityImageResult(
            resultCode,
            requestCode,
            data,
            this,
            imgSettingUserAvatar,
            outputUri
        )
    }
}