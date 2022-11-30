package com.example.tango.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.tango.databinding.FragmentHomeBinding
import com.example.tango.ui.BottomNavActivity
import com.example.tango.viewModels.ProfilesListViewModel


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val profilesListViewModel: ProfilesListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavActivity = requireActivity() as BottomNavActivity

        val spanCount = 3 // 3 columns
        val spacing = 60 // 50px
        val includeEdge = true
        val layoutManager = GridLayoutManager(bottomNavActivity, spanCount)
        binding.usersRV.layoutManager = layoutManager
        val adapter = UsersGridAdapter(profilesListViewModel, bottomNavActivity)
        binding.usersRV.adapter = adapter

        binding.usersRV.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))

        profilesListViewModel.observeProfiles().observe(bottomNavActivity) { profileVMs ->
            adapter.submitList(profileVMs)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// adapted from https://stackoverflow.com/questions/28531996/android-recyclerview-gridlayoutmanager-column-spacing
class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view!!) // item position
        val column = position % spanCount // item column
        if (includeEdge) {
            outRect.left =
                spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right =
                (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right =
                spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }
}