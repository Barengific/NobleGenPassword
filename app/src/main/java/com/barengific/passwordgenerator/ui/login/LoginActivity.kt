package com.barengific.passwordgenerator.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.barengific.passwordgenerator.*
import com.barengific.passwordgenerator.databinding.ActivityLoginBinding

import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    private val cryptographyManager = CryptographyManager()
    private val ciphertextWrapper
        get() = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
            applicationContext,
            SHARED_PREFS_FILENAME,
            Context.MODE_PRIVATE,
            CIPHERTEXT_WRAPPER
        )

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
//                    Toast.makeText(applicationContext,
//                        "Authentication error: $errString", Toast.LENGTH_SHORT)
//                        .show()
                    val intent = Intent(applicationContext, LoginActivity::class.java).apply {}
                    startActivity(intent)
//                    Log.d("aaaaaaaa", "ineeeeeerrrr")
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
//                    Log.d("aaaaaaaa", "insuccc")
                    val intent = Intent(applicationContext, MainActivity::class.java).apply {
                        putExtra("fromLogin","fin")
                    }
                    startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
//                    Toast.makeText(applicationContext, "Authentication failed",
//                        Toast.LENGTH_SHORT)
//                        .show()
                    val intent = Intent(applicationContext, LoginActivity::class.java).apply {}
                    startActivity(intent)
                    Log.d("aaaaaaaa", "infalllled")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Noble Manager")
            .setSubtitle("Please scan your fingerprint or your face to proceed.")
            .setNegativeButtonText("CANCEL")
            .build()

        biometricPrompt.authenticate(promptInfo)


    }

}

