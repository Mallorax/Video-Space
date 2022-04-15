package pl.patrykzygo.videospace.ui.login

//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.os.bundleOf
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.navigation.fragment.navArgs
//import dagger.hilt.android.AndroidEntryPoint
//import pl.patrykzygo.videospace.databinding.FragmentLoginBinding
//
//@AndroidEntryPoint
//class LoggedInFragment : Fragment() {
//
//    private var _binding: FragmentLoginBinding? = null
//    val binding get() = _binding!!
//
//    val viewModel: LoggedInViewModel by viewModels()
//
//    private val args: LoggedInFragmentArgs by navArgs()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentLoginBinding.inflate(inflater, container, false)
//        subscribeToObservers()
//        viewModel.createSession(args.requestToken)
//        return binding.root
//    }
//
//    private fun subscribeToObservers() {
//        viewModel.sessionEvent.observe(viewLifecycleOwner) {
//            requireActivity().supportFragmentManager.setFragmentResult(
//                "sessionId",
//                bundleOf("sessionId" to it)
//            )
//
//        }
//        viewModel.message.observe(viewLifecycleOwner) {
////            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
//        }
//    }
//
//
//}