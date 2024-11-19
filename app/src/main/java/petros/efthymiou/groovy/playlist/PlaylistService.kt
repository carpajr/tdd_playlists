package petros.efthymiou.groovy.playlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class PlaylistService {

    val playlistAPI = PlaylistAPI()

    suspend fun fetchPlaylists(): Flow<Result<List<Playlist>>> {
        return playlistAPI.fetchAllPlaylists()
    }
}
