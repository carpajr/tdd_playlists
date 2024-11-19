package petros.efthymiou.groovy.playlist

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import petros.efthymiou.groovy.utils.BaseUnitTest
import petros.efthymiou.groovy.utils.getValueForTest

class PlaylistViewModelShould: BaseUnitTest() {
    private val repository: PlaylistRepository = mock()
    private val playlists = mock<List<Playlist>>()
    private val expected = Result.success(playlists)
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun getPlaylistsFromRepository(): Unit = runBlocking {
        val viewModel = mockSuccessfulCase()
        viewModel.playlists.getValueForTest()
        verify(repository, times(1)).getPlaylists()
    }

    @Test
    fun emitsPlaylistsFromRepository(): Unit = runBlocking {
        val viewModel = mockSuccessfulCase()
        assertEquals(expected, viewModel.playlists.getValueForTest())
    }

    @Test
    fun emitErrorWhenReceiveError() {
        runBlocking {
        whenever(repository.getPlaylists()).thenReturn(
            flow {
                emit(Result.failure<List<Playlist>>(exception))
            }
        )
        }
        val viewModel = PlayListViewModel(repository)
        assertEquals(exception, viewModel.playlists.getValueForTest()!!.exceptionOrNull())
    }

    private fun mockSuccessfulCase(): PlayListViewModel {
        runTest {
            whenever(repository.getPlaylists()).thenReturn(
                flow {
                    emit(expected)
                }
            )
        }

        return PlayListViewModel(repository)
    }
}