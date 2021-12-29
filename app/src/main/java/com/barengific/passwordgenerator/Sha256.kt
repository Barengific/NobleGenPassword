package com.barengific.passwordgenerator

import android.util.Log
import java.math.BigInteger

class Sha256 {
    var rt2 = intArrayOf(2, 3, 5, 7, 11, 13, 17, 19)
    var rt3 = intArrayOf(
        2,
        3,
        5,
        7,
        11,
        13,
        17,
        19,
        23,
        29,
        31,
        37,
        41,
        43,
        47,
        53,
        59,
        61,
        67,
        71,
        73,
        79,
        83,
        89,
        97,
        101,
        103,
        107,
        109,
        113,
        127,
        131,
        137,
        139,
        149,
        151,
        157,
        163,
        167,
        173,
        179,
        181,
        191,
        193,
        197,
        199,
        211,
        223,
        227,
        229,
        233,
        239,
        241,
        251,
        257,
        263,
        269,
        271,
        277,
        281,
        283,
        293,
        307,
        311
    )

    fun rt2s(): ArrayList<String> {
        //2^1/2 - 2^1/2 * 2^32
        val rt: ArrayList<String> = ArrayList()
        for (i in rt2.indices) {
            rt.add(
                addZeros(
                    String.format(
                        java.lang.Long.toBinaryString(
                            ((Math.sqrt(
                                rt2[i]
                                    .toDouble()
                            ) - Math.sqrt(rt2[i].toDouble()).toInt()) * Math.pow(
                                2.0,
                                32.0
                            )).toLong()
                        )
                    ), 32
                )
            )
        }
        return rt
    }

    fun rt3s(): ArrayList<String> {
        //2^1/3 - 2^1/3 * 2^32
        val rt: ArrayList<String> = ArrayList()
        for (i in rt3.indices) {
            rt.add(
                addZeros(
                    String.format(
                        java.lang.Long.toBinaryString(
                            ((Math.cbrt(
                                rt3[i]
                                    .toDouble()
                            ) - Math.cbrt(rt3[i].toDouble()).toInt()) * Math.pow(
                                2.0,
                                32.0
                            )).toLong()
                        )
                    ), 32
                )
            )
        }
        return rt
    }

    fun cho(a: String, b: String, c: String): String {
//    #use 'a' input to determine whether to take 'b' or 'c'
        var res = ""
        for (i in 0 until a.length) {
            if (a[i] == '1') {
                res += b[i]
            } else if (a[i] == '0') {
                res += c[i]
            } else {
                println("nothing")
            }
        }
        Log.d("aaacho", res)
        return res
    }

    fun mj(a: String, b: String, c: String): String {
//    #take majority input value
        var res = ""
        for (i in 0 until a.length) {
            if ((a[i] == '1' && b[i] == '1') xor
                (a[i] == '1' && c[i] == '1') xor
                (b[i] == '1' && c[i] == '1')
            ) {
                res += "1"
            } else if ((a[i] == '0' && b[i] == '0') xor
                (a[i] == '0' && c[i] == '0') xor
                (b[i] == '0' && c[i] == '0')
            ) {
                res += "0"
            } else {
                println("maj nothing")
            }
        }
        Log.d("aaamj", res)
        return res
    }

    fun addZeros(msg: String, newLen: Int): String {
        var msg = msg
        for (i in msg.length until newLen) {
            msg = "0$msg"
        }
        if (msg.length != 32) {
            //System.out.println("add zeroes: " + msg.length());
        }
        //Log.d("addZeros", msg)
        return msg
    }

    fun rmZeros(msg: String, newLen: Int): String {
        var msg = msg
        val target = msg.length - newLen
        msg = msg.substring(target, msg.length)
        if (msg.length != 32) {
            println("remove zeroes: " + msg.length)
        }
        Log.d("aaarmZeros", msg)
        return msg
    }

}