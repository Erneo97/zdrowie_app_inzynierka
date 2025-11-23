package com.example.firstcomposeap.ui.components.meal

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.balansapp.ui.service.LoginViewModel
import com.example.balansapp.ui.service.data.PrzyjacieleInfo
import com.example.firstcomposeap.ui.service.ProductViewModel

@Composable
fun FriendsMealTab(
    loginViewModel: LoginViewModel,
                   onAddClick: () -> Unit,
                   productViewModel: ProductViewModel,
                   date: String
) {

    var friends by remember { mutableStateOf<List<PrzyjacieleInfo>>(emptyList()) }

    LaunchedEffect(Unit) {
        friends = loginViewModel.downloadFrendsInformationList()
    }

    if( !loginViewModel.isListFrendsLoaded ) {
        CircularProgressIndicator()
        return
    }
    Log.e("FriendsMealTab ", " FriendsMealTab ${friends.size}")
    val userList = remember { mutableStateListOf<String>() }
    userList.clear()
    friends.filter {
        it.czyDozwolony
    }.forEach { friend ->
        userList.add("${friend.email} - ${friend.imie} ${friend.nazwisko}")
    } // TODO: zmienic z mojego czyDozwolony na właściciela

    var selected by remember { mutableStateOf(userList.get(0)) }

    Column {

        SelectBox(
            options = userList,
            selectedOption = selected,
            onOptionSelected = { selected = it },
            )


        UniversalMealTab(
            loginViewModel = loginViewModel,
            onAddClick = onAddClick,
            productViewModel = productViewModel,
            date = date,
            downloadMealUserDay = { productViewModel.downloadMealUserDay() },
            updataMealUser = { productViewModel.updateUserMeal() }
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectBox(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    label: String = "Wybierz opcje"
) {
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = options

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .shadow(20.dp, RoundedCornerShape(12.dp))
                .background(Color.White, RoundedCornerShape(12.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                cursorColor = Color.Black
            ),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genderOptions.forEach { gender ->
                DropdownMenuItem(
                    text = { Text(gender) },
                    onClick = {
                        onOptionSelected(gender)
                        expanded = false
                    }
                )
            }
        }
    }
}
