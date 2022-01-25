package com.barengific.passwordgenerator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.barengific.passwordgenerator.databinding.RestoreActivityBinding
import java.io.*
import kotlinx.android.synthetic.main.secfrag_activity.*
import kotlinx.android.synthetic.main.secfrag_activity.view.*


class Secfrag : AppCompatActivity() {
    private lateinit var binding: RestoreActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.secfrag_activity)

        binding = RestoreActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO read from encrypted shared preferences

        tvMasterKeyv.editText?.text = ""
        tvDigitv.editText?.text = ""



        btnCancels.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java).apply {
                putExtra("fromRestore","cancel")
            }
            startActivity(intent)
            finish()
        }

    }

}