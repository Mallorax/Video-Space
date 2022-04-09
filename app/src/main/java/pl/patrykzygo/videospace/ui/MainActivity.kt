package pl.patrykzygo.videospace.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.databinding.ActivityMainBinding
import pl.patrykzygo.videospace.factories.DefaultFragmentFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: DefaultFragmentFactory

    private lateinit var binding: ActivityMainBinding
    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        binding = ActivityMainBinding.inflate(layoutInflater)
        setupNavigation()
        setContentView(binding.root)
    }

    private fun setupNavigation(){
        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.home_fragment,
                R.id.user_lists_fragment,
                R.id.search_fragment
            ),
            binding.mainActivityRoot
        )
        binding.drawerNavView.setupWithNavController(navController)
        binding.appBarLayout.toolbar.setupWithNavController(navController, appBarConfig)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}