package pl.patrykzygo.videospace.ui.delegate

import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController

interface AppBarDelegate {

    fun setUpAppBar(navController: NavController, toolbar: Toolbar)
}