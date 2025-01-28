package com.example.uas.ui.view.kandang

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
import com.example.uas.model.Kandang
import com.example.uas.ui.customwidget.CustomeTopAppBar
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.viewmodel.PenyediaViewModel
import com.example.uas.ui.viewmodel.hewan.HomeUiState
import com.example.uas.ui.viewmodel.hewan.HomeViewModel
import com.example.uas.ui.viewmodel.kandang.DetailUiStateKandang
import com.example.uas.ui.viewmodel.kandang.DetailViewModelKandang
import com.example.uas.ui.viewmodel.kandang.toKnd
import com.google.accompanist.systemuicontroller.rememberSystemUiController

object DestinasiDetailKandang : DestinasiNavigasi {
    override val route = "detailkandang"
    override val titleRes = "Detail Kandang"
    const val IDKANDANG = "id_kandang"
    val routeWithArgs = "$route/{$IDKANDANG}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailViewKandang(
    modifier: Modifier = Modifier,
    NavigateBack: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit = { },
    viewModel: DetailViewModelKandang = viewModel(factory = PenyediaViewModel.Factory),
    viewModelHewan: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val listHewan = viewModelHewan.hwnUiState

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(Color.White)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomeTopAppBar(
                title = DestinasiDetailKandang.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = { viewModel.handleNavigateBack(systemUiController, NavigateBack)},
                onRefresh = {
                    viewModel.getKandangById()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onEditClick,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Kandang"
                )
            }
        }
    ) { innerPadding ->
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        BodyDetailKnd(
            detailUiStateKandang = viewModel.detailUiStateKandang,
            modifier = Modifier.padding(innerPadding)
                .padding(horizontal = 20.dp, ),
            onDeleteClick = {
                deleteConfirmationRequired = true
            },
            retryAction = { viewModel.getKandangById()
            },
            listHewan = listHewan,
            )

        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    viewModel.deleteKnd()
                    systemUiController.setStatusBarColor(Color.Transparent)
                    onDeleteClick()
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
fun BodyDetailKnd(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    detailUiStateKandang: DetailUiStateKandang,
    listHewan: HomeUiState,
    onDeleteClick: () -> Unit
) {
    when {
        detailUiStateKandang.isLoading -> {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        detailUiStateKandang.isError -> {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = detailUiStateKandang.errorMessage,
                    color = Color.Red
                )
            }
        }
        detailUiStateKandang.isUiEventNotEmpty -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ItemDetailKnd(
                    kandang = detailUiStateKandang.detailUiEventKandang.toKnd(),
                    modifier = modifier,
                    listHewan = listHewan,
                    )

                Button(
                    onClick = onDeleteClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF22B14C)
                    )
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
                    )                }
            }
        }
    }
}

@Composable
fun ItemDetailKnd(
    modifier: Modifier = Modifier,
    kandang: Kandang,
    listHewan: HomeUiState
){
    val listNamaHewan = when (listHewan) {
        is HomeUiState.Success -> listHewan.hewan
        else -> emptyList()
    }
    val namaHewan = listNamaHewan.find { it.idHewan == kandang.idHewan }?.namaHewan ?: "Tidak Diketahui"

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
            ComponentDetailHwn(judul = "Id Kandang", isi = kandang.idKandang)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailHwn(judul = "Nama Hewan", isi = namaHewan)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailHwn(judul = "Kapasitas", isi = kandang.kapasitas.toString())
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailHwn(judul = "Lokasi", isi = kandang.lokasi)
        }
    }
}

@Composable
fun ComponentDetailHwn(
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
            text = isi,
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
