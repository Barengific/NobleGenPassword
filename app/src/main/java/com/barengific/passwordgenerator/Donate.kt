package com.barengific.passwordgenerator

import android.content.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.barengific.passwordgenerator.databinding.DonationActivityBinding
import com.barengific.passwordgenerator.setting.SettingsActivity
import kotlinx.android.synthetic.main.donation_activity.*
import kotlinx.android.synthetic.main.donation_activity.btnCancels

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

            Toast.makeText(applicationContext, "BTC Address Copied", Toast.LENGTH_SHORT).show()
            Log.d("aaa", "copying123")
        }

        tvETH.setStartIconOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("PGen", tvETH.editText?.text.toString())
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip)

            Toast.makeText(applicationContext, "ETH Address Copied", Toast.LENGTH_SHORT).show()
            Log.d("aaa", "copying123")
        }

        tvBCH.setStartIconOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("PGen", tvBCH.editText?.text.toString())
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip)

            Toast.makeText(applicationContext, "BCH Address Copied", Toast.LENGTH_SHORT).show()
            Log.d("aaa", "copying123")
        }

        tvLTC.setStartIconOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("PGen", tvLTC.editText?.text.toString())
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip)

            Toast.makeText(applicationContext, "LTC Address Copied", Toast.LENGTH_SHORT).show()
            Log.d("aaa", "copying123")
        }

        tvETC.setStartIconOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("PGen", tvETC.editText?.text.toString())
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip)

            Toast.makeText(applicationContext, "ETC Address Copied", Toast.LENGTH_SHORT).show()
            Log.d("aaa", "copying123")
        }

        tvAlgo.setStartIconOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("PGen", tvAlgo.editText?.text.toString())
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip)

            Toast.makeText(applicationContext, "ALGO Address Copied", Toast.LENGTH_SHORT).show()
            Log.d("aaa", "copying123")
        }

        tvNano.setStartIconOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("PGen", tvNano.editText?.text.toString())
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip)

            Toast.makeText(applicationContext, "NANO Address Copied", Toast.LENGTH_SHORT).show()
            Log.d("aaa", "copying123")
        }

        btnCancels.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java).apply {
                putExtra("fromDonate","cancel")
            }
            startActivity(intent)
            finish()
        }
    }

}