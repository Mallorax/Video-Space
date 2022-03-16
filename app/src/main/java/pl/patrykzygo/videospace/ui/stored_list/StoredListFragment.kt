package pl.patrykzygo.videospace.ui.stored_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.patrykzygo.videospace.databinding.FragmentStoredListBinding
import pl.patrykzygo.videospace.ui.factories.MainViewModelFactory

class StoredListFragment(private val viewModelFactory: MainViewModelFactory): Fragment() {

    private var _binding: FragmentStoredListBinding? = null
    val binding get() = _binding!!

    lateinit var viewModel: StoredListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentStoredListBinding.inflate(inflater, container, false)
        viewModel = viewModelFactory.create(StoredListViewModel::class.java)
        return binding.root
    }
}