package com.barengific.passwordgenerator

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.barengific.passwordgenerator.Backup.Companion.checkList
import com.barengific.passwordgenerator.database.AppDatabase
import com.barengific.passwordgenerator.database.Word
import kotlinx.android.synthetic.main.text_row_item_backup.view.*
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

class Backup : AppCompatActivity(){

    lateinit var recyclerView: RecyclerView

    companion object {
        var checkList: MutableList<Int> = mutableListOf(-1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.backup_activity)


        //db initialise
        val passphrase: ByteArray = SQLiteDatabase.getBytes("bob".toCharArray())//TODO change pass phrase
        val factory = SupportFactory(passphrase)
        val room = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-names")
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

        val rvSize = recyclerView.adapter?.itemCount
        Log.d("aaaaaITEMCOun", rvSize.toString())
        for(i in 0 until rvSize!!){
            val cb = recyclerView.findViewHolderForAdapterPosition(i)?.itemView?.findViewById<CheckBox>(R.id.checkBox)
            cb?.isChecked = true

            recyclerView.adapter?.notifyDataSetChanged()
            val bb = recyclerView.findViewHolderForAdapterPosition(0)?.itemView?.findViewById<CheckBox>(R.id.checkBox)


//            recyclerView.findViewHolderForAdapterPosition(i).adapterPosition.
            Log.d("aaaaaSEL: $i", recyclerView.findViewHolderForAdapterPosition(i)?.itemView?.findViewById<CheckBox>(R.id.checkBox)?.isChecked.toString())
        }




    }
}

//TODO remove pgen in recyclerview
//remove copy and more image

class CustomAdapters(private val dataSets: List<Word>) :
    RecyclerView.Adapter<CustomAdapters.ViewHolder>() {

    companion object {
        var position: Int = 0
//        lateinit var checkBox: CheckBox
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
        viewHolder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                setPosition(viewHolder.position)
                setPosition(viewHolder.adapterPosition)
                return false
            }
        })

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