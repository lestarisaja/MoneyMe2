package org.d3if3114.assessment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keuangan")
data class Keuangan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val jenis: String,
    val keterangan: String,
    val tanggal: String,
    val nominal: String
)
