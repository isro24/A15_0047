package com.example.uas.ui.view.monitoring

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.R
import com.example.uas.model.Monitoring
import com.example.uas.model.Petugas
import com.example.uas.ui.customwidget.CustomeTopAppBar
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.view.petugas.DestinasiHomePetugas
import com.example.uas.ui.viewmodel.PenyediaViewModel
import com.example.uas.ui.viewmodel.monitoring.HomeUiStateMonitoring
import com.example.uas.ui.viewmodel.monitoring.HomeViewModelMonitoring

object DestinasiHomeMonitoring : DestinasiNavigasi {
    override val route = "homemonitoring"
    override val titleRes = "Home Monitoring"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeViewMonitoring(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    NavigateBack: () -> Unit,
    viewModel: HomeViewModelMonitoring = viewModel(factory = PenyediaViewModel.Factory)
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomeTopAppBar(
                title = DestinasiHomeMonitoring.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = NavigateBack,
                onRefresh = {
                    viewModel.getMnt()
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
                    contentDescription = "Tambah Monitoring"
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

            HomeStatusMonitoring(
                homeUiStateMonitoring = viewModel.mntUiState,
                retryAction = {viewModel.getMnt()},
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
            .background(color = Color(0xFF28D15A))
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Header Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.padding(start = 25.dp)
                ) {
                }
                Box(
                    modifier = Modifier.padding(end = 35.dp, bottom = 15.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                }
            }
            Spacer(modifier = Modifier.size(30.dp))
        }
    }
}

@Composable
fun HomeStatusMonitoring(
    homeUiStateMonitoring: HomeUiStateMonitoring,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
){
    when (homeUiStateMonitoring) {
        is HomeUiStateMonitoring.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is HomeUiStateMonitoring.Success ->
            if (homeUiStateMonitoring.monitoring.isEmpty()){
                return Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = "Tidak ada data Monitoring")
                }
            }else {
                MntLayout(
                    monitoring = homeUiStateMonitoring.monitoring,
                    modifier=modifier.fillMaxWidth(),
                    onDetailClick = {
                        onDetailClick(it.idMonitoring.toString())
                    }
                )
            }
        is HomeUiStateMonitoring.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
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
fun MntLayout(
    monitoring: List<Monitoring>,
    modifier: Modifier = Modifier,
    onDetailClick: (Monitoring) -> Unit,
){
    LazyColumn (
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        items(monitoring) { monitoring ->
            MntCard(
                monitoring = monitoring,
                modifier = Modifier
                    .fillMaxWidth(),
                onDetailClick = onDetailClick
            )
        }
    }
}

@Composable
fun MntCard(
    monitoring: Monitoring,
    modifier: Modifier = Modifier,
    onDetailClick: (Monitoring) -> Unit,
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
                text = monitoring.tanggalMonitoring,
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(
                onClick = { onDetailClick(monitoring) },
                modifier = Modifier.size(35.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Detail Monitoring",
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}