package com.barengific.passwordgenerator

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
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
//import com.barengific.passwordgenerator.databinding.FragmentHomeBinding
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
import java.io.UnsupportedEncodingException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.InvalidParameterSpecException
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec
import android.content.Intent
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.barengific.passwordgenerator.CustomAdapter.Companion.position

import com.barengific.passwordgenerator.ui.login.LoginActivity
import java.util.concurrent.Executor

import android.content.SharedPreferences
import androidx.core.content.ContextCompat.getSystemService
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import android.content.Context.CLIPBOARD_SERVICE
import android.text.method.PasswordTransformationMethod

import android.view.MotionEvent
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.text_row_item.view.*
import androidx.lifecycle.Lifecycle

import androidx.lifecycle.*
import com.barengific.passwordgenerator.crypt.Aqtik
import com.barengific.passwordgenerator.database.WordDao
import net.sqlcipher.database.SQLiteDatabase
import kotlin.concurrent.thread
import net.sqlcipher.database.SupportFactory




class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    var arrr: List<Word> = listOf(Word(0, "", "", ""))

    init {
        instance = this
    }

    companion object {
        var pos: Int = 0
        lateinit var recyclerView: RecyclerView
        lateinit var arrrNew: List<Word>
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

    //TODO
    //lateinit var recyclerView: RecyclerView
//    lateinit var db = Room.databaseBuilder(applicationContext,
//        AppDatabase::class.java, "database-name"
//    ).allowMainThreadQueries().build()

    //private lateinit var biometricPrompt: BiometricPrompt
    private val cryptographyManager = CryptographyManager()
    private val ciphertextWrapper
        get() = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
            applicationContext,
            SHARED_PREFS_FILENAME,
            Context.MODE_PRIVATE,
            CIPHERTEXT_WRAPPER
        )

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

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

//        //authenticate
        val fromLogin = getIntent().extras?.get("fromLogin")
        val fromIntro = getIntent().extras?.get("fromIntro")

        Log.d("aaaaaafromlog", fromLogin.toString())
        if(nameS.equals("nonon") or nameT.equals("nonon")){
            val intent = Intent(applicationContext, AppIntroduction::class.java).apply {}
            startActivity(intent)
        }
        //

        if(!(authStatus or fromLogin.toString().equals("fin"))){
            val intent = Intent(applicationContext, LoginActivity::class.java).apply {}
            startActivity(intent)
        }

        var secretKey: SecretKey?
        if(nameDB.equals("nonon")){
            secretKey = generateKey(nameS+nameT)
        }


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

