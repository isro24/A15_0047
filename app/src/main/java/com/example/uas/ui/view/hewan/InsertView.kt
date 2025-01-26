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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.uas.ui.viewmodel.hewan.InsertUiEvent
import com.example.uas.ui.viewmodel.hewan.InsertUiState
import com.example.uas.ui.viewmodel.hewan.InsertViewModel
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomeTopAppBar(
                title  = DestinasiInsertHewan.titleRes,
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
            EntryBody(
                insertUiState = viewModel.uiState,
                onHewanValueChange = viewModel::updateInsertHwnState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.insertHwn()
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
fun EntryBody(
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
    insertUiEvent: InsertUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertUiEvent) -> Unit = {},
    enabled: Boolean = true
){
    val tipePakan = listOf("Karnivora", "Herbivora", "Omnivora")

    var chosenDropdown by remember {
        mutableStateOf(insertUiEvent.tipePakan)
    }

    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        OutlinedTextField(
            value = insertUiEvent.namaHewan,
            onValueChange = {onValueChange(insertUiEvent.copy(namaHewan = it))},
            label = { Text(text = "Nama Hewan") },
            modifier = Modifier.fillMaxWidth(),
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
        DropDownTipePakan(
            selectedValue = chosenDropdown,
            options = tipePakan,
            label = "Tipe Pakan",
            onValueChangedEvent = {
                chosenDropdown = it
                onValueChange(insertUiEvent.copy(tipePakan = it))
            },
        )
        OutlinedTextField(
            value = insertUiEvent.populasi?.toString()?: "",
            onValueChange = {
                val intValue = it.toIntOrNull()
                onValueChange(insertUiEvent.copy(populasi = intValue))},
            label = { Text(text = "Populasi") },
            modifier = Modifier.fillMaxWidth(),
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
        OutlinedTextField(
            value = insertUiEvent.zonaWilayah,
            onValueChange = {onValueChange(insertUiEvent.copy(zonaWilayah = it))},
            label = { Text(text = "Zona Wilayah") },
            modifier = Modifier.fillMaxWidth(),
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
        if (enabled){
            Text(
                text = "Isi Semua Data!",
                modifier = Modifier.padding(12.dp)
            )
        }
    }

}