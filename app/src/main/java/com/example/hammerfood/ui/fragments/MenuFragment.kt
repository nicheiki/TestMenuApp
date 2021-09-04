package com.example.hammerfood.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hammerfood.R
import com.example.hammerfood.databinding.MenuFragmentBinding
import com.example.hammerfood.di.HammerApplication
import com.example.hammerfood.ui.adapters.ListAdapter
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch


class MenuFragment : Fragment() {

    private lateinit var binding: MenuFragmentBinding
    private lateinit var menuAdapter: ListAdapter
    private lateinit var spinnerAdapter: ArrayAdapter<CharSequence>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = MenuFragmentBinding.inflate(inflater, container, false)

        val viewModel: MenuViewModel by viewModels {
            MenuViewModelFactory((requireActivity().application as HammerApplication).repository)
        }

        //Spinner
        spinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.locations_spinner, android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.locationsSpinner.adapter = it
        }
        binding.locationsSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    lifecycleScope.launch {
                        viewModel.selectLocation(spinnerAdapter.getItem(position).toString())
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        //Banners
        val screenWidth = requireActivity().windowManager.currentWindowMetrics.bounds.width()

        binding.bannersOnTop.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            val bannersWidth = binding.banners.width
            if (scrollX + screenWidth / 2 > bannersWidth / 2) {
                binding.bannerSale30.alpha = 0.3f
                binding.bannerBdGift.alpha = 1f
            } else {
                binding.bannerSale30.alpha = 1f
                binding.bannerBdGift.alpha = 0.3f
            }
        }

        //Categories
        resources.getStringArray(R.array.food_categories).forEach {
            val chip = layoutInflater.inflate(R.layout.category_chip, null) as Chip
            chip.apply {
                text = it
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) viewModel.selectCategory(it)
                }
            }
            binding.categories.addView(chip)
        }

        //RecyclerView
        menuAdapter = ListAdapter()
        binding.menuRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = menuAdapter
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(resources.getDrawable(R.drawable.divider, null))
            addItemDecoration(divider)
        }

        //Buttons
        binding.reloadButton.setOnClickListener {
            lifecycleScope.launch {
                binding.menuProgress.visibility = View.VISIBLE
                binding.nothingFound.visibility = View.GONE
                viewModel.fetchMenu()
                binding.menuProgress.visibility = View.GONE
                viewModel.selectCategory()
            }
        }

        //Observers
        viewModel.selectedLocation.observe(viewLifecycleOwner, {
            binding.locationsSpinner.setSelection(spinnerAdapter.getPosition(it))
        })

        viewModel.selectedCategory.observe(viewLifecycleOwner, { selectedCategory ->
            if (!selectedCategory.isNullOrEmpty()) {
                binding.categories.children.map { it as Chip }
                    .first { chip -> chip.text == selectedCategory }.isChecked = true
            }
        })

        viewModel.menu.observe(viewLifecycleOwner, {
            menuAdapter.setList(it)
            menuAdapter.notifyDataSetChanged()
            binding.menuRv.isVisible = it.isNotEmpty()
            binding.nothingFound.isVisible = it.isEmpty()

        })

        return binding.root
    }

}