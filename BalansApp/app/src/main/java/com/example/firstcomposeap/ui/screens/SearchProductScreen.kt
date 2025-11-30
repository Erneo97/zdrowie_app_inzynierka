package com.example.firstcomposeap.ui.screens

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.example.balansapp.R
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.balansapp.ui.components.FullSizeButton
import com.example.balansapp.ui.navigation.main.Screen
import com.example.firstcomposeap.ui.components.icon.Arrow_back_ios_new
import com.example.firstcomposeap.ui.service.ProductViewModel
import com.example.firstcomposeap.ui.service.SearchViewModel
import com.example.firstcomposeap.ui.service.data.Produkt

fun setFlagToSendData(mainText: String, productViewModel: ProductViewModel, context: Context) {

    if(mainText == context.getString(R.string.product)) {
        productViewModel.isSelectedProductsReadyToSend.value = true
    }
    else {
        productViewModel.isSelectedRecipesReadyToSend.value = true
    }
}

@Composable
fun SearchProductScreen(
    navController: NavController,
    onClose: () -> Unit,
    searchViewModel: SearchViewModel = viewModel(),
    productViewModel: ProductViewModel,
    onlyProduct: Boolean = false
) {
    val context = LocalContext.current
    val query = searchViewModel.searchQuery
    val suggestions = searchViewModel.suggestionsList
    val productsList = remember { mutableStateListOf<Produkt>() }




    var mainText by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    BackHandler {
        setFlagToSendData(mainText, productViewModel, context)
        productViewModel.foundProduct =  null
        onClose()
        productViewModel.selectedTabIndexProductRecipe = 0
    }



    LaunchedEffect(searchViewModel.searchedProducts) {  // aktualizacja przy zmianie wyszukiwania
        productsList.clear()
        productsList.addAll(searchViewModel.searchedProducts)
        val updated = if( !onlyProduct) {
            productViewModel.selectedProducts
        } else {
            productViewModel.selectedProductsFromRecipe
        }

        for (sel in updated) {
            for (i in productsList.indices) {
                if (productsList[i].id == sel.id) {
                    productsList[i] = sel
                }
            }
        }

    }

    LaunchedEffect(productViewModel.consumedProduct) {  // aktualizacja listy przez jeden produkt, zmodyfikowany produkt
        if( productViewModel.consumedProduct != null ) {
            val newProd = productViewModel.consumedProduct!!
//            aktualizacja wartości w liście produktów wyszukiwarki
            var index = productsList.indexOfFirst { it.id == newProd.id  }
            if (index != -1) {
                // dodanie nowego produktu
                productsList[index] = newProd // lokalnie

                if(!onlyProduct) {
                    index = productViewModel.selectedProducts.indexOfFirst { it.id == newProd.id }
                    if( index !=- 1) {
                        productViewModel.selectedProducts[index] = newProd
                    }
                    productViewModel.selectedProducts.add(newProd)
                }
                else  {
                    index = productViewModel.selectedProductsFromRecipe.indexOfFirst { it.id == newProd.id }
                    if( index !=- 1) {
                        productViewModel.selectedProductsFromRecipe[index] = newProd
                    }
                    productViewModel.selectedProductsFromRecipe.add(productViewModel.consumedProduct!!)
                }
            }


            productViewModel.consumedProduct = null
        }

    }

    LaunchedEffect(productViewModel.selectedTabIndexProductRecipe) {
        mainText = when (productViewModel.selectedTabIndexProductRecipe) {
            0 -> context.getString(R.string.product)
            1 -> context.getString(R.string.meal)
            else -> ""
        }
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            NavigationButtonsRetAdd(
                onClose = {onClose()
                    searchViewModel.onSearchQueryChange("")
                    setFlagToSendData(mainText, productViewModel, context)
                    productViewModel.selectedTabIndexProductRecipe = 0
                          },
                onAdd = {
                    if(mainText == context.getString(R.string.product)) {
                        navController.navigate(Screen.NewProduct.route)
                    }
                    else {
                        navController.navigate(Screen.NewRecipe.route)
                    }
                },
                mainText = "Dodaj nowy $mainText"
            )

            Text(
                "${context.getString(R.string.search)} $mainText",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(12.dp))



            if( productViewModel.selectedTabIndexProductRecipe == 0) {
                TextField(
                    value = query,
                    onValueChange = { searchViewModel.onSearchQueryChange(it) },
                    placeholder = { Text("Nazwa produktu...") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        }
                )
            }
            if( !onlyProduct) {
                TabRow(selectedTabIndex = productViewModel.selectedTabIndexProductRecipe) {
                    listOf(
                        context.getString(R.string.products),
                        context.getString(R.string.mealHead)
                    ).forEachIndexed { index, title ->
                        Tab(
                            selected = productViewModel.selectedTabIndexProductRecipe == index,
                            onClick = {
                                productViewModel.selectedTabIndexProductRecipe = index
                                searchViewModel.searchedProducts = emptyList()
                            },
                            text = { Text(title, fontSize = 22.sp) }
                        )
                    }
                }
            }
        }

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
                    .background(Color.White)
            ) {
                items(productsList, key = { it.id }) { item ->
                    val isChecked = if( !onlyProduct)
                        productViewModel.selectedProducts.contains(item)
                    else
                        productViewModel.selectedProductsFromRecipe.contains(item)

                    SearchedItem(
                        product = item,
                        isChecked = isChecked,
                        onCheckedChange = { prod, checked ->    // wybralismy select box dodając / odejmując produkt z naszel listy spożytych produktóww
                            if (checked) {
                                if(   !onlyProduct) {
                                    productViewModel.selectedProducts.add(prod)
                                }
                                else { // dodanie produktu do Przepisu
                                    productViewModel.selectedProductsFromRecipe.add(prod)
                                    productViewModel.isChangeOnRecipe.value = !productViewModel.isChangeOnRecipe.value
                                }
                            } else {
                                if( !onlyProduct) {
                                    productViewModel.selectedProducts.remove(prod)
                                    productViewModel.isChangeOnRecipe.value = !productViewModel.isChangeOnRecipe.value
                                }
                                else {
                                    productViewModel.selectedProductsFromRecipe.remove(prod)
                                }
                            }
                        },
                        onClick = { // komponent naciśnięty czyli wyświetlamy szczegłuy produktu z makro
                            productViewModel.getProductById(item.id.toInt())
                            navController.navigate(Screen.ProductConsumedDetails.route) {
                                launchSingleTop = true
                                restoreState = true

                                popUpTo(Screen.ProductSearch.route) {
                                    saveState = true
                                }
                            }
                        }
                    )
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                }
            }
        }
        else
            Text("Brak pasujących wyników wyszukiwania")

        Spacer(Modifier.height(16.dp))

    }
}

