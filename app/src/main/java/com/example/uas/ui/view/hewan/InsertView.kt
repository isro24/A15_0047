package com.example.uas.ui.view.hewan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.R
import com.example.uas.ui.customwidget.CustomeTopAppBar
import com.example.uas.ui.customwidget.DropDownTipePakan
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.viewmodel.PenyediaViewModel
import com.example.uas.ui.viewmodel.hewan.FormErrorState
import com.example.uas.ui.viewmodel.hewan.InsertUiEvent
import com.example.uas.ui.viewmodel.hewan.InsertUiState
import com.example.uas.ui.viewmodel.hewan.InsertViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DestinasiInsertHewan: DestinasiNavigasi {
    override val route = "item_entry"
    override val titleRes = "Tambah Hewan"
}

@OptIn(ExperimentalMaterial3Api:: class)
@Composable
fun InsertViewHewan(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertViewModel = viewModel(factory= PenyediaViewModel.Factory)
){
    val uiState = viewModel.uiState
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(Color.White)
    }

    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.resetSnackBarMessage()
            }
        }
    }

    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomeTopAppBar(
                title  = DestinasiInsertHewan.titleRes,
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
            EntryBody(
                insertUiState = viewModel.uiState,
                onHewanValueChange = viewModel::updateInsertHwnState,
                onSaveClick = {
                    coroutineScope.launch {
                        if (viewModel.validateFields()){
                            viewModel.insertHwn()
                            delay(600)
                            withContext(Dispatchers.Main){
                                navigateBack()
                            }
                        }else{
                            snackbarHostState.showSnackbar("Data tidak valid")
                        }
                    }
                },
                uiState = uiState,
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun EntryBody(
    uiState: InsertUiState,
    insertUiState: InsertUiState,
    onHewanValueChange: (InsertUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        FormInput(
            insertUiEvent = insertUiState.insertUiEvent,
            onValueChange = onHewanValueChange,
            modifier = Modifier.fillMaxWidth(),
            errorState = uiState.isEntryValid
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
    insertUiEvent: InsertUiEvent,
    modifier: Modifier = Modifier,
    errorState: FormErrorState = FormErrorState(),
    onValueChange: (InsertUiEvent) -> Unit = {},
    enabled: Boolean = true
){
    val tipePakan = listOf("Karnivora", "Herbivora", "Omnivora")

    var chosenDropdown by remember {
        mutableStateOf(insertUiEvent.tipePakan)
    }

    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ){
        OutlinedTextField(
            value = insertUiEvent.namaHewan,
            onValueChange = {onValueChange(insertUiEvent.copy(namaHewan = it))},
            label = { Text(text = "Nama Hewan") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorState.namaHewanError!= null,
            enabled = enabled,
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.lionicon),
                    contentDescription = "Nama Hewan",
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        Text(
            text = errorState.namaHewanError ?: "",
            color = Color.Red
        )
        DropDownTipePakan(
            selectedValue = chosenDropdown,
            options = tipePakan,
            label = "Tipe Pakan",
            onValueChangedEvent = {
                chosenDropdown = it
                onValueChange(insertUiEvent.copy(tipePakan = it))
            },
        )
        Text(
            text = errorState.tipePakanError ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            value = insertUiEvent.populasi?.toString()?: "",
            onValueChange = {
                val intValue = it.toIntOrNull()
                onValueChange(insertUiEvent.copy(populasi = intValue))},
            label = { Text(text = "Populasi") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorState.populasiError!= null,
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.populasi),
                    contentDescription = "Populasi",
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        Text(
            text = errorState.populasiError ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            value = insertUiEvent.zonaWilayah,
            onValueChange = {onValueChange(insertUiEvent.copy(zonaWilayah = it))},
            label = { Text(text = "Zona Wilayah") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorState.zonaWilayahError!= null,
            enabled = enabled,
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.zonawilayah),
                    contentDescription = "Zona Wilayah",
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        Text(
            text = errorState.zonaWilayahError ?: "",
            color = Color.Red
        )
        if (enabled){
            Text(
                text = "Isi Semua Data!",
                modifier = Modifier.padding(12.dp)
            )
        }
    }

}