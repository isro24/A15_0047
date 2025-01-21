package com.example.uas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uas.ui.view.hewan.DestinasiDetailHewan
import com.example.uas.ui.view.hewan.DestinasiHomeHewan
import com.example.uas.ui.view.hewan.DestinasiInsertHewan
import com.example.uas.ui.view.hewan.DestinasiUpdateHewan
import com.example.uas.ui.view.hewan.DetailViewHewan
import com.example.uas.ui.view.hewan.HomeScreenHewan
import com.example.uas.ui.view.hewan.InsertViewHewan
import com.example.uas.ui.view.hewan.UpdateViewHewan
import com.example.uas.ui.view.mainhome.DestinasiMainHome
import com.example.uas.ui.view.mainhome.MainHome
import com.example.uas.ui.view.petugas.DestinasiDetailPetugas
import com.example.uas.ui.view.petugas.DestinasiHomePetugas
import com.example.uas.ui.view.petugas.DestinasiInsertPetugas
import com.example.uas.ui.view.petugas.DestinasiUpdatePetugas
import com.example.uas.ui.view.petugas.DetailViewPetugas
import com.example.uas.ui.view.petugas.HomeViewPetugas
import com.example.uas.ui.view.petugas.InsertViewPetugas
import com.example.uas.ui.view.petugas.UpdateViewPetugas

@Composable
fun PengelolaHalaman(navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        startDestination = DestinasiMainHome.route,
        modifier = Modifier
    ){

// HomeView
        composable (DestinasiMainHome.route){
            MainHome(navController)
        }

// Hewan
        composable (DestinasiHomeHewan.route){
            HomeScreenHewan(
                navigateToItemEntry = {navController.navigate(DestinasiInsertHewan.route)},
                onDetailClick = { idHewan ->
                    navController.navigate("${DestinasiDetailHewan.route}/$idHewan")
                },
                NavigateBack = {
                    navController.navigate(DestinasiMainHome.route) {
                        popUpTo(DestinasiMainHome.route) {
                            inclusive = true
                        }
                    }
                },
            )
        }
        composable (DestinasiInsertHewan.route){
            InsertViewHewan(navigateBack = {
                navController.navigate(DestinasiHomeHewan.route){
                    popUpTo(DestinasiHomeHewan.route){inclusive = true} }
                }
            )
        }
        composable(
            DestinasiDetailHewan.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiDetailHewan.IDHEWAN){
                    type = NavType.StringType
                }
            )
        ) {
            val idHewan = it.arguments?.getString(DestinasiDetailHewan.IDHEWAN)
            idHewan?.let {
                DetailViewHewan(
                    NavigateBack = {
                        navController.navigate(DestinasiHomeHewan.route) {
                            popUpTo(DestinasiHomeHewan.route) {
                                inclusive = true
                            }
                        }
                    },
                    onEditClick =  {navController.navigate("${DestinasiUpdateHewan.route}/$it")},
                    onDeleteClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
        composable(
            DestinasiUpdateHewan.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiUpdateHewan.IDHEWAN) {
                    type = NavType.StringType
                }
            )
        ) {
            UpdateViewHewan(
                NavigateBack = {
                    navController.popBackStack()
                },
                onNavigate = {
                    navController.popBackStack()
                },
            )
        }


// Petugas
        composable (DestinasiHomePetugas.route){
            HomeViewPetugas(
                navigateToItemEntry = {navController.navigate(DestinasiInsertPetugas.route)},
                onDetailClick = { idPetugas ->
                    navController.navigate("${DestinasiDetailPetugas.route}/$idPetugas")
                },
                NavigateBack = {
                    navController.navigate(DestinasiMainHome.route) {
                        popUpTo(DestinasiMainHome.route) {
                            inclusive = true
                        }
                    }
                },
            )
        }
        composable (DestinasiInsertPetugas.route){
            InsertViewPetugas(navigateBack = {
                navController.navigate(DestinasiHomePetugas.route){
                    popUpTo(DestinasiHomePetugas.route){inclusive = true} }
            }
            )
        }
        composable(
            DestinasiDetailPetugas.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiDetailPetugas.IDPETUGAS){
                    type = NavType.StringType
                }
            )
        ) {
            val idPetugas = it.arguments?.getString(DestinasiDetailPetugas.IDPETUGAS)
            idPetugas?.let {
                DetailViewPetugas(
                    NavigateBack = {
                        navController.navigate(DestinasiHomePetugas.route) {
                            popUpTo(DestinasiHomePetugas.route) {
                                inclusive = true
                            }
                        }
                    },
                    onEditClick =  {navController.navigate("${DestinasiUpdatePetugas.route}/$it")},
                    onDeleteClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
        composable(
            DestinasiUpdatePetugas.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiUpdatePetugas.IDPETUGAS) {
                    type = NavType.StringType
                }
            )
        ) {
            UpdateViewPetugas(
                NavigateBack = {
                    navController.popBackStack()
                },
                onNavigate = {
                    navController.popBackStack()
                },
            )
        }
    }
}