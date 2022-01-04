package com.barengific.passwordgenerator

import android.annotation.SuppressLint
import android.content.*
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.barengific.passwordgenerator.databinding.ActivityMainBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.barengific.passwordgenerator.database.AppDatabase
import com.barengific.passwordgenerator.database.Word
import kotlinx.android.synthetic.main.fragment_home.*

import android.widget.Toast
import android.text.Editable

import android.text.TextWatcher
import android.util.Base64
import android.view.ContextMenu.ContextMenuInfo
import android.util.Log
import android.view.*

import android.widget.TextView
import android.view.MenuInflater

import android.view.ContextMenu
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.length_layout.*
import android.widget.AdapterView.OnItemClickListener
import com.barengific.passwordgenerator.crypt.Acvb
import com.barengific.passwordgenerator.crypt.Encode.encode
import com.barengific.passwordgenerator.crypt.Decode.decode
import java.io.UnsupportedEncodingException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.InvalidParameterSpecException
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    var arrr: List<Word> = listOf(Word(0,"","",""))

    companion object {
        var pos: Int = 0
        fun getPosi() : Int = pos
        fun setPosi(pos: Int) { this.pos = pos}
    }

//    init {
//        0.also { pos = it }
//    }

    var pos: Int = 0;

//    fun getPosi(): Int {
//        return pos
//    }
//
//    fun setPosi(pos: Int) {
//        this.pos = pos
//    }

    lateinit var recyclerView: RecyclerView
//    lateinit var db = Room.databaseBuilder(applicationContext,
//        AppDatabase::class.java, "database-name"
//    ).allowMainThreadQueries().build()

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        //intro activity
//        val message = intent.getStringExtra("fromIntro")
//        if(message.toString().equals("fin")){
//            Log.d("aaa", "no more intro")
//        }else{
//            val intent = Intent(this, AppIntroduction::class.java).apply {
////            putExtra(EXTRA_MESSAGE, message)
//            }
//            startActivity(intent)
//        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //db initialise
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()
        val wordDao = db.wordDao()

        //recycle view
        val arr = wordDao.getAll()
        var adapter = CustomAdapter(arr)
        recyclerView = findViewById<View>(R.id.rview) as RecyclerView
        recyclerView.setHasFixedSize(false)
        recyclerView.setAdapter(adapter)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

//        adapter = CustomAdapter(arr)
//        recyclerView.setAdapter(adapter)
//        adapter.notifyDataSetChanged()
//        adapter.notifyDataSetChanged()
//        recyclerView.adapter?.notifyDataSetChanged()
//        adapter.notifyItemRangeChanged(0,5)


        //length dropdown
//        val spinner: Spinner = findViewById(R.id.p_len_spinner)
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.p_len_array,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spinner.adapter = adapter
//        }


        //length dropdown
        val Lines = resources.getStringArray(R.array.p_len_array).toList()
        val adapterr = ArrayAdapter(this, R.layout.length_layout, Lines)
        filled_exposed_dropdown.setAdapter(adapterr)
        //filled_exposed_dropdown.setText("10",false)

