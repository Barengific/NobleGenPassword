package com.barengific.passwordgenerator.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.barengific.passwordgenerator.*

private const val TITLE_TAG = "settingsActivityTitle"

class SettingsActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, HeaderFragment())
                .commit()
        } else {
            title = savedInstanceState.getCharSequence(TITLE_TAG)
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                setTitle(R.string.title_activity_settings)
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG, title)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (supportFragmentManager.popBackStackImmediate()) {
            Log.d("aaa", "back_it")
            val masterKey = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val sharedPreferencesEE: SharedPreferences = EncryptedSharedPreferences.create(
                this,
                "secret_shared_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

            val nameE = sharedPreferencesEE.getString("signatureS", "nonon")
            Log.d("aaa_EEE_Settings", nameE.toString())
            //for settings //readwrite
            val preferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
            //read
//            val name = sharedPreferences.getString("signature", "nonon")
            //write
            with (preferences.edit()){
                putString("signatureS", "nonon")
                putString("signatureT", "nonon")
                apply()
            }

            return true
        }else{
            Log.d("aaa", "far rrr")
            MainActivity.authStatus = true
            val intent = Intent(this, MainActivity::class.java).apply {
//            putExtra(EXTRA_MESSAGE, message)
                putExtra("fromLogin","fin")
            }
            startActivity(intent)
            finish()
//            return false
        }
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        MainActivity.authStatus = true
        val intent = Intent(this, MainActivity::class.java).apply {
//            putExtra(EXTRA_MESSAGE, message)
            putExtra("fromLogin","fin")
        }
        startActivity(intent)
        finish()
        setResult(RESULT_CANCELED)
        super.onBackPressed()
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        // Instantiate the new Fragment
        val args = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            pref.fragment
        ).apply {
            arguments = args
            setTargetFragment(caller, 0)
        }
        // Replace the existing Fragment with the new Fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings, fragment)
            .addToBackStack(null)
            .commit()
        title = pref.title
        return true
    }
    //add intro
    //add tutorial


    class HeaderFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.header_preferences, rootKey)
        }
    }

    class SecurityFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

            val intent = Intent(this.context, SecFrag::class.java).apply {
                putExtra("fromSettings","fin")
            }
            startActivity(intent)
            //(activity as FragmentActivity).supportFragmentManager.popBackStack()
            requireActivity().finish()
        }

    }

//TODO call finish for fragments
    class BackupFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.backup_preferences, rootKey)

            val intent = Intent(this.context, Backup::class.java).apply {
                putExtra("fromSettings","fin")
            }
            startActivity(intent)
            requireActivity().finish()
            (activity as FragmentActivity).supportFragmentManager.popBackStack()
        }
    }

    class RestoreFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.restore_preferences, rootKey)

            val intent = Intent(this.context, Restore::class.java).apply {
                putExtra("fromSettings","fin")
            }
            startActivity(intent)
            //(activity as FragmentActivity).supportFragmentManager.popBackStack()
            requireActivity().finish()
        }
    }

    class IntroFragment : PreferenceFragmentCompat() {
        @SuppressLint("ResourceType")
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val intent = Intent(this.context, AppIntroduction::class.java).apply {
                putExtra("fromSettings","fin")
            }
            startActivity(intent)
            //getActivity()?.getFragmentManager()?.popBackStack()
            (activity as FragmentActivity).supportFragmentManager.popBackStack()
        }
    }

    class TutFragment : PreferenceFragmentCompat() {
        @SuppressLint("ResourceType")
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val intent = Intent(this.context, TutIntroduction::class.java).apply {
                putExtra("fromSettings","fin")
            }
            startActivity(intent)
            (activity as FragmentActivity).supportFragmentManager.popBackStack()

        }
    }

    //TODO
    class RSTFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val alert = this.context?.let { AlertDialog.Builder(it) }

            alert?.setCancelable(false)
            alert?.setTitle("Warning!")
            alert?.setMessage("Are you sure you want to continue? All your saved passwords will be deleted!")
            alert?.setPositiveButton("Confirm") { _, _ ->
                val intent = Intent(this.context, CredentialsEnt::class.java).apply {
                    putExtra("fromSettings", "rst")
                }
                startActivity(intent)
                requireActivity().finish()
                (activity as FragmentActivity).supportFragmentManager.popBackStack()
            }
            alert?.setNegativeButton("Cancel") { _, _ ->
                val intent = Intent(this.context, SettingsActivity::class.java).apply {
                    putExtra("fromSettings", "fin")
                }
                startActivity(intent)
                requireActivity().finish()
                (activity as FragmentActivity).supportFragmentManager.popBackStack()
            }

            val dialog = alert?.create()
            dialog?.setCanceledOnTouchOutside(false)
            dialog?.show()

        }
    }

    class HelpFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val intent = Intent(this.context, TutIntroduction::class.java).apply {
                putExtra("fromSettings","fin")
            }
            startActivity(intent)
            requireActivity().finish()
            (activity as FragmentActivity).supportFragmentManager.popBackStack()
        }
    }

}
