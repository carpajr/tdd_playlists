package petros.efthymiou.groovy.playlist

import retrofit2.http.GET

interface PlaylistAPI {

    @GET("playlists")
    fun fetchAllPlaylists(): List<Playlist>
}