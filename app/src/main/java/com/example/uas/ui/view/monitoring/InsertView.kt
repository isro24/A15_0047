package com.example.uas.ui.view.monitoring

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.R
import com.example.uas.ui.customwidget.CustomeTopAppBar
import com.example.uas.ui.customwidget.DropDownKandangToMonitoring
import com.example.uas.ui.customwidget.DropDownNamaPetugasToMonitoring
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.viewmodel.PenyediaViewModel
import com.example.uas.ui.viewmodel.hewan.HomeUiState
import com.example.uas.ui.viewmodel.hewan.HomeViewModel
import com.example.uas.ui.viewmodel.kandang.HomeUiStateKandang
import com.example.uas.ui.viewmodel.kandang.HomeViewModelKandang
import com.example.uas.ui.viewmodel.monitoring.FormErrorStateMonitoring
import com.example.uas.ui.viewmodel.monitoring.InsertUiEventMonitoring
import com.example.uas.ui.viewmodel.monitoring.InsertUiStateMonitoring
import com.example.uas.ui.viewmodel.monitoring.InsertViewModelMonitoring
import com.example.uas.ui.viewmodel.petugas.HomeUiStatePetugas
import com.example.uas.ui.viewmodel.petugas.HomeViewModelPetugas
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DestinasiInsertMonitoring: DestinasiNavigasi {
    override val route = "item_entrymonitoring"
    override val titleRes = "Tambah Monitoring"
}

