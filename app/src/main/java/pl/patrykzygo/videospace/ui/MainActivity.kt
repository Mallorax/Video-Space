package pl.patrykzygo.videospace.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.databinding.ActivityMainBinding
import pl.patrykzygo.videospace.factories.DefaultFragmentFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //TODO: A lot of logic here that should be in viewmodel ideally
    // It's just temporary to mess around with few things without VM

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
        setLoginListener()
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        performLoginCheck()
    }

    private fun setupNavigation() {
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

    private fun setLoginListener() {
        supportFragmentManager.setFragmentResultListener("sessionId", this) { _, bundle ->
            val result = bundle.getString("sessionId")
            result?.let { saveSessionId(it) }
        }
    }

    private fun performLoginCheck(){
        val sessionId = readSessionId()
        if (sessionId != null){
            Snackbar.make(binding.root, "User logged in with session: $sessionId", Snackbar.LENGTH_LONG).show()
        }else{
            Snackbar.make(binding.root, "User logged out", Snackbar.LENGTH_LONG).show()
        }
    }

//    private fun testPreferences(){
//        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
//        val result = sharedPref.getString("test", null)
//        if (result != null){
//            Snackbar.make(binding.root, "Test has been read: $result", Snackbar.LENGTH_LONG).show()
//        }else{
//            with(sharedPref.edit()){
//                putString("test", "test")
//                apply()
//                Snackbar.make(binding.root, "Test has been saved", Snackbar.LENGTH_LONG).show()
//            }
//        }
//    }

    private fun readSessionId(): String? {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return null
        val result = sharedPref.getString("sessionId", null)
        return result
    }

    private fun saveSessionId(sessionId: String) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("sessionId", sessionId)
            apply()
            Snackbar.make(binding.root, "Session saved", Snackbar.LENGTH_LONG).show()
        }
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