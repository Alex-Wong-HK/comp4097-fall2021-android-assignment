package hk.edu.hkbu.comp.comp4097.qpon.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hk.edu.hkbu.comp.comp4097.qpon.Greeting
import hk.edu.hkbu.comp.comp4097.qpon.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Coupon (
    val createdAt: Float,
    val updatedAt: Float,
    val id: Float,
    val title:String,
    val quota:Float,
    val coin:Float,
    val restaurant:String,
    val expirydate:String,
    val region:String,
    val mall:String,
    val image: String,
    val detail:String
)

object CouponViewModel: ViewModel() {
    private val _coupons = MutableLiveData(mutableListOf<Coupon>())
    var data: LiveData<MutableList<Coupon>> = _coupons

    init {
        reloadData()
    }

    private fun reloadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val json = Network.getTextFromNetwork("https://comp4097-2021ass1.herokuapp.com")
            _coupons.postValue(mutableListOf<Coupon>().apply {
                addAll(Gson().fromJson<List<Coupon>>(json,object : TypeToken<List<Coupon>>() {}.type))
            })
        }
    }
}
object RedeemViewModel: ViewModel() {
    private val _coupons = MutableLiveData(mutableListOf<Coupon>())
    var data: LiveData<MutableList<Coupon>> = _coupons

    init {
        reloadData()
    }

    private fun reloadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val json = Network.getTextFromNetwork("https://comp4097-2021ass1.herokuapp.com")
            _coupons.postValue(mutableListOf<Coupon>().apply {
                addAll(Gson().fromJson<List<Coupon>>(json,object : TypeToken<List<Coupon>>() {}.type))
            })
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalCoilApi
@Composable
fun Home(nav:NavHostController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Home") }) }) {
        val data by CouponViewModel.data.observeAsState(listOf())
        LazyColumn(modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 64.dp)) {
            items(data, itemContent = { item ->
                ListItem( modifier = Modifier.clickable {
                    nav.navigate("couponDetail/${item.id}/Home")
                }) {
                    Spacer(Modifier.height(8.dp))
                    Card(modifier = Modifier.fillMaxWidth(), elevation = 8.dp) {
                        Column {
                            Image(
                                painter = rememberImagePainter(item.image),
                                contentDescription = item.title,
                                modifier = Modifier.fillMaxWidth().aspectRatio(1.77f)
                                    .clipToBounds(),
                                contentScale = ContentScale.Crop
                            )
                            Column(Modifier.padding(8.dp)) {
                                Text(item.title, fontWeight = FontWeight.Bold)
                                Text(item.detail)
                                Text("Coins :"+item.coin.toString())
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            })
        }
    }
}
