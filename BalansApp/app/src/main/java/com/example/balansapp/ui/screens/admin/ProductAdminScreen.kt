package com.example.balansapp.ui.screens.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.FullSizeButton
import com.example.balansapp.ui.components.input.InputField
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.navigation.main.MainLayoutAdmin
import com.example.balansapp.ui.navigation.main.Screen
import com.example.balansapp.ui.service.AdminVievModel
import com.example.firstcomposeap.ui.components.icon.Delete
import com.example.firstcomposeap.ui.components.icon.Done
import com.example.firstcomposeap.ui.screens.SearchedItem
import com.example.firstcomposeap.ui.service.ProductViewModel
import com.example.firstcomposeap.ui.service.SearchViewModel
import com.example.firstcomposeap.ui.service.data.Produkt

@Composable
fun ProductAdminScreen(navController: NavHostController, adminVievModel: AdminVievModel, productViewModel: ProductViewModel) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.products)) }
    val searchViewModel: SearchViewModel = viewModel ()

    LaunchedEffect(Unit, adminVievModel.loadingData) {
        adminVievModel.downloadProducToConfirmeList()
    }

    val tabs = listOf(
        "Prośby o zaakceptowanie produktu",
        "Zarządzaj produktami"
    )


    MainLayoutAdmin (
        navController = navController,
        selectedItem = selectedItem,
        onItemSelected = { selectedItem = it }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LogoBackGround()
            }

            Column {
                if( productViewModel.selectedTabIndex == 0) {
                    FullSizeButton(
                        text = "Dodaj produkt / producent",
                        onClick = { navController.navigate(Screen.NewProduct.route)},
                    )
                }
                var searchProduct by remember { mutableStateOf("") }
                if( productViewModel.selectedTabIndex == 0) {
                    InputField(
                        value = searchProduct,
                        onValueChange = {searchProduct = it},
                        label = "Szukany produkt",
                    )
                }


                TabRow(selectedTabIndex = productViewModel.selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = productViewModel.selectedTabIndex == index,
                            onClick = { productViewModel.selectedTabIndex = index },
                            text = { Text(title, fontSize = 22.sp) }
                        )
                    }
                }


                when (productViewModel.selectedTabIndex) {
                    0 -> {
                        val filtratedProduct = adminVievModel.productssList
                            .filter { it.nazwa.contains(searchProduct, ignoreCase = true)
                                || it.producent.contains(searchProduct, ignoreCase = true)}

                        LazyColumn {
                            items(filtratedProduct) {
                                produkt ->
                                ProduktToConfirm(
                                    produkt = produkt,
                                    onRemove = {
                                        adminVievModel.rejectProduct(produkt.id.toInt())
                                    },
                                    onAccept = {
                                        adminVievModel.confirmProduct(produkt.id.toInt())
                                    }
                                )
                            }
                        }

                    }
                    1 -> {
                        editProductTab(
                            searchViewModel = searchViewModel,
                            onSelect = {
                                productViewModel.downloadProductToEdit(it)
                                navController.navigate(Screen.EditProduct.route) })
                    }
                }
            }
        }
    }
}

@Composable
fun editProductTab(searchViewModel: SearchViewModel, onSelect : (Int) -> Unit ) {
    val query = searchViewModel.searchQuery
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val suggestions = searchViewModel.suggestionsList

    val productsList = remember { mutableStateListOf<Produkt>() }

    LaunchedEffect(searchViewModel.searchedProducts) {  // aktualizacja przy zmianie wyszukiwania
        productsList.clear()
        productsList.addAll(searchViewModel.searchedProducts)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
                isFocused = false
                searchViewModel.downloadSearcheProducts()
            },
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            value = query,
            onValueChange = { searchViewModel.onSearchQueryChange(it) },
            placeholder = { Text("Nazwa produktu...") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    searchViewModel.downloadSearcheProducts()
                }
        )

        //  Lista z podpowiedziami do wyszukiwanej frazy
        AnimatedVisibility(visible = suggestions.isNotEmpty() && isFocused) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    items(suggestions) { item ->
                        Text(
                            text = item,
                            modifier = Modifier
                                .clickable {
                                    searchViewModel.selectSuggestion(item)
                                    searchViewModel.downloadSearcheProducts()
                                    focusManager.clearFocus()
                                    isFocused = false
                                }
                                .padding(12.dp)
                        )
                        HorizontalDivider(
                            Modifier,
                            DividerDefaults.Thickness,
                            DividerDefaults.color
                        )
                    }
                }
            }
        }


        if(!isFocused) {
            // lista produktów wyszukanych, możliwych do dodania
            LazyColumn(
                modifier = Modifier
                    .weight(9f)
                    .fillMaxWidth()
                    .heightIn(max = 650.dp)
            ) {
                items(productsList, key = { it.id }) { item ->
                    SearchedItem(
                        product = item,
                        isChecked = false,
                        onCheckedChange = { prod, checked ->   }, // Pozostaje puste, bo nie dodajemy tego produkktu do posilku
                        onClick = { onSelect(item.id.toInt() ) },
                        visibilityChech = false
                    )
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                }

            }
        }
        else {
            Text("Brak pasujących wyników wyszukiwania")
        }

    }


}

@Composable
fun ProduktToConfirm(produkt: Produkt,
                     onRemove: () -> Unit, 
                     onAccept: () -> Unit) {
    Column(modifier = getModiverCard(true)) {
        Row {
            Column (Modifier.weight(0.8f))  {
                Text("${produkt.id} ${produkt.producent} - ${produkt.nazwa}", fontWeight = FontWeight.Bold)
                HorizontalDivider()
                Text("Dostępne jednostki:")
                produkt.objetosc.forEach {
                    Text("\t${it.wartosc} ${it.jednostki}")
                    Text("\tkcal: ${it.kcal}")
                    Text("\tbialko: ${it.bialko}")
                    Text("\ttluszcze: ${it.tluszcze}")
                    Text("\tweglowodany: ${it.weglowodany}")
                    Text("\tbłonnik: ${it.blonnik}")
                    HorizontalDivider()
                }

            }

            Column (Modifier
                .weight(0.2f)
                .padding(6.dp) ) {
                FloatingActionButton(
                    onClick = onRemove,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(35.dp)
                ) {
                    Icon(
                        imageVector = Delete,
                        contentDescription = "Usuń element",
                        tint = Color.White
                    )
                }

                Spacer(Modifier.height(40.dp))
                FloatingActionButton(
                    onClick = onAccept,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(35.dp)
                ) {
                    Icon(
                        imageVector = Done,
                        contentDescription = "Zaakceptuj",
                        tint = Color.White
                    )
                }
            }
        }
    }
}