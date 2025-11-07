package com.example.firstcomposeap.ui.components.profile


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.components.HeadText
import com.example.balansapp.ui.service.LoginViewModel
import com.example.balansapp.ui.service.data.PommiarWagii
import com.example.firstcomposeap.ui.components.UniversalEditCard
import com.example.firstcomposeap.ui.components.getFormOnlyDate
import com.example.firstcomposeap.ui.components.profile.profileTab.AddWeightDialog
import com.example.firstcomposeap.ui.components.profile.profileTab.EditUserDialog
import kotlin.collections.emptyList


@Composable
fun ProfilTab (loginViewModel: LoginViewModel) {
    HeadText(
        fontSize = 48.sp,
        text = "To jest ekran profilu"
    )
    val user = loginViewModel.user

    UserInformationCard(loginViewModel = loginViewModel)
    UserWeightCard(loginViewModel = loginViewModel)
}


@Composable
fun UserInformationCard(loginViewModel: LoginViewModel
) {
    val user = loginViewModel.user
    var showDialog by remember { mutableStateOf(false) }

    UniversalEditCard(
        data = {
            Text("imie: ${user?.imie}", fontSize = 30.sp)
            Text("nazwisko: ${user?.nazwisko}", fontSize = 30.sp)

            Spacer(modifier = Modifier.height(5.dp))
            Text("plec: ${user?.plec}", fontSize = 25.sp)
            Text("wzrost: ${user?.wzrost}", fontSize = 25.sp)
            Spacer(modifier = Modifier.height(5.dp))

            Text("email: ${user?.email}", fontSize = 25.sp)

            Spacer(modifier = Modifier.height(25.dp))
            Text("id: ${user?.id}", fontSize = 30.sp)
            Text("dania: ${user?.dania}", fontSize = 30.sp)
            Text("aktualnyPlan: ${user?.aktualnyPlan}", fontSize = 30.sp)
            Text("przyjaciele: ${user?.przyjaciele}", fontSize = 30.sp)
        },
        onClick = { showDialog = true }
    )

    if (showDialog) {
        EditUserDialog(
            user = user,
            onDismiss = { showDialog = false },
            onConfirm = { updatedUser ->
                loginViewModel.updateUserBasicInfo(updatedUser)
                showDialog = false
            }
        )
    }
}


@Composable
fun UserWeightCard(loginViewModel: LoginViewModel
) {
    val wagii = loginViewModel. user?.waga ?: emptyList()
    var lastData by remember { mutableStateOf<PommiarWagii?>(wagii.lastOrNull() ?: PommiarWagii(0.0, "", 0.0, 0.0, 0.0)) }
    var showDialog by remember { mutableStateOf(false) }


    LaunchedEffect(loginViewModel.user) {
        lastData = wagii.lastOrNull() ?: PommiarWagii(0.0, "", 0.0, 0.0, 0.0)
    }



    UniversalEditCard(
        data = {
            if( lastData != null ) {
                Text("waga: ${lastData?.wartosc ?: "-"} kg.", fontSize = 30.sp)
                Text("Z dnia: ${getFormOnlyDate(lastData?.data ?: "1990-01-01")}")
                Spacer(Modifier.height(15.dp))
                Text("tk. tłuszczowa: ${lastData?.tluszcz ?: "-"} %.", fontSize = 25.sp)
                Text("tk. mięśniowa: ${lastData?.miesnie ?: "-"} %.", fontSize = 25.sp)
                Text("nawodnienie: ${lastData?.nawodnienie ?: "-"} %.", fontSize = 25.sp)

            }
            else {
                Text("Brak danych o wadze", fontSize = 30.sp)
            }

        },
        onClick =  {  showDialog = true }
    )

    if (showDialog) {
        AddWeightDialog(
            onDismiss = { showDialog = false },
            onConfirm = { newPomiar ->
                loginViewModel.addWeightoUser(newPomiar)
                showDialog = false
            }
        )
    }

}
