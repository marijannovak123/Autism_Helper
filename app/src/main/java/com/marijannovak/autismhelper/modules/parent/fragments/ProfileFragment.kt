package com.marijannovak.autismhelper.modules.parent.fragments


import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.data.models.UserUpdateRequest
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import com.marijannovak.autismhelper.utils.ImageHelper
import com.marijannovak.autismhelper.utils.logTag
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.IOException


class ProfileFragment : InjectableFragment<ParentViewModel>() {

    private var menu: Menu? = null
    private var user: User? = null
    private var scaledBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            viewModel.userLiveData.observe(this, Observer { setUpUi(it) })
        }

        ivProfilePic.setOnClickListener {
            val intent = Intent()
            intent.type = "image/jpg"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(intent, Constants.REQUEST_CODE_IMAGE_LOADED)
        }

        val textWatcher = object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                user?.let {
                    if(etUsername.text.toString() != it.username || etParentPassword.text.toString() != it.parentPassword) {
                        menu?.setGroupVisible(R.id.group_profile, true)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //
            }
        }

        etUsername.addTextChangedListener(textWatcher)
        etParentPassword.addTextChangedListener(textWatcher)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUser()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE_IMAGE_LOADED && resultCode == Activity.RESULT_OK) {
            data?.let {
                val filePath = it.data
                try {
                    val stream = context!!.contentResolver.openInputStream(filePath)
                    val bitmap = BitmapFactory.decodeStream(stream)
                    stream.close()
                    scaledBitmap = ImageHelper.scaleBitmap(bitmap)

                    Glide.with(activity!!)
                            .load(bitmap)
                            .apply(RequestOptions()
                                    .placeholder(R.drawable.ic_profile_placeholder))
                            .into(ivProfilePic)

                    menu?.setGroupVisible(R.id.group_profile, true)
                }
                catch (e: IOException)
                {
                    Log.e(logTag(), e.message)
                    e.printStackTrace()
                }
            }

        }
    }

    private fun setUpUi(profile: User?) {
        this.user = profile
        profile?.let {
            with(it) {
                etUsername.setText(username)
                etEmail.setText(email)
                etParentPassword.setText(parentPassword)

                Glide.with(activity!!)
                        .load(scaledBitmap ?: profilePicPath)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.ic_profile_placeholder))
                        .into(ivProfilePic)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu = menu
        inflater?.inflate(R.menu.menu_profile, menu)
        menu?.setGroupVisible(R.id.group_profile, false)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(it.itemId) {
                R.id.action_update_profile -> {
                    this.user?.let {
                        val filename = "${it.id}.jpg"
                        val picPath = ImageHelper.saveProfileBitmap(activity, scaledBitmap, filename).absolutePath
                        viewModel.updateUserData(it.id, UserUpdateRequest(etUsername.text.toString().trim(), etParentPassword.text.toString().trim(), filename), picPath, scaledBitmap!!)

                    }

                }

                else -> {}
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
