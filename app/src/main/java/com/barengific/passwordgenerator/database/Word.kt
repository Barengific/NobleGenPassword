package com.barengific.passwordgenerator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "p_type") val pType: String?,
    @ColumnInfo(name = "key") val key: String?,
    @ColumnInfo(name = "value") val value: String?
)