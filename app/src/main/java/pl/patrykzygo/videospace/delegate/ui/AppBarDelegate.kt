package pl.patrykzygo.videospace.delegate.ui

import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController

interface AppBarDelegate {

    fun setUpAppBar(navController: NavController, toolbar: Toolbar)
}