package com.marijannovak.autismhelper.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Handler
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.AppCompatButton
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.config.Constants.Companion.CHILD_ID_SUFFIX
import com.marijannovak.autismhelper.config.Constants.Companion.FINGERPRINT_KEY
import com.marijannovak.autismhelper.config.Constants.Companion.GENDERS
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_DATE
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_EMAIL
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_NAME
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.Child
import org.jetbrains.anko.alert
import org.jetbrains.anko.keyguardManager
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey


class DialogHelper {

    companion object {

        var keyStore: KeyStore? = null
        var cipher: Cipher? = null

        fun showPromptDialog(context: Context, message: String, confirmListener: () -> Unit) {
            context.alert(message) {
                positiveButton(R.string.confirm) { confirmListener() }
                negativeButton(R.string.cancel) { }
            }.show()
        }

        @SuppressLint("SetTextI18n")
        private fun showDatePicker(context: Context, date: Calendar, etDate: EditText) {
            val onDateSet = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                date.set(Calendar.YEAR, year)
                date.set(Calendar.MONTH, month)
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                etDate.setText(date.timeInMillis.toDateString())
            }
            DatePicker.show(context, onDateSet, date)
        }

        @SuppressLint("InflateParams")
        fun showForgotPasswordDialog(context: Context, confirmListener: (String) -> Unit) {
            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            val inflater = LayoutInflater.from(context)
            val alertView = inflater.inflate(R.layout.dialog_forgot_password, null)

            builder.setView(alertView)
            builder.setCancelable(false)
            val alertDialog = builder.create()

            val btnPositive = alertView.findViewById<AppCompatButton>(R.id.btnPositive)
            val btnNegative = alertView.findViewById<AppCompatButton>(R.id.btnNegative)
            val etEmail = alertView.findViewById<EditText>(R.id.etEmail)

            btnPositive.setOnClickListener {
                val email = etEmail.text.toString().trim()

                if (InputValidator.validate(email, VALIDATION_EMAIL)) {
                    confirmListener(email)
                    alertDialog.dismiss()
                } else {
                    etEmail.error = context.getString(R.string.malformed_email)
                }
            }

            btnNegative.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

        @SuppressLint("InflateParams")
        fun showEnterParentPasswordDialog(context: Context, parentPassword: String, confirmListener: (String) -> Unit, fingerprintListener: () -> Unit) {
            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            val inflater = LayoutInflater.from(context)
            val alertView = inflater.inflate(R.layout.dialog_enter_password, null)

            builder.setView(alertView)
            builder.setCancelable(false)
            val alertDialog = builder.create()

            val btnPositive = alertView.findViewById<AppCompatButton>(R.id.btnPositive)
            val btnNegative = alertView.findViewById<AppCompatButton>(R.id.btnNegative)
            val etPassword = alertView.findViewById<EditText>(R.id.etParentPassword)
            val ivFingerprint = alertView.findViewById<ImageView>(R.id.ivFingerprint)


            if (parentPassword == "") {
                val message = alertView.findViewById<TextView>(R.id.tvMessage)
                message.text = context.getText(R.string.choose_parent_password)
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val fingerprintManager = context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager

                if(fingerprintManager.isHardwareDetected
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED
                        && fingerprintManager.hasEnrolledFingerprints()
                        && context.keyguardManager.isKeyguardSecure) {
                    ivFingerprint.visibility = View.VISIBLE
                    genKey()
                    if(cipherInit()) {
                        val cryptoObject = FingerprintManager.CryptoObject(cipher)
                        val helper = FingerprintHelper(context, {
                            ivFingerprint.colorFilter = PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP)
                            Handler().postDelayed(fingerprintListener, 10)
                        })
                        helper.startAuth(fingerprintManager, cryptoObject)
                    }
                }
            }

            btnPositive.setOnClickListener {
                val password = etPassword.text.toString().trim()
                confirmListener(password)
                alertDialog.dismiss()
            }

            btnNegative.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

        private fun cipherInit(): Boolean {
            try {
                cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: NoSuchPaddingException) {
                e.printStackTrace()
            }

            try {
                keyStore?.load(null)
                val key = keyStore?.getKey(FINGERPRINT_KEY, null) as SecretKey
                cipher?.init(Cipher.ENCRYPT_MODE, key)
                return true
            } catch (e1: IOException) {

                e1.printStackTrace()
                return false
            } catch (e1: NoSuchAlgorithmException) {

                e1.printStackTrace()
                return false
            } catch (e1: CertificateException) {

                e1.printStackTrace()
                return false
            } catch (e1: UnrecoverableKeyException) {

                e1.printStackTrace()
                return false
            } catch (e1: KeyStoreException) {

                e1.printStackTrace()
                return false
            } catch (e1: InvalidKeyException) {

                e1.printStackTrace()
                return false
            }
        }

        private fun genKey() {
            try {
                keyStore = KeyStore.getInstance("AndroidKeyStore")
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            }

            var keyGenerator: KeyGenerator? = null

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
                }
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: NoSuchProviderException) {
                e.printStackTrace()
            }


