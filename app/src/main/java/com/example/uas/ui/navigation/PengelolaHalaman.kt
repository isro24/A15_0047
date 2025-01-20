package com.example.uas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uas.ui.view.hewan.DestinasiDetailHewan
import com.example.uas.ui.view.hewan.DestinasiHomeHewan
import com.example.uas.ui.view.hewan.DestinasiInsertHewan
import com.example.uas.ui.view.hewan.HomeScreenHewan
import com.example.uas.ui.view.hewan.InsertHwnScreen

@Composable
fun PengelolaHalaman(navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        startDestination = DestinasiHomeHewan.route,
        modifier = Modifier
    ){
        composable (DestinasiHomeHewan.route){
            HomeScreenHewan(
                navigateToItemEntry = {navController.navigate(DestinasiInsertHewan.route)},
                onDetailClick = { idHewan ->
                    navController.navigate("${DestinasiDetailHewan.route}/$idHewan")
                }
            )
        }
        composable (DestinasiInsertHewan.route){
            InsertHwnScreen(navigateBack = {
                navController.navigate(DestinasiHomeHewan.route){
                    popUpTo(DestinasiHomeHewan.route){inclusive = true}
                }
            })
        }
    }
}