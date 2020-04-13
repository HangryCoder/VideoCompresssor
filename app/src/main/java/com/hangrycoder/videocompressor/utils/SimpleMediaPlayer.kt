package com.hangrycoder.videocompressor.utils

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class SimpleMediaPlayer(context: Context, videoUri: Uri?) {

    private val exoPlayer: SimpleExoPlayer

    init {
        val trackSelector = DefaultTrackSelector(context)
        val loadControl = DefaultLoadControl()
        val renderersFactory = DefaultRenderersFactory(context)

        exoPlayer = ExoPlayerFactory.newSimpleInstance(
            context, renderersFactory, trackSelector, loadControl
        )

        val userAgent = "Compressed Video"
        val mediaSource = ExtractorMediaSource
            .Factory(DefaultDataSourceFactory(context, userAgent))
            .setExtractorsFactory(DefaultExtractorsFactory())
            .createMediaSource(videoUri)

        exoPlayer.prepare(mediaSource)
    }

    fun getPlayer() = exoPlayer

    fun play() {
        exoPlayer.playWhenReady = true
    }

    fun stop() {
        exoPlayer.release()
    }
}