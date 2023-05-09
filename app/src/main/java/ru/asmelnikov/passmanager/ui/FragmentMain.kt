package ru.asmelnikov.passmanager.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.viewmodel.observe
import ru.asmelnikov.passmanager.data.Resource
import ru.asmelnikov.passmanager.data.model.ApiResponse
import ru.asmelnikov.passmanager.data.model.RandomSideEffects
import ru.asmelnikov.passmanager.data.model.RandomState
import ru.asmelnikov.passmanager.databinding.FragmentMainBinding
import ru.asmelnikov.passmanager.utils.Constants.API_KEY
import ru.asmelnikov.passmanager.utils.Constants.CHARACTERS


class FragmentMain : Fragment() {

    private val viewModel by viewModel<MainViewModel>()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var sliderValue = 7

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            button.setOnClickListener {
                viewModel.loadRandom(
                    apiResponse = ApiResponse(
                        n = 1,
                        length = sliderValue,
                        replacement = true,
                        characters = CHARACTERS,
                        apiKey = API_KEY
                    )
                )
            }

            slider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
                sliderValue = value.toInt()
            })

            copyButton.setOnClickListener {
                val clipboard =
                    resultTextView.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
                val clip = ClipData.newPlainText("Order Number", resultTextView.text.toString())
                clipboard?.setPrimaryClip(clip)
                Toast.makeText(requireContext(), "Text copied to clipboard", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        viewModel.observe(state = ::render, sideEffect = ::handleSideEffects, lifecycleOwner = this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: RandomState) {
        binding.apply {
            Log.d("MainViewModel", "render: $state")
            when (state.result) {
                is Resource.Error -> {
                    resultTextView.text = state.result?.message.toString()
                    progressBar.isVisible = false
                }
                is Resource.Loading -> {
                    progressBar.isVisible = state.isLoading
                }
                is Resource.Success -> {
                    resultTextView.text = state.result?.data?.random?.data.toString()
                        .replace("[\\[\\]]".toRegex(), "")
                    progressBar.isVisible = false
                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Click to search button!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun handleSideEffects(sideEffects: RandomSideEffects) {
        when (sideEffects) {
            is RandomSideEffects.Toast -> {
                Toast.makeText(
                    requireContext(),
                    "JSON-RPC error with message ${sideEffects.text}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
