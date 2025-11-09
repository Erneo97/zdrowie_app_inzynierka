package com.example.firstcomposeap.ui.components.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.R
import com.example.balansapp.ui.components.FullSizeButton
import com.example.balansapp.ui.components.HeadText
import com.example.balansapp.ui.service.LoginViewModel
import com.example.balansapp.ui.service.data.PrzyjacieleInfo
import com.example.balansapp.ui.service.data.ZaproszenieInfo
import com.example.firstcomposeap.ui.components.profile.StatystykiTab.InvateFriendDialoge
import com.example.firstcomposeap.ui.components.profile.ZnajomiTab.UserFriendCard
import com.example.firstcomposeap.ui.components.profile.ZnajomiTab.UserInfitationCard


@Composable
fun ZnajomiTab (loginViewModel: LoginViewModel) {
    Spacer(modifier = Modifier.height(20.dp))

    val context = LocalContext.current
    val tabs = listOf(context.getString(R.string.friends)
        , context.getString(R.string.invitation))

    var selectedTabIndex by remember { mutableStateOf(0) }

    var showInvitateFriendDialoge by remember { mutableStateOf(false) }
    FullSizeButton(text = "Zaproś znajomego", onClick = {showInvitateFriendDialoge = true})


    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, fontSize = 22.sp) }
                )
            }
        }

        when (selectedTabIndex ) {
            0 -> {FirendsTab(loginViewModel)}
            1 -> {InfitationTab(loginViewModel)}
        }
    }


    if( showInvitateFriendDialoge) {
        InvateFriendDialoge(
            onConfirm = {email ->
                loginViewModel.invitateUser(email)
            },
            onDismiss = {showInvitateFriendDialoge = false}
        )
    }

    loginViewModel.invitationResult?.let {
        result -> LaunchedEffect(result) {
                val msg = when(result) {
                    is LoginViewModel.InvitationResult.Success -> result.message
                    is LoginViewModel.InvitationResult.Error -> result.message
                }
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                if(result is LoginViewModel.InvitationResult.Success) {
                    showInvitateFriendDialoge = false
                }

                loginViewModel.invitationResult = null
            }
    }

}

@Composable
fun FirendsTab(loginViewModel: LoginViewModel) {
    val znajomi = remember { mutableStateListOf<PrzyjacieleInfo> () }

    LaunchedEffect(Unit) {
        val list = loginViewModel.downloadFrendsInformationList()
        znajomi.clear()
        znajomi.addAll(list)
    }

    if( znajomi.isEmpty()) {
        HeadText(text = "Nie masz zanjomych, czas poznać nowych", fontSize = 25.sp)
    }

    else {
        znajomi.forEach { info ->
            UserFriendCard(info = info,
                onChange = {},
                onDelete = {znajomi.remove(info)
                    loginViewModel.deleteUserFrend(info)
                }
            )
        }
    }
}

@Composable
fun InfitationTab(loginViewModel: LoginViewModel) {

    val zaproszenia = remember {
        mutableStateListOf<ZaproszenieInfo>()
    }

    LaunchedEffect(Unit) {
        val list = loginViewModel.downloadInfitationInformationList()
        zaproszenia.clear()
        zaproszenia.addAll(list)
    }

    if( zaproszenia.isEmpty()) {
        HeadText(text = "Nie masz zaproszeń do grona znajomych", fontSize = 25.sp)
    }
    else {
        zaproszenia.forEach { info ->
            UserInfitationCard(info = info,
                onAccept = {zaproszenia.remove(info)
                           loginViewModel.acceptInvitation(info)},
                onDelete = {zaproszenia.remove(info)
                            loginViewModel.cancelInvitation(info)}
            )
        }
    }




}
