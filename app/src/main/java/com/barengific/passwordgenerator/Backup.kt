package com.barengific.passwordgenerator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.barengific.passwordgenerator.database.AppDatabase
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

class Backup : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.backup_activity)


        //db initialise
        val passphrase: ByteArray = SQLiteDatabase.getBytes("bob".toCharArray())//TODO change pass phrase
        val factory = SupportFactory(passphrase)
        val room = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-names")
            .openHelperFactory(factory)
            .allowMainThreadQueries()
            .build()
        val wordDao = room.wordDao()

        //recycler initialise
        val arr = wordDao.getAll()
        var adapter = CustomAdapter(arr)
        MainActivity.recyclerView = findViewById<View>(R.id.rview) as RecyclerView
        MainActivity.recyclerView.setHasFixedSize(false)
        MainActivity.recyclerView.setAdapter(adapter)
        MainActivity.recyclerView.setLayoutManager(LinearLayoutManager(this))

    }
}