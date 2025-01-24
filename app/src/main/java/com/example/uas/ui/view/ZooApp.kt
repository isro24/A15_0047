package com.example.uas.ui.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.uas.ui.navigation.PengelolaHalaman

@Composable
fun ZooApp(
    modifier: Modifier
){
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        content = {
            Surface (
                modifier = Modifier
                    .fillMaxSize()
                    .padding()
            ){
                PengelolaHalaman()
            }
        }
    )
}
