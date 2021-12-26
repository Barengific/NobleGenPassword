package com.barengific.passwordgenerator

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
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

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    lateinit var editText: EditText

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
        //confirmFireMissiles()
        //

//        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
//        val editTextNumberPassword1 = findViewById<EditText>(R.id.editTextNumberPassword1)
//        val editTextNumberPassword2 = findViewById<EditText>(R.id.editTextNumberPassword2)
//        val editTextNumberPassword3 = findViewById<EditText>(R.id.editTextNumberPassword3)
//        val editTextNumberPassword4 = findViewById<EditText>(R.id.editTextNumberPassword4)
//
//        editText = findViewById(R.id.editTextPassword)
//        editText.text.toString()

        val editText = findViewById<EditText>(R.id.editTextPassword)
        val message = editText.text.toString()

//        val editTextPasswordv = editTextPassword.text.toString()
//        val editTextNumberPassword1v = editTextNumberPassword1.text.toString()
//        val editTextNumberPassword2v = editTextNumberPassword2.text.toString()
//        val editTextNumberPassword3v = editTextNumberPassword3.text.toString()
//        val editTextNumberPassword4v = editTextNumberPassword4.text.toString()


        val dialog = AlertDialog.Builder(this)
            .setTitle("Welcome")
            .setMessage("Please enter the required information") // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setView(R.layout.alertdialog)
            .setPositiveButton(
                android.R.string.yes
            ) { dialog, which ->
                if (editText == null)
                    Log.d("aaaa", "not null")fdfdffgddsg
                else
                    Log.d("aaaa", "null")
            } // A null listener allows the button to dismiss the dialog and take no further action.
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(false)
            .show()
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

    fun confirmFireMissiles() {
        val newFragment = FireMissilesDialogFragment()
        newFragment.dialog?.setCanceledOnTouchOutside(false)
        newFragment.dialog?.setCancelable(false)
        newFragment.show(supportFragmentManager,"missiles")
        //newFragment.show(supportFragmentManager, "missiles")
//        Log.d("TAG", newFragment.isCancelable.toString());
//        newFragment.onCancel()

    }

}

class FireMissilesDialogFragment : DialogFragment() {

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return activity?.let {
//            val builder = AlertDialog.Builder(it)
//            // Get the layout inflater
//            val inflater = requireActivity().layoutInflater;
//
//            // Inflate and set the layout for the dialog
//            // Pass null as the parent view because its going in the dialog layout
//            builder.setView(inflater.inflate(R.layout.alertdialog, null))
//                // Add action buttons
//                .setPositiveButton(R.string.submit,
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // sign in the user ...
//                    })
////                .setNegativeButton(R.string.cancel,
////                    DialogInterface.OnClickListener { dialog, id ->
////                        getDialog()?.cancel()
////                    })
////                .setCancelable(false)
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }

}


