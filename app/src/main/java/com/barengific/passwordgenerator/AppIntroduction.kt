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

class AppIntroduction : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(
            AppIntroFragment.newInstance(
            title = "Welcome...baz",
            description = "This is MYY the first slide of the example"
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Yehh...Let's get started!",
            description = "This is the last slide, I won't annoy you more :)"
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