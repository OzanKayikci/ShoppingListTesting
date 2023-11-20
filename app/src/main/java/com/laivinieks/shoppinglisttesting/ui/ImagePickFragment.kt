package com.laivinieks.shoppinglisttesting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.laivinieks.shoppinglisttesting.databinding.FragmentImagePickBinding
import com.laivinieks.shoppinglisttesting.databinding.FragmentShoppingBinding
import com.laivinieks.shoppinglisttesting.viewmodel.ShoppingViewModel

class ImagePickFragment : Fragment() {

    private var _binding: FragmentImagePickBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ShoppingViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagePickBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}