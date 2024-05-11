package org.d3if3114.assessment2.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

import org.d3if3114.assessment2.database.KeuanganDb
import org.d3if3114.assessment2.ui.theme.Assessment2Theme
import org.d3if3114.assessment2.util.ViewModelFactory
import org.d3if3114.assessment2.R

const val KEY_ID_KEUANGAN = "idKeuangan"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = KeuanganDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)

    val viewModel: DetailViewModel = viewModel(factory = factory)

    var jenis by remember { mutableStateOf("Pemasukan") }
    var keterangan by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    var nominal by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getKeuangan(id) ?: return@LaunchedEffect
        if (data != null) {
            jenis = data.jenis
            keterangan = data.keterangan
            tanggal = data.tanggal
            nominal = data.nominal

        } else {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.atur_keuangan))
                    else
                        Text(text = stringResource(id = R.string.edit_keuangan))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        if (jenis == "" || keterangan == "" || tanggal == "" || nominal == "") {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }
                        if (id == null) {
                            viewModel.insert(jenis, keterangan, tanggal, nominal)
                        } else {
                            viewModel.update(id, jenis, keterangan, tanggal, nominal)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null) {
                        DeleteAction { showDialog = true }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
//                .verticalScroll(rememberScrollState())
        ) {
            FormKeuangan(
                jenis = jenis,
                onJenisChange = { jenis = it },
                keterangan = keterangan,
                onKeteranganChange = { keterangan = it },
                tanggal = tanggal,
                onTanggalChange = { tanggal = it },
                nominal = nominal,
                onNominalChange = { nominal = it },
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = {expanded = true}) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.hapus))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}
@Composable
fun FormKeuangan(
    jenis: String, onJenisChange: (String) -> Unit,
    keterangan: String, onKeteranganChange: (String) -> Unit,
    tanggal: String, onTanggalChange: (String) -> Unit,
    nominal: String, onNominalChange: (String) -> Unit,
    modifier: Modifier
) {
    val radioOptions = listOf(
        stringResource(id = R.string.pemasukan),
        stringResource(id = R.string.pengeluaran)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Image(
            painter = painterResource(id = R.drawable.uang),
            contentDescription = "Uang",
            modifier = Modifier.size(170.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            radioOptions.forEach { text ->
                ClassOption(
                    label = text,
                    isSelected = jenis == text,
                    onOptionSelected = { onJenisChange(it) },
                    modifier = Modifier
                        .selectable(
                            selected = jenis == text,
                            onClick = { onJenisChange(text) },
                            role = Role.RadioButton
                        )

                )
            }
        }
        OutlinedTextField(
            value = keterangan,
            onValueChange = { onKeteranganChange(it) },
            label = { Text(text = stringResource(R.string.keterangan)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = tanggal,
            onValueChange = { onTanggalChange(it) },
            label = { Text(text = stringResource(R.string.tanggal)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = nominal,
            onValueChange = { onNominalChange(it) },
            label = { Text(text = stringResource(R.string.nominal)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ClassOption(label: String, isSelected: Boolean, onOptionSelected: (String) -> Unit, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = { onOptionSelected(label) })
        Text(
            text = label,
            style = if (isSelected) MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ) else MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 1.dp)
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    Assessment2Theme {
        DetailScreen(rememberNavController())
    }
}