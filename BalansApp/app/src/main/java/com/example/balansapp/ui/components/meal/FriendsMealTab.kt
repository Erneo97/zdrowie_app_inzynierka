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
import com.example.firstcomposeap.ui.service.data.PomiarWagiOptions

@Composable
fun FriendsMealTab(
    loginViewModel: LoginViewModel,
                   onAddClick: () -> Unit,
                   productViewModel: ProductViewModel,
                   date: String
) {
    productViewModel.clearListMealsMap()
    var friends by remember { mutableStateOf<List<PrzyjacieleInfo>>(emptyList()) }

    LaunchedEffect(Unit) {
        friends = loginViewModel.downloadFrendsListICanModife()
    }

    if( !loginViewModel.isListFrendsLoaded ) {
        CircularProgressIndicator()
        return
    }
    Log.e("FriendsMealTab ", " FriendsMealTab ${friends.size}")
    val userList = remember { mutableStateListOf<String>() }
    userList.clear()
    friends.forEach { friend ->
        userList.add("${friend.email} - ${friend.imie} ${friend.nazwisko}")
    }

    LaunchedEffect(productViewModel.selectedUserToEditMeal) {
        productViewModel.downloadMealAnotherUserDay()
    }

    Column {

        SelectBox(
            options = userList,
            selectedOption = productViewModel.selectedUserToEditMeal,
            onOptionSelected = { productViewModel.selectedUserToEditMeal = it },
            )


        UniversalMealTab(
            loginViewModel = loginViewModel,
            onAddClick = onAddClick,
            productViewModel = productViewModel,
            date = date,
            downloadMealUserDay = {
                productViewModel.downloadMealAnotherUserDay()
                                  },
            updataMealUser = { productViewModel.updateAnotherUserMeal() }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPomiarWagiOptionBox(
    options: List<PomiarWagiOptions>,
    selectedOption: PomiarWagiOptions,
    onOptionSelected: (PomiarWagiOptions) -> Unit,
    label: String = "Wybierz opcje"
) {
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = options

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption.label,
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
                    text = { Text(gender.label) },
                    onClick = {
                        onOptionSelected(gender)
                        expanded = false
                    }
                )
            }
        }
    }
}