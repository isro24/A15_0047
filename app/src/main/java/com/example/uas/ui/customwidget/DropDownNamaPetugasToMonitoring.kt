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
import com.example.uas.model.Petugas
import com.example.uas.ui.viewmodel.petugas.FormErrorStatePetugas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownNamaPetugasToMonitoring(
    expanded: MutableState<Boolean>,
    selectedNamaPetugas: MutableState<String>,
    onValueChange: (Int) -> Unit,
    listPetugas: List<Petugas>,
    errorStatePetugas: FormErrorStatePetugas = FormErrorStatePetugas()
){
    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
        OutlinedTextField(
            value = selectedNamaPetugas.value,
            onValueChange = {},
            label = { Text("Nama Petugas") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            isError = errorStatePetugas.namaPetugasError!=null,
            enabled = true,
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.officericon),
                    contentDescription = "Nama Petugas",
                    modifier = Modifier.size(30.dp)
                )
            }
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            if (listPetugas.isNotEmpty()) {
                listPetugas.forEach { petugas ->
                    DropdownMenuItem(
                        text = { Text(petugas.namaPetugas) },
                        onClick = {
                            selectedNamaPetugas.value = petugas.namaPetugas
                            onValueChange(petugas.idPetugas)
                            expanded.value = false
                        }
                    )
                }
            } else {
                DropdownMenuItem(
                    text = { Text("Tidak ada data Petugas") },
                    onClick = {}
                )
            }
        }
    }
}