package pl.patrykzygo.videospace.ui.movies_list

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.data.app.DiscoverMovieRequest
import pl.patrykzygo.videospace.databinding.FragmentMoviesListBinding
import pl.patrykzygo.videospace.others.SortOptions
import pl.patrykzygo.videospace.ui.factories.MainViewModelFactory
import pl.patrykzygo.videospace.ui.delegate.AppBarDelegate
import pl.patrykzygo.videospace.ui.delegate.AppBarDelegateImpl

@AndroidEntryPoint
class MoviesListFragment(val viewModelFactory: MainViewModelFactory) : Fragment(),
    AppBarDelegate by AppBarDelegateImpl() {

    private var _binding: FragmentMoviesListBinding? = null
    val binding get() = _binding!!
    lateinit var viewModel: MoviesListViewModel
    private val adapter = createRecyclerViewAdapter()

    private lateinit var movieGenre: DiscoverMovieRequest


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        viewModel = viewModelFactory.create(MoviesListViewModel::class.java)
        movieGenre = MoviesListFragmentArgs.fromBundle(requireArguments()).request

        createMenu()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAppBar(findNavController(), binding.toolbar)
        binding.lifecycleOwner = viewLifecycleOwner
        val recyclerView = binding.moviesListVerticalRecycler
        recyclerView.adapter = adapter

        subscribeObservers()
    }

    private fun createMenu() {
        binding.toolbar.inflateMenu(R.menu.sort_list_menu)
        resetMenuItems()
        binding.toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.most_popular_sort_option -> {
                resetMenuItems()
                viewModel.triggerSortByMostPopular()
                true
            }
            R.id.release_date_sort_option -> {
                resetMenuItems()
                viewModel.triggerSortByReleaseDate()
                true
            }
            R.id.score_average_sort_option -> {
                resetMenuItems()
                viewModel.triggerSortByAverageScore()
                true
            }
            R.id.vote_count_sort_option -> {
                resetMenuItems()
                viewModel.triggerSortByVoteCount()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.getMovies(movieGenre).collectLatest {
                adapter.submitData(it)
            }
        }
        viewModel.sortOption.observe(viewLifecycleOwner, Observer {
            displaySelectedSortOption(it)
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Snackbar.make(requireView(), "Error: $it", Snackbar.LENGTH_LONG).show()
        })
    }


    private fun createRecyclerViewAdapter(): MoviesListPagingDataAdapter {
        val adapter =
            MoviesListPagingDataAdapter(MoviesListPagingDataAdapter.OnMovieClickListener { movie, _ ->
                if (movie != null) {
                    val action =
                        MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetails(movie)
                    findNavController().navigate(action)
                }

            })
        return adapter
    }

    private fun displaySelectedSortOption(sortOption: String) {
        val iconDesc =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_arrow_downward)
        val iconAsc =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_arrow_upward)
        val menu = binding.toolbar.menu
        when (sortOption) {
            SortOptions.POPULARITY_DESC -> {
                selectMenuItem(menu.findItem(R.id.most_popular_sort_option), iconDesc)
            }
            SortOptions.POPULARITY_ASC -> {
                selectMenuItem(menu.findItem(R.id.most_popular_sort_option), iconAsc)
            }
            SortOptions.VOTE_COUNT_DESC -> {
                selectMenuItem(menu.findItem(R.id.vote_count_sort_option), iconDesc)
            }
            SortOptions.VOTE_COUNT_ASC -> {
                selectMenuItem(menu.findItem(R.id.vote_count_sort_option), iconAsc)
            }
            SortOptions.SCORE_AVERAGE_DESC -> {
                selectMenuItem(menu.findItem(R.id.score_average_sort_option), iconDesc)
            }
            SortOptions.SCORE_AVERAGE_ASC -> {
                selectMenuItem(menu.findItem(R.id.score_average_sort_option), iconAsc)
            }
            SortOptions.RELEASE_DATE_DESC -> {
                selectMenuItem(menu.findItem(R.id.release_date_sort_option), iconDesc)
            }
            SortOptions.RELEASE_DATE_ASC -> {
                selectMenuItem(menu.findItem(R.id.release_date_sort_option), iconAsc)
            }
            else -> return
        }

    }

    private fun selectMenuItem(item: MenuItem, iconDrawable: Drawable?) {
        val textColor = ContextCompat.getColor(requireContext(), R.color.purple_200)
        val icon = iconDrawable?.let { DrawableCompat.wrap(it) }
        iconDrawable?.setTint(textColor)

        item.icon = icon

        val title = SpannableString(item.title)
        title.setSpan(ForegroundColorSpan(textColor), 0, title.length, 0)
        item.title = title
    }

    private fun resetMenuItems() {
        val textColor = ContextCompat.getColor(requireContext(), R.color.black)
        val defaultImage =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_arrow_upward)
        val subMenu = binding.toolbar.menu.findItem(R.id.main_menu_item).subMenu
        for (i in 0 until subMenu.size()) {
            val item = subMenu.getItem(i)

            item.icon = defaultImage

            val title = SpannableString(item.title)
            title.setSpan(ForegroundColorSpan(textColor), 0, title.length, 0)
            item.title = title
        }


    }


}