package com.barengific.passwordgenerator.setting

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.barengific.passwordgenerator.R
import com.barengific.passwordgenerator.databinding.SecfragActivityBinding
import kotlinx.android.synthetic.main.secfrag_activity.*


class SecFrag : AppCompatActivity() {
    private lateinit var binding: SecfragActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.secfrag_activity)

        //TODO
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        hideSystemBars()

        binding = SecfragActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO read from encrypted shared preferences
        val masterKey = this.let {
            MasterKey.Builder(it, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        }

        val sharedPreferencesEE: SharedPreferences = masterKey.let {
            this.let { it1 ->
                EncryptedSharedPreferences.create(
                    it1,
                    "secret_shared_prefs",
                    it,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
            }
        }

        //read
        val nameS = sharedPreferencesEE.getString("signatureS", "nonon")
        val nameT = sharedPreferencesEE.getString("signatureT", "nonon")

        tvMasterKeyv.editText?.setText(nameS)
        tvDigitv.editText?.setText(nameT)

        btnCancels.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java).apply {
                putExtra("fromSecFrag","cancel")
            }
            startActivity(intent)
            tvMasterKeyv.editText?.setText(R.string.nonon) //("nonon")
            tvDigitv.editText?.setText(R.string.nonon)
            finish()
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this, SettingsActivity::class.java).apply {
//            putExtra(EXTRA_MESSAGE, message)
            putExtra("fromSecFrag","cancel")
        }
        startActivity(intent)
        tvMasterKeyv.editText?.setText(R.string.nonon)
        tvDigitv.editText?.setText(R.string.nonon)
        finish()
        setResult(RESULT_CANCELED)
        super.onBackPressed()
    }

    private fun hideSystemBars() {
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

}