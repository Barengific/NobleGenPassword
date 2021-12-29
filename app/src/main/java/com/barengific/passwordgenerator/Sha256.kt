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

    fun xor(a: String, b: String, c: String): String {
//        Log.d("aaainxorA", a)
//        Log.d("aaainxorB", b)
//        Log.d("aaainxorC", c)

        var res: String = ""
        var ress: String = "a"
        Log.d("aaainxorQQQQQ", (a[0].digitToInt() xor b[0].digitToInt() xor c[0].digitToInt()).toString())
        for (i in 0 until a.length) {
            if ((a[i].digitToInt() xor b[i].digitToInt() xor c[i].digitToInt()) == 1) {
                res += "1"
                Log.d("aaainxorIF0", "res")
                Log.d("aaainxorIF0", res)
            } else if ((a[i].digitToInt() xor b[i].digitToInt() xor c[i].digitToInt()) == 0) {
                res += "0"
                Log.d("aaainxorIF1", "res")
                Log.d("aaainxorIF1", res.toString())
                Log.d("aaainxorIF1", res.length.toString())
                var qqq: Boolean = res.isNullOrEmpty()
                Log.d("aaainxorIF1", qqq.toString())
                Log.d("aaainxorIFOUTTT1", ress)
            } else {
                //Log.d("aaainxorIF2", "res")
                //Log.d("aaainxorIF2", res)
                Log.d("aaainxorIF2", res.toString())
                Log.d("aaainxorIF2", res.length.toString())
                var qqq: Boolean = res.isNullOrEmpty()
                Log.d("aaainxorIF2", qqq.toString())
                Log.d("aaainxorIFOUTT2", ress)
                println("____")
            }
        }
        Log.d("aaaxor", res)
        return res
    }

    fun adder(a: String, b: String, c: String, d: String): String {
        Log.d("aaainadderA", a)
        Log.d("aaainadderB", b)
        Log.d("aaainadderC", c)
        Log.d("aaainadderD", d)

        var res = ""

        val aint = a.toLong(2)
        val bint = b.toLong(2)
        val cint = c.toLong(2)
        val dint = d.toLong(2)
        val r = aint + bint + cint + dint
        val binr = java.lang.Long.toBinaryString(r)
        if (binr.length == 32) {
            res = binr
        } else if (binr.length > 32) {
            res = java.lang.Long.toBinaryString((r % Math.pow(2.0, 32.0)).toLong())
            if (res.length < 32) {
                res = addZeros(binr, 32)
            } else if (res.length > 32) {
                //System.out.println("toobig");
            }
        } else if (binr.length < 32) {
            res = addZeros(binr, 32)
        }
        if (res.length > 32) {
            res = rmZeros(res, 32)
        }
        Log.d("aaaadder", res)
        return res
    }

    fun addersz(a: Long): String {
        var res = ""
        val binr = java.lang.Long.toBinaryString(a)
        if (binr.length == 32) {
            res = binr
        } else if (binr.length > 32) {
            res = java.lang.Long.toBinaryString((a % Math.pow(2.0, 32.0)).toLong())
        } else if (binr.length < 32) {
            res = addZeros(binr, 32)
        }
        if (res.length < 32) {
            res = addZeros(res, 32)
        }
        if (res.length != 32) {
            println("tooo smlll still")
        }
        Log.d("aaaaddersz", res)
        return res
    }

    fun rotr(a: String, rotnumber: Int): String {
        var a = a
        for (i in 0 until rotnumber) {
            val last_char = a.substring(a.length - 1)
            a = a.substring(0, a.length - 1)
            a = last_char + a
        }
        Log.d("aaarotr", a)
        return a
    }

    fun shr(a: String, rotnumber: Int): String {
        var a = a
        for (i in 0 until rotnumber) {
            a = a.substring(0, a.length - 1)
            a = "0$a"
        }
        Log.d("aaashr", a)
        return a
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