package pl.patrykzygo.videospace.ui.user_lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.constants.MovieStatus
import pl.patrykzygo.videospace.databinding.FragmentUserListsBinding
import pl.patrykzygo.videospace.delegate.ui.AppBarDelegate
import pl.patrykzygo.videospace.delegate.ui.AppBarDelegateImpl
import pl.patrykzygo.videospace.ui.stored_list.StoredListFragment

@AndroidEntryPoint
class UserListsFragment : Fragment(),
    AppBarDelegate by AppBarDelegateImpl() {

    private var _binding: FragmentUserListsBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        setOnStoredListFragmentResultListener()
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showStoredLists()
    }


    private fun showStoredLists() {
        addStoredListToContainer(
            R.id.watching_list_fragment_container,
            StoredListFragment(MovieStatus.WATCHING)
        )
        addStoredListToContainer(
            R.id.plan_to_watch_fragment_container,
            StoredListFragment(MovieStatus.PLAN_TO_WATCH)
        )
        addStoredListToContainer(
            R.id.completed_list_fragment_container,
            StoredListFragment(MovieStatus.COMPLETED)
        )
        addStoredListToContainer(
            R.id.on_hold_fragment_container,
            StoredListFragment(MovieStatus.ON_HOLD)
        )
        addStoredListToContainer(
            R.id.dropped_fragment_container,
            StoredListFragment(MovieStatus.DROPPED)
        )
    }

    private fun setOnStoredListFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener(
            "storedListResult",
            viewLifecycleOwner
        ) { _, bundle ->
            val movieId = bundle.getInt("movieId")
            val action =
                UserListsFragmentDirections.actionUsersListFragmentToMovieDetails(movieId)
            findNavController().navigate(action)
        }

    }

    private fun addStoredListToContainer(containerId: Int, fragment: Fragment) {
        parentFragmentManager.commit {
            replace(containerId, fragment)
            setReorderingAllowed(true)
        }
    }

}