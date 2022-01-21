package com.barengific.passwordgenerator

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

private const val TITLE_TAG = "settingsActivityTitle"

class SettingsActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );

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
            Log.d("aaaaa", "backit")
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
            Log.d("aaaaaEEESEttings", nameE.toString())
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
            Log.d("aaaaa", "farrrr")
            MainActivity.authStatus = true
            val intent = Intent(this, MainActivity::class.java).apply {
//            putExtra(EXTRA_MESSAGE, message)
                putExtra("fromLogin","fin")
            }
            startActivity(intent)
//            return false
        }
        return super.onSupportNavigateUp()
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


            //
            val masterKey = this.context?.let {
                MasterKey.Builder(it, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()
            }

            val sharedPreferencesEE: SharedPreferences = masterKey?.let {
                this.context?.let { it1 ->
                    EncryptedSharedPreferences.create(
                        it1,
                        "secret_shared_prefs",
                        it,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
                }
            }!!

            //write
            //sharedPreferencesEE.edit().putString("signatureS", "encrrrr").apply()
            //read
            val nameS = sharedPreferencesEE.getString("signatureS", "nonon")
            val nameT = sharedPreferencesEE.getString("signatureT", "nonon")

            //for settings //readwrite
            val preferences = PreferenceManager.getDefaultSharedPreferences(this.context /* Activity context */)
            //read
//            val name = sharedPreferences.getString("signature", "nonon")
            //write
            with (preferences.edit()){
                putString("signatureS",nameS)
                putString("signatureT",nameT)
                apply()
            }
            setPreferencesFromResource(R.xml.security_preferences, rootKey)
        }

    }


    class SyncFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.sync_preferences, rootKey)
        }
    }

    class BackupFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.backup_preferences, rootKey)
        }
    }

    class RestoreFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.restore_preferences, rootKey)
        }
    }

    class IntroFragment : PreferenceFragmentCompat() {
        @SuppressLint("ResourceType")
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val intent = Intent(this.context, AppIntroduction::class.java).apply {
                putExtra("fromSettings","fin")
            }
            startActivity(intent)
        }
    }

    class TutFragment : PreferenceFragmentCompat() {
        @SuppressLint("ResourceType")
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val intent = Intent(this.context, TutIntroduction::class.java).apply {
                putExtra("fromSettings","fin")
            }
            startActivity(intent)
        }
    }

    class RSTFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val newFragment = ConDialogFragment()
            (activity as FragmentActivity).supportFragmentManager

            newFragment.show((activity as FragmentActivity).supportFragmentManager, "missiles")


//            val intent = Intent(this.context, CredentialsEnt::class.java).apply {
//                putExtra("fromSettings","fin")
//            }
//            startActivity(intent)
        }
    }


}

class ConDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            builder.setView(inflater.inflate(R.layout.alertdialog_about, null))
                .setPositiveButton(R.string.confirm,
                    DialogInterface.OnClickListener { dialog, id ->
                        val intent = Intent(this.context, CredentialsEnt::class.java).apply {
                            putExtra("fromSettings","fin")
                        }
                        startActivity(intent)
                        getDialog()?.show()
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                        
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}