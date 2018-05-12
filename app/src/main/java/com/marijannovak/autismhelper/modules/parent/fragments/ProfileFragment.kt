package com.marijannovak.autismhelper.modules.parent.fragments


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.config.Constants.Companion.PASSWORD_PLACEHOLDER
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : InjectableFragment<ParentViewModel>() {

    private var menu: Menu? = null

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
            viewModel.resourceLiveData.observe(this, Observer { userChildrenJoins -> setUpUi(userChildrenJoins!!.data!![0]) })
        }

        val textWatcher = object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                menu?.setGroupVisible(R.id.group_profile, true)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //
            }
        }

        etUsername.addTextChangedListener(textWatcher)
        etEmail.addTextChangedListener(textWatcher)
        etParentPassword.addTextChangedListener(textWatcher)
        etPassword.addTextChangedListener(textWatcher)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUserWithChildren()
    }

    private fun setUpUi(profile: UserChildrenJoin?) {
        profile?.let {
            with(it.user) {
                etUsername.setText(username)
                etEmail.setText(email)
                etParentPassword.setText(parentPassword)
                etPassword.setText(PASSWORD_PLACEHOLDER)

            }
            etUsername.setText(it.user.username)
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
                    viewModel.updateUserData()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //TODO: UPDATE CHILDREN DATA, UPDATE PASSWORDS, INFO ETC.

}
