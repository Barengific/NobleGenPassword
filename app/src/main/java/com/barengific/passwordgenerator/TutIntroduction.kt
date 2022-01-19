package com.barengific.passwordgenerator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class TutIntroduction : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        addSlide(AppIntroFragment.newInstance(
            title = "Select...",
            description = "Select a master key and a 4 digit pin number"
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "save",
            description = "the app automatically saves that master key and pin"
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "when app started, need to generate?",
            description = "enter a key phrase to find a password for it and select password length"
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "New pass",
            description = "click generate"
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "save or copy",
            description = "save the password for later use, copy the password to use now"
        ))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        //TODO intent go to main activity
        val intent = Intent(this, CredentialsEnt::class.java).apply {
//            putExtra(EXTRA_MESSAGE, message)
            putExtra("fromIntro","fin")
        }
        startActivity(intent)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        val intent = Intent(this, CredentialsEnt::class.java).apply {
//            putExtra(EXTRA_MESSAGE, message)
            putExtra("fromIntro","fin")
        }
        startActivity(intent)
        finish()
    }
}