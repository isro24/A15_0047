package com.example.uas.ui.view.kandang

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.R
import com.example.uas.ui.customwidget.CustomeTopAppBar
import com.example.uas.ui.customwidget.DropdownNamaHewanToKandang
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.viewmodel.PenyediaViewModel
import com.example.uas.ui.viewmodel.hewan.HomeUiState
import com.example.uas.ui.viewmodel.hewan.HomeViewModel
import com.example.uas.ui.viewmodel.kandang.FormErrorStateKandang
import com.example.uas.ui.viewmodel.kandang.InsertUiEventKandang
import com.example.uas.ui.viewmodel.kandang.InsertUiStateKandang
import com.example.uas.ui.viewmodel.kandang.InsertViewModelKandang
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DestinasiInsertKandang: DestinasiNavigasi {
    override val route = "item_entrykandang"
    override val titleRes = "Tambah Kandang"
}

@OptIn(ExperimentalMaterial3Api:: class)
@Composable
fun InsertViewKandang(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertViewModelKandang = viewModel(factory= PenyediaViewModel.Factory),
    viewModelHewan: HomeViewModel = viewModel(factory= PenyediaViewModel.Factory)
){
    val uiStateKandang = viewModel.uiStateKandang
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val listHewan = viewModelHewan.hwnUiState
    val snackbarHostState = remember { SnackbarHostState() }

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(Color.White)
    }

    LaunchedEffect(uiStateKandang.snackBarMessage) {
        uiStateKandang.snackBarMessage?.let { message ->
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
                title  = DestinasiInsertKandang.titleRes,
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
            EntryBodyKandang(
                insertUiStateKandang = viewModel.uiStateKandang,
                onKandangValueChange = viewModel::updateInsertKndState,
                listHewan = listHewan,
                onSaveClick = {
                    coroutineScope.launch {
                        if (viewModel.validateFields()){
                            viewModel.insertKnd()
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
                uiStateKandang = uiStateKandang,
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun EntryBodyKandang(
    uiStateKandang: InsertUiStateKandang,
    insertUiStateKandang: InsertUiStateKandang,
    onKandangValueChange: (InsertUiEventKandang) -> Unit,
    onSaveClick: () -> Unit,
    listHewan: HomeUiState,
    modifier: Modifier = Modifier,
    modeUpdate: Boolean = false
){
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        FormInput(
            insertUiEventKandang = insertUiStateKandang.insertUiEventKandang,
            onValueChange = onKandangValueChange,
            modifier = Modifier.fillMaxWidth(),
            listHewan = listHewan,
            errorState = uiStateKandang.isEntryValidKandang,
            modeUpdate = !modeUpdate
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
    insertUiEventKandang: InsertUiEventKandang,
    modifier: Modifier = Modifier,
    errorState: FormErrorStateKandang = FormErrorStateKandang(),
    onValueChange: (InsertUiEventKandang) -> Unit = {},
    enabled: Boolean = true,
    listHewan: HomeUiState,
    modeUpdate: Boolean = false
){
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ){
        val listNamaHewan = when (listHewan) {
            is HomeUiState.Success -> listHewan.hewan
            else -> emptyList()
        }

        val expanded by remember { mutableStateOf(false) }
        val selectedNamaHewan by remember { mutableStateOf("") }

        if(modeUpdate){
            OutlinedTextField(
                value = insertUiEventKandang.idKandang,
                onValueChange = {onValueChange(insertUiEventKandang.copy(idKandang = it))},
                label = { Text(text = "Id Kandang") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorState.idKandangError!=null,
                enabled = enabled,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.cageicon),
                        contentDescription = "Id Kandang",
                        modifier = Modifier.size(30.dp)
                    )
                }
            )
        }
        Text(
            text = errorState.idKandangError ?: "",
            color = Color.Red
        )
        DropdownNamaHewanToKandang(
            expanded = remember { mutableStateOf(expanded) },
            selectedNamaHewan = remember { mutableStateOf(selectedNamaHewan) },
            onValueChange = { idHewan -> onValueChange(insertUiEventKandang.copy(idHewan = idHewan))},
            listHewan = listNamaHewan
        )
        Text(
            text = errorState.idHewanError ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            value = insertUiEventKandang.kapasitas?.toString()?: "",
            onValueChange = {
                val intValue = it.toIntOrNull()
                onValueChange(insertUiEventKandang.copy(kapasitas = intValue))},
            label = { Text(text = "Kapasitas") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorState.kapasitasError!=null,
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.populasi),
                    contentDescription = "Kapasitas",
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        Text(
            text = errorState.kapasitasError ?: "",
            color = Color.Red
        )
        OutlinedTextField(
            value = insertUiEventKandang.lokasi,
            onValueChange = {onValueChange(insertUiEventKandang.copy(lokasi = it))},
            label = { Text(text = "Lokasi") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorState.lokasiError!=null,
            enabled = enabled,
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.zonawilayah),
                    contentDescription = "Zona WIlayah",
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        Text(
            text = errorState.lokasiError ?: "",
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