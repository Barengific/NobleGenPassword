package com.barengific.passwordgenerator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    @PrimaryKey(autoGenerate = true) val wid: Int = 0,
    @ColumnInfo(name = "p_type") val pType: String?,
    @ColumnInfo(name = "keyer") val key: String?,
    @ColumnInfo(name = "valuer") val value: String?
)