package com.example.uas.ui.view.monitoring

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.model.Monitoring
import com.example.uas.ui.customwidget.CustomeTopAppBar
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.viewmodel.PenyediaViewModel
import com.example.uas.ui.viewmodel.hewan.HomeUiState
import com.example.uas.ui.viewmodel.hewan.HomeViewModel
import com.example.uas.ui.viewmodel.kandang.HomeUiStateKandang
import com.example.uas.ui.viewmodel.kandang.HomeViewModelKandang
import com.example.uas.ui.viewmodel.monitoring.DetailUiStateMonitoring
import com.example.uas.ui.viewmodel.monitoring.DetailViewModelMonitoring
import com.example.uas.ui.viewmodel.monitoring.toMnt
import com.example.uas.ui.viewmodel.petugas.HomeUiStatePetugas
import com.example.uas.ui.viewmodel.petugas.HomeViewModelPetugas
import com.google.accompanist.systemuicontroller.rememberSystemUiController

object DestinasiDetailMonitoring : DestinasiNavigasi {
    override val route = "detailmonitoring"
    override val titleRes = "Detail Monitoring"
    const val IDMONITORING = "id_petugas"
    val routeWithArgs = "$route/{$IDMONITORING}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailViewMonitoring(
    modifier: Modifier = Modifier,
    NavigateBack: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit = { },
    viewModel: DetailViewModelMonitoring = viewModel(factory = PenyediaViewModel.Factory),
    viewModelPetugas: HomeViewModelPetugas = viewModel(factory= PenyediaViewModel.Factory),
    viewModelKandang: HomeViewModelKandang = viewModel(factory= PenyediaViewModel.Factory),
    viewModelHewan: HomeViewModel = viewModel(factory= PenyediaViewModel.Factory),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val listPetugas = viewModelPetugas.ptgUiState
    val listKandang = viewModelKandang.kndUiState
    val listHewan = viewModelHewan.hwnUiState

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(Color.White)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomeTopAppBar(
                title = DestinasiDetailMonitoring.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = { viewModel.handleNavigateBack(systemUiController, NavigateBack)},
                onRefresh = {
                    viewModel.getMonitoringById()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onEditClick,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Monitoring"
                )
            }
        }
    ) { innerPadding ->
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        BodyDetailMnt(
            detailUiStateMonitoring = viewModel.detailUiStateMonitoring,
            modifier = Modifier.padding(innerPadding)
                .padding(horizontal = 20.dp),
            onDeleteClick = {
                deleteConfirmationRequired = true
            },
            retryAction = { viewModel.getMonitoringById()
            },
            listPetugas = listPetugas,
            listKandang = listKandang,
            listHewan = listHewan,
        )
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    viewModel.deleteMnt()
                    onDeleteClick()
                    systemUiController.setStatusBarColor(Color.Transparent)
                    deleteConfirmationRequired = false
                },
                onDeleteCancel = {
                    deleteConfirmationRequired = false
                },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


@Composable
fun BodyDetailMnt(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    detailUiStateMonitoring: DetailUiStateMonitoring,
    listPetugas: HomeUiStatePetugas,
    listKandang: HomeUiStateKandang,
    listHewan: HomeUiState,
    onDeleteClick: () -> Unit
) {
    when {
        detailUiStateMonitoring.isLoading -> {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        detailUiStateMonitoring.isError -> {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = detailUiStateMonitoring.errorMessage,
                    color = Color.Red
                )
            }
        }
        detailUiStateMonitoring.isUiEventNotEmpty -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ItemDetailMnt(
                    monitoring = detailUiStateMonitoring.detailUiEventMonitoring.toMnt(),
                    modifier = modifier,
                    listPetugas = listPetugas,
                    listKandang = listKandang,
                    listHewan = listHewan
                )
                Button(
                    onClick = onDeleteClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF22B14C))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.padding(end = 8.dp),
                        tint = Color.White
                    )
                    Text(
                        text = "Delete",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ItemDetailMnt(
    modifier: Modifier = Modifier,
    monitoring: Monitoring,
    listPetugas: HomeUiStatePetugas,
    listKandang: HomeUiStateKandang,
    listHewan: HomeUiState,
){
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
    val namaPetugas = listNamaPetugas.find { it.idPetugas == monitoring.idPetugas }?.namaPetugas ?: "Tidak Diketahui"
    val idKandang = listIdKandang.find { it.idKandang == monitoring.idKandang }
    val namaHewan = listNamaHewan.find { it.idHewan == idKandang?.idHewan }?.namaHewan ?: "Tidak Diketahui"

    val dataKandang = idKandang?.let { kandang ->
        "${kandang.idKandang} - $namaHewan"
    } ?: "Data tidak tersedia"

    Card(
        modifier = modifier.fillMaxWidth().padding(top = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = Color.Black
        )
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            ComponentDetailMnt(judul = "Nama Petugas", isi = namaPetugas)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailMnt(judul = "Data Kandang", isi = dataKandang)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailMnt(judul = "Tanggal Monitoring", isi = monitoring.tanggalMonitoring)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailMnt(judul = "Hewan Sakit", isi = monitoring.hewanSakit.toString())
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailMnt(judul = "Hewan Sehat", isi = monitoring.hewanSehat.toString())
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailMnt(judul = "Status", isi = monitoring.status)
        }
    }
}

@Composable
fun ComponentDetailMnt(
    modifier: Modifier = Modifier,
    judul:String,
    isi:String
){
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$judul : ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = if (judul == "Tanggal Monitoring") formatDateTime(isi) else isi,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(onDismissRequest = { /*Do nothing*/ },
        title = { Text("Delete Data") },
        text = { Text("Apakah anda yakin ingin menghapus data?") },
        dismissButton = {
            TextButton(onClick = { onDeleteCancel() }) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = { onDeleteConfirm() }) {
                Text(text = "Yes")
            }
        }
    )
}