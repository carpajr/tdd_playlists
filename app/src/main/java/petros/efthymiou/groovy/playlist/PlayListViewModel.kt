package petros.efthymiou.groovy.playlist
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData

class PlayListViewModel (
    private val repository: PlaylistRepository
): ViewModel() {
    val playlists = liveData<Result<List<Playlist>>> {
        emitSource(repository.getPlaylists().asLiveData())
    }
}