            try {
                keyStore?.load(null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    keyGenerator!!.init(KeyGenParameterSpec.Builder(Constants.FINGERPRINT_KEY, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            .setUserAuthenticationRequired(true)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build()
                    )
                }
                keyGenerator!!.generateKey()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: CertificateException) {
                e.printStackTrace()
            } catch (e: InvalidAlgorithmParameterException) {
                e.printStackTrace()
            }
        }

        //todo: test correct data set
        @SuppressLint("InflateParams")
        fun showAddChildDialog(context: Context, userId: String, userChildrenNo: Int, showAnotherCheck: Boolean, confirmListener: (Child, Boolean) -> Unit, cancel: () -> Unit) {
            val selectedDate: Calendar = Calendar.getInstance()

            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            val inflater = LayoutInflater.from(context)
            val alertView = inflater.inflate(R.layout.dialog_add_child, null)

            builder.setView(alertView)
            builder.setCancelable(false)
            val alertDialog = builder.create()

            val btnPositive = alertView.findViewById<AppCompatButton>(R.id.btnPositive)
            val btnNegative = alertView.findViewById<AppCompatButton>(R.id.btnNegative)

            if (userChildrenNo == 0) {
                btnNegative.visibility = View.INVISIBLE
                btnNegative.isEnabled = false
            }

            val etName = alertView.findViewById<EditText>(R.id.etChildName)
            val etDateOfBirth = alertView.findViewById<EditText>(R.id.etChildDateOfBirth)
            val spGender = alertView.findViewById<Spinner>(R.id.spGender)
            val cbAddAnother = alertView.findViewById<CheckBox>(R.id.cbAddAnother)

            cbAddAnother.visibility = if (!showAnotherCheck) View.GONE else View.VISIBLE
            spGender.adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, GENDERS)

            etDateOfBirth.setOnClickListener { showDatePicker(context, selectedDate, etDateOfBirth) }

            btnPositive.setOnClickListener {
                val name = etName.text.toString().trim()
                val dateOfBirth = selectedDate.timeInMillis

                val childId = userId + CHILD_ID_SUFFIX + name.hashCode()
                val child = Child(childId, userId, name, spGender.selectedItem.toString(), dateOfBirth)

                val errors = InputValidator.validateChild(child)

                if (errors.isEmpty()) {
                    confirmListener(child, cbAddAnother.isChecked && showAnotherCheck)
                    alertDialog.dismiss()
                } else {
                    handleChildAddErrors(errors, etName, etDateOfBirth)
                }
            }

