package com.example.firstcomposeap.ui.components.profile

import android.R.attr.shadowColor
import android.icu.util.UniversalTimeScale
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.components.HeadText
import com.example.balansapp.ui.service.LoginViewModel
import com.example.balansapp.ui.service.data.Uzytkownik
import com.example.firstcomposeap.ui.components.UniversalEditCard
import java.nio.file.WatchEvent

@Composable
fun ProfilTab (loginViewModel: LoginViewModel) {
    HeadText(
        fontSize = 48.sp,
        text = "To jest ekran profilu"
    )
    val user = loginViewModel.user

    UserInformationCard(user = user, onClick = {})

}


@Composable
fun UserInformationCard(user: Uzytkownik?,
                        onClick: () -> Unit // kliknięcie wywoła edycję danych użytkownika
) {
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
            Text("waga: ${user?.waga}", fontSize = 30.sp)
            Text("dania: ${user?.dania}", fontSize = 30.sp)
            Text("aktualnyPlan: ${user?.aktualnyPlan}", fontSize = 30.sp)
            Text("przyjaciele: ${user?.przyjaciele}", fontSize = 30.sp)
        },
        onClick = onClick
    )


}


