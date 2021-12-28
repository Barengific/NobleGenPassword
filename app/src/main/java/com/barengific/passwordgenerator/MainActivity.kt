package com.barengific.passwordgenerator

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
import kotlinx.android.synthetic.main.fragment_home.switch1
import kotlinx.android.synthetic.main.fragment_home.editTextTextPassword
import kotlinx.android.synthetic.main.fragment_home.editTextNumberPassword1
import kotlinx.android.synthetic.main.fragment_home.editTextNumberPassword2
import kotlinx.android.synthetic.main.fragment_home.editTextNumberPassword3
import kotlinx.android.synthetic.main.fragment_home.editTextNumberPassword4
import kotlinx.android.synthetic.main.fragment_home.btnSubmit
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    lateinit var editText: EditText

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        //
        //

//        switch1.setOnClickListener {
//            if (switch1.isChecked){
//                switch1.setTextColor(Color.WHITE)
//                Log.d("aaa","on")
//            }
//            else{
//                switch1.setTextColor(Color.BLACK)
//                Log.d("aaa","off")
//            }
//        }

        val hidePasswordMethod = PasswordTransformationMethod()
        switch1.setOnClickListener {
            editTextTextPassword.apply {
                transformationMethod =
                    if (transformationMethod is PasswordTransformationMethod)
                        null //shows password
                    else
                        hidePasswordMethod //hides password
            }
            editTextNumberPassword1.apply {
                transformationMethod =
                    if (transformationMethod is PasswordTransformationMethod)
                        null //shows password
                    else
                        hidePasswordMethod //hides password
            }
            editTextNumberPassword2.apply {
                transformationMethod =
                    if (transformationMethod is PasswordTransformationMethod)
                        null //shows password
                    else
                        hidePasswordMethod //hides password
            }
            editTextNumberPassword3.apply {
                transformationMethod =
                    if (transformationMethod is PasswordTransformationMethod)
                        null //shows password
                    else
                        hidePasswordMethod //hides password
            }
            editTextNumberPassword4.apply {
                transformationMethod =
                    if (transformationMethod is PasswordTransformationMethod)
                        null //shows password
                    else
                        hidePasswordMethod //hides password
            }
        }
        btnSubmit.setOnClickListener{
            if(editTextTextPassword.text.toString().isNotEmpty()
                && editTextNumberPassword1.text.toString().isNotEmpty()
                && editTextNumberPassword2.text.toString().isNotEmpty()
                && editTextNumberPassword3.text.toString().isNotEmpty()
                && editTextNumberPassword4.text.toString().isNotEmpty()){
                    if(editTextTextPassword.text.toString().length >= 8){
                        if(editTextNumberPassword1.text.toString().length == 1
                            && editTextNumberPassword2.text.toString().length == 1
                            && editTextNumberPassword3.text.toString().length == 1
                            && editTextNumberPassword4.text.toString().length == 1){
                            editTextTextPassword.visibility = View.INVISIBLE
                            editTextNumberPassword1.visibility = View.INVISIBLE
                            editTextNumberPassword2.visibility = View.INVISIBLE
                            editTextNumberPassword3.visibility = View.INVISIBLE
                            editTextNumberPassword4.visibility = View.INVISIBLE
                            switch1.visibility = View.INVISIBLE
                            btnSubmit.visibility = View.INVISIBLE

                            //val path = applicationContext.getFilesDir()
                            //Log.d("aaa", path.toString())

                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            Log.d("aaa",
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                    .toString()
                            )
//                            val PERMISSION_ALL = 1
//                            val PERMISSIONS = arrayOf(
//                                Manifest.permission.READ_EXTERNAL_STORAGE,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE
//                            )
                            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                .toString()+"/barzzzz.txt").writeText("hello,hello,heloo barzz")
                        }else{
                            Toast.makeText(applicationContext, "Sigma values cannot be more than 1 digit", Toast.LENGTH_LONG).show()
                        }
                    }else{
                        Toast.makeText(applicationContext, "Master Key has to be above 8 characters", Toast.LENGTH_LONG).show()
                    }
            } else{
                Log.d("aaa", "buttonsss")
                Toast.makeText(applicationContext, "Please fill in all the required fields", Toast.LENGTH_LONG).show()
            }


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
