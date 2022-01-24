package com.barengific.passwordgenerator

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.barengific.passwordgenerator.database.AppDatabase
import com.barengific.passwordgenerator.database.Word
import kotlinx.android.synthetic.main.backup_activity.*
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.barengific.passwordgenerator.databinding.ActivityMainBinding
import com.barengific.passwordgenerator.databinding.RestoreActivityBinding
import java.io.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.backup_activity.btnRestore
import kotlinx.android.synthetic.main.restore_activity.*

class Restore : AppCompatActivity() {
    private lateinit var binding: RestoreActivityBinding

    lateinit var recyclerView: RecyclerView

    companion object {
        var checkList: MutableList<Int> = mutableListOf(-1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restore_activity)

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
            val filename = editTextFileName.editText?.text.toString()
            read(this, filename,
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))

            Log.d("aaaaaYYY", read(this, filename,
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            ))
        }

    }

    fun read(context: Context, name: String, dir: File) : String {

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