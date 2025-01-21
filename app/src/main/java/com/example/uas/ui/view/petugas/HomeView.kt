package com.example.uas.ui.view.petugas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.R
import com.example.uas.model.Petugas
import com.example.uas.ui.customwidget.CustomeTopAppBar
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.viewmodel.PenyediaViewModel
import com.example.uas.ui.viewmodel.petugas.HomeUiStatePetugas
import com.example.uas.ui.viewmodel.petugas.HomeViewModelPetugas

object DestinasiHomePetugas : DestinasiNavigasi {
    override val route = "homepetugas"
    override val titleRes = "Home Petugas"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeViewPetugas(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    viewModel: HomeViewModelPetugas = viewModel(factory = PenyediaViewModel.Factory)
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var search by remember { mutableStateOf("") }

    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomeTopAppBar(
                title = DestinasiHomePetugas.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = {
                    viewModel.getPtg()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Petugas"
                )
            }
        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SearchBar(
                query = search,
                onQueryChanged = {
                    search = it
                }
            )
            HomeStatusPetugas(
                homeUiStatePetugas = viewModel.ptgUiState,
                retryAction = {viewModel.getPtg()},
                modifier = Modifier.fillMaxWidth(),
                onDetailClick = onDetailClick,
                search = search
            )
        }
    }
}
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Cari Petugas...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Ikon Cari"
            )
        },
        shape = MaterialTheme.shapes.large,
        singleLine = true
    )
}

@Composable
fun HomeStatusPetugas(
    homeUiStatePetugas: HomeUiStatePetugas,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    search: String
){
    when (homeUiStatePetugas) {
        is HomeUiStatePetugas.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is HomeUiStatePetugas.Success ->{
            val filteredPetugas = homeUiStatePetugas.petugas.filter {
                it.namaPetugas.contains(search, ignoreCase = true)
            }
            if (homeUiStatePetugas.petugas.isEmpty() || filteredPetugas.isEmpty()){
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (homeUiStatePetugas.petugas.isEmpty())
                            "Tidak ada data Petugas"
                        else
                            "Petugas tidak ditemukan"
                    )
                }
            }else {
                PtgLayout(
                    petugas = filteredPetugas,
                    modifier=modifier.fillMaxWidth(),
                    onDetailClick = {
                        onDetailClick(it.idPetugas.toString())
                    }
                )
            }
        }
        is HomeUiStatePetugas.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier){
    Image(
        modifier = modifier
            .size(100.dp)
            .padding(40.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun OnError(retryAction: () -> Unit, modifier: Modifier){
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(painter = painterResource(id = R.drawable.connection_error), contentDescription = ""
        )
        Text(text = stringResource(id = R.string.loading_failed),
            modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun PtgLayout(
    petugas: List<Petugas>,
    modifier: Modifier = Modifier,
    onDetailClick: (Petugas) -> Unit,
){
    LazyColumn (
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        items(petugas) { petugas ->
            PtgCard(
                petugas = petugas,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(petugas) },
            )
        }
    }
}

@Composable
fun PtgCard(
    petugas: Petugas,
    modifier: Modifier = Modifier,
){
    Card (
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = petugas.namaPetugas,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}