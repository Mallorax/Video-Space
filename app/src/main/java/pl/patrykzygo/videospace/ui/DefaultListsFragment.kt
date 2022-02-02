package pl.patrykzygo.videospace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.databinding.FragmentDefaultListsBinding
import pl.patrykzygo.videospace.ui.movies_list.MoviesListFragment

@AndroidEntryPoint
class DefaultListsFragment : Fragment() {

    private var _binding: FragmentDefaultListsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDefaultListsBinding.inflate(inflater)
        setUpAppBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager = parentFragmentManager
        fragmentManager.commit {
            replace(binding.mostPopularMoviesContainer.id, MoviesListFragment())
            setReorderingAllowed(true)
        }
    }

    private fun setUpAppBar() {
        val navController = findNavController()
        val appBarConfig = AppBarConfiguration(navController.graph)
        binding.appBar.toolbar.setupWithNavController(navController, appBarConfig)
    }
}