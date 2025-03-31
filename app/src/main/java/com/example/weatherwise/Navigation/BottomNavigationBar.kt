package com.example.weatherwise.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.weatherwise.ui.theme.LightPurple
import com.example.weatherwise.ui.theme.PurpleBlue
import com.example.weatherwise.ui.theme.PurplePink


@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val navigationItems = listOf(
        NavigationItem(
            title = "Home",
            icon = Icons.Default.Home,
            route = ScreenRoutes.HomeRoute()
        ),
        NavigationItem(
            title = "Favorite",
            icon = Icons.Default.Favorite,
            route = ScreenRoutes.FavouriteRoute
        ),
        NavigationItem(
            title = "Alert",
            icon = Icons.Default.NotificationsActive,
            route = ScreenRoutes.AlertRoute
        ),
        NavigationItem(
            title = "Settings",
            icon = Icons.Default.Settings,
            route = ScreenRoutes.SettingsRoute
        )

    )

    val selectedNavigationIndex = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        containerColor = Color.Transparent,
    ) {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedNavigationIndex.intValue == index,
                onClick = {
                    selectedNavigationIndex.intValue = index
                    navController.navigate(item.route)
                   // navController.popBackStack(item.route, false)
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(
                        item.title,
                        color = if (index == selectedNavigationIndex.intValue)
                            Color.White
                        else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = Color.Gray,
                    selectedIconColor = MaterialTheme.colorScheme.surface,
                    indicatorColor = LightPurple
                )

            )
        }
    }


}






