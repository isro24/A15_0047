package com.example.uas.ui.view.kandang

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.model.Hewan
import com.example.uas.ui.customwidget.CustomeTopAppBar
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.viewmodel.PenyediaViewModel
import com.example.uas.ui.viewmodel.kandang.InsertUiEventKandang
import com.example.uas.ui.viewmodel.kandang.InsertUiStateKandang
import com.example.uas.ui.viewmodel.kandang.InsertViewModelKandang
import kotlinx.coroutines.launch

object DestinasiInsertKandang: DestinasiNavigasi {
    override val route = "item_entrykandang"
    override val titleRes = "Tambah Kandang"
}

@OptIn(ExperimentalMaterial3Api:: class)
@Composable
fun InsertViewKandang(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertViewModelKandang = viewModel(factory= PenyediaViewModel.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomeTopAppBar(
                title  = DestinasiInsertKandang.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            EntryBodyKandang(
                insertUiStateKandang = viewModel.uiState,
                onKandangValueChange = viewModel::updateInsertKndState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.insertKnd()
                        navigateBack()
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun EntryBodyKandang(
    insertUiStateKandang: InsertUiStateKandang,
    onKandangValueChange: (InsertUiEventKandang) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        FormInput(
            insertUiEventKandang = insertUiStateKandang.insertUiEventKandang,
            onValueChange = onKandangValueChange,
            modifier = Modifier.fillMaxWidth()
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
    onValueChange: (InsertUiEventKandang) -> Unit = {},
    enabled: Boolean = true,
    listHewan: List<Hewan> = emptyList()
){
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        OutlinedTextField(
            value = insertUiEventKandang.idKandang,
            onValueChange = {onValueChange(insertUiEventKandang.copy(idKandang = it))},
            label = { Text(text = "Id Kandang") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEventKandang.idHewan?.toString()?:"",
            onValueChange = {
                val intValue = it.toIntOrNull()
                onValueChange(insertUiEventKandang.copy(idHewan = intValue))},
            label = { Text(text = "Id Hewan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEventKandang.kapasitas?.toString()?: "",
            onValueChange = {
                val intValue = it.toIntOrNull()
                onValueChange(insertUiEventKandang.copy(kapasitas = intValue))},
            label = { Text(text = "Kapasitas") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = insertUiEventKandang.lokasi,
            onValueChange = {onValueChange(insertUiEventKandang.copy(lokasi = it))},
            label = { Text(text = "Lokasi") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        if (enabled){
            Text(
                text = "Isi Semua Data!",
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}