/**
 *  Górny pasek nawigacyjny na podstrinach, powrót oraz przycisk akcji (dodaj, zapisz, etc)
 */
@Composable
fun NavigationButtonsRetAdd(
    onClose: () -> Unit,
    onAdd: () -> Unit,
    mainText: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        FloatingActionButton(
            onClick = onClose,
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(42.dp)
                .weight(1f)
        ) {
            Icon(
                imageVector = Arrow_back_ios_new,
                contentDescription = "Anuluj",
                tint = Color.White
            )
        }
        Box(Modifier.weight(3f)) {
            FullSizeButton(
                text = mainText,
                onClick = onAdd
            )
        }
    }
}

/**
 * Komponent do wyświetlania produktu / dania w wyszukiwarce
 */
@Composable
fun SearchedItem(product: Produkt,
                 isChecked: Boolean,
                 onCheckedChange: (Produkt, Boolean) -> Unit,
                 onClick: ()  -> Unit
) {
    val shadowColor = MaterialTheme.colorScheme.primary
    var innerisChecked by remember {   mutableStateOf(isChecked) }

    var changeProduct by remember { mutableStateOf(product) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() }
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text =" ${changeProduct.producent} - ${changeProduct.nazwa}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "${changeProduct.objetosc[0].wartosc} ${changeProduct.objetosc[0].jednostki.displayName} - ${product.objetosc[0].kcal} kcal",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(4.dp))
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {

                Spacer(modifier = Modifier.height(12.dp))

                Checkbox(
                    checked = innerisChecked,
                    onCheckedChange = { checked ->
                        innerisChecked = !innerisChecked
                        onCheckedChange(product, innerisChecked)

                    }
                )
            }
        }
    }
}

