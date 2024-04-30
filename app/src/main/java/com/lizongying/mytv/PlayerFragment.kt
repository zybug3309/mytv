package com.lizongying.mytv

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import com.lizongying.mytv.databinding.PlayerBinding
import com.lizongying.mytv.models.TVViewModel

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

class PlayerFragment : Fragment(), SurfaceHolder.Callback {

    private var _binding: PlayerBinding? = null
    private var tvViewModel: TVViewModel? = null
    private val aspectRatio = 16f / 9f


    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceHolder: SurfaceHolder
    private var ijkMediaPlayer: IjkMediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlayerBinding.inflate(inflater, container, false)

        surfaceView = _binding!!.surfaceView
        surfaceHolder = surfaceView.holder
        surfaceHolder.addCallback(this)

        (activity as MainActivity).fragmentReady("PlayerFragment")
        return _binding!!.root
    }

    @OptIn(UnstableApi::class)
    fun play(tvViewModel: TVViewModel) {
        this.tvViewModel = tvViewModel
        ijkMediaPlayer?.reset()
        ijkMediaPlayer?.setDisplay(surfaceHolder)
        ijkMediaPlayer?.setDataSource(tvViewModel.getVideoUrlCurrent())
        ijkMediaPlayer?.setOnPreparedListener{
            Log.i(TAG, "play onPrepared")
            it.start()
            tvViewModel.setErrInfo("")
        }
        ijkMediaPlayer?.prepareAsync()
    }

    override fun onStart() {
        Log.i(TAG, "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.i(TAG, "onResume")
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (ijkMediaPlayer?.isPlaying() == true) {
            ijkMediaPlayer?.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ijkMediaPlayer?.release()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ijkMediaPlayer?.release()
        _binding = null
    }

    companion object {
        private const val TAG = "PlaybackVideoFragment"
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.i(TAG, "surfaceCreated")
        if (ijkMediaPlayer == null) {
            ijkMediaPlayer = IjkMediaPlayer()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }
}