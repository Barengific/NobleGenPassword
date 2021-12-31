package com.barengific.passwordgenerator.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.lifecycle.LiveData




@Dao
interface WordDao {
    @Query("SELECT * FROM word")
    fun getAll(): List<Word>

    @Query("SELECT * FROM word WHERE wid IN (:wordIds)")
    fun loadAllByIds(wordIds: IntArray): List<Word>

    @Query("SELECT * FROM word WHERE keyer LIKE :k")
    fun findByKey(k: String): Word

    @Query("SELECT * FROM word WHERE valuer LIKE :v")
    fun findByVal(v: String): Word

    @Query("SELECT * FROM word WHERE p_type LIKE :pType")
    fun findByType(pType: String): Word

    @Insert
    fun insertAll(vararg words: Word)
    //LiveData<Word>

    @Delete
    fun delete(word: Word)
}