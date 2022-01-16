package com.barengific.passwordgenerator

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.credentials_ent.*
import java.io.File


class CredentialsEnt : AppCompatActivity() {
    //private lateinit var appBarConfiguration: AppBarConfiguration

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.credentials_ent)

        btnSubmit.setOnClickListener {
            if (editTextPassword.editText?.text.toString().isNotEmpty()
                && editTextNumberPassword1.text.toString().isNotEmpty()
                && editTextNumberPassword2.text.toString().isNotEmpty()
                && editTextNumberPassword3.text.toString().isNotEmpty()
                && editTextNumberPassword4.text.toString().isNotEmpty()
            ) {
                if (editTextPassword.editText?.toString()?.length >= 8) {
                    if (editTextNumberPassword1.text.toString().length == 1
                        && editTextNumberPassword2.text.toString().length == 1
                        && editTextNumberPassword3.text.toString().length == 1
                        && editTextNumberPassword4.text.toString().length == 1
                    ) {
                        //val path = applicationContext.getFilesDir()
                        //Log.d("aaa", path.toString())

                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        Log.d("aaa", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString())

                        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/barz.txt").writeText(
                            editTextTextPassword.text.toString() +"\n" +
                                    editTextNumberPassword1.text.toString() +"\n" +
                                    editTextNumberPassword2.text.toString() +"\n" +
                                    editTextNumberPassword3.text.toString() +"\n" +
                                    editTextNumberPassword4.text.toString())

                        val intent = Intent(this, MainActivity::class.java).apply {
                            //putExtra(EXTRA_MESSAGE, message)
                            putExtra("fromIntro","fin")
                        }
                        Log.d("aaa", "in credentials")
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Sigma values cannot be more than 1 digit",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Master Key has to be above 8 characters",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Log.d("aaa", "buttonsss")
                Toast.makeText(
                    applicationContext,
                    "Please fill in all the required fields",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }

}
