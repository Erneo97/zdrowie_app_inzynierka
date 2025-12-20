package com.example.firstcomposeap.ui.components.treningplans

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstcomposeap.ui.service.data.CwiczenieWTreningu

/**
 * Komponent do tylko do podglądu danego ćwiczenia, serie, powtózenia i obciązenie.
 */
@Composable
fun CwiczenieTreningStatsItem(
    cwiczenie: CwiczenieWTreningu,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text =" ${cwiczenie.nazwa}",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(2.dp))
        HorizontalDivider()

        Spacer(modifier = Modifier.height(8.dp))
        Text("czas ćwiczenia:  ${cwiczenie.czas}", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(2.dp))


        Spacer(modifier = Modifier.height(2.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))


        cwiczenie.serie.forEachIndexed { index, seria ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 1.dp)
            ) {
                Text("${index + 1}", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(2.dp))


                Column (modifier = Modifier.weight(4f),) {
                    Text("Powtórzenia")
                    Text("${seria.liczbaPowtorzen}", fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column (modifier = Modifier.weight(4f),) {
                    Text("Obciążenie")
                    Text("${seria.obciazenie}", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

}