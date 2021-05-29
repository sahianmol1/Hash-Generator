package com.example.hashgenerator

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hashgenerator.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.generateButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                onGenerateClicked()
            }
        }
        setHasOptionsMenu(true)
        return view
    }

    override fun onResume() {
        super.onResume()
        val hashAlgorithms = resources.getStringArray(R.array.hash_algorithms)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, hashAlgorithms)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    private fun getHashData(): String {
        val algorithm = binding.autoCompleteTextView.text.toString()
        val plainText = binding.plainText.text.toString()
        return homeViewModel.getHash(plainText, algorithm)
    }

    private suspend fun animate() {
        binding.apply {

            generateButton.isClickable = false

            textTitle.animate().alpha(0f).duration = 400L
            generateButton.animate().alpha(0f).duration = 400L
            textInputLayout.animate()
                .alpha(0f)
                .translationXBy(1200f)
                .duration = 400L
            binding.plainText.animate()
                .alpha(0f)
                .translationXBy(-1200f)
                .duration = 400L

            delay(300)

            viewSuccess.animate().alpha(1f).duration = 600L
            viewSuccess.animate().rotationBy(720f).duration = 600L
            viewSuccess.animate().scaleXBy(900f).duration = 800L
            viewSuccess.animate().scaleYBy(900f).duration = 800L

            delay(500)

            successImageView.animate().alpha(1f).duration = 1000L

            delay(1500)
        }
    }

    private suspend fun onGenerateClicked() {
        if (binding.plainText.text.isEmpty()) {
            showSnackBar("Empty Field!")
        } else {
            animate()
            navigateToSuccess()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear_menu) {
            binding.plainText.text.clear()
            showSnackBar("Cleared")
            return true
        }
        return true
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, "Empty Field1", Snackbar.LENGTH_SHORT).setAction("Okay") {}
            .show()
    }

    private fun navigateToSuccess() {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToSuccessFragment(
                getHashData()
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}