package com.barengific.passwordgenerator

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.barengific.passwordgenerator.databinding.ActivityMainBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.barengific.passwordgenerator.database.AppDatabase
import com.barengific.passwordgenerator.database.Word

import android.widget.Toast
import android.text.Editable

import android.text.TextWatcher
import android.view.ContextMenu.ContextMenuInfo
import android.util.Log
import android.view.*

import android.widget.TextView
import android.view.MenuInflater

import android.view.ContextMenu
import android.content.Intent

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import android.content.Context.CLIPBOARD_SERVICE

import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment

import com.barengific.passwordgenerator.setting.AppIntroduction
import com.barengific.passwordgenerator.setting.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.btnGenerate
import kotlinx.android.synthetic.main.fragment_home.btnSave
import kotlinx.android.synthetic.main.fragment_home.editTextKeyGen
import kotlinx.android.synthetic.main.fragment_home.filled_exposed_dropdown
import kotlinx.android.synthetic.main.fragment_home.tvCopy
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var arrr: List<Word> = listOf(Word(0, "", "", ""))

    init {
        instance = this
    }

    companion object {
        var pos: Int = 0
        lateinit var recyclerView: RecyclerView
        var posis: MutableList<Int> = mutableListOf(-1)
        var authStatus = false
        private var instance: MainActivity? = null
        fun getPosi(): Int = pos
        fun setPosi(pos: Int) {
            this.pos = pos
        }
        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        val sha = Sha256()

        sha.hashes("")?.let { Log.d("aaaSHA256", it) }
        sha.hashes("123")?.let { Log.d("aaaSHA256", it) }
        sha.hashes("abc")?.let { Log.d("aaaSHA256", it) }
        sha.hashes("ABC")?.let { Log.d("aaaSHA256", it) }

        hideSystemBars()

        val masterKey = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferencesEE: SharedPreferences = EncryptedSharedPreferences.create(
            this,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

        val nameS = sharedPreferencesEE.getString("signatureS", "nonon")
        val nameT = sharedPreferencesEE.getString("signatureT", "nonon")
        val nameDB = sharedPreferencesEE.getString("signatureDB", "nonon")

//      //authenticate
        val fromLogin = intent.extras?.get("fromLogin")
        val fromIntro = intent.extras?.get("fromIntro")

        Log.d("aaa_from_log", fromLogin.toString())
        if(nameS.equals("nonon") or nameT.equals("nonon")){
            val intent = Intent(applicationContext, AppIntroduction::class.java).apply {}
            startActivity(intent)
            finish()
        }
        //

        //TODO
//        if(!(authStatus or fromLogin.toString().equals("fin"))){
//            val intent = Intent(applicationContext, LoginActivity::class.java).apply {}
//            startActivity(intent)
//        }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))

        //***************************************************

        //db initialise
        val passphrase: ByteArray = SQLiteDatabase.getBytes("bob".toCharArray())
        val factory = SupportFactory(passphrase)
        val room = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-names")
            .openHelperFactory(factory)
            .allowMainThreadQueries()
            .build()
        val wordDao = room.wordDao()

        //recycle view
        val arr = wordDao.getAll()
        val adapter = CustomAdapter(arr)
        recyclerView = findViewById<View>(R.id.rview) as RecyclerView
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //length dropdown ********************
        val lines = resources.getStringArray(R.array.p_len_array).toList()
        val adapterDD = ArrayAdapter(this, R.layout.length_layout, lines)
        filled_exposed_dropdown.setAdapter(adapterDD)

        val ss = PGen()

        //Listeners
        //encrypt / decrypt to / fro database data
        btnGenerate.setOnClickListener {

//            val sharedPref = this?.getSharedPreferences(
//                getString(R.string.preference_file_key_del), Context.MODE_PRIVATE)
//
//            val sharedPref = this?.getPreferences(Context.MODE_PRIVATE)
//
//            //write save
//            val newHighScore: Int = 12345
//            val sharedPrefSave = this?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
//            with (sharedPrefSave.edit()) {
//                putInt(getString(R.string.saved_high_score_key_del), newHighScore)
//                apply()
//            }
//
//            // read retrieve
//            val sharedPrefRead = this?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
//            val defaultValue = resources.getInteger(R.integer.saved_high_score_default_key_del)
//            val highScore = sharedPrefRead.getInt(getString(R.string.saved_high_score_key_del), defaultValue)
//
//            //for settings //read write
//            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
//            //read
//            val name = sharedPreferences.getString("signature", "nonon")
//            //write
//            with (sharedPreferences.edit()){
//                putString("signature", "newsing")
//                apply()
//            }
//            val nameS = sharedPreferences.getString("signature", "nonon")
//
//            val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
//            val masterKeyAliasS: MasterKey.Builder = MasterKey.Builder(this)
//            val sharedPreferencesE: SharedPreferences = EncryptedSharedPreferences.create(
//                "secret_shared_prefs",
//                masterKeyAlias,
//                this,
//                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//            )
//
//            val masterKey = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
//                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//                .build()
//
//            val sharedPreferencesEE: SharedPreferences = EncryptedSharedPreferences.create(
//                this,
//                "secret_shared_prefs",
//                masterKey,
//                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
//
//            sharedPreferencesEE.edit().putString("signatureS", "_encrypt_-_").apply()
//            val nameE = sharedPreferencesEE.getString("signatureS", "nonon")
//            Log.d("aaa IEEE", nameE.toString())
//            Log.d("aaa ESTEEM", masterKey.toString())
//            // use the shared preferences and editor as you normally would
//
//            // use the shared preferences and editor as you normally would
//            val editor = sharedPreferences.edit()
            Log.d("aaa", nameS.toString())
            Log.d("aaa", nameT.toString())

            if (filled_exposed_dropdown.editableText.toString().toIntOrNull() == null) {
                tvGen.editText?.setText(
                    ss.pGen(
                        editTextKeyGen.editText?.text.toString(),
                        nameS.toString(),
                        nameT.toString()[0].toString(),
                        nameT.toString()[1].toString(),
                        nameT.toString()[2].toString(),
                        nameT.toString()[3].toString(),
                        10
                    )
                )
            } else {
                tvGen.editText?.setText(
                    ss.pGen(
                        editTextKeyGen.editText?.text.toString(),
                        nameS.toString(),
                        nameT.toString()[0].toString(),
                        nameT.toString()[1].toString(),
                        nameT.toString()[2].toString(),
                        nameT.toString()[3].toString(),
                        filled_exposed_dropdown.editableText.toString().toInt()
                    )
                )
            }
        }


        tvCopy.setOnClickListener {
            // Creates a new text clip to put on the clipboard
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("PGen", tvGen.editText?.text.toString())
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip)

            Toast.makeText(applicationContext, "Text Copied", Toast.LENGTH_LONG).show()
        }

        tvGen.setStartIconOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("PGen", tvGen.editText?.text.toString())
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip)

            Toast.makeText(applicationContext, "Text Copied", Toast.LENGTH_LONG).show()
            Log.d("aaa", "copying123")
        }

        btnSave.setOnClickListener {
            val aa = Word(
                0,
                "pgen",
                editTextKeyGen.editText?.text.toString(),
                tvGen.editText?.text.toString()
            )
            wordDao.insertAll(aa)

            arrr = wordDao.getAll()
            val adapter = CustomAdapter(arrr)
            recyclerView.setHasFixedSize(false)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)

            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
            //TODO check for duplicates, i.e. compare key and length if already exists when don't add to db
        }

        filled_exposed_dropdown.setOnItemClickListener { parent, _, position, _ ->
            Int
            val selection = parent.getItemAtPosition(position) as String
            Log.d("aaa_new_MAtrial_Spinner", selection)
            tvGen.editText?.setText(
                ss.pGen(
                    editTextKeyGen.editText?.text.toString(),
                    nameS.toString(),
                    nameT.toString()[0].toString(),
                    nameT.toString()[1].toString(),
                    nameT.toString()[2].toString(),
                    nameT.toString()[3].toString(),
                    selection.toInt()
                )
            )

        }

        editTextKeyGen.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (filled_exposed_dropdown.editableText.toString().toIntOrNull() == null) {
                    tvGen.editText?.setText(
                        ss.pGen(
                            editTextKeyGen.editText?.text.toString(),
                            nameS.toString(),
                            nameT.toString()[0].toString(),
                            nameT.toString()[1].toString(),
                            nameT.toString()[2].toString(),
                            nameT.toString()[3].toString(),
                            10
                        )
                    )
                } else {
                    tvGen.editText?.setText(
                        ss.pGen(
                            editTextKeyGen.editText?.text.toString(),
                            nameS.toString(),
                            nameT.toString()[0].toString(),
                            nameT.toString()[1].toString(),
                            nameT.toString()[2].toString(),
                            nameT.toString()[3].toString(),
                            filled_exposed_dropdown.editableText.toString().toInt()
                        )
                    )
                }
                //tvGen.editText?.setText(ss.pgen(editTextKeyGen.editText?.text.toString(),"jimbob","4","5","6","7",filled_exposed_dropdown.editableText.toString().toInt()))
            }
        })
        var btnHideAllStatus = false
        btnHideAll.setOnClickListener {
            if(btnHideAllStatus){
                btnHideAll.text = resources.getString(R.string.hide) //"Hide"
                btnHideAll.setIconResource(R.drawable.ic_baseline_visibility_off_24)
                btnHideAllStatus = false

                arrr = wordDao.getAll()
                val adapter = CustomAdapter(arrr)
                recyclerView.setHasFixedSize(false)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

            }else{
                btnHideAll.text = resources.getString(R.string.show) //"Show"
                btnHideAll.setIconResource(R.drawable.ic_baseline_visibility_24)
                btnHideAllStatus = true

                arrr = wordDao.getAll()
                val arSize = arrr.size

                for (i in 0 until arSize) {
                    arrr[i].value = "****"
                    arrr[i].key = "****"
                }// check - database\Word changed from val to var ^

                val adapter = CustomAdapter(arrr)
                recyclerView.setHasFixedSize(false)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

            }//

        }

        registerForContextMenu(recyclerView)

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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.menu.main -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            R.id.action_settings -> openSettings()
            R.id.action_about -> openAbout()
            R.id.action_exit -> finish()
