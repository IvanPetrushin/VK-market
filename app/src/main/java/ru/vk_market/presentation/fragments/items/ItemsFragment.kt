package ru.vk_market.presentation.fragments.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.marginTop
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tower0000.vktask2024.ui.fragments.ItemsViewModel
import ru.vk_market.R
import ru.vk_market.presentation.adapters.ItemsAdapter
import ru.vk_market.utils.PageMode
import ru.vk_market.utils.ResourceState
import dagger.hilt.android.AndroidEntryPoint
import ru.vk_market.databinding.FragmentItemsBinding

@AndroidEntryPoint
class ItemsFragment : Fragment() {
    private lateinit var binding: FragmentItemsBinding
    private val productsAdapter by lazy { ItemsAdapter() }
    private val vm by activityViewModels<ItemsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var categories: List<String>? = null
        setupItemsRv()

        binding.nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                vm.loadMoreItems()
            }
        })

        productsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("item", it) }
            findNavController().navigate(R.id.action_itemsFragment_to_itemInfoFragment, b)
        }

        binding.buttonSearch.setOnClickListener {
            if (binding.edSearchRequest.text.toString() == "") return@setOnClickListener
            vm.switchToSearchMode(binding.edSearchRequest.text.toString())
            binding.buttonReset.visibility = View.VISIBLE
            setMarginToRecyclerView(1000)
        }

        binding.buttonReset.setOnClickListener {
            vm.switchToBaseMode()
            setMarginToRecyclerView(0)
        }

        binding.buttonCategoryMenu.setOnClickListener {
            showPopupMenu(it, categories)
        }

        vm.itemsData.observe(viewLifecycleOwner) { state ->


                with(binding.stateViewLayout) {
                    stateView.visibility =
                        if (state is ResourceState.Success) {
                            binding.rvItems.visibility = View.VISIBLE
                            productsAdapter.differ.submitList(state.data?.products)
                            View.GONE
                        } else View.VISIBLE

                    val imageRes =
                        if (state is ResourceState.Loading) R.drawable.loading_animation else R.drawable.network
                    statusImageView.setImageResource(imageRes)

                    errorTextView.visibility =
                        if (state is  ResourceState.Error) {
                            binding.rvItems.visibility = View.INVISIBLE
                            View.VISIBLE
                        } else View.GONE

                    retryButton.visibility =
                        if (state is  ResourceState.Error) {
                            binding.rvItems.visibility = View.INVISIBLE
                            View.VISIBLE
                        } else View.GONE

                    retryButton.setOnClickListener {
                        vm.switchToBaseMode()
                        productsAdapter.differ.submitList(state.data?.products)
                    }

                }

        }

        vm.categories.observe(viewLifecycleOwner) {
            when (it) {
                is ResourceState.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }

                is ResourceState.Success -> {
                    binding.progressbar.visibility = View.INVISIBLE
                    categories = it.data
                }

                is ResourceState.Error -> {
                    binding.progressbar.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireContext(),
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> Unit
            }
        }

        vm.pageMode.observe(viewLifecycleOwner) {
            when (it) {
                is PageMode.Base -> {
                    hideCategory()
                    hideSearchResult()
                }

                is PageMode.Search -> {
                    hideCategory()
                    showSearchResult(it.data)
                }

                is PageMode.Category -> {
                    hideSearchResult()
                    showCategory(it.data)
                }

                else -> Unit
            }
        }
    }

    private fun setupItemsRv() {
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = productsAdapter
        }
    }

    private fun showPopupMenu(view: View, categories: List<String>?) {
        if (categories == null) return

        val popupMenu = PopupMenu(requireContext(), view)
        for (cat in categories) {
            popupMenu.menu.add(cat).setActionView(R.layout.menu_item)
        }

        popupMenu.setOnMenuItemClickListener { item ->
            val categoryName = item.title.toString().uppercase()
            vm.switchToCategoryMode(categoryName)
            setMarginToRecyclerView(100)
            true
        }

        popupMenu.show()
    }

    private fun hideCategory() {
        binding.tvCategory.visibility = View.GONE
        binding.buttonReset.visibility = View.GONE
        setMarginToRecyclerView(0)
    }

    private fun hideSearchResult() {
        with(binding) {
            edSearchRequest.text = null
            buttonReset.visibility = View.INVISIBLE
            tvSearchResults.visibility = View.INVISIBLE
            setMarginToRecyclerView(0)
        }
    }

    private fun showCategory(category: String?) {
        with(binding) {
            tvCategory.text = category?.uppercase()
            tvCategory.visibility = View.VISIBLE
            buttonReset.visibility = View.VISIBLE
        }
    }

    private fun showSearchResult(query: String?) {
        with(binding) {
            tvSearchResults.text = getString(
                R.string.search_results,
                query
            ).uppercase()
            buttonReset.visibility = View.VISIBLE
            tvSearchResults.visibility = View.VISIBLE
            setMarginToRecyclerView(100)
        }
    }

    private fun setMarginToRecyclerView(marginTop: Int) {
        val layoutParams = binding.rvItems.layoutParams as ViewGroup.MarginLayoutParams

        layoutParams.setMargins(0, marginTop, 0, 0)
        binding.rvItems.layoutParams = layoutParams
    }
}