//        //unsecure connection
//        val db = Room.databaseBuilder(
//            applicationContext,
//            AppDatabase::class.java, "database-name"
//        ).allowMainThreadQueries().build()
//        //val wordDao = db.wordDao()
//        val wordDaos = db.wordDao()
//        val arrs = wordDaos.getAll()


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

        //length dropdown ********************8 TODO
        val Lines = resources.getStringArray(R.array.p_len_array).toList()
        val adapterr = ArrayAdapter(this, R.layout.length_layout, Lines)
        filled_exposed_dropdown.setAdapter(adapterr)

        val ss = Pgen()

        //Listeners
        //TODO encrypt / decrypt to / fro database data
        btnGenerate.setOnClickListener {

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
            Log.d("paaaaaaaa", nameS.toString())
            Log.d("paaaaaaaa", nameT.toString())

            if (filled_exposed_dropdown.editableText.toString().toIntOrNull() == null) {
                tvGen.editText?.setText(
                    ss.pgen(
                        editTextKeyGen.editText?.text.toString(),
                        nameS.toString(),
                        nameT.toString().get(0).toString(),
                        nameT.toString().get(1).toString(),
                        nameT.toString().get(2).toString(),
                        nameT.toString().get(3).toString(),
                        10
                    )
                )
            } else {
                tvGen.editText?.setText(
                    ss.pgen(
                        editTextKeyGen.editText?.text.toString(),
                        nameS.toString(),
                        nameT.toString().get(0).toString(),
                        nameT.toString().get(1).toString(),
                        nameT.toString().get(2).toString(),
                        nameT.toString().get(3).toString(),
                        filled_exposed_dropdown.editableText.toString().toInt()
                    )
                )
            }

            //encrypt/decrypt
//            val acvb = Acvb
//
//            val enout =
//                acvb.encrypt_AES("aaaaaaaaaaaaaaaa", "hello this is a mesage", "qqqqqqqqqqqqqqqq")
//            Log.d("aaaQQQ_EN", enout!!)
//
//            val deout = acvb.decrypt("aaaaaaaaaaaaaaaa", enout, "qqqqqqqqqqqqqqqq")
//            Log.d("aaaQQQ_DEC", deout!!)
//
//            val sc = generateKey("aaaaaaaaaaaaaaaa")
//            val en = encryptMsg("hell this is a msg", sc)
//            Log.d("aaaWWW_EN", en!!)
//
//            val de = decryptMsg(en, sc)
//            Log.d("aaaWWW_DE", de!!)

        }


        tvCopy.setOnClickListener {
            // Creates a new text clip to put on the clipboard
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("PGen", tvGen.editText?.text.toString())
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip)

            Toast.makeText(applicationContext, "Text Copied", Toast.LENGTH_LONG).show()
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

        tvGen.setStartIconOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("PGen", tvGen.editText?.text.toString())
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip)

            Toast.makeText(applicationContext, "Text Copied", Toast.LENGTH_LONG).show()
            Log.d("aaaaaaaaa", "copyyyingggg123")
        }

        btnSave.setOnClickListener {

//            val aa = Word(
//                0,
//                "pgen",
//                Aqtik.encrypt(editTextKeyGen.editText?.text.toString()),
//                Aqtik.encrypt(tvGen.editText?.text.toString())
//            )
            val aa = Word(
                0,
                "pgen",
                editTextKeyGen.editText?.text.toString(),
                tvGen.editText?.text.toString()
            )
            wordDao.insertAll(aa)

            arrr = wordDao.getAll()
            var adapter = CustomAdapter(arrr)
            //recyclerView = findViewById<View>(R.id.rview) as RecyclerView
            recyclerView.setHasFixedSize(false)
            recyclerView.setAdapter(adapter)
            recyclerView.setLayoutManager(LinearLayoutManager(this))


            //var arrrNew: MutableList<Word> = listOf(Word(0, "pa", "pa", "pa"), Word(0, "pb", "pb", "pb")) as MutableList<Word>
           // arrrNew.add()

            var aNew: MutableList<Word> = mutableListOf(Word(arrr[0].wid, arrr[0].pType, Aqtik.decrypt(arrr[0].key), Aqtik.decrypt(arrr[0].value)))
            //arrr += a
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }

//            for (item in arrr.indices) {
//                val aS = Aqtik.decrypt(arrr[item].key)
//                val aT = Aqtik.decrypt(arrr[item].value)
//                val aWW = Word(arrr[item].wid, arrr[item].pType, aS, aT)
//                //aNew.add(aWW)
//                arrrNew += aWW
//            }

//            Thread(Runnable {
//                Thread.sleep(1000)
//                println("test")
//                for (item in arrr.indices) {
//                    val aS = Aqtik.decrypt(arrr[item].key)
//                    val aT = Aqtik.decrypt(arrr[item].value)
//                    val aWW = Word(arrr[item].wid, arrr[item].pType, aS, aT)
//                    aNew.add(aWW)
//
//                }
//                var adapter = CustomAdapter(aNew)
//                //recyclerView = findViewById<View>(R.id.rview) as RecyclerView
//                recyclerView.setHasFixedSize(false)
//                recyclerView.setAdapter(adapter)
//                recyclerView.setLayoutManager(LinearLayoutManager(this))
//            })

//            thread { Thread.sleep(1000)
//                for (item in arrr.indices) {
//                    val aS = Aqtik.decrypt(arrr[item].key)
//                    val aT = Aqtik.decrypt(arrr[item].value)
//                    val aWW = Word(arrr[item].wid, arrr[item].pType, aS, aT)
//                    aNew.add(aWW)
//
//                }
//                var adapter = CustomAdapter(aNew)
//                //recyclerView = findViewById<View>(R.id.rview) as RecyclerView
//                recyclerView.setHasFixedSize(false)
//                recyclerView.setAdapter(adapter)
//                recyclerView.setLayoutManager(LinearLayoutManager(this))
//
//            }

