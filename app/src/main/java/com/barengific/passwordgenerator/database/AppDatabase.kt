package com.barengific.passwordgenerator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private lateinit var INSTANCE:AppDatabase
        private lateinit var context: Context
        fun getInstance(con: Context):AppDatabase= Room.databaseBuilder(
            con,
            AppDatabase::class.java,
            "word"
        )

            .createFromAsset("word.db")
            .allowMainThreadQueries()
            .build()

    }

    abstract fun wordDao(): WordDao

}