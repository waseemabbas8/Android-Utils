package com.softsolutions.ilamkidunya.css.ui.main.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import com.paginate.Paginate
import com.softsolutions.ilamkidunya.css.databinding.FragmentVideosBinding
import com.softsolutions.ilamkidunya.css.model.video.Video
import com.softsolutions.ilamkidunya.css.ui.common.PaginationListFragment
import com.softsolutions.ilamkidunya.css.ui.main.MainFragmentDirections
import com.softsolutions.ilamkidunya.css.utils.OnListItemClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class VideosFragment : PaginationListFragment<Video>() {

    override val mViewModel: VideoViewModel by viewModel()
    override val mAdapter = VideoListAdapter()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): ViewDataBinding =
        FragmentVideosBinding.inflate(inflater, container, false)

    override fun initRecyclerView() {
        (binding as FragmentVideosBinding).apply {
            mAdapter.setItemClickListener(object : OnListItemClickListener<Video> {
                override fun onItemClick(item: Video, pos: Int) {
                    gotoVideoPlayer(item)
                }
            })
            rvVideo.adapter = mAdapter
            rvVideo.setHasFixedSize(true)
            Paginate.with(rvVideo, this@VideosFragment).addLoadingListItem(false).build()
        }
    }

    private fun gotoVideoPlayer(item: Video) {
        val action = MainFragmentDirections.actionMainFragmentToVideoPlayerFragment(item)
        binding.root.findNavController().navigate( action)
    }

}
