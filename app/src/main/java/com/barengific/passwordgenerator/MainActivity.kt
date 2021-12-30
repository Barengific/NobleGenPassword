package com.barengific.passwordgenerator

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.content.DialogInterface.OnShowListener
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Menu
import android.view.View
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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.barengific.passwordgenerator.databinding.ActivityMainBinding
import kotlinx.coroutines.NonCancellable.cancel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cuneytayyildiz.onboarder.utils.visible
import java.io.File
import kotlinx.android.synthetic.main.fragment_home.*
import com.barengific.passwordgenerator.Sha256 as Sha2561

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

////
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

        val spinner: Spinner = findViewById(R.id.p_len_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.p_len_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        val ss = Pgen()
        var qq = 0
        btnGenerate.setOnClickListener {
            //sss.hashes("")
            tvGen.setText(ss.pgen(editTextKeyGen.text.toString(),"jimbob","4","5","6","7",10))
//            if(qq == 0){
//                tvGen.setText(ss.hashes(""))
//                qq = 1
//            }else if(qq == 1){
//                tvGen.setText(ss.hashes("123"))
//                qq = 2
//            }else if(qq == 2){
//                tvGen.setText(ss.hashes("abc"))
//                qq = 3
//            }else{
//                tvGen.setText("")
//                qq = 0
//            }
        }

        tvCopy.setOnClickListener{
            //tvGen.setText("COPIED")
            // Creates a new text clip to put on the clipboard
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("PGen", tvGen.text.toString())
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip)
            Toast.makeText(applicationContext,"Text Copied", Toast.LENGTH_LONG).show()

        }


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

}
