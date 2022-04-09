package pl.patrykzygo.videospace.delegate.ui

import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import pl.patrykzygo.videospace.R

class AppBarDelegateImpl() : AppBarDelegate {

    override fun setUpAppBar(navController: NavController, toolbar: Toolbar) {
        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.home_fragment,
                R.id.user_lists_fragment,
                R.id.search_fragment
            )
        )
        toolbar.setupWithNavController(navController, appBarConfig)
    }

}