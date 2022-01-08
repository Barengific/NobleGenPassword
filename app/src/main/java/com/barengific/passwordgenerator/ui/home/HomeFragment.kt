package com.barengific.passwordgenerator.ui.home

import android.os.Bundle

import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.barengific.passwordgenerator.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
//
//    private var here = 0;
//
//    companion object {
//        var heress: Int = 0
//        fun getHeres(): Int = heress
//        fun setHeres(heress: Int) {
//            this.heress = heress
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root



//        if (getHeres() == 0) {
//            setHeres(1)
//            Log.d("aaa", "inn hommmee")
//            val intent = Intent(this.context, MainActivity::class.java).apply {
////            putExtra(EXTRA_MESSAGE, message)
//            }
//            startActivity(intent)
//        } else if (heress == 1) {
//            Log.d("aaa", "nottt inn hommmee")
////            val intent = Intent(this.context, MainActivity::class.java).apply {
//////            putExtra(EXTRA_MESSAGE, message)
////            }
////            startActivity(intent)
//            //setHeres(0)
//
//        }
//
//        MainActivity.authStatus = true
        return root
    }

//    @SuppressLint("MissingSuperCall")
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
////        super.onActivityCreated(savedInstanceState)
//        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
//        //
//    }

}

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
//        //
//
//        //length dropdown
//        val Lines = resources.getStringArray(R.array.p_len_array).toList()
////        val adapterr = getActivity()?.let { ArrayAdapter(it, R.layout.length_layout, Lines) }
//        val adapterr = this.context?.let { ArrayAdapter(it, R.layout.length_layout, Lines) }
//        filled_exposed_dropdown.setAdapter(adapterr)
//
////        ***************************************************
//
//        //db initialise
//        val db = this.context?.let {
//            Room.databaseBuilder(
//                it,
//                AppDatabase::class.java, "database-name"
//            ).allowMainThreadQueries().build()
//        }
//        val wordDao = db?.wordDao()
//        //
//        //recycle view
//        val arr = wordDao.getAll()
//        var adapter = CustomAdapter(arr)
//        recyclerView = binding.rview // findViewById<View>(R.id.rview) as RecyclerView
//        recyclerView.setHasFixedSize(false)
//        recyclerView.setAdapter(adapter)
//        recyclerView.setLayoutManager(LinearLayoutManager(this.context))
//
////        adapter = CustomAdapter(arr)
////        recyclerView.setAdapter(adapter)
////        adapter.notifyDataSetChanged()
////        adapter.notifyDataSetChanged()
////        recyclerView.adapter?.notifyDataSetChanged()
////        adapter.notifyItemRangeChanged(0,5)
//
//        //length dropdown ********************8
/////////        val Lines = resources.getStringArray(R.array.p_len_array).toList()
////////        val adapterr = ArrayAdapter(this, R.layout.length_layout, Lines)
////////        filled_exposed_dropdown.setAdapter(adapterr)
//        //filled_exposed_dropdown.setText("10",false)
//
////        (TextField.editableText as? AutoCompleteTextView)?.setAdapter(adapterr)
//////        val spinnerr: Spinner = findViewById(R.id.filled_exposed_dropdown)
////        // Create an ArrayAdapter using the string array and a default spinner layout
////        ArrayAdapter.createFromResource(this,R.array.p_len_array,
////            android.R.layout.simple_dropdown_item_1line
////        ).also { adapter ->
////            // Specify the layout to use when the list of choices appears
////            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
////            // Apply the adapter to the spinner
////            //spinnerr.adapter = adapter
////
////        }
//
//        val ss = Pgen()
//
//
//        //Listeners
//        btnGenerate.setOnClickListener {
//            val sharedPref = this?.getSharedPreferences(
//                getString(R.string.preference_file_key_del), Context.MODE_PRIVATE)
//
//            val sharedPreff = this?.getPreferences(Context.MODE_PRIVATE)
//
//            //write save
//            val newHighScore: Int = 12345
//            val sharedPrefSave = this?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
//            with (sharedPrefSave.edit()) {
//                putInt(getString(R.string.saved_high_score_key_del), newHighScore)
//                apply()
//            }
//
//
//            // read retreive
//            val sharedPrefRead = this?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
//            val defaultValue = resources.getInteger(R.integer.saved_high_score_default_key_del)
//            val highScore = sharedPrefRead.getInt(getString(R.string.saved_high_score_key_del), defaultValue)
//
//            //for settings //redwrite
//            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
//            //read
//            val name = sharedPreferences.getString("signature", "nonon")
//            //write
//            with (sharedPreferences.edit()){
//                putString("signature", "newwwsigg")
//                apply()
//            }
//            val nameS = sharedPreferences.getString("signature", "nonon")
//
////            Log.d("aaaaaaSgared1", highScore.toString() )
////            Log.d("aaaaaaSgared2", name.toString() )
////            Log.d("aaaaaaSgared3", nameS.toString() )
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
//            sharedPreferencesEE.edit().putString("signatureS", "encrrrr").apply()
//            val nameE = sharedPreferencesEE.getString("signatureS", "nonon")
//            Log.d("aaaaaEEEEEEE", nameE.toString())
//            Log.d("aaaaaEEEEEEEMM", masterKey.toString())
//            // use the shared preferences and editor as you normally would
//
//            // use the shared preferences and editor as you normally would
//            val editor = sharedPreferences.edit()
//
//
//
//
//            if (filled_exposed_dropdown.editableText.toString().toIntOrNull() == null) {
//                tvGen.editText?.setText(
//                    ss.pgen(
//                        editTextKeyGen.editText?.text.toString(),
//                        "jimbob",
//                        "4",
//                        "5",
//                        "6",
//                        "7",
//                        10
//                    )
//                )
//            } else {
//                tvGen.editText?.setText(
//                    ss.pgen(
//                        editTextKeyGen.editText?.text.toString(),
//                        "jimbob",
//                        "4",
//                        "5",
//                        "6",
//                        "7",
//                        filled_exposed_dropdown.editableText.toString().toInt()
//                    )
//                )
//            }
//
//            //encrypt/decrypt
////            val acvb = Acvb
////
////            val enout =
////                acvb.encrypt_AES("aaaaaaaaaaaaaaaa", "hello this is a mesage", "qqqqqqqqqqqqqqqq")
////            Log.d("aaaQQQ_EN", enout!!)
////
////            val deout = acvb.decrypt("aaaaaaaaaaaaaaaa", enout, "qqqqqqqqqqqqqqqq")
////            Log.d("aaaQQQ_DEC", deout!!)
////
////            val sc = generateKey("aaaaaaaaaaaaaaaa")
////            val en = encryptMsg("hell this is a msg", sc)
////            Log.d("aaaWWW_EN", en!!)
////
////            val de = decryptMsg(en, sc)
////            Log.d("aaaWWW_DE", de!!)
//
//        }
//
//
//        tvCopy.setOnClickListener {
//            // Creates a new text clip to put on the clipboard
//            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            val clip: ClipData = ClipData.newPlainText("PGen", tvGen.editText?.text.toString())
//            // Set the clipboard's primary clip.
//            clipboard.setPrimaryClip(clip)
//
//            Toast.makeText(applicationContext, "Text Copied", Toast.LENGTH_LONG).show()
////
//            //tvGen.setText(getPosi().toString())
////            tvGen.setText(wordDao.getAll().get(1).wid.toString() + "_" + wordDao.getAll().get(1).pType
////                    + "_" + wordDao.getAll().get(1).key + "_" + wordDao.getAll().get(1).value)
////
////            val text1: TextView? =
////                recyclerView.findViewHolderForAdapterPosition(getPosi())?.itemView?.findViewById(
////                    R.id.textView4)
////
////            Log.d("aaacc", text1?.text.toString())
////            Log.d("aaa", editTextKeyGen.editText?.text.toString())
//        }
//
//        tvGen.setStartIconOnClickListener {
//            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            val clip: ClipData = ClipData.newPlainText("PGen", tvGen.editText?.text.toString())
//            // Set the clipboard's primary clip.
//            clipboard.setPrimaryClip(clip)
//
//            Toast.makeText(applicationContext, "Text Copied", Toast.LENGTH_LONG).show()
//            Log.d("aaaaaaaaa", "copyyyingggg123")
//        }
//
//        btnSave.setOnClickListener {
//            val aa = Word(
//                0,
//                "pgen",
//                editTextKeyGen.editText?.text.toString(),
//                tvGen.editText?.text.toString()
//            )
//            wordDao.insertAll(aa)
//
//            arrr = wordDao.getAll()
//
//            var adapter = CustomAdapter(arrr)
//            //recyclerView = findViewById<View>(R.id.rview) as RecyclerView
//            recyclerView.setHasFixedSize(false)
//            recyclerView.setAdapter(adapter)
//            recyclerView.setLayoutManager(LinearLayoutManager(this))
//

