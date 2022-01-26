package com.barengific.passwordgenerator

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.barengific.passwordgenerator.databinding.DonationActivityBinding
import java.io.*



class Donate : AppCompatActivity() {
    private lateinit var binding: DonationActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donation_activity)

        binding = DonationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        

    }

}