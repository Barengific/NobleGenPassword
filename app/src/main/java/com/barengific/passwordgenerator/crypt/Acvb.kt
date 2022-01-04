package com.barengific.passwordgenerator.crypt

import android.util.Base64
import com.barengific.passwordgenerator.crypt.Acvb
import java.lang.Exception
import javax.crypto.Cipher
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

        val mstrIvParameter = mstrIvParameter + mstrIvParameterE

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

        val mstrIvParameter = mstrIvParameter + mstrIvParameterE

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
}
