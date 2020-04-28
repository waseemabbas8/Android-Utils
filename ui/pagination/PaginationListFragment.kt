package com.softsolutions.bookryt.ui.common.pagination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.paginate.Paginate

abstract class PaginationListFragment<T> : Fragment(), Paginate.Callbacks {

    protected abstract val mViewModel: PaginationListViewModel<T>
    protected abstract val mAdapter: PaginationListAdapter<T>
    protected lateinit var binding: ViewDataBinding

    protected var page = 0
    private var isLoading = false
    private var hasLoadedAllItems = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = initBinding(inflater, container)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        observeData()
    }

    private fun observeData() {
        mViewModel.items.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
            isLoading = false
        })

        mViewModel.hasLoadedAll.observe(viewLifecycleOwner, Observer {
            hasLoadedAllItems = it
        })

        mViewModel.state.observe(viewLifecycleOwner, Observer {
            mAdapter.setState(it)
        })
    }

    override fun onLoadMore() {
        if (!isLoading) {
            isLoading = true
            mViewModel.loadData(page++)
        }
    }

    override fun isLoading(): Boolean = isLoading

    override fun hasLoadedAllItems(): Boolean = hasLoadedAllItems

    abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup?): ViewDataBinding

    abstract fun initRecyclerView()

}