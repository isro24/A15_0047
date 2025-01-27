package com.example.uas.ui.view.petugas

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
import com.example.uas.model.Petugas
import com.example.uas.ui.customwidget.CustomeTopAppBar
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.viewmodel.PenyediaViewModel
import com.example.uas.ui.viewmodel.petugas.DetailUiStatePetugas
import com.example.uas.ui.viewmodel.petugas.DetailViewModelPetugas
import com.example.uas.ui.viewmodel.petugas.toPtg
import com.google.accompanist.systemuicontroller.rememberSystemUiController

object DestinasiDetailPetugas : DestinasiNavigasi {
    override val route = "detailpetugas"
    override val titleRes = "Detail Petugas"
    const val IDPETUGAS = "id_petugas"
    val routeWithArgs = "$route/{$IDPETUGAS}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailViewPetugas(
    modifier: Modifier = Modifier,
    NavigateBack: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit = { },
    viewModel: DetailViewModelPetugas = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(Color.White)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomeTopAppBar(
                title = DestinasiDetailPetugas.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = { viewModel.handleNavigateBack(systemUiController, NavigateBack)},
                onRefresh = {
                    viewModel.getPetugasById()
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
                    contentDescription = "Edit Petugas"
                )
            }
        }
    ) { innerPadding ->
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        BodyDetailPtg(
            detailUiStatePetugas = viewModel.detailUiStatePetugas,
            modifier = Modifier.padding(innerPadding)
                .padding(horizontal = 20.dp, ),
            onDeleteClick = {
                deleteConfirmationRequired = true
            },
            retryAction = { viewModel.getPetugasById()
            }
        )

        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    viewModel.deletePtg()
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
fun BodyDetailPtg(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    detailUiStatePetugas: DetailUiStatePetugas,
    onDeleteClick: () -> Unit
) {
    when {
        detailUiStatePetugas.isLoading -> {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        detailUiStatePetugas.isError -> {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = detailUiStatePetugas.errorMessage,
                    color = Color.Red
                )
            }
        }
        detailUiStatePetugas.isUiEventNotEmpty -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ItemDetailPtg(
                    petugas = detailUiStatePetugas.detailUiEventPetugas.toPtg(),
                    modifier = modifier
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
fun ItemDetailPtg(
    modifier: Modifier = Modifier,
    petugas: Petugas,
){
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
            ComponentDetailPtg(judul = "Nama Petugas", isi = petugas.namaPetugas)
            Spacer(modifier = Modifier.padding(4.dp))
            ComponentDetailPtg(judul = "Jabatan", isi = petugas.jabatan)
        }
    }
}

@Composable
fun ComponentDetailPtg(
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