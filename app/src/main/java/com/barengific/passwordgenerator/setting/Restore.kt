package com.barengific.passwordgenerator.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.barengific.passwordgenerator.database.AppDatabase
import com.barengific.passwordgenerator.database.Word
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.barengific.passwordgenerator.R
import com.barengific.passwordgenerator.databinding.RestoreActivityBinding
import java.io.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.restore_activity.*
import kotlin.system.exitProcess


class Restore : AppCompatActivity() {
    private lateinit var binding: RestoreActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restore_activity)

        //TODO
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        binding = RestoreActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //db initialise
        val passphrase: ByteArray =
            SQLiteDatabase.getBytes("bob".toCharArray())//TODO change pass phrase
        val factory = SupportFactory(passphrase)
        val room =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-names")
                .openHelperFactory(factory)
                .allowMainThreadQueries()
                .build()
        val wordDao = room.wordDao()

        btnRestore.setOnClickListener {
            val filename = tvMv.editText?.text.toString()
            read(this, filename,
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))

            Log.d("aaa YYY", read(this, filename,
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            ))

            val gson = Gson()

            val ww = read(this, filename,
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))

            val enums: Array<Word> = gson.fromJson(ww,Array<Word>::class.java)

            Log.d("aaa_ZZZ", enums.toString())

            for (i in enums.indices){
                val aa = Word(
                    0,
                    "pGen",
                    enums[i].key.toString(),
                    enums[i].value.toString()
                )
                wordDao.insertAll(aa)
            }

            Toast.makeText(applicationContext, "Successfully restored. Please restart the app to continue!", Toast.LENGTH_LONG).show()
            finish()
            exitProcess(0)

        }

        btnCancel.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java).apply {
                putExtra("fromRestore","cancel")
            }
            startActivity(intent)
            finish()
        }

    }

    private fun read(context: Context, name: String, dir: File) : String {

        val masterKey = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val fii = File(dir, name)

        val encryptedFile = EncryptedFile.Builder(
            context,
            fii,
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        val encryptedInputStream = encryptedFile.openFileInput()
        val objectInputStream = ObjectInputStream(encryptedInputStream)
        val sourceObject = objectInputStream.readObject()

        return sourceObject.toString()
    }

}