//        (TextField.editableText as? AutoCompleteTextView)?.setAdapter(adapterr)
////        val spinnerr: Spinner = findViewById(R.id.filled_exposed_dropdown)
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter.createFromResource(this,R.array.p_len_array,
//            android.R.layout.simple_dropdown_item_1line
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            //spinnerr.adapter = adapter
//
//        }

        val ss = Pgen()


        //Listeners
        btnGenerate.setOnClickListener {
            //tvGen.setText(ss.pgen(editTextKeyGen.editText.toString(),"jimbob","4","5","6","7",spinner.selectedItem.toString().toInt()))
            //tvGen.editText?.setText(ss.pgen(editTextKeyGen.editText?.text.toString(),"jimbob","4","5","6","7",spinner.selectedItem.toString().toInt()))
            if(filled_exposed_dropdown.hasSelection()){
                tvGen.editText?.setText(ss.pgen(editTextKeyGen.editText?.text.toString(),"jimbob","4","5","6","7",filled_exposed_dropdown.editableText.toString().toInt()))
            }else{
                tvGen.editText?.setText(ss.pgen(editTextKeyGen.editText?.text.toString(),"jimbob","4","5","6","7",10))
            }

            Log.d("aaa", "in gennnnn")
            val rv = encode("Hello")
            Log.d("aaaENNN", rv)
            // get code from edittext
            Log.d("aaa", "text - $rv")
            val rv2 = decode(rv)
            Log.d("aaaDEC", rv2)

            val acvb = Acvb

            val enout = acvb.encrypt_AES("aaaaaaaaaaaaaaaa","hello this is a mesage", "qqqqqqqqqqqqqqqq")
            Log.d("aaaQQQ_EN", enout!!)

            val deout = acvb.decrypt("aaaaaaaaaaaaaaaa", enout, "qqqqqqqqqqqqqqqq")
            Log.d("aaaQQQ_DEC", deout!!)

        }


        tvCopy.setOnClickListener{
            // Creates a new text clip to put on the clipboard
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("PGen", tvGen.editText?.text.toString())
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip)

            Toast.makeText(applicationContext,"Text Copied", Toast.LENGTH_LONG).show()
//
          //tvGen.setText(getPosi().toString())
//            tvGen.setText(wordDao.getAll().get(1).wid.toString() + "_" + wordDao.getAll().get(1).pType
//                    + "_" + wordDao.getAll().get(1).key + "_" + wordDao.getAll().get(1).value)
//
//            val text1: TextView? =
//                recyclerView.findViewHolderForAdapterPosition(getPosi())?.itemView?.findViewById(
//                    R.id.textView4)
//
//            Log.d("aaacc", text1?.text.toString())
//            Log.d("aaa", editTextKeyGen.editText?.text.toString())
        }

        btnSave.setOnClickListener{
            val aa = Word(0,"pgen", editTextKeyGen.editText?.text.toString(),  tvGen.editText?.text.toString())
            wordDao.insertAll(aa)

            arrr = wordDao.getAll()

            var adapter = CustomAdapter(arrr)
            //recyclerView = findViewById<View>(R.id.rview) as RecyclerView
            recyclerView.setHasFixedSize(false)
            recyclerView.setAdapter(adapter)
            recyclerView.setLayoutManager(LinearLayoutManager(this))

//            recyclerView.setAdapter(adapter)
//            adapter.notifyDataSetChanged()
//            recyclerView.adapter?.notifyDataSetChanged()
//            adapter.notifyItemRangeChanged(0,5)

            //TODO check for duplicates, i.e. comparedkey and length if already exists when don't add to db
        }

//        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                tvGen.editText?.setText(ss.pgen(editTextKeyGen.editText.toString(),"jimbob","4","5","6","7",spinner.selectedItem.toString().toInt()))
//            } // to close the onItemSelected
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        })

        filled_exposed_dropdown.setOnItemClickListener(OnItemClickListener { parent, view, position, rowId ->Int
            val selection = parent.getItemAtPosition(position) as String
            Log.d("aaanewMAterialSpinner", selection)
            tvGen.editText?.setText(ss.pgen(editTextKeyGen.editText?.text.toString(),"jimbob","4","5","6","7",selection.toInt()))

        })

