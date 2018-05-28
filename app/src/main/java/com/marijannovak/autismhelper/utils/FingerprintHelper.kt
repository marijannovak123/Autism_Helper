package com.marijannovak.autismhelper.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.widget.Toast

@RequiresApi(Build.VERSION_CODES.M)
class FingerprintHelper(private val context: Context, private val onSuccess: () -> Unit): FingerprintManager.AuthenticationCallback() {

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
        Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show()
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
        //NOOP
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
        onSuccess()
    }
}