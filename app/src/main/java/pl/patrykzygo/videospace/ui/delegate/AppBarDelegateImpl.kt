package pl.patrykzygo.videospace.ui.delegate

import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController

class AppBarDelegateImpl() : AppBarDelegate {

    override fun setUpAppBar(navController: NavController, toolbar: Toolbar) {
        val appBarConfig = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfig)
    }
}