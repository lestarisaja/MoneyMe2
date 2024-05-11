package org.d3if3114.assessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3114.assessment2.database.KeuanganDao
import org.d3if3114.assessment2.model.Keuangan

class DetailViewModel(private val dao: KeuanganDao) : ViewModel() {
    fun insert(jenis: String, keterangan: String, tanggal: String, nominal: String) {
        val keuangan = Keuangan(
            jenis = jenis,
            keterangan = keterangan,
            tanggal = tanggal,
            nominal = nominal
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(keuangan)
        }
    }
    suspend fun getKeuangan(id: Long): Keuangan? {
        return dao.getKeuanganById(id)
    }

    fun update(id: Long, jenis: String, keterangan: String, tanggal: String, nominal: String) {
        val keuangan = Keuangan(
            id = id,
            jenis = jenis,
            keterangan = keterangan,
            tanggal = tanggal,
            nominal = nominal
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(keuangan)
        }
    }
    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}