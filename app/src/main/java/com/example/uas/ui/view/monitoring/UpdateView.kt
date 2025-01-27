package com.example.uas.ui.view.monitoring

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
import com.example.uas.model.Hewan
import com.example.uas.model.Kandang
import com.example.uas.model.Petugas
import com.example.uas.ui.customwidget.CustomeTopAppBar
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.viewmodel.PenyediaViewModel
import com.example.uas.ui.viewmodel.monitoring.UpdateViewModelMonitoring
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DestinasiUpdateMonitoring : DestinasiNavigasi {
    override val route = "updatemonitoring"
    override val titleRes = "Edit Monitoring"
    const val IDMONITORING = "id_Monitoring"
    val routeWithArgs = "$route/{$IDMONITORING}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateViewMonitoring(
    NavigateBack: () -> Unit,
    onNavigate:()-> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateViewModelMonitoring = viewModel(factory = PenyediaViewModel.Factory),
    listPetugas: List<Petugas> = emptyList(),
    listKandang: List<Kandang> = emptyList(),
    listHewan: List<Hewan> = emptyList(),
){
    val coroutineScope = rememberCoroutineScope()
    val uiStateMonitoring = viewModel.updateUIStateMonitoring
    val snackbarHostState = remember { SnackbarHostState() }

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(Color.White)
    }

    LaunchedEffect(uiStateMonitoring.snackBarMessage) {
        uiStateMonitoring.snackBarMessage?.let { message ->
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
                title = DestinasiUpdateMonitoring.titleRes,
                canNavigateBack = true,
                navigateUp = { viewModel.handleNavigateBack(systemUiController, NavigateBack)}
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ){ padding ->
        EntryBodyMonitoring(
            uiStateMonitoring = uiStateMonitoring,
            modifier = Modifier.padding(padding)
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .fillMaxWidth(),
            onMonitoringValueChange = viewModel::updateInsertMntState,
            insertUiStateMonitoring = viewModel.updateUIStateMonitoring,
            listPetugas = listPetugas,
            listKandang = listKandang,
            listHewan = listHewan,
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