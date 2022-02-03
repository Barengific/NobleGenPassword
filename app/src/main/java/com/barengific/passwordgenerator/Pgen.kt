package com.barengific.passwordgenerator

import android.util.Log
import java.math.BigInteger

class Pgen {

    var rt2 = intArrayOf(2, 3, 5, 7, 11, 13, 17, 19)
    var rt3 = intArrayOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71,
        73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167,
        173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269,
        271,        277,        281,        283,        293,        307,        311
    )

    val decode = arrayOf("@","#","£","_","&","-","/","*",";","!","?",".","0","1","2","3","4","5","6","7","8","9","A","B","D","E","F","G","H","K","L","M","N","P","Q","R","S","T","U","V","W","Y","a","b","c","d","e","f","g","h","i","j","k","m","n","p","q","r","s","t","u","v","x","z"
    );

    fun pgen(msg:String, mkey:String, int1:String, int2:String, int3:String, int4:String, plen:Int): String?{
        val mkey = mkey
        val int1 = int1.toInt()
        val int2 = int2.toInt()
        val int3 = int3.toInt()
        val int4 = int4.toInt()
        val plen = plen

        var message = ""
        val msg = msg+hashesPure(mkey)
        val msgb = hashes(msg, int1, int2, int3, int4)
        for (i in 0 until msgb!!.length) {
            message += String.format(
                "%08d",
                java.lang.Long.toBinaryString(msgb!![i].toLong()).toLong()
            ) //msg to binary
        }

        var min = 0
        var max = 6
        val chunks: ArrayList<String> = ArrayList()
        for (i in 0 until 42) {
            chunks.add(message.substring(min, max)) //split into chunks of 6bit
            min += 6
            max += 6
        }

        var res = ""
        for(i in 0 until plen){
            val vol = chunks[i].toInt(2)
            res += decode[vol]
        }

        return res
    }
    fun hashesPure(msg:String): String? {
        val int1 = 0
        val int2 = 0
        val int3 = 0
        val int4 = 0

        val rt22 = rt2s()
        val rt33 = rt3s()

        var message = ""
        for (i in 0 until msg.length) {
            message += String.format(
                "%08d",
                java.lang.Long.toBinaryString(msg[i].toLong()).toLong()
            ) //msg to binary
        }
        val msgLen = String.format(
            "%064d", java.lang.Long.toBinaryString(
                message.length.toLong()
            ).toLong()
        ) //msg length in binary
        val chunkno = chunkNo(message) //chuncks required //padding number
        message += "1"
        val padding = chunkno * 512 - (message.length + 64)
        for (i in 0 until padding) {
            message += "0"
        } //padding applied
        message += msgLen
        var min = 0
        var max = 512
        val chunks: ArrayList<String> = ArrayList()
        for (i in 0 until chunkno) {
            chunks.add(message.substring(min, max)) //split into chunks of 512bit
            min += 512
            max += 512
        }

        //process the message in successive 512bit chunks:
        for (i in 0 until chunkno) {
            val newMsg = chunks[i]
            min = 0
            max = 32
            val msgChunks: ArrayList<String> = ArrayList()
            for (j in 0..15) {
                msgChunks.add(newMsg.substring(min, max))
                min += 32
                max += 32
            }
            for (j in 16..63) { //
                val s0 = sig0(msgChunks[msgChunks.size - 15], int1, int2, int4)
                val s1 = sig1(msgChunks[msgChunks.size - 2], int1, int2, int4)
//                val s0 = sig0(msgChunks[msgChunks.size - 15])
//                val s1 = sig1(msgChunks[msgChunks.size - 2])
                val addup = adder(
                    msgChunks[msgChunks.size - 16],
                    s0,
                    msgChunks[msgChunks.size - 7],
                    s1
                )
                msgChunks.add(addup)
            }
            var a = rt22[0]
            var b = rt22[1]
            var c = rt22[2]
            var d = rt22[3]
            var e = rt22[4]
            var f = rt22[5]
            var g = rt22[6]
            var h = rt22[7]
            for (j in 0..63) {
                val S1 = sigma1(e, int1, int2, int3)
//                val S1 = sigma1(e)
                val ch = cho(e, f, g)
                val temp1 = addersz(
                    h.toLong(2) + S1.toLong(2) + ch.toLong(2) + rt33[j]
                        .toLong(2) + msgChunks[j].toLong(2)
                )
                val S0 = sigma0(a, int1, int2, int3)
//                val S0 = sigma0(a)
                val maj = mj(a, b, c)
                val temp2 = addersz(S0.toLong(2) + maj.toLong(2))
                h = g
                g = f
                f = e
                e = addersz(d.toLong(2) + temp1.toLong(2))
                d = c
                c = b
                b = a
                a = addersz(temp1.toLong(2) + temp2.toLong(2))
            }
            rt22[0] = addersz(rt22[0].toLong(2) + a.toLong(2))
            rt22[1] = addersz(rt22[1].toLong(2) + b.toLong(2))
            rt22[2] = addersz(rt22[2].toLong(2) + c.toLong(2))
            rt22[3] = addersz(rt22[3].toLong(2) + d.toLong(2))
            rt22[4] = addersz(rt22[4].toLong(2) + e.toLong(2))
            rt22[5] = addersz(rt22[5].toLong(2) + f.toLong(2))
            rt22[6] = addersz(rt22[6].toLong(2) + g.toLong(2))
            rt22[7] = addersz(rt22[7].toLong(2) + h.toLong(2))
        }
        var digest = ""
        for (j in 0..7) {
            digest += rt22[j]
        }
        return BigInteger(digest, 2).toString(16)
    }

    fun hashes(msg:String, int1:Int, int2:Int, int3:Int, int4:Int): String? {
        val rt22 = rt2s()
        val rt33 = rt3s()

        var message = ""
        for (i in 0 until msg.length) {
            message += String.format(
                "%08d",
                java.lang.Long.toBinaryString(msg[i].toLong()).toLong()
            ) //msg to binary
        }
        val msgLen = String.format(
            "%064d", java.lang.Long.toBinaryString(
                message.length.toLong()
            ).toLong()
        ) //msg length in binary
        val chunkno = chunkNo(message) //chuncks required //padding number
        message += "1"
        val padding = chunkno * 512 - (message.length + 64)
        for (i in 0 until padding) {
            message += "0"
        } //padding applied
        message += msgLen
        var min = 0
        var max = 512
        val chunks: ArrayList<String> = ArrayList()
        for (i in 0 until chunkno) {
            chunks.add(message.substring(min, max)) //split into chunks of 512bit
            min += 512
            max += 512
        }

        //process the message in successive 512bit chunks:
        for (i in 0 until chunkno) {
            val newMsg = chunks[i]
            min = 0
            max = 32
            val msgChunks: ArrayList<String> = ArrayList()
            for (j in 0..15) {
                msgChunks.add(newMsg.substring(min, max))
                min += 32
                max += 32
            }
            for (j in 16..63) { //
                val s0 = sig0(msgChunks[msgChunks.size - 15], int1, int2, int4)
                val s1 = sig1(msgChunks[msgChunks.size - 2], int1, int2, int4)
//                val s0 = sig0(msgChunks[msgChunks.size - 15])
//                val s1 = sig1(msgChunks[msgChunks.size - 2])
                val addup = adder(
                    msgChunks[msgChunks.size - 16],
                    s0,
                    msgChunks[msgChunks.size - 7],
                    s1
                )
                msgChunks.add(addup)
            }
            var a = rt22[0]
            var b = rt22[1]
            var c = rt22[2]
            var d = rt22[3]
            var e = rt22[4]
            var f = rt22[5]
            var g = rt22[6]
            var h = rt22[7]
            for (j in 0..63) {
                val S1 = sigma1(e, int1, int2, int3)
//                val S1 = sigma1(e)
                val ch = cho(e, f, g)
                val temp1 = addersz(
                    h.toLong(2) + S1.toLong(2) + ch.toLong(2) + rt33[j]
                        .toLong(2) + msgChunks[j].toLong(2)
                )
                val S0 = sigma0(a, int1, int2, int3)
//                val S0 = sigma0(a)
                val maj = mj(a, b, c)
                val temp2 = addersz(S0.toLong(2) + maj.toLong(2))
                h = g
                g = f
                f = e
                e = addersz(d.toLong(2) + temp1.toLong(2))
                d = c
                c = b
                b = a
                a = addersz(temp1.toLong(2) + temp2.toLong(2))
            }
            rt22[0] = addersz(rt22[0].toLong(2) + a.toLong(2))
            rt22[1] = addersz(rt22[1].toLong(2) + b.toLong(2))
            rt22[2] = addersz(rt22[2].toLong(2) + c.toLong(2))
            rt22[3] = addersz(rt22[3].toLong(2) + d.toLong(2))
            rt22[4] = addersz(rt22[4].toLong(2) + e.toLong(2))
            rt22[5] = addersz(rt22[5].toLong(2) + f.toLong(2))
            rt22[6] = addersz(rt22[6].toLong(2) + g.toLong(2))
            rt22[7] = addersz(rt22[7].toLong(2) + h.toLong(2))
        }
        var digest = ""
        for (j in 0..7) {
            digest += rt22[j]
        }
        return BigInteger(digest, 2).toString(16)
    }

    private fun rt2s(): ArrayList<String> {
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

    private fun rt3s(): ArrayList<String> {
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

    fun chunkNo(msg: String): Int {
        var chunks = 1
        var bsize = 447
        if (msg.length <= bsize) {
            chunks = 1
        } else if (msg.length >= bsize + 1) {
            while (true) {
                bsize += 512
                chunks += 1
                if (msg.length <= bsize) {
                    break
                }
            }
        }
        return chunks
    }

    //, int1:Int, int2:Int, int3:Int, int4:Int
    fun sig0(bits: String, int1:Int, int2:Int, int4:Int): String {
        val a = rotr(bits, 7+int1)
        val b = rotr(bits, 18-int2)
        val c = shr(bits, 3+int4)
        return xor(a, b, c)
    }

    fun sig1(bits: String, int1:Int, int2:Int, int4:Int): String {
        val a = rotr(bits, 17+int1)
        val b = rotr(bits, 19-int2)
        val c = shr(bits, 10+(int4/2).toInt())
        return xor(a, b, c)
    }

    fun sigma0(bits: String, int1:Int, int2:Int, int3:Int): String {
        var res = ""
        val a = rotr(bits, 2+int1)
        val b = rotr(bits, 13-int2)
        val c = rotr(bits, 22-(int3/2).toInt())
        res = xor(a, b, c)
        return res
    }

    fun sigma1(bits: String, int1:Int, int2:Int, int3:Int): String {
        var res = ""
        val a = rotr(bits, 6+int1)
        val b = rotr(bits, 11-(int2/2).toInt())
        val c = rotr(bits, 25-int3)
        res = xor(a, b, c)
        return res
    }

    fun xor(a: String, b: String, c: String): String {
        var res: String = ""
        for (i in 0 until a.length) {
            if ((a[i].digitToInt() xor b[i].digitToInt() xor c[i].digitToInt()) == 1) {
                res += "1"
            } else if ((a[i].digitToInt() xor b[i].digitToInt() xor c[i].digitToInt()) == 0) {
                res += "0"
            } else {
                println("____")
            }
        }
        return res
    }

    fun adder(a: String, b: String, c: String, d: String): String {

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
        return res
    }

    fun rotr(a: String, rotnumber: Int): String {
        var a = a
        for (i in 0 until rotnumber) {
            val last_char = a.substring(a.length - 1)
            a = a.substring(0, a.length - 1)
            a = last_char + a
        }
        return a
    }

    fun shr(a: String, rotnumber: Int): String {
        var a = a
        for (i in 0 until rotnumber) {
            a = a.substring(0, a.length - 1)
            a = "0$a"
        }
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
                //println("maj nothing")
            }
        }
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
        return msg
    }

    fun rmZeros(msg: String, newLen: Int): String {
        var msg = msg
        val target = msg.length - newLen
        msg = msg.substring(target, msg.length)
        if (msg.length != 32) {
            //println("remove zeroes: " + msg.length)
        }
        return msg
    }

}