//            R.id.action_adfree -> openAdFree()
            R.id.action_donation -> openDonate()
            //TODO donation and ad free version
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openSettings() {
        Log.d("aaa", "in open settings")
        val intent = Intent(this, SettingsActivity::class.java).apply {
//            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
        finish()
    }

    private fun openAbout() {
        Log.d("aaa", "in open donation")
        val newFragment = FireMissilesDialogFragment()
        newFragment.show(supportFragmentManager, "missiles")
    }

    //TODO donation option
    private fun openDonate() {
        Log.d("aaa", "in open donate")
        val intent = Intent(this, Donate::class.java).apply {
//            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
        finish()

    }

    //TODO ad free version option
//    fun openAdFree() {
//        Log.d("aaaOptions", "coming soon")
//    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }

    //
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val inflater: MenuInflater = this.menuInflater //getActivity().getMenuInflater()
        inflater.inflate(R.menu.rv_menu_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val value: TextView? =
            recyclerView.findViewHolderForAdapterPosition(getPosi())?.itemView?.findViewById(
                R.id.textView4
            )

        when (item.itemId) {
            R.id.menu_copy -> {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("PGen", value?.text.toString())
                clipboard.setPrimaryClip(clip)
                Toast.makeText(applicationContext, "Text Copied", Toast.LENGTH_LONG).show()
            }
            R.id.menu_delete -> {
                val passphrase: ByteArray = SQLiteDatabase.getBytes("bob".toCharArray())
                val factory = SupportFactory(passphrase)
                val room = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-names")
                    .openHelperFactory(factory)
                    .allowMainThreadQueries()
                    .build()
                val wordDao = room.wordDao()

                val wid: TextView? =
                    recyclerView.findViewHolderForAdapterPosition(getPosi())?.itemView?.findViewById(
                        R.id.textView1
                    )
                val pType: TextView? =
                    recyclerView.findViewHolderForAdapterPosition(getPosi())?.itemView?.findViewById(
                        R.id.textView2
                    )
                val key: TextView? =
                    recyclerView.findViewHolderForAdapterPosition(getPosi())?.itemView?.findViewById(
                        R.id.textView3
                    )

                val a = Word(
                    wid?.text.toString().toInt(),
                    pType?.text.toString(),
                    key?.text.toString(),
                    value?.text.toString()
                )
                room.wordDao().delete(a)
                arrr = wordDao.getAll()
                val adapter = CustomAdapter(arrr)
                recyclerView.setHasFixedSize(false)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)
                room.close()

            }
            R.id.menu_hide -> {
                Log.d("aaa menu_hide", getPosi().toString())
                val passphrase: ByteArray = SQLiteDatabase.getBytes("bob".toCharArray())
                val factory = SupportFactory(passphrase)
                val room = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-names")
                    .openHelperFactory(factory)
                    .allowMainThreadQueries()
                    .build()
                val wordDao = room.wordDao()

                arrr = wordDao.getAll()

                if(posis.contains(getPosi())){//if existent then show
                    posis.remove(getPosi())

                    val pSize = posis.size
                    for (i in 0 until pSize) {
                        Log.d("aaaaCVCVCV", posis[i].toString())
                        if((posis[i] != -1)){
                            val qSize = posis[i]
                            arrr[qSize].value = "****"
                            arrr[qSize].key = "****"
                        }
                    }
                    val adapter = CustomAdapter(arrr)
                    recyclerView.setHasFixedSize(false)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    room.close()

                }else{//if not existent then hide
                    posis.add(getPosi())

                    val pSize = posis.size
                    for (i in 0 until pSize) {
                        Log.d("aaaaCVCVCV", posis[i].toString())
                        if((posis[i] != -1)){
                            val qSize = posis[i]
                            arrr[qSize].value = "****"
                            arrr[qSize].key = "****"
                        }
                    }
                    val adapter = CustomAdapter(arrr)
                    recyclerView.setHasFixedSize(false)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    room.close()

                }

            }
            R.id.menu_cancel -> {
                Log.d("aaa menu_cancel", getPosi().toString())
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        super.onResume()
        //Toast.makeText(applicationContext, "onPause called", Toast.LENGTH_SHORT).show()
        authStatus = false
    }

    override fun onStop() {
        super.onStop()
        authStatus = false
        //Toast.makeText(applicationContext, authStatus.toString()+"STOP called", Toast.LENGTH_SHORT).show()
    }

}

class CustomAdapter(private val dataSet: List<Word>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnCreateContextMenuListener {
        var ivCopy: ImageView
        var ivMore: ImageView

        @SuppressLint("ResourceType")
        override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenuInfo?) {
            MainActivity.pos = adapterPosition
            MainActivity.setPosi(layoutPosition)
        }

        val textView1: TextView
        val textView2: TextView
        val textView3: TextView
        val textView4: TextView

        init {
            ivCopy = view.findViewById(R.id.ivCopy) as ImageView
            ivMore = view.findViewById(R.id.ivMore) as ImageView
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

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        viewHolder.itemView.setOnLongClickListener {
            setPosition(viewHolder.layoutPosition)
            setPosition(viewHolder.adapterPosition)
            false
        }

        viewHolder.ivMore.setOnClickListener(object : View.OnClickListener {

            override fun onClick(view: View?) {
                val wrapper: Context = ContextThemeWrapper(view?.context, R.style.PopupMenu)

                val popup = PopupMenu(wrapper, viewHolder.ivMore)
                //inflating menu from xml resource
                popup.inflate(R.menu.rv_menu_context)
                //adding click listener
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_copy -> {
                            Log.d("aaa menu", "copy")
                            val clipboard =
                                view?.context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            val clip: ClipData =
                                ClipData.newPlainText("PGen", viewHolder.textView4.text.toString())
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(view.context, "Text Copied", Toast.LENGTH_LONG).show()

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

                            val wid: TextView = viewHolder.textView1
                            val pType: TextView = viewHolder.textView2
                            val key: TextView = viewHolder.textView3
                            val value: TextView = viewHolder.textView4

                            val a = Word(
                                wid.text.toString().toInt(),
                                pType.text.toString(),
                                key.text.toString(),
                                value.text.toString()
                            )
                            room?.wordDao()?.delete(a)
                            val arrr = wordDao?.getAll()
                            val adapter = arrr?.let { CustomAdapter(it) }

                            MainActivity.recyclerView.setHasFixedSize(false)
                            MainActivity.recyclerView.adapter = adapter
                            MainActivity.recyclerView.layoutManager =
                                LinearLayoutManager(view?.context)
                            room?.close()
                            Log.d("aaa menu", "DDDelete")

                        }

                        R.id.menu_cancel -> {
                            Log.d("aaa menu", "cancel") //TODO
                        }
                        R.id.menu_hide -> {
                            val passphrase: ByteArray =
                                SQLiteDatabase.getBytes("bob".toCharArray())//DB passphrase change
                            val factory = SupportFactory(passphrase)
                            val room = view?.context?.let {
                                Room.databaseBuilder(it, AppDatabase::class.java, "database-names")
                                    .openHelperFactory(factory)
                                    .allowMainThreadQueries()
                                    .build()
                            }
                            val wordDao = room?.wordDao()

                            val arrr = wordDao?.getAll()

                            if (MainActivity.posis.contains(viewHolder.adapterPosition)) {//if existent then show
                                MainActivity.posis.remove(viewHolder.adapterPosition)

                                val pSize = MainActivity.posis.size
                                for (i in 0 until pSize) {
                                    Log.d("aaaaCVCVCVQQ", MainActivity.posis[i].toString())
                                    if ((MainActivity.posis[i] != -1)) {
                                        val qSize = MainActivity.posis[i]
                                        arrr?.get(qSize)?.value = "****"
                                        arrr?.get(qSize)?.key = "****"
                                    }
                                }

                                val adapter = arrr?.let { CustomAdapter(it) }
                                MainActivity.recyclerView.setHasFixedSize(false)
                                MainActivity.recyclerView.adapter = adapter
                                MainActivity.recyclerView.layoutManager =
                                    LinearLayoutManager(view?.context)
                                room?.close()

                            } else {//if not existent then hide
                                MainActivity.posis.add(viewHolder.adapterPosition)

                                val pSize = MainActivity.posis.size
                                for (i in 0 until pSize) {
                                    Log.d("aaaaCVCVCV", MainActivity.posis[i].toString())
                                    if ((MainActivity.posis[i] != -1)) {
                                        val qSize = MainActivity.posis[i]
                                        arrr?.get(qSize)?.value = "****"
                                        arrr?.get(qSize)?.key = "****"
                                    }
                                }
                                val adapter = arrr?.let { CustomAdapter(it) }
                                MainActivity.recyclerView.setHasFixedSize(false)
                                MainActivity.recyclerView.adapter = adapter
                                MainActivity.recyclerView.layoutManager =
                                    LinearLayoutManager(view?.context)
                                room?.close()

                            }

                        }

                    }
                    true
                }
                //displaying the popup
                popup.show()
            }
        })

        viewHolder.ivCopy.setOnClickListener { view ->
            Log.d("aaaaICONu", "inn copy")
            val clipboard = view?.context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("PGen", viewHolder.textView4.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(view.context, "Text Copied", Toast.LENGTH_LONG).show()
        }

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView1.text = dataSet[position].wid.toString()
        viewHolder.textView2.text = dataSet[position].pType.toString()
        viewHolder.textView3.text = dataSet[position].key.toString()
        viewHolder.textView4.text = dataSet[position].value.toString()
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }

    override fun getItemCount() = dataSet.size

    private var position: Int = 0

//    fun getPosition(): Int {
//        return position
//    }

    private fun setPosition(position: Int) {
        this.position = position
    }

}

class FireMissilesDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            builder.setView(inflater.inflate(R.layout.alertdialog_about, null))
                .setNegativeButton(R.string.cancel
                ) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}