//
//        autoCompleteTextView.setOnItemClickListener(OnItemClickListener { parent, view, position, rowId ->
//            val selection = parent.getItemAtPosition(position) as String
//            //TODO Do something with the selected text
//        })


        editTextKeyGen.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(filled_exposed_dropdown.hasSelection()){
                    tvGen.editText?.setText(ss.pgen(editTextKeyGen.editText?.text.toString(),"jimbob","4","5","6","7",filled_exposed_dropdown.editableText.toString().toInt()))
                }else{
                    tvGen.editText?.setText(ss.pgen(editTextKeyGen.editText?.text.toString(),"jimbob","4","5","6","7",10))
                }
                //tvGen.editText?.setText(ss.pgen(editTextKeyGen.editText?.text.toString(),"jimbob","4","5","6","7",filled_exposed_dropdown.editableText.toString().toInt()))
            }
        })

        registerForContextMenu(recyclerView);
        val ustomAdapter = CustomAdapter(arr)

        //val selectedPostion = (ustomAdapter as AdapterContextMenuInfo).position

        val plaintext: ByteArray = "...".encodeToByteArray()
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)
        val key: SecretKey = keygen.generateKey()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val ciphertext: ByteArray = cipher.doFinal(plaintext)
        val iv: ByteArray = cipher.iv


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = this.menuInflater //getActivity().getMenuInflater()
        inflater.inflate(R.menu.rv_menu_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_copy -> {
                val text1: TextView? =
                    recyclerView.findViewHolderForAdapterPosition(getPosi())?.itemView?.findViewById(
                        R.id.textView4)

                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("PGen", text1?.text.toString())
                clipboard.setPrimaryClip(clip)
                Toast.makeText(applicationContext,"Text Copied", Toast.LENGTH_LONG).show()
            }
            R.id.menu_delete -> {
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "database-name"
                ).allowMainThreadQueries().build()
                val wordDao = db.wordDao()
//                arrr = wordDao.getAll()
//                Log.d("aaamenu_delete", arrr.toString())

                val wid: TextView? =
                    recyclerView.findViewHolderForAdapterPosition(getPosi())?.itemView?.findViewById(
                        R.id.textView1)
                val pType: TextView? =
                    recyclerView.findViewHolderForAdapterPosition(getPosi())?.itemView?.findViewById(
                        R.id.textView2)
                val key: TextView? =
                    recyclerView.findViewHolderForAdapterPosition(getPosi())?.itemView?.findViewById(
                        R.id.textView3)
                val value: TextView? =
                    recyclerView.findViewHolderForAdapterPosition(getPosi())?.itemView?.findViewById(
                        R.id.textView4)

                var a = Word(wid?.text.toString().toInt(), pType?.text.toString(), key?.text.toString(), value?.text.toString())
                db.wordDao().delete(a)
                arrr = wordDao.getAll()
                var adapter = CustomAdapter(arrr)
                recyclerView.setHasFixedSize(false)
                recyclerView.setAdapter(adapter)
                recyclerView.setLayoutManager(LinearLayoutManager(this))
                db.close()

            }
            R.id.menu_cancel -> {
                Log.d("aaamenu_cancel",  getPosi().toString())
            }
        }
        return super.onContextItemSelected(item)
    }


    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidParameterSpecException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class,
        UnsupportedEncodingException::class
    )
    fun encryptMsg(message: String, secret: SecretKey?): String? {
        var cipher: Cipher? = null
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secret)
        val cipherText: ByteArray = cipher.doFinal(message.toByteArray(charset("UTF-8")))
        return Base64.encodeToString(cipherText, Base64.NO_WRAP)
    }

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidParameterSpecException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        UnsupportedEncodingException::class
    )
    fun decryptMsg(cipherText: String?, secret: SecretKey?): String? {
        var cipher: Cipher? = null
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secret)
        val decode: ByteArray = Base64.decode(cipherText, Base64.NO_WRAP)
        //String("aa")
        //String(cipher.doFinal(decode), 'U')
        return String(cipher.doFinal(decode))
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun generateKey(key: String): SecretKey? {
        val secret: SecretKeySpec
        secret = SecretKeySpec(key.toByteArray(), "AES")
        return secret
    }

}

class CustomAdapter(private val dataSet: List<Word>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    companion object {
        var position: Int = 0
        fun getPosi() : Int = position
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnCreateContextMenuListener {
        var icon: ImageView
        var fileName: TextView
        var menuButton: ImageView
        @SuppressLint("ResourceType")
        override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenuInfo?) {
            //menuInfo is null
//            menu.add(
//                Menu.NONE, Menu.NONE, Menu.NONE, Menu.NONE
//                //Menu.NONE, "R.string.remove_backup"
//            )
//            menu.add(
//                R.id.ivCopy, "Copy",
//                Menu.NONE, Menu.NONE
//                //Menu.NONE, "R.string.restore_backup"
//            )
            //menu.add(0, Menu.NONE, getAdapterPosition(), Menu.NONE);
            //menu.add(0,menu.size(),1,"a")
            //menu.add(R.id.ivMore)
//            Log.d("aaahol", getPosition().toString())
            MainActivity.pos = getPosition()
            MainActivity.setPosi(getPosition())

//            menu.setHeaderTitle("Hi")
        }

        val textView1: TextView
        val textView2: TextView
        val textView3: TextView
        val textView4: TextView

        init {
            icon = view.findViewById(R.id.ivCopy) as ImageView
            fileName = view.findViewById(R.id.textView4) as TextView
            menuButton = view.findViewById(R.id.ivMore) as ImageView
            view.setOnCreateContextMenuListener(this)

            // Define click listener for the ViewHolder's View.
            textView1 = view.findViewById(R.id.textView1)
            textView2 = view.findViewById(R.id.textView2)
            textView3 = view.findViewById(R.id.textView3)
            textView4 = view.findViewById(R.id.textView4)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                setPosition(viewHolder.position)
                setPosition(viewHolder.adapterPosition)
                return false
            }
        })

            //Log.d("aaaholder", viewHolder.adapterPosition.toString())
        //Log.d("aaaholder2", getPosition().toString())

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView1.text = dataSet[position].wid.toString()
        viewHolder.textView2.text = dataSet[position].pType.toString()
        viewHolder.textView3.text = dataSet[position].key .toString()
        viewHolder.textView4.text = dataSet[position].value.toString()
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }

    override fun getItemCount() = dataSet.size
    ///
    //
    //
    private var position: Int = 0

    fun getPosition(): Int {
        return position
    }
    fun setPosition(position: Int) {
        this.position = position
    }

}



