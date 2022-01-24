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

            val time = System.currentTimeMillis()

            if(CustomAdapters.isSelected) {
                //save all
                val gson = Gson()
                val arrJ = gson.toJson(arr)
//                Log.d("aaaaaJSON", arrJ)

                save(
                    this, "noblest_$time.txt",
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    arrJ)
            }else if (!CustomAdapters.isSelected){
                Toast.makeText(applicationContext, "Please Select At Least One !", Toast.LENGTH_LONG).show()
            }else {
                //save checkList
//                var savedList: MutableList<Word>

                val savedList = mutableListOf<Word>()
                for (i in 0 until checkList.size){
                    val cli = checkList[i]
                    Log.d("aaaaacli", cli.toString())
                    if(cli != -1){
                        val ari = arr[cli]
                        Log.d("aaaaaari", ari.toString())
                        savedList.add(ari)
                    }

                }
                Log.d("aaaaainsavv", savedList.toString())
            }

            save(this, "noblest_$time.txt",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "yeahMadeIT")
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

//        Log.d("aaaaAaAaA", sourceObject.toString())

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