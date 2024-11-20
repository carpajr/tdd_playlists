package petros.efthymiou.groovy.playlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import petros.efthymiou.groovy.R
import javax.inject.Inject
import kotlin.concurrent.thread

@AndroidEntryPoint
class PlaylistFragment : Fragment() {

    lateinit var viewModel: PlayListViewModel

    @Inject
    lateinit var viewModelFactory: PlayListViewModelFactory

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlist, container, false)

        setupViewModel()

        viewModel.loader.observe(this as LifecycleOwner) { loading ->
            when (loading) {
                true -> view.findViewById<View>(R.id.loader).visibility = View.VISIBLE
                else -> view.findViewById<View>(R.id.loader).visibility = View.GONE
            }
        }

        viewModel.playlists.observe(this as LifecycleOwner) { playlists ->
            playlists.getOrNull()?.let { playlist ->

                //view.findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                //view.findViewById<RecyclerView>(R.id.playlists_list).visibility = View.VISIBLE
                setupList(view.findViewById<View>(R.id.playlists_list), playlist)
            }
        }

        return view
    }

    private fun setupList(
        view: View?,
        playlists: List<Playlist>
    ) {
        with(view as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyPlaylistRecyclerViewAdapter(playlists) { id ->
                val action =
                    PlaylistFragmentDirections.actionPlaylistFragmentToPlaylistDetailFragment(id)

                findNavController().navigate(action)
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[PlayListViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlaylistFragment().apply {}
    }
}