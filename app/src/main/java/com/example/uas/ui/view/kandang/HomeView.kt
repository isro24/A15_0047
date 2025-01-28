package com.example.uas.ui.view.kandang

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.R
import com.example.uas.model.Kandang
import com.example.uas.ui.customwidget.CustomeTopAppBar
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.viewmodel.PenyediaViewModel
import com.example.uas.ui.viewmodel.kandang.HomeUiStateKandang
import com.example.uas.ui.viewmodel.kandang.HomeViewModelKandang

object DestinasiHomeKandang : DestinasiNavigasi {
    override val route = "homekandang"
    override val titleRes = "Halaman Kandang"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeViewKandang(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    NavigateBack: () -> Unit,
    onDetailClick: (String) -> Unit,
    viewModel: HomeViewModelKandang = viewModel(factory = PenyediaViewModel.Factory)
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var search by remember { mutableStateOf("") }

    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomeTopAppBar(
                title = DestinasiHomeKandang.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = NavigateBack,
                onRefresh = {
                    viewModel.getKnd()
                },
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
                    contentDescription = "Tambah Kandang"
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

            SearchBar(
                query = search,
                onQueryChanged = {
                    search = it
                },
            )

            Text(
                text = "Daftar Kandang Kebun Binatang",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
                    .offset(y = (-50).dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            HomeStatusKandang(
                homeUiStateKandang = viewModel.kndUiState,
                retryAction = {viewModel.getKnd()},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .offset(y = (-50).dp)
                ,
                onDetailClick = onDetailClick,
                search = search
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
                        Color(0xFF189E9B)
                    )
                )
            )

    ) {
        Image(
            painter = painterResource(id = R.drawable.bgkandang),
            contentDescription = "Header Background",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f),
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
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Kandang",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = "Mari kelola data Kandang",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
            .padding(16.dp)
            .offset(y = (-50).dp)
        ,
        placeholder = { Text("Cari kandang...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Ikon Cari"
            )
        },
        shape = MaterialTheme.shapes.large,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun HomeStatusKandang(
    homeUiStateKandang: HomeUiStateKandang,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    search: String
){
    when (homeUiStateKandang) {
        is HomeUiStateKandang.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is HomeUiStateKandang.Success ->{
            val filteredKandang = homeUiStateKandang.kandang.filter {
                it.idKandang.contains(search, ignoreCase = true)
            }
            if (homeUiStateKandang.kandang.isEmpty() || filteredKandang.isEmpty()){
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (homeUiStateKandang.kandang.isEmpty())
                            "Tidak ada data Kandang"
                        else
                            "Kandang tidak ditemukan"
                    )
                }
            }else {
                KndLayout(
                    kandang = filteredKandang,
                    modifier=modifier.fillMaxWidth(),
                    onDetailClick = {
                        onDetailClick(it.idKandang)
                    }
                )
            }
        }
        is HomeUiStateKandang.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
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
fun KndLayout(
    kandang: List<Kandang>,
    modifier: Modifier = Modifier,
    onDetailClick: (Kandang) -> Unit,
){
    LazyColumn (
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        items(kandang) { kandang ->
            KndCard(
                kandang = kandang,
                modifier = Modifier
                    .fillMaxWidth(),
                onDetailClick = onDetailClick
            )
        }
    }
}

@Composable
fun KndCard(
    kandang: Kandang,
    modifier: Modifier = Modifier,
    onDetailClick: (Kandang) -> Unit,
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
                text = kandang.idKandang,
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(
                onClick = { onDetailClick(kandang) },
                modifier = Modifier.size(35.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Detail Kandang",
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}