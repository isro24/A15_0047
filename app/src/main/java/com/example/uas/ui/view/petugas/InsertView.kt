package com.example.uas.ui.view.petugas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.R
import com.example.uas.ui.customwidget.CustomeTopAppBar
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.viewmodel.PenyediaViewModel
import com.example.uas.ui.viewmodel.petugas.FormErrorStatePetugas
import com.example.uas.ui.viewmodel.petugas.InsertUiEventPetugas
import com.example.uas.ui.viewmodel.petugas.InsertUiStatePetugas
import com.example.uas.ui.viewmodel.petugas.InsertViewModelPetugas
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DestinasiInsertPetugas: DestinasiNavigasi {
    override val route = "item_entrypetugas"
    override val titleRes = "Tambah Petugas"
}

@OptIn(ExperimentalMaterial3Api:: class)
@Composable
fun InsertViewPetugas(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertViewModelPetugas = viewModel(factory= PenyediaViewModel.Factory)
){
    val uiStatePetugas = viewModel.uiStatePetugas
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(Color.White)
    }

    LaunchedEffect(uiStatePetugas.snackBarMessage) {
        uiStatePetugas.snackBarMessage?.let { message ->
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
                title  = DestinasiInsertPetugas.titleRes,
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
            EntryBodyPetugas(
                insertUiStatePetugas = viewModel.uiStatePetugas,
                onPetugasValueChange = viewModel::updateInsertPtgState,
                onSaveClick = {
                    coroutineScope.launch {
                        if (viewModel.validateFields()){
                            viewModel.insertPtg()
                            delay(600)
                            withContext(Dispatchers.Main){
                                navigateBack()
                            }
                        }else{
                            snackbarHostState.showSnackbar("Data tidak valid")
                        }
                    }
                },
                uiStatePetugas = uiStatePetugas,
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun EntryBodyPetugas(
    uiStatePetugas: InsertUiStatePetugas,
    insertUiStatePetugas: InsertUiStatePetugas,
    onPetugasValueChange: (InsertUiEventPetugas) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        FormInput(
            insertUiEventPetugas = insertUiStatePetugas.insertUiEventPetugas,
            onValueChange = onPetugasValueChange,
            modifier = Modifier.fillMaxWidth(),
            errorStatePetugas = uiStatePetugas.isEntryValidPetugas
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
    insertUiEventPetugas: InsertUiEventPetugas,
    modifier: Modifier = Modifier,
    onValueChange: (InsertUiEventPetugas) -> Unit = {},
    errorStatePetugas: FormErrorStatePetugas = FormErrorStatePetugas(),
    enabled: Boolean = true
){
    val jabatanList = listOf("Keeper", "Dokter Hewan", "Kurator")
    var pilihanJabatan by remember { mutableStateOf(insertUiEventPetugas.jabatan) }

    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ){
        OutlinedTextField(
            value = insertUiEventPetugas.namaPetugas,
            onValueChange = {onValueChange(insertUiEventPetugas.copy(namaPetugas = it))},
            label = { Text(text = "Nama Petugas") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorStatePetugas.namaPetugasError!=null,
            enabled = enabled,
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.officericon),
                    contentDescription = "Nama Petugas",
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        Text(
            text = errorStatePetugas.namaPetugasError ?: "",
            color = Color.Red
        )
        Text(text = "Jabatan", fontWeight = FontWeight.Bold)

        jabatanList.forEach { jabatan ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = pilihanJabatan == jabatan,
                    onClick = {
                        pilihanJabatan = jabatan
                        onValueChange(insertUiEventPetugas.copy(jabatan = jabatan))
                    },
                )
                Text(
                    text = jabatan,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }
        Text(
            text = errorStatePetugas.jabatanError ?: "",
            color = Color.Red
        )

        if (enabled){
            Text(
                text = "Isi Semua Data!",
                modifier = Modifier.padding(5.dp)
            )
        }
    }

}