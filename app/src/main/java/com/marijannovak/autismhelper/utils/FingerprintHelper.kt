package com.marijannovak.autismhelper.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.marijannovak.autismhelper.R
import org.jetbrains.anko.toast

@RequiresApi(Build.VERSION_CODES.M)
class FingerprintHelper(private val context: Context, private val onSuccess: () -> Unit)
    : FingerprintManager.AuthenticationCallback() {

    private var cancellationSignal: CancellationSignal? = null

    fun startAuth(manager: FingerprintManager, cryptoObject: FingerprintManager.CryptoObject) {
        cancellationSignal = CancellationSignal()
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null)
        }
    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
        //NOOP
    }

    override fun onAuthenticationFailed() {
       context.toast(R.string.authentication_failed)
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
        //NOOP
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
        onSuccess()
    }
}