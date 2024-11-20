package petros.efthymiou.groovy.playlist
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import petros.efthymiou.groovy.utils.BaseUnitTest

class PlaylistRepositoryShould: BaseUnitTest() {

    private val service: PlaylistService = mock()
    private val mapper: PlaylistMapper = mock()
    private val playlists = mock<List<Playlist>>()
    private val playlistsRaw = mock<List<PlaylistRaw>>()
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun getsMappedPlaylistsForService(): Unit = runBlocking {
        val repository = mockSuccessfulCase()
        repository.getPlaylists()
        verify(service, times(1)).fetchPlaylists()
    }

    @Test
    fun emitPlaylistsFromService(): Unit = runTest {
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

    @Test
    fun delegateBusinessLogicToMapper(): Unit = runTest {
        val repository = mockSuccessfulCase()
        repository.getPlaylists().first()

        verify(mapper, times(1)).invoke(playlistsRaw)
    }

    private suspend fun mockSuccessfulCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.success(playlistsRaw))
            }
        )

        whenever(mapper.invoke(playlistsRaw)).thenReturn(playlists)

        return PlaylistRepository(service, mapper)
    }

    private suspend fun mockFailureCase(): PlaylistRepository {
        whenever(service.fetchPlaylists()).thenReturn(
            flow {
                emit(Result.failure<List<PlaylistRaw>>(exception))
            }
        )

        return PlaylistRepository(service, mapper)
    }

}