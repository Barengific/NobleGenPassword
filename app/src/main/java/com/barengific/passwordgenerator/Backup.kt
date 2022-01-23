package com.barengific.passwordgenerator

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.barengific.passwordgenerator.database.AppDatabase
import com.barengific.passwordgenerator.database.Word
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

class Backup : AppCompatActivity(){

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
        var adapter = CustomAdapter(arr)
        MainActivity.recyclerView = findViewById<View>(R.id.rview) as RecyclerView
        MainActivity.recyclerView.setHasFixedSize(false)
        MainActivity.recyclerView.setAdapter(adapter)
        MainActivity.recyclerView.setLayoutManager(LinearLayoutManager(this))

    }
}

//TODO remove pgen in recyclerview
//remove copy and more image

class CustomAdapter(private val dataSet: List<Word>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    companion object {
        var position: Int = 0
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnCreateContextMenuListener {
        var fileName: TextView
        val textView1: TextView
        val textView3: TextView
        val textView4: TextView

        init {
            fileName = view.findViewById(R.id.textView4) as TextView
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

        viewHolder.ivMore.setOnClickListener(object : View.OnClickListener {

            override fun onClick(view: View?) {
                //creating a popup menu
                val popup = PopupMenu(view?.context, viewHolder.ivMore)
                //inflating menu from xml resource
                popup.inflate(R.menu.rv_menu_context)
                //adding click listener
                popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        when (item.itemId) {
                            R.id.menu_copy -> {
                                Log.d("aaaamenuu","copy")
                                val clipboard = view?.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip: ClipData = ClipData.newPlainText("PGen", viewHolder.textView4.text.toString())
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(view?.context, "Text Copied", Toast.LENGTH_LONG).show()

                            }
                            R.id.menu_delete -> {
                                val passphrase: ByteArray = SQLiteDatabase.getBytes("bob".toCharArray())
                                val factory = SupportFactory(passphrase)
                                val room = view?.context?.let {
                                    Room.databaseBuilder(it, AppDatabase::class.java, "database-names")
                                        .openHelperFactory(factory)
                                        .allowMainThreadQueries()
                                        .build()
                                }
                                val wordDao = room?.wordDao()
//
//                                Log.d("aaaamenuu","delete")
//                                val db = view?.context?.let {
//                                    Room.databaseBuilder(
//                                        it,
//                                        AppDatabase::class.java, "database-name"
//                                    ).allowMainThreadQueries().build()
//                                }
//                                val wordDao = db?.wordDao()

                                val wid: TextView? = viewHolder.textView1
                                val key: TextView? = viewHolder.textView3
                                val value: TextView? = viewHolder.textView4

                                var a = Word(
                                    wid?.text.toString().toInt(),
                                    pType?.text.toString(),
                                    key?.text.toString(),
                                    value?.text.toString()
                                )
                                room?.wordDao()?.delete(a)
                                val arrr = wordDao?.getAll()
                                var adapter = arrr?.let { CustomAdapter(it) }

                                MainActivity.recyclerView.setHasFixedSize(false)
                                MainActivity.recyclerView.setAdapter(adapter)
                                MainActivity.recyclerView.setLayoutManager(LinearLayoutManager(view?.context))
                                room?.close()
                                Log.d("aaaamenuu","DDDdelete")

                            }

                            R.id.menu_cancel ->  {
                                Log.d("aaaamenuu","canceeel") //TODO
                            }
                            R.id.menu_hide ->  {
                                val passphrase: ByteArray = SQLiteDatabase.getBytes("bob".toCharArray())//DB passprhase change
                                val factory = SupportFactory(passphrase)
                                val room = view?.context?.let {
                                    Room.databaseBuilder(it, AppDatabase::class.java, "database-names")
                                        .openHelperFactory(factory)
                                        .allowMainThreadQueries()
                                        .build()
                                }
                                val wordDao = room?.wordDao()

                                val arrr = wordDao?.getAll()

                                var btnHideAllStatus = false

                                Log.d("aaaaMORE", viewHolder.adapterPosition.toString())
                                Log.d("aaaaMORE2", position.toString())
                                Log.d("aaaaMORE3", MainActivity.posis.toString())
                                if(MainActivity.posis.contains(viewHolder.adapterPosition)){//if existent then show
                                    MainActivity.posis.remove(viewHolder.adapterPosition)

                                    val pSize = MainActivity.posis.size
                                    for (i in 0 until pSize) {
                                        Log.d("aaaaCVCVCVQQ", MainActivity.posis.get(i).toString())
                                        if((MainActivity.posis.get(i) != -1)){
                                            val qSize = MainActivity.posis.get(i)
                                            arrr?.get(qSize)?.value = "****"
                                            arrr?.get(qSize)?.key = "****"
                                        }
                                    }

                                    var adapter = arrr?.let { CustomAdapter(it) }
                                    MainActivity.recyclerView.setHasFixedSize(false)
                                    MainActivity.recyclerView.setAdapter(adapter)
                                    MainActivity.recyclerView.setLayoutManager(LinearLayoutManager(view?.context))
                                    room?.close()

                                }else{//if not existent then hide
                                    MainActivity.posis.add(viewHolder.adapterPosition)

                                    val pSize = MainActivity.posis.size
                                    for (i in 0 until pSize) {
                                        Log.d("aaaaCVCVCV", MainActivity.posis.get(i).toString())
                                        if((MainActivity.posis.get(i) != -1)){
                                            val qSize = MainActivity.posis.get(i)
                                            arrr?.get(qSize)?.value = "****"
                                            arrr?.get(qSize)?.key = "****"
                                        }
                                    }
                                    var adapter = arrr?.let { CustomAdapter(it) }
                                    //recyclerView = findViewById<View>(R.id.rview) as RecyclerView
                                    MainActivity.recyclerView.setHasFixedSize(false)
                                    MainActivity.recyclerView.setAdapter(adapter)
                                    MainActivity.recyclerView.setLayoutManager(LinearLayoutManager(view?.context))
                                    room?.close()

                                }

                            }

                        }
                        return true
                    }
                })
                //displaying the popup
                popup.show()
            }
        })

        viewHolder.ivCopy.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                Log.d("aaaaICONu","innn copyy")
                val clipboard = view?.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("PGen", viewHolder.textView4.text.toString())
                clipboard.setPrimaryClip(clip)
                Toast.makeText(view?.context, "Text Copied", Toast.LENGTH_LONG).show()

            }
        })

        viewHolder.textView1.text = dataSet[position].wid.toString()
        viewHolder.textView3.text = dataSet[position].key.toString()
        viewHolder.textView4.text = dataSet[position].value.toString()
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }

    override fun getItemCount() = dataSet.size

    //
    private var position: Int = 0

    fun getPosition(): Int {
        return position
    }

    fun setPosition(position: Int) {
        this.position = position
    }
}