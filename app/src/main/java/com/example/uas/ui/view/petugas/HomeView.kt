package com.example.uas.ui.view.petugas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
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
    NavigateBack: () -> Unit,
    viewModel: HomeViewModelPetugas = viewModel(factory = PenyediaViewModel.Factory)
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomeTopAppBar(
                title = DestinasiHomePetugas.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = NavigateBack,
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
            HeaderSection()

            Text(
                text = "Daftar Petugas Kebun Binatang",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onBackground
            )

            HomeStatusPetugas(
                homeUiStatePetugas = viewModel.ptgUiState,
                retryAction = {viewModel.getPtg()},
                modifier = Modifier.fillMaxWidth(),
                onDetailClick = onDetailClick,
            )
        }
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF21AB49),
                        Color(0xFF13C8D1)
                    )
                )
            )

    ) {
        Image(
            painter = painterResource(id = R.drawable.bgpetugas),
            contentDescription = "Header Background",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.1f),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Informasi dan Manajemen",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Text(
                text = "Petugas",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = "Mari kelola data Petugas",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}

@Composable
fun HomeStatusPetugas(
    homeUiStatePetugas: HomeUiStatePetugas,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
){
    when (homeUiStatePetugas) {
        is HomeUiStatePetugas.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is HomeUiStatePetugas.Success ->
            if (homeUiStatePetugas.petugas.isEmpty()){
                return Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = "Tidak ada data Petugas")
                }
            }else {
                PtgLayout(
                    petugas = homeUiStatePetugas.petugas,
                    modifier=modifier.fillMaxWidth(),
                    onDetailClick = {
                        onDetailClick(it.idPetugas.toString())
                    }
                )
            }
        is HomeUiStatePetugas.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier){
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun OnError(retryAction: () -> Unit, modifier: Modifier){
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(painter = painterResource(id = R.drawable.connection_error), contentDescription = "",
            modifier = Modifier.size(110.dp)
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
                    .fillMaxWidth(),
                onDetailClick = onDetailClick
            )
        }
    }
}

@Composable
fun PtgCard(
    petugas: Petugas,
    modifier: Modifier = Modifier,
    onDetailClick: (Petugas) -> Unit,
){
    Card (
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = petugas.namaPetugas,
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(
                onClick = { onDetailClick(petugas) },
                modifier = Modifier.size(35.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Detail Petugas",
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}