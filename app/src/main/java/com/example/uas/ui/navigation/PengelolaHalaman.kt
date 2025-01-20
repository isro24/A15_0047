package com.example.uas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uas.ui.view.hewan.DestinasiDetail
import com.example.uas.ui.view.hewan.DestinasiEntry
import com.example.uas.ui.view.hewan.DestinasiHomeHewan
import com.example.uas.ui.view.hewan.HomeScreenHewan

@Composable
fun PengelolaHalaman(modifier: Modifier = Modifier,navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        startDestination = DestinasiHomeHewan.route,
        modifier = Modifier
    ){
        composable (DestinasiHomeHewan.route){
            HomeScreenHewan(
                navigateToItemEntry = {navController.navigate(DestinasiEntry.route)},
                onDetailClick = { idHewan ->
                    navController.navigate("${DestinasiDetail.route}/$idHewan")
                }
            )
        }
    }
}