//            runOnUiThread {
//                // Stuff that updates the UI
//                for (item in arrr.indices) {
//                    val aS = Aqtik.decrypt(arrr[item].key)
//                    val aT = Aqtik.decrypt(arrr[item].value)
//                    val aWW = Word(arrr[item].wid, arrr[item].pType, aS, aT)
//                    aNew.add(aWW)
//
//                }
//                recyclerView.adapter?.notifyItemInserted(0)
//                adapter.notifyItemInserted(0)
//
//                var adapter = CustomAdapter(aNew)
//                //recyclerView = findViewById<View>(R.id.rview) as RecyclerView
//                recyclerView.setHasFixedSize(false)
//                recyclerView.setAdapter(adapter)
//                recyclerView.setLayoutManager(LinearLayoutManager(this))
//                //recyclerView.adapter
//
//                adapter.notifyItemRangeInserted(0, 1);
//
//            }

//            Log.d("aaaaDBDD", arrr.toString())
//            Log.d("aaaaDBDD", arrrNew.toString())
//            Log.d("aaaaDBDDINCDI", arrr.indices.toString())

//            var adapter = CustomAdapter(aNew)
//            //recyclerView = findViewById<View>(R.id.rview) as RecyclerView
//            recyclerView.setHasFixedSize(false)
//            recyclerView.setAdapter(adapter)
//            recyclerView.setLayoutManager(LinearLayoutManager(this))

            //TODO check for duplicates, i.e. comparedkey and length if already exists when don't add to db
        }

        filled_exposed_dropdown.setOnItemClickListener(OnItemClickListener { parent, view, position, rowId ->
            Int
            val selection = parent.getItemAtPosition(position) as String
            Log.d("aaanewMAterialSpinner", selection)
            tvGen.editText?.setText(
                ss.pgen(
                    editTextKeyGen.editText?.text.toString(),
                    nameS.toString(),
                    nameT.toString().get(0).toString(),
                    nameT.toString().get(1).toString(),
                    nameT.toString().get(2).toString(),
                    nameT.toString().get(3).toString(),
                    selection.toInt()
                )
            )

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
                if (filled_exposed_dropdown.editableText.toString().toIntOrNull() == null) {
                    tvGen.editText?.setText(
                        ss.pgen(
                            editTextKeyGen.editText?.text.toString(),
                            nameS.toString(),
                            nameT.toString().get(0).toString(),
                            nameT.toString().get(1).toString(),
                            nameT.toString().get(2).toString(),
                            nameT.toString().get(3).toString(),
                            10
                        )
                    )
                } else {
                    tvGen.editText?.setText(
                        ss.pgen(
                            editTextKeyGen.editText?.text.toString(),
                            nameS.toString(),
                            nameT.toString().get(0).toString(),
                            nameT.toString().get(1).toString(),
                            nameT.toString().get(2).toString(),
                            nameT.toString().get(3).toString(),
                            filled_exposed_dropdown.editableText.toString().toInt()
                        )
                    )
                }
                //tvGen.editText?.setText(ss.pgen(editTextKeyGen.editText?.text.toString(),"jimbob","4","5","6","7",filled_exposed_dropdown.editableText.toString().toInt()))
            }
        })

        registerForContextMenu(recyclerView);
        val ustomAdapter = CustomAdapter(arr)

        //val selectedPostion = (ustomAdapter as AdapterContextMenuInfo).position


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            R.menu.main -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            R.id.action_settings -> openSettings()
            R.id.action_about -> openAbout()
            R.id.action_exit -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun openSettings() {
        Log.d("aaa", "in open settings")
        val intent = Intent(this, SettingsActivity::class.java).apply {
//            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

    fun openAbout() {
        Log.d("aaa", "in open about")
        val newFragment = FireMissilesDialogFragment()
        newFragment.show(supportFragmentManager, "missiles")
    }

    //TODO donation option
    fun openDonate() {

    }

    //TODO ad free version option
    fun openAdFree() {

    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }

    //
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = this.menuInflater //getActivity().getMenuInflater()
        inflater.inflate(R.menu.rv_menu_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_copy -> {
                val text1: TextView? =
                    recyclerView.findViewHolderForAdapterPosition(getPosi())?.itemView?.findViewById(
                        R.id.textView4
                    )

                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("PGen", text1?.text.toString())
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
                val value: TextView? =
                    recyclerView.findViewHolderForAdapterPosition(getPosi())?.itemView?.findViewById(
                        R.id.textView4
                    )

                var a = Word(
                    wid?.text.toString().toInt(),
                    pType?.text.toString(),
                    key?.text.toString(),
                    value?.text.toString()
                )
                room.wordDao().delete(a)
                arrr = wordDao.getAll()
                var adapter = CustomAdapter(arrr)
                recyclerView.setHasFixedSize(false)
                recyclerView.setAdapter(adapter)
                recyclerView.setLayoutManager(LinearLayoutManager(this))
                //room.close()

            }
            R.id.menu_cancel -> {
                Log.d("aaamenu_cancel", getPosi().toString())
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

    fun encryptMsgs(message: String, secret: SecretKey?): String {
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

//    override fun onResume() {
//        super.onResume()
//
//        if (ciphertextWrapper != null) {
//            if (SampleAppUser.fakeToken == null) {
//                showBiometricPromptForDecryption()
//            } else {
//                // The user has already logged in, so proceed to the rest of the app
//                // this is a todo for you, the developer
//                updateApp(getString(R.string.already_signedin))
//            }
//        }
//
//    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
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
        //Toast.makeText(applicationContext, authStatus.toString()+"STOPP called", Toast.LENGTH_SHORT).show()
    }

}

class CustomAdapter(private val dataSet: List<Word>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    companion object {
        var position: Int = 0
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnCreateContextMenuListener {
        var ivCopy: ImageView
        var fileName: TextView
        var ivMore: ImageView

        @SuppressLint("ResourceType")
        override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenuInfo?) {
            MainActivity.pos = getPosition()
            MainActivity.setPosi(getPosition())

        }

        val textView1: TextView
        val textView2: TextView
        val textView3: TextView
        val textView4: TextView

        init {
            ivCopy = view.findViewById(R.id.ivCopy) as ImageView
            fileName = view.findViewById(R.id.textView4) as TextView
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

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
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
                                val clipboard = view?.context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                                val clip: ClipData = ClipData.newPlainText("PGen", viewHolder.textView4.text.toString())
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(view?.context, "Text Copied", Toast.LENGTH_LONG).show()

                            }
                            R.id.menu_delete -> {
                                Log.d("aaaamenuu","delete")
                                val db = view?.context?.let {
                                    Room.databaseBuilder(
                                        it,
                                        AppDatabase::class.java, "database-name"
                                    ).allowMainThreadQueries().build()
                                }
                                val wordDao = db?.wordDao()

                                val wid: TextView? = viewHolder.textView1
                                val pType: TextView? = viewHolder.textView2
                                val key: TextView? = viewHolder.textView3
                                val value: TextView? = viewHolder.textView4

                                var a = Word(
                                    wid?.text.toString().toInt(),
                                    pType?.text.toString(),
                                    key?.text.toString(),
                                    value?.text.toString()
                                )
                                db?.wordDao()?.delete(a)
                                val arrr = wordDao?.getAll()
                                var adapter = arrr?.let { CustomAdapter(it) }

                                MainActivity.recyclerView.setHasFixedSize(false)
                                MainActivity.recyclerView.setAdapter(adapter)
                                MainActivity.recyclerView.setLayoutManager(LinearLayoutManager(view?.context))
                                db?.close()
                                Log.d("aaaamenuu","DDDdelete")

                            }

                            R.id.menu_cancel ->  {
                                Log.d("aaaamenuu","canceeel") //TODO
                            }
                            R.id.menu_hide ->  {
                                Log.d("aaaamenuu","canceeel") //TODO
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
                val clipboard = view?.context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("PGen", viewHolder.textView4.text.toString())
                clipboard.setPrimaryClip(clip)
                Toast.makeText(view?.context, "Text Copied", Toast.LENGTH_LONG).show()

            }
        })

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

class FireMissilesDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            builder.setView(inflater.inflate(R.layout.alertdialog_about, null))
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

