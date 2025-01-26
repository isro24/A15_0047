package com.example.uas.ui.customwidget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import com.example.uas.model.Kandang

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownKandang(
    expanded: MutableState<Boolean>,
    selectedKandang: MutableState<String>,
    selectedHewan: MutableState<String>,
    onValueChange: (String) -> Unit,
    listKandang: List<Kandang>,
    listHewan: List<Hewan>
){
    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
        OutlinedTextField(
            value = selectedKandang.value,
            onValueChange = {},
            label = { Text("Data Kandang") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            enabled = true,
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.cageicon),
                    contentDescription = "Data Kandang",
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            if (listKandang.isNotEmpty() && listHewan.isNotEmpty()) {
                listKandang.forEach { kandang ->
                    val hewan = listHewan.find { it.idHewan == kandang.idHewan }
                    DropdownMenuItem(
                        text = {
                            Text("${kandang.idKandang} - ${hewan?.namaHewan ?: "Tidak Diketahui"}")
                        },
                        onClick = {
                            selectedKandang.value = kandang.idKandang
                            selectedHewan.value = hewan?.namaHewan ?: "Tidak Diketahui"
                            onValueChange(kandang.idKandang)
                            expanded.value = false
                        }
                    )
                }
            } else {
                DropdownMenuItem(
                    text = { Text("Tidak ada data Kandang atau Hewan") },
                    onClick = {}
                )
            }
        }
    }
}