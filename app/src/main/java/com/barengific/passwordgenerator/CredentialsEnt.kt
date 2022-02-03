package com.barengific.passwordgenerator

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.barengific.passwordgenerator.database.AppDatabase
import kotlinx.android.synthetic.main.credentials_ent.*
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

class CredentialsEnt : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.credentials_ent)

        //TODO
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        hideSystemBars()

        val fromSettings = intent.extras?.get("fromSettings")
        if(fromSettings.toString().equals("rst")){
            //delete all database entries
            val passphrase: ByteArray = SQLiteDatabase.getBytes("bob".toCharArray())
            val factory = SupportFactory(passphrase)
            val room = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-names")
                .openHelperFactory(factory)
                .allowMainThreadQueries()
                .build()
            val wordDao = room.wordDao()

            val arr = wordDao.getAll()

            for(i in arr.indices){
                wordDao.delete(arr[i])
            }

        }

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
                            "Pin cannot be more or less than 4 digits",
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
                Log.d("aaa", "buttons")
                Toast.makeText(
                    applicationContext,
                    "Please fill in all the required fields",
                    Toast.LENGTH_LONG
                ).show()
            }//TODO make toast short

        }

    }
    private fun saveInfo(mk: String, pik: String){
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

//        val nameS = sharedPreferencesEE.getString("signatureS", "nonon")
//        val nameT = sharedPreferencesEE.getString("signatureT", "nonon")
//        Log.d("aaa_IEEE_MASTER", nameT.toString())
//        Log.d("aaa_IEEE_PIN", nameS.toString())

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
