package org.d3if3114.assessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if3114.assessment2.database.KeuanganDao
import org.d3if3114.assessment2.model.Keuangan

class MainViewModel(dao: KeuanganDao) : ViewModel() {
    val data: StateFlow<List<Keuangan>> = dao.getKeuangan().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
    private fun getDataDummy(): List<Keuangan> {
        val data = mutableListOf<Keuangan>()
        data.add(
            Keuangan(
                id = 1,
                "Pengeluaran",
                "Makan Seblak",
                "10-05-2024",
                "50.000"
            )
        )

        data.add(
            Keuangan(
                id = 2,
                "Pengeluaran",
                "Renang",
                "12-05-2024",
                "200.000"
            )
        )

        data.add(
            Keuangan(
                id = 3,
                "Pemasukan",
                "Uang Saku",
                "12-05-2024",
                "5.000.000"
            )
        )
        return data
    }
}