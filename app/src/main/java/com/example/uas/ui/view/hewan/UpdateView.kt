package com.example.uas.ui.view.hewan

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
import com.example.uas.ui.viewmodel.hewan.UpdateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DestinasiUpdateHewan : DestinasiNavigasi {
    override val route = "update"
    override val titleRes = "Edit Hewan"
    const val IDHEWAN = "id_hewan"
    val routeWithArgs = "$route/{$IDHEWAN}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateViewHewan(
    NavigateBack: () -> Unit,
    onNavigate:()-> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val coroutineScope = rememberCoroutineScope()

    Scaffold (
        modifier = modifier,
        topBar = {
            CustomeTopAppBar(
                title = DestinasiUpdateHewan.titleRes,
                canNavigateBack = true,
                navigateUp = NavigateBack,
            )
        }
    ){ padding ->
        EntryBody(
            modifier = Modifier.padding(padding)
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .fillMaxWidth(),
            onSiswaValueChange = viewModel::updateInsertHwnState,
            insertUiState = viewModel.updateUIState,
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