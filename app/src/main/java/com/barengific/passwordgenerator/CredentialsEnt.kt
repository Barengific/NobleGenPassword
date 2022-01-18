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
import androidx.security.crypto.MasterKeys
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
                        //val path = applicationContext.getFilesDir()
                        //Log.d("aaa", path.toString())

                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        Log.d("aaa", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString())

                        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/barz.txt").writeText(
                            editTextPassword.editText?.text.toString() +"\n" +
                                    editTextPin.editText?.text.toString())

                        val intent = Intent(this, MainActivity::class.java).apply {
                            //putExtra(EXTRA_MESSAGE, message)
                            putExtra("fromIntro","fin")
                        }
                        Log.d("aaa", "in credentials")
                        startActivity(intent)
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
    fun saveInfo(mk: String, pik: Int){
        //TODO save keys to encrypted sharedPrefs
        val sharedPref = this?.getSharedPreferences(
            getString(R.string.preference_file_key_del), Context.MODE_PRIVATE)

        val sharedPreff = this?.getPreferences(Context.MODE_PRIVATE)

        //write save
        val newHighScore: Int = 12345
        val sharedPrefSave = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPrefSave.edit()) {
            putInt(getString(R.string.saved_high_score_key_del), newHighScore)
            apply()
        }


        // read retreive
        val sharedPrefRead = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        val defaultValue = resources.getInteger(R.integer.saved_high_score_default_key_del)
        val highScore = sharedPrefRead.getInt(getString(R.string.saved_high_score_key_del), defaultValue)

        //for settings //redwrite
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
        //read
        val name = sharedPreferences.getString("signature", "nonon")
        //write
        with (sharedPreferences.edit()){
            putString("signature", "newwwsigg")
            apply()
        }
        val nameS = sharedPreferences.getString("signature", "nonon")

//            Log.d("aaaaaaSgared1", highScore.toString() )
//            Log.d("aaaaaaSgared2", name.toString() )
//            Log.d("aaaaaaSgared3", nameS.toString() )

        val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val masterKeyAliasS: MasterKey.Builder = MasterKey.Builder(this)
        val sharedPreferencesE: SharedPreferences = EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            masterKeyAlias,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val masterKey = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferencesEE: SharedPreferences = EncryptedSharedPreferences.create(
            this,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

        sharedPreferencesEE.edit().putString("signatureS", "encrrrr").apply()
        val nameE = sharedPreferencesEE.getString("signatureS", "nonon")
        Log.d("aaaaaEEEEEEE", nameE.toString())
        Log.d("aaaaaEEEEEEEMM", masterKey.toString())
        // use the shared preferences and editor as you normally would

        // use the shared preferences and editor as you normally would
        val editor = sharedPreferences.edit()
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
