package pl.patrykzygo.videospace.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.databinding.ActivityMainBinding
import pl.patrykzygo.videospace.di.DefaultFragmentFactoryQualifier
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    @Inject
    @DefaultFragmentFactoryQualifier
    lateinit var fragmentFactory: DefaultFragmentFactory

    private lateinit var binding: ActivityMainBinding
    private val navController by lazy {
        findNavController(R.id.navHostFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}