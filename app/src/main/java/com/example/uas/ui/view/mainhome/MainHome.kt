package com.example.uas.ui.view.mainhome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButtonDefaults.elevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.uas.R
import com.example.uas.ui.navigation.DestinasiNavigasi
import com.example.uas.ui.view.hewan.DestinasiHomeHewan
import com.example.uas.ui.view.kandang.DestinasiHomeKandang
import com.example.uas.ui.view.monitoring.DestinasiHomeMonitoring
import com.example.uas.ui.view.petugas.DestinasiHomePetugas

object DestinasiMainHome : DestinasiNavigasi {
    override val route = "homemain"
    override val titleRes = "Halaman Utama"
}

@Composable
fun MainHome(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF189E9B))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
        ) {
            Header()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 70.dp, topEnd = 70.dp))
                .background(color = Color.White)
        ) {
            Body(navController)
        }
    }
}

@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxSize()
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
            painter = painterResource(id = R.drawable.lion),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.7f)
                .padding(top = 25.dp, end = 66.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.iconprofil),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(110.dp)
                .align(Alignment.TopEnd)
                .padding(top = 35.dp, end = 36.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Selamat Datang di Zoo Monitoring",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Pilih kategori yang ingin Anda kelola!",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                    )
            )
        }
    }
}

@Composable
fun Body(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Daftar Kategori!",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                modifier = Modifier.offset(y = (-70).dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {

                MenuButton(
                    text = "Kandang",
                    iconRes = R.drawable.cageicon,
                    onClick = { navController.navigate(DestinasiHomeKandang.route) }
                )
                Spacer(modifier = Modifier.width(46.dp))
                MenuButton(
                    text = "Hewan",
                    iconRes = R.drawable.lionhome,
                    onClick = { navController.navigate(DestinasiHomeHewan.route) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                MenuButton(
                    text = "Petugas",
                    iconRes = R.drawable.officerhome,
                    onClick = { navController.navigate(DestinasiHomePetugas.route) }
                )
                Spacer(modifier = Modifier.width(46.dp))
                MenuButton(
                    text = "Monitoring",
                    iconRes = R.drawable.monitoringhome,
                    onClick = { navController.navigate(DestinasiHomeMonitoring.route) }
                )
            }
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Card(
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(60.dp))
                .clickable { onClick() },
            shape = RoundedCornerShape(60.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF29FF73)
            ),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
    }
}

