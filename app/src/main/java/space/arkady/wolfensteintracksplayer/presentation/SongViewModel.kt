package space.arkady.wolfensteintracksplayer.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import space.arkady.wolfensteintracksplayer.exoplayer.MusicService
import space.arkady.wolfensteintracksplayer.exoplayer.MusicServiceConnection
import space.arkady.wolfensteintracksplayer.exoplayer.currentPlayerPosition
import space.arkady.wolfensteintracksplayer.util.Constants.UPDATE_PLAYER_POSITION_INTERVAL
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    private val playbackState = musicServiceConnection.playbackState

    private var _curSongDuration = MutableLiveData<Long>()
    val curSongDuration: LiveData<Long> = _curSongDuration

    private var _curPlayerPosition = MutableLiveData<Long>()
    val curPlayerPosition: LiveData<Long> = _curPlayerPosition

    init {
        updateCurrentPlayerPosition()
    }

    private fun updateCurrentPlayerPosition() = viewModelScope.launch {
        while (true) {
            val pos = playbackState.value?.currentPlayerPosition
            if (curPlayerPosition.value != pos) {
                _curPlayerPosition.postValue(pos!!)
                _curSongDuration.postValue(MusicService.curSongDuration)
            }
            delay(UPDATE_PLAYER_POSITION_INTERVAL)
        }
    }
}