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
import java.io.*
import com.google.gson.Gson

class Restore : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    companion object {
        var checkList: MutableList<Int> = mutableListOf(-1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restore_activity)

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
            val filename = "a"

            read(this, filename,
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))

            Log.d("aaaaaYYY", read(this, filename,
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            ))
        }


    }


    fun save(context: Context, name: String, dir: File, source: Any) : Boolean {
        var realName = name
        val masterKeyAlias = MasterKey.Builder(
            context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        var file = File(dir, realName)
        //var file = File(context.filesDir.path + dir, realName)

        // Check if file doesn't yet exist and change filename if necessary
        var changes: Boolean
        do {
            changes = false
            if (file.exists()) {
                realName += ".1"
                file = File(context.filesDir.path + dir, realName)
                changes = true
            }
        } while (changes)

        // Create missing directories
        if (!file.parentFile!!.exists()) {
            file.parentFile!!.mkdirs()
        }

        val encryptedFile = EncryptedFile.Builder(
            context,
            file,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        val encryptedOutputStream = encryptedFile.openFileOutput()
        val objectOutputStream = ObjectOutputStream(encryptedOutputStream)

        objectOutputStream.writeObject(source.toString())

        objectOutputStream.close()
        encryptedOutputStream.flush()
        encryptedOutputStream.close()

        return true
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