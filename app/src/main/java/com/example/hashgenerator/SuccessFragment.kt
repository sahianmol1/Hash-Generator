package com.example.hashgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.hashgenerator.databinding.FragmentSuccessBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SuccessFragment : Fragment() {

    private var _binding: FragmentSuccessBinding? = null
    private val binding get() = _binding!!
    private val args: SuccessFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =  FragmentSuccessBinding.inflate(inflater, container, false)
        val view= binding.root

        val hashText = args.hashMessage

        binding.apply {
            hashTextLayout.text = hashText
            buttonCoopy.setOnClickListener {
                lifecycleScope.launch {
                    onCopyClicked()
                }
            }
        }

        return view
    }

    private suspend fun onCopyClicked() {
        copyToClipboard(args.hashMessage)
        applyAnimations()
    }

    private fun copyToClipboard(hash: String) {
        val clipBoardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Encrypted Text", hash)
        clipBoardManager.setPrimaryClip(clipData)
    }

    private suspend fun applyAnimations() {
        binding.apply {
            include.messageBackground.animate().translationY(80f).duration = 200L
            include.messageTextView.animate().translationY(80f).duration = 200L

            delay(2000L)

            include.messageBackground.animate().translationY(-80f).duration = 500L
            include.messageTextView.animate().translationY(-80f).duration = 500L
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

