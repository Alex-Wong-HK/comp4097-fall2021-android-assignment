package hk.edu.hkbu.comp.comp4097.qpon.ui.coupon

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import hk.edu.hkbu.comp.comp4097.qpon.Network
import hk.edu.hkbu.comp.comp4097.qpon.ui.home.CouponViewModel
import json.MallViewModel

@ExperimentalMaterialApi
@Composable
fun CouponDetails(nav: NavHostController, couponId: Float, lastIndex:String,context: Context ) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(lastIndex) },
            navigationIcon = {
                IconButton(onClick = { nav.popBackStack() }) {
                    // handler for physical back button
                    BackHandler(enabled = true, onBack = { nav.popBackStack() })
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
    })
    {
        val data by CouponViewModel.data.observeAsState(listOf())
        LazyColumn(modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 64.dp)) {

            items(data.filter { it.id == couponId }, itemContent = { item ->
                Spacer(Modifier.height(8.dp))
                Card(modifier = Modifier.fillMaxWidth(), elevation = 8.dp) {
                    Column {
                        Image(
                            painter = rememberImagePainter(item.image),
                            contentDescription = item.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.77f)
                                .clipToBounds(),
                            contentScale = ContentScale.Crop
                        )
                        Column(Modifier.padding(8.dp)) {
                            Text(item.title, fontWeight = FontWeight.Bold)
                            Text(item.detail)
                            Text("Coins :" + item.coin)
                            Text("Mall :" + item.mall)
                            Text("Expiry Date :" + item.expirydate)
                            Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){

                                AlertDialogConfirm(context,item.id)
                                Button(onClick = {
                                    nav.navigate(
                                        "map/${item.mall}")
                                                 },contentPadding = PaddingValues(
                                    start = 20.dp,
                                    top = 12.dp,
                                    end = 20.dp,
                                    bottom = 12.dp
                                )) {
                                    Icon(
                                        Icons.Filled.LocationOn,
                                        contentDescription = "Address",
                                        modifier = Modifier.size(ButtonDefaults.IconSize)
                                    )
                                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                    Text("Address")
                                }
                            }

                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            })
        }
    }
}
@Composable
fun AlertDialogConfirm(context: Context,coupon:Float) {
    var msg:String = ""
    MaterialTheme {
        Column {
            val openDialog = remember { mutableStateOf(false)  }
            val openDialogNoMoney = remember { mutableStateOf(false)  }
            val openDialogOk = remember { mutableStateOf(false)  }
            val openDialogNoLogin = remember { mutableStateOf(false)  }
            Button(onClick = {
                val sp = context.getSharedPreferences("user", Context.MODE_PRIVATE)
                if(sp.getString("user",null) == null){
                    openDialogNoLogin.value = true
                }else{
                    openDialog.value = true
                }


            },contentPadding = PaddingValues(
                start = 20.dp,
                top = 12.dp,
                end = 20.dp,
                bottom = 12.dp
            )) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Redeem",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Redeem")
            }

            if (openDialog.value) {

                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    title = {
                        Text(text = "Alert")
                    },
                    text = {
                        Text("Are you confirm to Redeem this coupon? ")
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                openDialog.value = false
                            }) {
                            Text("Dismiss")
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                openDialog.value = false

                                    Network.RedeemCoupon(coupon.toInt().toString(),context)
                                    Thread.sleep(3000)
                                    if (Network.result.isBlank()) {
                                        msg = "Time out."
                                        openDialogNoMoney.value = true
                                    } else {
                                        msg = Network.result
                                        openDialogNoMoney.value = true
                                    }



                            }) {
                            Text("Confirm")
                        }
                    },

                )
            }
            if(openDialogNoMoney.value){
                AlertDialog(
                    onDismissRequest = {
                        openDialogNoMoney.value = false
                    },
                    title = {
                        Text(text = "Alert")
                    },
                    text = {
                        Text(text = msg)
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                openDialogNoMoney.value = false
                            }) {
                            Text("Confirm")
                        }
                    },
                )
            }

            if(openDialogNoLogin.value){
                AlertDialog(
                    onDismissRequest = {
                        openDialogNoLogin.value = false
                    },
                    title = {
                        Text(text = "Alert")
                    },
                    text = {
                        Text(text = "Please Login First")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                openDialogNoLogin.value = false
                            }) {
                            Text("Confirm")
                        }
                    },
                )
            }
        }

    }
}
