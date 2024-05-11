package org.d3if3114.assessment2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3114.assessment2.model.Keuangan

@Dao
interface KeuanganDao {
    @Insert
    suspend fun insert(keuangan: Keuangan)
    @Update
    suspend fun update(keuangan: Keuangan)
    @Query("SELECT * FROM keuangan ORDER BY jenis DESC")
    fun getKeuangan(): Flow<List<Keuangan>>
    @Query("SELECT * FROM keuangan WHERE id = :id")
    suspend fun getKeuanganById(id: Long): Keuangan?
    @Query("DELETE FROM keuangan WHERE id = :id")
    suspend fun deleteById(id: Long)
}