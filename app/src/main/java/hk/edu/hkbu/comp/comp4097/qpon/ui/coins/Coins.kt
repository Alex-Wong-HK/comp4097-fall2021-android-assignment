package hk.edu.hkbu.comp.comp4097.qpon.ui.coins

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import json.MallViewModel

data class Building(val title:String, val min:Int, val max:Int) {
    companion object {
        val data = listOf(
            Building("Coins <= 300", 0,300),
            Building("300 < Coins < 600", 301, 599),
            Building("Coins >= 600", 600, 10000000),
        )
    }
}


@ExperimentalMaterialApi
@Composable
fun Coins(nav: NavHostController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Mall") }) }) {

        LazyColumn(modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 64.dp)) {
            items(Building.data, itemContent = { item ->
                ListItem( modifier = Modifier.clickable {
                    nav.navigate("couponFilterByCoins/${item.min}/${item.max}/${item.title}")
                }) {
                    Text(item.title)
                }
            })
        }
    }
}