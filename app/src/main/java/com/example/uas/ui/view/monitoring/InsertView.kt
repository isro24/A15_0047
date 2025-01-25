package com.example.uas.ui.view.monitoring

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.ui.customwidget.CustomeTopAppBar
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.viewmodel.PenyediaViewModel
import com.example.uas.ui.viewmodel.monitoring.InsertUiEventMonitoring
import com.example.uas.ui.viewmodel.monitoring.InsertUiStateMonitoring
import com.example.uas.ui.viewmodel.monitoring.InsertViewModelMonitoring
import kotlinx.coroutines.launch

object DestinasiInsertMonitoring: DestinasiNavigasi {
    override val route = "item_entrymonitoring"
    override val titleRes = "Tambah Monitoring"
}

@OptIn(ExperimentalMaterial3Api:: class)
@Composable
fun InsertViewMonitoring(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertViewModelMonitoring = viewModel(factory= PenyediaViewModel.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomeTopAppBar(
                title  = DestinasiInsertMonitoring.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            EntryBodyMonitoring(
                insertUiStateMonitoring = viewModel.uiState,
                onMonitoringValueChange = viewModel::updateInsertMntState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.insertMnt()
                        navigateBack()
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun EntryBodyMonitoring(
    insertUiStateMonitoring: InsertUiStateMonitoring,
    onMonitoringValueChange: (InsertUiEventMonitoring) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        FormInput(
            insertUiEventMonitoring = insertUiStateMonitoring.insertUiEventMonitoring,
            onValueChange = onMonitoringValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF22B14C)
            )
        ){
            Text(text = "Simpan")
        }
    }
}

@Composable
fun FormInput(
    insertUiEventMonitoring: InsertUiEventMonitoring,
    modifier: Modifier = Modifier,
    onValueChange: (InsertUiEventMonitoring) -> Unit = {},
    enabled: Boolean = true
){
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        OutlinedTextField(
            value = insertUiEventMonitoring.idPetugas?.toString()?:"",
            onValueChange = {
                val intValue = it.toIntOrNull()
                onValueChange(insertUiEventMonitoring.copy(idPetugas = intValue))},
            label = { Text(text = "Id Petugas") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEventMonitoring.idKandang,
            onValueChange = {onValueChange(insertUiEventMonitoring.copy(idKandang = it))},
            label = { Text(text = "Id Kandang") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEventMonitoring.tanggalMonitoring,
            onValueChange = {onValueChange(insertUiEventMonitoring.copy(tanggalMonitoring = it))},
            label = { Text(text = "Tanggal Monitoring") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEventMonitoring.hewanSakit?.toString() ?: "",
            onValueChange = {
                val intValue = it.toIntOrNull()
                onValueChange(insertUiEventMonitoring.copy(hewanSakit = intValue))
            },
            label = { Text(text = "Hewan Sakit") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = insertUiEventMonitoring.hewanSehat?.toString() ?: "",
            onValueChange = {
                val intValue = it.toIntOrNull()
                onValueChange(insertUiEventMonitoring.copy(hewanSehat = intValue))
            },
            label = { Text(text = "Hewan Sehat") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(
            text = "Status: ${insertUiEventMonitoring.status}" ,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
            )
        if (enabled){
            Text(
                text = "Isi Semua Data!",
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}