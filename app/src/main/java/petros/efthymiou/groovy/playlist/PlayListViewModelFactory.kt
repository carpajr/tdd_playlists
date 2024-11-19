package petros.efthymiou.groovy.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlayListViewModelFactory (
    private val repository: PlaylistRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayListViewModel(repository) as T
    }
}