//        }
//
//        filled_exposed_dropdown.setOnItemClickListener(OnItemClickListener { parent, view, position, rowId ->
//            Int
//            val selection = parent.getItemAtPosition(position) as String
//            Log.d("aaanewMAterialSpinner", selection)
//            tvGen.editText?.setText(
//                ss.pgen(
//                    editTextKeyGen.editText?.text.toString(),
//                    "jimbob",
//                    "4",
//                    "5",
//                    "6",
//                    "7",
//                    selection.toInt()
//                )
//            )
//
//        })
//
////
////        autoCompleteTextView.setOnItemClickListener(OnItemClickListener { parent, view, position, rowId ->
////            val selection = parent.getItemAtPosition(position) as String
////            //
////        })
//
//
//        editTextKeyGen.editText?.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {}
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                if (filled_exposed_dropdown.editableText.toString().toIntOrNull() == null) {
//                    tvGen.editText?.setText(
//                        ss.pgen(
//                            editTextKeyGen.editText?.text.toString(),
//                            "jimbob",
//                            "4",
//                            "5",
//                            "6",
//                            "7",
//                            10
//                        )
//                    )
//                } else {
//                    tvGen.editText?.setText(
//                        ss.pgen(
//                            editTextKeyGen.editText?.text.toString(),
//                            "jimbob",
//                            "4",
//                            "5",
//                            "6",
//                            "7",
//                            filled_exposed_dropdown.editableText.toString().toInt()
//                        )
//                    )
//                }
//                //tvGen.editText?.setText(ss.pgen(editTextKeyGen.editText?.text.toString(),"jimbob","4","5","6","7",filled_exposed_dropdown.editableText.toString().toInt()))
//            }
//        })
//
//        registerForContextMenu(recyclerView);
//        val ustomAdapter = CustomAdapter(arr)
//
//        //val selectedPostion = (ustomAdapter as AdapterContextMenuInfo).position
//
//
//
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//        MainActivity.authStatus = true
//    }
//}
//
//class CustomAdapter(private val dataSet: List<Word>) :
//    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
//
//    companion object {
//        var position: Int = 0
//    }
//
//
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
//        View.OnCreateContextMenuListener {
//        var ivCopy: ImageView
//        var fileName: TextView
//        var ivMore: ImageView
//
//        @SuppressLint("ResourceType")
//        override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
//            MainActivity.pos = getPosition()
//            MainActivity.setPosi(getPosition())
//
//        }
//
//        val textView1: TextView
//        val textView2: TextView
//        val textView3: TextView
//        val textView4: TextView
//
//        init {
//            ivCopy = view.findViewById(R.id.ivCopy) as ImageView
//            fileName = view.findViewById(R.id.textView4) as TextView
//            ivMore = view.findViewById(R.id.ivMore) as ImageView
//            view.setOnCreateContextMenuListener(this)
//
//            // Define click listener for the ViewHolder's View.
//            textView1 = view.findViewById(R.id.textView1)
//            textView2 = view.findViewById(R.id.textView2)
//            textView3 = view.findViewById(R.id.textView3)
//            textView4 = view.findViewById(R.id.textView4)
//        }
//
//    }
//
//    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
//        // Create a new view, which defines the UI of the list item
//        val view = LayoutInflater.from(viewGroup.context)
//            .inflate(R.layout.text_row_item, viewGroup, false)
//
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//        viewHolder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
//            override fun onLongClick(v: View?): Boolean {
//                setPosition(viewHolder.position)
//                setPosition(viewHolder.adapterPosition)
//                return false
//            }
//        })
//
//
//        viewHolder.ivMore.setOnClickListener(object : View.OnClickListener {
//
//            override fun onClick(view: View?) {
//                //creating a popup menu
//                val popup = PopupMenu(view?.context, viewHolder.ivMore)
//                //inflating menu from xml resource
//                popup.inflate(R.menu.rv_menu_context)
//                //adding click listener
//                popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
//                    override fun onMenuItemClick(item: MenuItem): Boolean {
//                        when (item.itemId) {
//                            R.id.menu_copy -> {
//                                Log.d("aaaamenuu","copy")
//                                val clipboard = view?.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                                val clip: ClipData = ClipData.newPlainText("PGen", viewHolder.textView4.text.toString())
//                                clipboard.setPrimaryClip(clip)
//                                Toast.makeText(view?.context, "Text Copied", Toast.LENGTH_LONG).show()
//
//                            }
//                            R.id.menu_delete -> {
//                                Log.d("aaaamenuu","delete")
//                                val db = view?.context?.let {
//                                    Room.databaseBuilder(
//                                        it,
//                                        AppDatabase::class.java, "database-name"
//                                    ).allowMainThreadQueries().build()
//                                }
//                                val wordDao = db?.wordDao()
//
//                                val wid: TextView? = viewHolder.textView1
//                                val pType: TextView? = viewHolder.textView2
//                                val key: TextView? = viewHolder.textView3
//                                val value: TextView? = viewHolder.textView4
//
//                                var a = Word(
//                                    wid?.text.toString().toInt(),
//                                    pType?.text.toString(),
//                                    key?.text.toString(),
//                                    value?.text.toString()
//                                )
//                                db?.wordDao()?.delete(a)
//                                val arrr = wordDao?.getAll()
//                                var adapter = arrr?.let { CustomAdapter(it) }
//
//                                MainActivity.recyclerView.setHasFixedSize(false)
//                                MainActivity.recyclerView.setAdapter(adapter)
//                                MainActivity.recyclerView.setLayoutManager(LinearLayoutManager(view?.context))
//                                db?.close()
//                                Log.d("aaaamenuu","DDDdelete")
//
//
//
//                            }
//
//                            R.id.menu_cancel ->  {
//                                Log.d("aaaamenuu","canceeel") //
//                            }
//                            R.id.menu_hide ->  {
//                                Log.d("aaaamenuu","canceeel") //
//                            }
//
//                        }
//                        return true
//                    }
//                })
//                //displaying the popup
//                popup.show()
//            }
//        })
//
//        viewHolder.ivCopy.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(view: View?) {
//                Log.d("aaaaICONu","innn copyy")
//                val clipboard = view?.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                val clip: ClipData = ClipData.newPlainText("PGen", viewHolder.textView4.text.toString())
//                clipboard.setPrimaryClip(clip)
//                Toast.makeText(view?.context, "Text Copied", Toast.LENGTH_LONG).show()
//
//            }
//        })
//
//        viewHolder.textView1.text = dataSet[position].wid.toString()
//        viewHolder.textView2.text = dataSet[position].pType.toString()
//        viewHolder.textView3.text = dataSet[position].key.toString()
//        viewHolder.textView4.text = dataSet[position].value.toString()
//    }
//
//    override fun onViewRecycled(holder: ViewHolder) {
//        holder.itemView.setOnLongClickListener(null)
//        super.onViewRecycled(holder)
//    }
//
//    override fun getItemCount() = dataSet.size
//
//    //
//    //
//    private var position: Int = 0
//
//    fun getPosition(): Int {
//        return position
//    }
//
//    fun setPosition(position: Int) {
//        this.position = position
//    }
//
//}

