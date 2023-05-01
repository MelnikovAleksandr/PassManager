package ru.asmelnikov.passmanager.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.asmelnikov.passmanager.data.Resource
import ru.asmelnikov.passmanager.data.model.ApiResponse
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
                val clipboard = resultTextView.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
                val clip = ClipData.newPlainText("Order Number", resultTextView.text.toString())
                clipboard?.setPrimaryClip(clip)
                Toast.makeText(requireContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.randomLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.apply {
                        resultTextView.text =
                            it.data?.random?.data.toString().replace("[\\[\\]]".toRegex(), "")
                        progressBar.isVisible = false
                    }
                }
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Resource.Error -> binding.apply {
                    resultTextView.text = it.message
                    progressBar.isVisible = false
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}