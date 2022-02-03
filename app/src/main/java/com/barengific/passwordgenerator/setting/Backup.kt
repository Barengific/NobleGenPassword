package com.barengific.passwordgenerator.setting

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.barengific.passwordgenerator.setting.Backup.Companion.checkList
import com.barengific.passwordgenerator.database.AppDatabase
import com.barengific.passwordgenerator.database.Word
import kotlinx.android.synthetic.main.backup_activity.*
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.barengific.passwordgenerator.R
import java.io.*
import com.google.gson.Gson

class Backup : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    companion object {
        var checkList: MutableList<Int> = mutableListOf(-1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.backup_activity)

        //TODO
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        hideSystemBars()

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
        val adapter = CustomAdapters(arr)
        recyclerView = findViewById<View>(R.id.rview) as RecyclerView
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnSelectAll.setOnClickListener {
            CustomAdapters.isSelected = true
            recyclerView.adapter?.notifyDataSetChanged()
        }

        btnSelectNone.setOnClickListener {
            CustomAdapters.isSelected = false
            recyclerView.adapter?.notifyDataSetChanged()
        }

        btnBackups.setOnClickListener {
            val time = System.currentTimeMillis()
            when {
                CustomAdapters.isSelected -> {
                    //save all
                    val gson = Gson()
                    val arrJ = gson.toJson(arr)
        //                Log.d("aaa JSON", arrJ)
                    save(
                        this, "nobles_$time.txt",
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        arrJ)
                    Toast.makeText(applicationContext, "File saved as: nobles_$time.txt in your downloads folder!", Toast.LENGTH_LONG).show()


                }
                !CustomAdapters.isSelected and (checkList.size <= 1) -> {
                    Toast.makeText(applicationContext, "Please Select At Least One !", Toast.LENGTH_LONG).show()
                }
                else -> {
                    //save checkList
        //                var savedList: MutableList<Word>

                    val savedList = mutableListOf<Word>()
                    for (i in 0 until checkList.size){
                        val cli = checkList[i]
        //                    Log.d("aaa cli", cli.toString())
                        if(cli != -1){
                            val ari = arr[cli]
        //                        Log.d("aaa ri", ari.toString())
                            savedList.add(ari)
                        }

                    }
        //                Log.d("aaa_in_save", savedList.toString())
                    Toast.makeText(applicationContext, "File saved as: nobles_$time.txt in your downloads folder!", Toast.LENGTH_LONG).show()
                }
            }

//            save(this, "nobles_$time.txt",
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//            "yeahMadeIT")
//            Log.d("aaa YYY", read(this, "noblest_$time.txt",
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    ))


            //confirm file save with toast with location of saved file
        }

        btnCancelb.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java).apply {
                putExtra("fromBackup","cancel")
            }
            startActivity(intent)
            finish()
        }


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

}

//TODO remove pGen in recyclerview
//remove copy and more image

class CustomAdapters(private val dataSets: List<Word>) :
    RecyclerView.Adapter<CustomAdapters.ViewHolder>() {

    companion object {
        var isSelected: Boolean = false
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

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item_backup, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        viewHolder.itemView.setOnLongClickListener {
            setPosition(viewHolder.adapterPosition)
            false
        }

        viewHolder.checkBox.isChecked = isSelected

        viewHolder.checkBox.setOnClickListener {
            if (viewHolder.checkBox.isChecked) {
                checkList.add(viewHolder.adapterPosition)
            }
            if (!viewHolder.checkBox.isChecked) {
                checkList.remove(viewHolder.adapterPosition)
            }
        }
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

    private fun setPosition(position: Int) {
        this.position = position
    }
}