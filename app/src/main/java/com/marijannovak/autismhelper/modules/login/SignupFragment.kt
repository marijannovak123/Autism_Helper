package com.marijannovak.autismhelper.modules.login


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.listeners.LoginSignupListener
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_EMAIL
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_PASSWORD
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_USERNAME
import com.marijannovak.autismhelper.models.Child
import com.marijannovak.autismhelper.models.SignupRequest
import com.marijannovak.autismhelper.utils.DatePicker
import com.marijannovak.autismhelper.utils.InputValidator
import kotlinx.android.synthetic.main.fragment_signup.*
import org.jetbrains.anko.support.v4.toast
import java.util.*
import kotlin.collections.ArrayList

class SignupFragment : Fragment(), DatePickerDialog.OnDateSetListener {
//todo : add children
    private var listener: LoginSignupListener? = null
    private var selectedDate: Calendar = Calendar.getInstance()
    private var childrenList: List<Child> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        btnSignUp.setOnClickListener { attemptSignup() }
        etDateOfBirth.setOnClickListener { showDatePickerDialog()}
    }

    private fun showDatePickerDialog() {
        DatePicker.newInstance(context!!, this, selectedDate).show()
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: android.widget.DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        selectedDate.set(Calendar.YEAR, year)
        selectedDate.set(Calendar.MONTH, month)
        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        etDateOfBirth.setText(dayOfMonth.toString() + "." + (month + 1) + "." + year)
    }

    private fun attemptSignup() {

        val email = etEmail.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        var valid = true

        if (!InputValidator.validate(email, VALIDATION_EMAIL)) {
            valid = false
            etEmail.error = resources.getString(R.string.malformed_email)
        } else {
            etEmail.error = null
        }

        if (!InputValidator.validate(password, VALIDATION_PASSWORD)) {
            valid = false
            etPassword.error = resources.getString(R.string.password_invalid)
        } else {
            etPassword.error = null
        }

        if (!InputValidator.validate(username, VALIDATION_USERNAME)) {
            valid = false
            etUsername.error = resources.getString(R.string.username_invalid)
        } else {
            etUsername.error = null
        }

        if (!InputValidator.validateDate(selectedDate)) {
            valid = false
            etDateOfBirth.error = resources.getString(R.string.date_invalid)
        } else {
            etDateOfBirth.error = null
        }

        if (!password.equals(confirmPassword)) {
            valid = false
            etConfirmPassword.error = resources.getString(R.string.passwords_not_same)
        } else {
            etConfirmPassword.error = null
        }

        if(valid)
            listener!!.onSignup(SignupRequest(email, username, password, childrenList))
        else
            toast(R.string.input_errors)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoginSignupListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement LoginSignupListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignupFragment()
    }
}
