package com.barengific.passwordgenerator.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.barengific.passwordgenerator.R
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

class TutIntroduction : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        setTransformer(
            AppIntroPageTransformerType.Parallax(
            titleParallaxFactor = 1.0,
            imageParallaxFactor = -1.0,
            descriptionParallaxFactor = 2.0
        ))

        addSlide(AppIntroFragment.newInstance(
            title = "Select...",
            description = "Select a master key and a 4 digit pin number",
            backgroundDrawable = R.drawable.background5
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Save",
            description = "The app automatically saves the master key and pin",
            backgroundDrawable = R.drawable.background6
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "When the app is started, generate a password",
            description = "enter a key phrase to find a password for it and select password length",
            backgroundDrawable = R.drawable.background7
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "New pass",
            description = "Click generate to see the newly generated password",
            backgroundDrawable = R.drawable.background8
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Save or copy",
            description = "Save the password for later use, copy the password to use now",
            backgroundDrawable = R.drawable.background9
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