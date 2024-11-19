package petros.efthymiou.groovy.playlist

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import petros.efthymiou.groovy.utils.BaseUnitTest

class PlayServiceShould: BaseUnitTest() {

    private lateinit var service: PlaylistService
    private val api: PlaylistAPI = mock()
    private val playlists: List<Playlist> = mock()
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun fetchPlaylistsFromAPI(): Unit = runTest {
        service = PlaylistService(api)

        service.fetchPlaylists().first()

        verify(api, times(1)).fetchAllPlaylists()
    }

    @Test
    fun convertValuesToFlowResultAndEmitsThem(): Unit = runTest {
        val service = mockSuccessfulCase()

        assertEquals(
            Result.success(playlists), service.fetchPlaylists().first()
        )
    }

    private fun mockSuccessfulCase(): PlaylistService {
        whenever(api.fetchAllPlaylists()).thenReturn(
            playlists
        )
        val service = PlaylistService(api)
        return service
    }

    @Test
    fun emitErrorResultWhenNetworkFails(): Unit = runTest {
        mockFailureCase()

        assertEquals("Something went wrong", service.fetchPlaylists().first().exceptionOrNull()?.message)
    }

    private fun mockFailureCase() {
        whenever(api.fetchAllPlaylists()).thenThrow(
            RuntimeException("Damn backend developer")
        )

        service = PlaylistService(api)
    }
}