            btnNegative.setOnClickListener {
                cancel()
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

        private fun handleChildAddErrors(errors: HashMap<String, String>, etName: EditText, etDateOfBirth: EditText) {
            if (errors.isEmpty()) {
                etName.error = null
                etDateOfBirth.error = null
            } else {
                for (e in errors.iterator())
                    when (e.key) {
                        VALIDATION_NAME -> etName.error = e.value
                        VALIDATION_DATE -> etDateOfBirth.error = e.value
                    }
            }
        }

        fun showPickChildDialog(context: Context, children: List<Child>, onConfirm: (Child) -> Unit) {
            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            val inflater = LayoutInflater.from(context)
            val alertView = inflater.inflate(R.layout.dialog_pick_child, null)

            builder.setView(alertView)
            builder.setCancelable(false)
            val alertDialog = builder.create()

            val btnPositive = alertView.findViewById<AppCompatButton>(R.id.btnPositive)
            val btnNegative = alertView.findViewById<AppCompatButton>(R.id.btnNegative)
            val spChildren = alertView.findViewById<Spinner>(R.id.spChildren)

            val childrenNames = children.map { child -> child.name }

            val childrenAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, childrenNames)
            spChildren.adapter = childrenAdapter

            btnPositive.setOnClickListener {
                onConfirm(children[spChildren.selectedItemPosition])
                alertDialog.dismiss()
            }

            btnNegative.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

        fun showEditChildDialog(context: Context, child: Child, message: String, onEdit: (Child) -> Unit) {
            val selectedDate: Calendar = Calendar.getInstance()

            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            val inflater = LayoutInflater.from(context)
            val alertView = inflater.inflate(R.layout.dialog_add_child, null)

            builder.setView(alertView)
            builder.setCancelable(false)
            val alertDialog = builder.create()

            val btnPositive = alertView.findViewById<AppCompatButton>(R.id.btnPositive)
            val btnNegative = alertView.findViewById<AppCompatButton>(R.id.btnNegative)
            val tvMessage = alertView.findViewById<TextView>(R.id.tvMessage)
            val etName = alertView.findViewById<EditText>(R.id.etChildName)
            val etDateOfBirth = alertView.findViewById<EditText>(R.id.etChildDateOfBirth)
            val spGender = alertView.findViewById<Spinner>(R.id.spGender)
            val cbAddAnother = alertView.findViewById<CheckBox>(R.id.cbAddAnother)

            tvMessage.text = message
            spGender.setSelection(if(child.gender == GENDERS[0]) 0 else 1)
            etName.setText(child.name)
            etDateOfBirth.setText(child.dateOfBirth.toDateString())
            cbAddAnother.visibility = View.GONE //reusing add child, hide this
            spGender.adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, GENDERS)

            etDateOfBirth.setOnClickListener { showDatePicker(context, selectedDate, etDateOfBirth) }

            btnPositive.setOnClickListener {
                val name = etName.text.toString().trim()
                val dateOfBirth = selectedDate.timeInMillis

                val updatedChild = child.copy(name = name, gender = spGender.selectedItem.toString(), dateOfBirth = dateOfBirth)

                val errors = InputValidator.validateChild(updatedChild)

                if (errors.isEmpty()) {
                    onEdit(updatedChild)
                    alertDialog.dismiss()
                } else {
                    handleChildAddErrors(errors, etName, etDateOfBirth)
                }
            }

            btnNegative.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

        fun showEnterPhraseTextDialog(context: Context, onConfirm: (AacPhrase) -> Unit) {
            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            val inflater = LayoutInflater.from(context)
            val alertView = inflater.inflate(R.layout.dialog_text_phrase, null)

            builder.setView(alertView)
            builder.setCancelable(false)
            val alertDialog = builder.create()

            val btnPositive = alertView.findViewById<AppCompatButton>(R.id.btnPositive)
            val btnNegative = alertView.findViewById<AppCompatButton>(R.id.btnNegative)
            val etPhraseText = alertView.findViewById<EditText>(R.id.etPhraseText)

            btnPositive.setOnClickListener {
                val phraseText = etPhraseText.text.toString()
                if(phraseText.isNotEmpty()) {
                    onConfirm(AacPhrase(0, phraseText, phraseText, ""))
                    alertDialog.dismiss()
                }
            }

            btnNegative.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

    }
}
