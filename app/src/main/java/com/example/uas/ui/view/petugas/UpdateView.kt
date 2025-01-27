package com.example.uas.ui.view.petugas

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.ui.customwidget.CustomeTopAppBar
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.viewmodel.PenyediaViewModel
import com.example.uas.ui.viewmodel.petugas.UpdateViewModelPetugas
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DestinasiUpdatePetugas : DestinasiNavigasi {
    override val route = "updatepetugas"
    override val titleRes = "Edit Petugas"
    const val IDPETUGAS = "id_petugas"
    val routeWithArgs = "$route/{$IDPETUGAS}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateViewPetugas(
    NavigateBack: () -> Unit,
    onNavigate:()-> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateViewModelPetugas = viewModel(factory = PenyediaViewModel.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    val uiStatePetugas = viewModel.updateUIStatePetugas
    val snackbarHostState = remember { SnackbarHostState() }

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(Color.White)
    }

    LaunchedEffect(uiStatePetugas.snackBarMessage) {
        uiStatePetugas.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Long
                )
                delay(600)
                viewModel.resetSnackBarMessage()
            }
        }
    }

    Scaffold (
        modifier = modifier,
        topBar = {
            CustomeTopAppBar(
                title = DestinasiUpdatePetugas.titleRes,
                canNavigateBack = true,
                navigateUp = { viewModel.handleNavigateBack(systemUiController, NavigateBack)}
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ){ padding ->
        EntryBodyPetugas(
            uiStatePetugas = uiStatePetugas,
            modifier = Modifier.padding(padding)
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .fillMaxWidth(),
            onPetugasValueChange = viewModel::updateInsertPtgState,
            insertUiStatePetugas = viewModel.updateUIStatePetugas,
            onSaveClick = {
                coroutineScope.launch {
                    if (viewModel.validateFields()){
                        viewModel.updateData()
                        delay(600)
                        withContext(Dispatchers.Main){
                            onNavigate()
                        }
                    } else{
                        snackbarHostState.showSnackbar("Data tidak valid")
                    }
                }
            }
        )
    }
}