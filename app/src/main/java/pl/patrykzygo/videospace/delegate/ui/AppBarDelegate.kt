package pl.patrykzygo.videospace.delegate.ui

import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import com.google.android.material.bottomappbar.BottomAppBar

interface AppBarDelegate {

    fun setUpAppBar(navController: NavController, toolbar: Toolbar)
}