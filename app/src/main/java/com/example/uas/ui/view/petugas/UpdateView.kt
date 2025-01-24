package com.example.uas.ui.view.petugas

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.ui.customwidget.CustomeTopAppBar
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.viewmodel.PenyediaViewModel
import com.example.uas.ui.viewmodel.petugas.UpdateViewModelPetugas
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

    Scaffold (
        modifier = modifier,
        topBar = {
            CustomeTopAppBar(
                title = DestinasiUpdatePetugas.titleRes,
                canNavigateBack = true,
                navigateUp = NavigateBack,
            )
        }
    ){ padding ->
        EntryBodyPetugas(
            modifier = Modifier.padding(padding)
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .fillMaxWidth(),
            onPetugasValueChange = viewModel::updateInsertPtgState,
            insertUiStatePetugas = viewModel.updateUIStatePetugas,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateData()
                    delay(600)
                    withContext(Dispatchers.Main){
                        onNavigate()
                    }
                }
            }
        )
    }
}