@OptIn(ExperimentalMaterial3Api:: class)
@Composable
fun InsertViewMonitoring(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertViewModelMonitoring = viewModel(factory= PenyediaViewModel.Factory),
    viewModelPetugas: HomeViewModelPetugas = viewModel(factory= PenyediaViewModel.Factory),
    viewModelKandang: HomeViewModelKandang = viewModel(factory= PenyediaViewModel.Factory),
    viewModelHewan: HomeViewModel = viewModel(factory= PenyediaViewModel.Factory),
){
    val uiStateMonitoring = viewModel.uiStateMonitoring
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(Color.White)
    }

    LaunchedEffect(uiStateMonitoring.snackBarMessage) {
        uiStateMonitoring.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.resetSnackBarMessage()
            }
        }
    }

    val listPetugas = viewModelPetugas.ptgUiState
    val listKandang = viewModelKandang.kndUiState
    val listHewan = viewModelHewan.hwnUiState

    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomeTopAppBar(
                title  = DestinasiInsertMonitoring.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = { viewModel.handleNavigateBack(systemUiController, navigateBack)}
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            EntryBodyMonitoring(
                insertUiStateMonitoring = viewModel.uiStateMonitoring,
                onMonitoringValueChange = viewModel::updateInsertMntState,
                listPetugas = listPetugas,
                listKandang = listKandang,
                listHewan = listHewan,
                onSaveClick = {
                    coroutineScope.launch {
                        if (viewModel.validateFields()){
                            viewModel.insertMnt()
                            delay(600)
                            withContext(Dispatchers.Main){
                                navigateBack()
                            }
                            systemUiController.setStatusBarColor(Color.Transparent)
                        }else{
                            snackbarHostState.showSnackbar("Data tidak valid")
                        }
                    }
                },
                uiStateMonitoring = uiStateMonitoring,
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun EntryBodyMonitoring(
    uiStateMonitoring: InsertUiStateMonitoring,
    insertUiStateMonitoring: InsertUiStateMonitoring,
    onMonitoringValueChange: (InsertUiEventMonitoring) -> Unit,
    onSaveClick: () -> Unit,
    listPetugas: HomeUiStatePetugas,
    listKandang: HomeUiStateKandang,
    listHewan: HomeUiState,
    modifier: Modifier = Modifier
){
    val scrollState = rememberScrollState()

    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
            .verticalScroll(scrollState)
    ){
        FormInput(
            insertUiEventMonitoring = insertUiStateMonitoring.insertUiEventMonitoring,
            onValueChange = onMonitoringValueChange,
            modifier = Modifier.fillMaxWidth(),
            errorStateMonitoring = uiStateMonitoring.isEntryValidMonitoring,
            listPetugas = listPetugas,
            listKandang = listKandang,
            listHewan = listHewan
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
    enabled: Boolean = true,
    errorStateMonitoring: FormErrorStateMonitoring = FormErrorStateMonitoring(),
    listPetugas: HomeUiStatePetugas,
    listKandang: HomeUiStateKandang,
    listHewan: HomeUiState,
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        val listNamaPetugas = when (listPetugas) {
            is HomeUiStatePetugas.Success -> listPetugas.petugas
            else -> emptyList()
        }
        val listIdKandang = when (listKandang) {
            is HomeUiStateKandang.Success -> listKandang.kandang
            else -> emptyList()
        }
        val listNamaHewan = when (listHewan) {
            is HomeUiState.Success -> listHewan.hewan
            else -> emptyList()
        }

        val expanded by remember { mutableStateOf(false) }
        val selectedNamaPetugas by remember { mutableStateOf("") }
        val selectedKandang by remember { mutableStateOf("") }
        val selectedHewan by remember { mutableStateOf("") }

        DropDownNamaPetugasToMonitoring(
            expanded = remember { mutableStateOf(expanded) },
            selectedNamaPetugas = remember { mutableStateOf(selectedNamaPetugas) },
            onValueChange = { idPetugas -> onValueChange(insertUiEventMonitoring.copy(idPetugas = idPetugas))},
            listPetugas = listNamaPetugas.filter { it.jabatan == "Dokter Hewan" }
        )
        Text(
            text = errorStateMonitoring.idPetugasError ?: "",
            color = Color.Red
        )

        DropDownKandangToMonitoring(
            expanded = remember { mutableStateOf(expanded) },
            selectedKandang = remember { mutableStateOf(selectedKandang) },
            selectedHewan = remember { mutableStateOf(selectedHewan) },
            onValueChange = { idKandang -> onValueChange(insertUiEventMonitoring.copy(idKandang = idKandang)) },
            listKandang = listIdKandang,
            listHewan = listNamaHewan
        )
        Text(
            text = errorStateMonitoring.idKandangError ?: "",
            color = Color.Red
        )

        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = { Text(text = "Pilih Tanggal") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            isError = errorStateMonitoring.tanggalMonitoringError!=null,
            enabled = false,
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Tanggal Monitoring"
                )
            }
        )
        Text(
            text = errorStateMonitoring.tanggalMonitoringError ?: "",
            color = Color.Red
        )

        if (showDatePicker) {
            val datePicker = DatePickerDialog(
                context,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(calendar.time)
                    showDatePicker = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        OutlinedTextField(
            value = selectedTime,
            onValueChange = {},
            label = { Text(text = "Pilih Jam") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showTimePicker = true },
            isError = errorStateMonitoring.tanggalMonitoringError!=null,
            enabled = false,
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.jam),
                    contentDescription = "Jam Monitoring",
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        Text(
            text = errorStateMonitoring.tanggalMonitoringError ?: "",
            color = Color.Red
        )

        if (showTimePicker) {
            val timePicker = TimePickerDialog(
                context,
                { _, hour, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    selectedTime = SimpleDateFormat("HH:mm", Locale.getDefault())
                        .format(calendar.time)
                    showTimePicker = false

                    if (selectedDate.isNotEmpty()) {
                        val combinedDateTime = "$selectedDate $selectedTime"
                        onValueChange(insertUiEventMonitoring.copy(tanggalMonitoring = combinedDateTime))
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }

        OutlinedTextField(
            value = insertUiEventMonitoring.hewanSakit?.toString() ?: "",
            onValueChange = {
                val intValue = it.toIntOrNull()
                onValueChange(insertUiEventMonitoring.copy(hewanSakit = intValue))
            },
            label = { Text(text = "Hewan Sakit") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorStateMonitoring.hewanSakitError!=null,
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.sakit),
                    contentDescription = "Hewan Sakit",
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        Text(
            text = errorStateMonitoring.hewanSakitError ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            value = insertUiEventMonitoring.hewanSehat?.toString() ?: "",
            onValueChange = {
                val intValue = it.toIntOrNull()
                onValueChange(insertUiEventMonitoring.copy(hewanSehat = intValue))
            },
            label = { Text(text = "Hewan Sehat") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorStateMonitoring.hewanSehatError!=null,
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.sehat),
                    contentDescription = "Hewan Sehat",
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        Text(
            text = errorStateMonitoring.hewanSehatError ?: "",
            color = Color.Red
        )
        Text(
            text = "Status: ${insertUiEventMonitoring.status}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        if (enabled) {
            Text(
                text = "Isi Semua Data!",
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}
