package com.barengific.passwordgenerator

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
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

import java.time.Instant
import java.util.*


class Backup : AppCompatActivity() {

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

            val masterKey = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val downloadFolder = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            //val file = File(downloadFolder?.path, "secret_datas.txt")

            Calendar.getInstance().getTime();
            File(
                this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/np_" + Calendar.getInstance().getTime() + ".npb"
            ).writeText("qAwerd")


            val downloadFolders = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadFolders, "secret_datas.txt")
            file.writeText("tttttttt")
            Log.d("aaaaaaffff", file.readText() )


            val f = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "bar22_new_file.txt"            )
            f.appendText("test ${Calendar.getInstance().getTime()}\n")
            f.readLines().forEach { line ->
                Log.d("aaaaaaLOG22", line)}

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val f = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "bar_new_file.txt"
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        f.appendText("test ${Instant.now().toEpochMilli()}\n")
                    }
                    f.readLines().forEach { line -> Log.d("aaaaaaLOG", line) }
                }
                Log.d(
                    "aaaaaaBACKq", File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            .toString() + "/barzzzz.txt"
                    ).readText()
                )


            val fii = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "bar_CAP.txt"            )

            val encryptedFile = EncryptedFile.Builder(
                this,
                fii,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            // write to the encrypted file
            val qqq = "hellooo".toByteArray()
            if(fii.exists()  ){
                fii.delete()
            }
            val encryptedOutputStream: FileOutputStream = encryptedFile.openFileOutput()
            encryptedOutputStream.write(qqq)
            encryptedOutputStream.flush()

            // read the encrypted file
            val encryptedInputStream: FileInputStream = encryptedFile.openFileInput()

            Log.d("awawawawa1", encryptedInputStream.toString())
//            Log.d("awawawawa2", encryptedInputStream.read().toString())
            encryptedOutputStream.flush()

            save(this, "bar_pop.txt",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "yeahMadeIT")

            Log.d("aaaaaYYY", get(this, "bar_pop.txt",
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
        objectOutputStream.writeObject(source)

        // Added this
        // Close streams
        objectOutputStream.close()
        encryptedOutputStream.flush()
        encryptedOutputStream.close()

        return true
    }

    fun get(context: Context, name: String, dir: File) : String {

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

        Log.d("aaaaAaAaA", sourceObject.toString())

//        val directory = File(context.filesDir.path + dir)
//
//        if (directory.exists() && directory.isDirectory) {
//            val files = directory.listFiles()
//            val masterKeyAlias = MasterKey.Builder(
//                context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
//                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//                .build()
//            val list = mutableListOf<Any>()
//
//            files?.forEach {
//                //ArchipelagoError.d(it.path)
//                val encryptedFile = EncryptedFile.Builder(
//                    context,
//                    it,
//                    masterKeyAlias,
//                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
//                ).build()
//
//                val encryptedInputStream = encryptedFile.openFileInput()
//                val objectInputStream = ObjectInputStream(encryptedInputStream)
//                val sourceObject = objectInputStream.readObject()
//
//                list.add(sourceObject)
//            }
//
//            return list.toTypedArray()
//        } else {
//            return arrayOf()
//        }

        return sourceObject.toString()
    }

}

//TODO remove pgen in recyclerview
//remove copy and more image

class CustomAdapters(private val dataSets: List<Word>) :
    RecyclerView.Adapter<CustomAdapters.ViewHolder>() {

    companion object {
        var position: Int = 0
//        lateinit var checkBox: CheckBox
        var isSelected: Boolean = false
        fun selectAll() {
            isSelected = true
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val checkBox: CheckBox
        val textView1: TextView
        val textView3: TextView
        val textView4: TextView

        init {
            checkBox = view.findViewById(R.id.checkBox)
            textView1 = view.findViewById(R.id.textView1)
            textView3 = view.findViewById(R.id.textView3)
            textView4 = view.findViewById(R.id.textView4)
        }

    }

    fun selectAllT() {
        isSelected = true
        notifyDataSetChanged()
    }

    fun deselectAll() {
        isSelected = false
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item_backup, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        viewHolder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                setPosition(viewHolder.position)
                setPosition(viewHolder.adapterPosition)
                return false
            }
        })

        viewHolder.checkBox.isChecked = isSelected

        viewHolder.checkBox.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
//                TODO("Not yet implemented")
                Log.d("aaa1", "on checkbox")
//                Log.d("aaa3", viewHolder.checkBox.isChecked.toString())
//                checkList.add(viewHolder.adapterPosition)

                if(viewHolder.checkBox.isChecked){
                    checkList.add(viewHolder.adapterPosition)
//                    Log.d("aaaWW", checkList.toString())

                }
                if(!viewHolder.checkBox.isChecked){
                    checkList.remove(viewHolder.adapterPosition)
//                    Log.d("aaaQQ", checkList.toString())

                }

//                if (checkArray.get(i) !== v as CheckBox) // assuming v is the View param passed in
//                    checkArray.get(i).setChecked(false)
            }

        })
        viewHolder.textView1.text = dataSets[position].wid.toString()
        viewHolder.textView3.text = dataSets[position].key.toString()
        viewHolder.textView4.text = dataSets[position].value.toString()
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }

    override fun getItemCount() = dataSets.size

    //
    private var position: Int = 0

    fun getPosition(): Int {
        return position
    }

    fun setPosition(position: Int) {
        this.position = position
    }
}