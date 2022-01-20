package com.barengific.passwordgenerator.crypt

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.barengific.passwordgenerator.MainActivity
import com.barengific.passwordgenerator.crypt.Acvb
import java.lang.Exception
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.Throws

object Aqtik {

    lateinit var mstrKeyParameter: String;
    lateinit var mstrIvParameter: String;

    private const val TAG = "AESCBCUtils"
    // CBC (cipher block chaining) mode, pkcs5padding complement mode
    // AES is the encryption mode, CBC is the working mode, and pkcs5padding is the filling mode
    /**
     * encryption and decryption algorithm / working mode / filling mode
     */
    private const val CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding"

    // AES encryption
    private const val AES = "AES"
    // key offset

    private const val mstrIvParameterX : String = "6390515264757125";
    private const val mstrIvParameterE : String = "639051521258";
    private const val mstrKeyParameterE : String = "ifbhAgGOcXMdRebE"

    fun encrypt_AES(strClearText: String): String? {
        getKey()

        try {
            val raw = mstrKeyParameter.toByteArray()
            // create AES key
            val skeySpec = SecretKeySpec(raw, AES)
            // create a cipher
            val cipher = Cipher.getInstance(CBC_PKCS5_PADDING)
            // create offset
            val iv = IvParameterSpec(mstrIvParameterE.toByteArray())
            // initialize the encryptor
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
            // perform encryption
            val cipherText = cipher.doFinal(strClearText.toByteArray())
            //Log.d(TAG, "encrypt result(not BASE64): " + cipherText.toString());
            var strBase64Content =
                Base64.encodeToString(cipherText, Base64.DEFAULT) // encode it by BASE64 again
            //Log.d(TAG, "encrypt result(BASE64): " + strBase64Content);
            strBase64Content =
                strBase64Content.replace(System.getProperty("line.separator").toRegex(), "")
            return strBase64Content
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(Exception::class)
    fun decrypt(strKey: String, strCipherText: String?, mstrIvParameter: String): String? {
        if(strKey.length > 16){
            //TODO cut key down to 16
        }else if(strKey.length < 16){
            //TODO increase key size
        }

        //val mstrIvParameter = mstrIvParameter + mstrIvParameterE

        try {
            val raw = strKey.toByteArray(charset("ASCII"))
            // create AES secret key
            val skeySpec =
                SecretKeySpec(raw, AES)
            // create cipher
            val cipher = Cipher.getInstance(CBC_PKCS5_PADDING)
            // create offset
            val iv =
                IvParameterSpec(mstrIvParameter.toByteArray())
            // initialize the decryptor
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
            // decrypt
            val cipherText = Base64.decode(
                strCipherText,
                Base64.DEFAULT
            ) // decode by BASE64 first
            //Log.d(TAG, "BASE64 decode result(): " + cipherText.toString());
            val clearText = cipher.doFinal(cipherText)
            //Log.d(TAG, "decrypt result: " + strClearText);
            return String(clearText)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getKey(){
        val masterKey = MasterKey.Builder(MainActivity.applicationContext().applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferencesEE: SharedPreferences = EncryptedSharedPreferences.create(
            MainActivity.applicationContext(),
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

        val nameS = sharedPreferencesEE.getString("signatureS", "nonon")
        val nameT = sharedPreferencesEE.getString("signatureT", "nonon")

        mstrIvParameter = nameT + mstrIvParameterE

        var strKey = nameS
        if(strKey?.length!! > 16){
            //TODO cut key down to 16
            strKey = strKey.substring(0,16)
        }else if(strKey.length < 16){
            //TODO increase key size
            val ksize = strKey.length
            val isize = 16 - ksize
            val incrS = mstrKeyParameterE.substring(0,isize)
            strKey = strKey + incrS
        }
        mstrKeyParameter = strKey
    }
}
