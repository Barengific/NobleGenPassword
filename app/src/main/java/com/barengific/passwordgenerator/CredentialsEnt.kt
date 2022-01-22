package com.barengific.passwordgenerator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.android.synthetic.main.credentials_ent.*
import java.io.File


class CredentialsEnt : AppCompatActivity() {
    //private lateinit var appBarConfiguration: AppBarConfiguration

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.credentials_ent)

        btnSubmit.setOnClickListener {
            if (editTextPassword.editText?.text.toString().isNotEmpty()
                && editTextPin.editText?.text.toString().isNotEmpty()) {
                if (editTextPassword.editText?.text.toString().length >= 8) {
                    if (editTextPin.editText?.text.toString().length == 4
                    ) {
                        saveInfo(editTextPassword.editText?.text.toString(), editTextPin.editText?.text.toString())

                        val intent = Intent(this, MainActivity::class.java).apply {
                            //putExtra(EXTRA_MESSAGE, message)
                            putExtra("fromIntro","fin")
                        }
                        Log.d("aaa", "in credentials")
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Sigma values cannot be more or less than 4 digits",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Master Key has to be above 8 characters",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Log.d("aaa", "buttonsss")
                Toast.makeText(
                    applicationContext,
                    "Please fill in all the required fields",
                    Toast.LENGTH_LONG
                ).show()
            }//TODO make toast short

        }

    }
    fun saveInfo(mk: String, pik: String){
        //TODO save keys to encrypted sharedPrefs

        val masterKey = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferencesEE: SharedPreferences = EncryptedSharedPreferences.create(
            this,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

        sharedPreferencesEE.edit().putString("signatureS", mk).apply()
        sharedPreferencesEE.edit().putString("signatureT", pik).apply()

        val nameS = sharedPreferencesEE.getString("signatureS", "nonon")
        val nameT = sharedPreferencesEE.getString("signatureT", "nonon")
        Log.d("aaaaaEEEEEEEMASTER", nameT.toString())
        Log.d("aaaaaEEEEEEEPINNNN", nameS.toString())

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }

}
