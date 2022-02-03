package com.barengific.passwordgenerator.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.barengific.passwordgenerator.CredentialsEnt
import com.barengific.passwordgenerator.R
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

class AppIntroduction : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!
        setTransformer(AppIntroPageTransformerType.Zoom)
        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(AppIntroFragment.newInstance(
            title = "Welcome...",
            description = "This is the Noble Password Manager",
            backgroundDrawable = R.drawable.background
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Simple & Effective!",
            description = "An easy way to create unique passwords",
            backgroundDrawable = R.drawable.background2
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Safe & Reliable!",
            description = "You don't have to worry about brute force attacks and even if the app gets deleted, the passwords can be derived back!",
            backgroundDrawable = R.drawable.background3
        ))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        //TODO intent go to main activity
        val fromSettings = intent.extras?.get("fromSettings")
        if(fromSettings?.equals("fin") == true){
            val intent = Intent(this, SettingsActivity::class.java).apply {
                putExtra("fromIntro","fin")
            }
            startActivity(intent)
        }else{
            val intent = Intent(this, CredentialsEnt::class.java).apply {
                putExtra("fromIntro","fin")
            }
            startActivity(intent)
        }
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        val fromSettings = intent.extras?.get("fromSettings")
        if(fromSettings?.equals("fin") == true){
            val intent = Intent(this, SettingsActivity::class.java).apply {
                putExtra("fromIntro","fin")
            }
            startActivity(intent)
        }else{
            val intent = Intent(this, CredentialsEnt::class.java).apply {
                putExtra("fromIntro","fin")
            }
            startActivity(intent)
        }
        finish()
    }
}