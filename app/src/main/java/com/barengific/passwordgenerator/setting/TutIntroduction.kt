package com.barengific.passwordgenerator.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment

class TutIntroduction : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        addSlide(AppIntroFragment.newInstance(
            title = "Select...",
            description = "Select a master key and a 4 digit pin number"
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Save",
            description = "The app automatically saves the master key and pin"
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "When the app is started, generate a password",
            description = "enter a key phrase to find a password for it and select password length"
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "New pass",
            description = "Click generate to see the newly generated password"
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Save or copy",
            description = "Save the password for later use, copy the password to use now"
        ))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        //TODO intent go to main activity
        val intent = Intent(this, SettingsActivity::class.java).apply {
//            putExtra(EXTRA_MESSAGE, message)
            putExtra("fromIntro","fin")
        }
        startActivity(intent)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        val intent = Intent(this, SettingsActivity::class.java).apply {
//            putExtra(EXTRA_MESSAGE, message)
            putExtra("fromIntro","fin")
        }
        startActivity(intent)
        finish()
    }
}