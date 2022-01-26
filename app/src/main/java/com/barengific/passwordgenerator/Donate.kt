package com.barengific.passwordgenerator

import android.content.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.barengific.passwordgenerator.databinding.DonationActivityBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.donation_activity.*
import java.io.*



class Donate : AppCompatActivity() {
    private lateinit var binding: DonationActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donation_activity)

        binding = DonationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvBTC.setStartIconOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("PGen", tvBTC.editText?.text.toString())
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip)

            Toast.makeText(applicationContext, "Text Copied", Toast.LENGTH_SHORT).show()
            Log.d("aaaaaaaaa", "copyyyingggg123")
        }

    }

}