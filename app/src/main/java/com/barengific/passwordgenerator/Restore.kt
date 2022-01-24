package com.barengific.passwordgenerator

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.barengific.passwordgenerator.Backup.Companion.checkList
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
        setContentView(R.layout.backup_activity)

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

        //recycler initialise
        val arr = wordDao.getAll()
        var adapter = CustomAdapters(arr)
        recyclerView = findViewById<View>(R.id.rview) as RecyclerView
        recyclerView.setHasFixedSize(false)
        recyclerView.setAdapter(adapter)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        btnSelectAll.setOnClickListener {
            CustomAdapters.isSelected = true
            recyclerView.adapter?.notifyDataSetChanged()
            Log.d("aaaaaSelected", CustomAdapters.isSelected.toString())
        }

        btnSelectNone.setOnClickListener {
            CustomAdapters.isSelected = false
            recyclerView.adapter?.notifyDataSetChanged()
            Log.d("aaaaaDeSelected", CustomAdapters.isSelected.toString())
        }

        btnBackups.setOnClickListener {
            //TODO if select all is true, then save all data
            //if select all is false, then save from checkList

            val time = System.currentTimeMillis()
            if(CustomAdapters.isSelected) {
                //save all
                val gson = Gson()
                val arrJ = gson.toJson(arr)
//                Log.d("aaaaaJSON", arrJ)
                save(
                    this, "nobles_$time.txt",
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    arrJ)
                Toast.makeText(applicationContext, "File saved as: nobles_$time.txt in your downloads folder!", Toast.LENGTH_LONG).show()

            }else if (!CustomAdapters.isSelected and (checkList.size <= 1)){
                Toast.makeText(applicationContext, "Please Select At Least One !", Toast.LENGTH_LONG).show()
            }else {
                //save checkList
//                var savedList: MutableList<Word>

                val savedList = mutableListOf<Word>()
                for (i in 0 until checkList.size){
                    val cli = checkList[i]
//                    Log.d("aaaaacli", cli.toString())
                    if(cli != -1){
                        val ari = arr[cli]
//                        Log.d("aaaaaari", ari.toString())
                        savedList.add(ari)
                    }

                }
//                Log.d("aaaaainsavv", savedList.toString())
                Toast.makeText(applicationContext, "File saved as: nobles_$time.txt in your downloads folder!", Toast.LENGTH_LONG).show()
            }

//            save(this, "nobles_$time.txt",
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//            "yeahMadeIT")
//            Log.d("aaaaaYYY", read(this, "noblest_$time.txt",
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    ))


            //confirm file save with toast with location of saved file
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