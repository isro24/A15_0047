package com.example.uas.ui.customwidget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.uas.R
import com.example.uas.model.Hewan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownNamaHewan(
    expanded: MutableState<Boolean>,
    selectedNamaHewan: MutableState<String>,
    onValueChange: (Int) -> Unit,
    listHewan: List<Hewan>
) {
    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
        OutlinedTextField(
            value = selectedNamaHewan.value,
            onValueChange = {},
            label = { Text("Nama Hewan") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            enabled = true,
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.lionicon),
                    contentDescription = "Nama Hewan",
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            if (listHewan.isNotEmpty()) {
                listHewan.forEach { hewan ->
                    DropdownMenuItem(
                        text = { Text(hewan.namaHewan) },
                        onClick = {
                            selectedNamaHewan.value = hewan.namaHewan
                            onValueChange(hewan.idHewan)
                            expanded.value = false
                        }
                    )
                }
            } else {
                DropdownMenuItem(
                    text = { Text("Tidak ada data hewan") },
                    onClick = {}
                )
            }
        }
    }
}