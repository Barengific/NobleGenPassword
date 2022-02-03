package com.barengific.passwordgenerator.crypt

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import com.barengific.passwordgenerator.crypt.Acvb
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.InvalidParameterSpecException
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.Throws

object Acvb {
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
    private const val mstrKeyParameter : String = "ifbhAgGOcXMdRebE"

    fun encrypt_AES(strKey: String, strClearText: String, mstrIvParameter: String): String? {
        var strKey = strKey
        if(strKey.length > 16){
            //TODO cut key down to 16
            val ksize = strKey.length
            strKey = strKey.substring(0,16)
        }else if(strKey.length < 16){
            //TODO increase key size
            val ksize = strKey.length
            val isize = 16 - ksize
            val incrS = mstrKeyParameter.substring(0,isize)
            strKey = strKey + incrS
        }

        //val mstrIvParameter = mstrIvParameter + mstrIvParameterE

        try {
            val raw = strKey.toByteArray()
            // create AES key
            val skeySpec = SecretKeySpec(raw, AES)
            // create a cipher
            val cipher = Cipher.getInstance(CBC_PKCS5_PADDING)
            // create offset
            val iv = IvParameterSpec(mstrIvParameter.toByteArray())
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


    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidParameterSpecException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class,
        UnsupportedEncodingException::class
    )
    fun encryptMsg(message: String, secret: SecretKey?): String? {
        var cipher: Cipher? = null
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secret)
        val cipherText: ByteArray = cipher.doFinal(message.toByteArray(charset("UTF-8")))
        return Base64.encodeToString(cipherText, Base64.NO_WRAP)
    }

    fun encryptMsgs(message: String, secret: SecretKey?): String {
        var cipher: Cipher? = null
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secret)
        val cipherText: ByteArray = cipher.doFinal(message.toByteArray(charset("UTF-8")))
        return Base64.encodeToString(cipherText, Base64.NO_WRAP)
    }

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidParameterSpecException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        UnsupportedEncodingException::class
    )
    fun decryptMsg(cipherText: String?, secret: SecretKey?): String? {
        var cipher: Cipher? = null
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secret)
        val decode: ByteArray = Base64.decode(cipherText, Base64.NO_WRAP)
        //String("aa")
        //String(cipher.doFinal(decode), 'U')
        return String(cipher.doFinal(decode))
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun generateKey(key: String): SecretKey? {
        val secret: SecretKeySpec
        secret = SecretKeySpec(key.toByteArray(), "AES")
        return secret
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

}
