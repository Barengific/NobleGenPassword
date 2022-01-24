package com.barengific.passwordgenerator

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
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

        btnSelectAll.setOnClickListener {
            CustomAdapters.isSelected = true
            recyclerView.adapter?.notifyDataSetChanged()
            Log.d("aaaaaSelected", CustomAdapters.isSelected.toString())
        }

        btnSelectNone.setOnClickListener{
            CustomAdapters.isSelected = false
            recyclerView.adapter?.notifyDataSetChanged()
            Log.d("aaaaaDeSelected", CustomAdapters.isSelected.toString())
        }

        btnBackups.setOnClickListener{
            //TODO if select all is true, then save all data
            //if select all is false, then save from checkList
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