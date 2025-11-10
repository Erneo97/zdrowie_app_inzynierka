package com.example.firstcomposeap.ui.components.profile.ZnajomiTab

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.balansapp.ui.service.data.PrzyjacieleInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstcomposeap.ui.components.icon.Delete
import androidx.compose.runtime.LaunchedEffect
import com.example.firstcomposeap.ui.components.icon.Utensils
import com.example.firstcomposeap.ui.components.icon.UtensilsCrossed

@Composable
fun UserFriendCard(info: PrzyjacieleInfo
                       , onDelete : () -> Unit
                       , onChange : () -> Unit) {

    var czyDozwolony by remember { mutableStateOf(info.czyDozwolony) }
    val shadowColor = MaterialTheme.colorScheme.primary
    val kolorOK = MaterialTheme.colorScheme.primary;
    val kolorErr = MaterialTheme.colorScheme.error

    var icon = if (info.czyDozwolony) UtensilsCrossed else Utensils
    var colorBackground = if (info.czyDozwolony) kolorOK else kolorErr


    LaunchedEffect(czyDozwolony) {
        icon = if (info.czyDozwolony) UtensilsCrossed else Utensils
        colorBackground = if (info.czyDozwolony) kolorOK else kolorErr
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .drawBehind {
                val shadowOffsetX = 8f
                val shadowOffsetY = 8f
                val shadowColor = shadowColor.copy(alpha = 0.25f)
                drawRoundRect(
                    color = shadowColor,
                    topLeft = Offset(shadowOffsetX, shadowOffsetY),
                    size = size,
                    cornerRadius = CornerRadius(25f, 25f),
                )
            }
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(25.dp),
                ambientColor = shadowColor.copy(alpha = 0.8f),
                spotColor = shadowColor.copy(alpha = 0.8f)
            )
            .border(
                width = 2.dp,
                color = Color.Gray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(25.dp)
            )
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            Column (modifier = Modifier.weight(1f)) {
                Text("${info.imie} ${info.nazwisko}", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                Text(info.email, fontSize = 20.sp)
            }
            Column (horizontalAlignment = Alignment.End) {
                FloatingActionButton(
                    onClick = {
                                czyDozwolony = !czyDozwolony
                                info.czyDozwolony = czyDozwolony
                                onChange()
                              },
                    containerColor = colorBackground,
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Pozwolenie do edycji",
                        tint = Color.White
                    )
                }
                Spacer(Modifier.height(25.dp))

                FloatingActionButton(
                    onClick = onDelete,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Delete,
                        contentDescription = "Usuń",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
