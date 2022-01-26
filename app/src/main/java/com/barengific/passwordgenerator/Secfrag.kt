package com.barengific.passwordgenerator

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.barengific.passwordgenerator.databinding.SecfragActivityBinding
import java.io.*
import kotlinx.android.synthetic.main.secfrag_activity.*


class Secfrag : AppCompatActivity() {
    private lateinit var binding: SecfragActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.secfrag_activity)

        binding = SecfragActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO read from encrypted shared preferences
        val masterKey = this?.let {
            MasterKey.Builder(it, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        }

        val sharedPreferencesEE: SharedPreferences = masterKey?.let {
            this?.let { it1 ->
                EncryptedSharedPreferences.create(
                    it1,
                    "secret_shared_prefs",
                    it,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
            }
        }!!

        //read
        val nameS = sharedPreferencesEE.getString("signatureS", "nonon")
        val nameT = sharedPreferencesEE.getString("signatureT", "nonon")

        tvMasterKeyv.editText?.setText(nameS)
        tvDigitv.editText?.setText(nameT)

        btnCancels.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java).apply {
                putExtra("fromSecfrag","cancel")
            }
            startActivity(intent)
            finish()
        }

    }

}