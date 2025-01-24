package com.example.uas.ui.customwidget

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomeTopAppBar(
    title: String,
    canNavigateBack:Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior?=null,
    navigateUp:() -> Unit = {},
    onRefresh:() -> Unit = {},
    backgroundColor: Color = Color(0xFF28D15A)
){
    CenterAlignedTopAppBar(
        title = { Text(text = title, color = Color.White) },
        actions = {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = "", tint =  Color.White, modifier= Modifier.clickable {
                onRefresh()
            })
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior, navigationIcon = {
            if(canNavigateBack){
                IconButton(onClick = navigateUp) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
    )
}