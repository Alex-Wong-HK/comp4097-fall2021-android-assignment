package hk.edu.hkbu.comp.comp4097.qpon.ui.nav

import android.R
import android.content.Context
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hk.edu.hkbu.comp.comp4097.qpon.ui.coins.Coins
import hk.edu.hkbu.comp.comp4097.qpon.ui.coins.CouponFilterByCoin
import hk.edu.hkbu.comp.comp4097.qpon.ui.coupon.CouponDetails
import hk.edu.hkbu.comp.comp4097.qpon.ui.home.Home
import hk.edu.hkbu.comp.comp4097.qpon.ui.mall.QponFilterByMall
import hk.edu.hkbu.comp.comp4097.qpon.ui.mall.RestaurantFilterByMall
import hk.edu.hkbu.comp.comp4097.qpon.ui.map.Map
import hk.edu.hkbu.comp.comp4097.qpon.ui.theme.QponTheme
import hk.edu.hkbu.comp.comp4097.qpon.ui.user.Redeem
import hk.edu.hkbu.comp.comp4097.qpon.ui.user.UserLogin
import hk.edu.hkbu.comp.comp4097.qpon.ui.user.UserLogout
import json.Mall

@ExperimentalMaterialApi
@Composable
fun Nav(context:Context) {
    val navController = rememberNavController()

    Scaffold(bottomBar = {
        BottomNavigation(modifier = Modifier.height(64.dp)) {

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination?.hierarchy

            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = android.R.drawable.star_on),
                        contentDescription = "Home"
                    )
                },
                label = { Text("Home") },
                selected = currentDestination?.any { it.route == "home" } == true,
                onClick = {
                    navController.navigate("home") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = false
                        }
                        launchSingleTop = false
                        restoreState = true
                    }
                }
            )
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = android.R.drawable.ic_dialog_map),
                        contentDescription = "Mall"
                    )
                },
                label = { Text("Mall") },
                selected = currentDestination?.any { it.route == "malls" } == true,
                onClick = {
                    navController.navigate("malls") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = android.R.drawable.ic_menu_compass),
                        contentDescription = "Coins"
                    )
                },
                label = { Text("Coins") },
                selected = currentDestination?.any { it.route == "coin" } == true,
                onClick = {
                    navController.navigate("coin") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = android.R.drawable.ic_dialog_info),
                        contentDescription = "User"
                    )
                },
                label = { Text("User") },
                selected = currentDestination?.any { it.route == "user" } == true,
                onClick = {
                    navController.navigate("user") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = false
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
            )
        }
    }) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
        ) {

            composable("mall/{mallId}") { backStackEntry ->
                val mallId = backStackEntry.arguments?.getString("mallId") ?: ""
                RestaurantFilterByMall(navController, mallId)
            }
            composable("coupons/{resId}") { backStackEntry ->
                val resId = backStackEntry.arguments?.getString("resId") ?: ""
                QponFilterByMall(navController, resId)
            }

            composable("couponDetail/{couponId}/{lastIndex}") { backStackEntry ->
                val couponId = backStackEntry.arguments?.getString("couponId")
                val lastIndex = backStackEntry.arguments?.getString("lastIndex") ?: ""
                if (couponId != null) {

                    CouponDetails(navController, couponId.toFloat(),lastIndex,context)
                }
            }
            composable("map/{mallId}") { backStackEntry ->
                val mallId = backStackEntry.arguments?.getString("mallId")?:""
                    Map(navController, mallId)
            }
            composable("couponFilterByCoins/{min}/{max}/{title}") { backStackEntry ->
                val min = backStackEntry.arguments?.getString("min")?:""
                val max = backStackEntry.arguments?.getString("max")?:""
                val title = backStackEntry.arguments?.getString("title")?:""
                CouponFilterByCoin(navController, min.toFloat(),max.toFloat(),title)
            }

            composable("redeem") { Redeem(navController) }
            composable("home") { Home(nav = navController) }
            composable("malls") { Mall(nav = navController) }
            composable("coin") {  Coins(nav = navController) }
            composable("user") {
                val sp = context.getSharedPreferences("user",Context.MODE_PRIVATE)
                val editor = sp.getString("user",null)
                if(editor == null) {
                    UserLogin(context,navController)
                }else{
                    UserLogout(context, navController)
                }
            }
        }
    }
}
