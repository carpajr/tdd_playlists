package petros.efthymiou.groovy.playlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlaylistService @Inject constructor(
   private val api: PlaylistAPI
) {
    suspend fun fetchPlaylists(): Flow<Result<List<PlaylistRaw>>> {
        return flow {
            try {
                val playlist = api.fetchAllPlaylists()
                emit(Result.success(playlist))
            } catch (e: Exception) {
               // emit(Result.failure(RuntimeException("Something went wrong", e)))
            }
        }
    }
}
