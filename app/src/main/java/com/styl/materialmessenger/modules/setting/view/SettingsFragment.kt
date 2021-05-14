package com.styl.materialmessenger.modules.setting.view

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.styl.materialmessenger.R
import com.styl.materialmessenger.adapter.SettingsAdapter
import com.styl.materialmessenger.entities.UserEntity
import com.styl.materialmessenger.entities.SettingEntity
import com.styl.materialmessenger.modules.BaseFragment
import com.styl.materialmessenger.modules.dialog.MessageDialogFragment
import com.styl.materialmessenger.activity.home.view.HomeActivity
import com.styl.materialmessenger.activity.login.LoginActivity
import com.styl.materialmessenger.utils.ImageProcessor
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingsFragment: BaseFragment(), SettingsAdapter.SettingListener, View.OnClickListener {

    companion object {
        const val PERMISSION_READ_REQUEST_CODE = 1000
        const val RESULT_CHOOSE_IMAGE = 1001
        const val RESULT_TAKE_PHOTO = 1002
        const val IMAGE_REQUEST = 1003
        val TAG = SettingsFragment::class.java.simpleName
    }

    private var settingsAdapter: SettingsAdapter? = null
    private var outputUri: Uri? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        showLoading()
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
                dismissLoading()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserEntity::class.java)
                tvSettingUserName.text = user?.name
                if (user?.imageUrl.isNullOrEmpty()) {
                    imgSettingUserAvatar.setImageResource(R.mipmap.ic_launcher_round)
                } else {
                    context?.let { Glide.with(it).load(user?.imageUrl).into(imgSettingUserAvatar) }
                }
                dismissLoading()
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
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser?.uid!!)
            val hashUrl: HashMap<String, Any> = HashMap()
            hashUrl["imageUrl"] = ""
            databaseReference?.updateChildren(hashUrl)
            dialog.dismiss()
        }

        dialogView?.findViewById<TextView>(R.id.btnCancelChangeImage)?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        outputUri = ImageProcessor.activityImageResult(
            resultCode,
            requestCode,
            data,
            this,
            imgSettingUserAvatar,
            outputUri
        )
        uploadImage()
    }

    private fun uploadImage() {
        if (outputUri != null) {
            storageReference = storageReference?.child("${System.currentTimeMillis()}.jpg")
            outputUri?.let { storageReference?.putFile(it)?.addOnSuccessListener {
                Log.d(TAG, "upload success")
                storageReference?.downloadUrl?.addOnSuccessListener {
                    Log.d(TAG, "url: $it")
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser?.uid!!)
                    val hashUrl: HashMap<String, Any> = HashMap()
                    hashUrl["imageUrl"] = it.toString()
                    databaseReference?.updateChildren(hashUrl)
                }
            }?.addOnFailureListener {
                Log.d(TAG, "$it")
            } }
            outputUri = null
        }
    }
}