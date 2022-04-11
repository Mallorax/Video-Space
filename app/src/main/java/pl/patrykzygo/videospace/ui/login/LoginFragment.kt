package pl.patrykzygo.videospace.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.databinding.FragmentLoginBinding

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    val binding get() = _binding!!

    val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        subscribeToObservers()
        viewModel.launchAuthEvent()

        return binding.root
    }

    private fun subscribeToObservers() {
        viewModel.authEvent.observe(viewLifecycleOwner) {
            val uri = Uri.parse(it)
            val intent = Intent(
                "android.intent.action.VIEW",
                uri
            )
            startActivity(intent)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner){
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        }
    }
}