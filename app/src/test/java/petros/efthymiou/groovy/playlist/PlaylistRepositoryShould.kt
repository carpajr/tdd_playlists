package petros.efthymiou.groovy.playlist
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import petros.efthymiou.groovy.utils.BaseUnitTest

class PlaylistRepositoryShould: BaseUnitTest() {

    private val service: PlaylistService = mock()
    private val playlists = mock<List<Playlist>>()
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun getsPlaylistsForService(): Unit = runBlocking {
        val repository = PlaylistRepository(service)
        repository.getPlaylists()
        verify(service, times(1)).fetchPlaylists()
    }

    @Test
    fun emitPlaylistsFromService(): Unit = runBlocking {
        val repository = mockSuccessfulCase()
        assertEquals(Result.success(playlists), repository.getPlaylists().first())
    }

    @Test
    fun propagateErrors(): Unit = runBlocking {
        val repository = mockFailureCase()
        assertEquals(
            exception,
            repository.getPlaylists().first().exceptionOrNull()
        )
    }

    private suspend fun mockFailureCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.failure<List<Playlist>>(exception))
            }
        )

        val repository = PlaylistRepository(service)
        return repository
    }
    private suspend fun mockSuccessfulCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.success(playlists))
            }
        )

        val repository = PlaylistRepository(service)
        return repository
    }
}