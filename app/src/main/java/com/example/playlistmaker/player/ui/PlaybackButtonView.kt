package com.example.playlistmaker.player.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R

enum class PlaybackState { PLAY, PAUSE }
class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val playBitmap: Bitmap?
    private val pauseBitmap: Bitmap?
    private var playbackState: PlaybackState = PlaybackState.PAUSE
    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private val minViewSize = resources.getDimensionPixelSize(R.dimen.playback_button)
    lateinit var onClickListener: () -> Unit

    fun changeStatePlayer(state: PlaybackState) {
        playbackState = state
        invalidate()
    }

    fun getPlaybackState(): PlaybackState {
        return playbackState
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.PlaybackButtonView, defStyleAttr, defStyleRes
        ).apply {
            try {
                playBitmap = getDrawable(R.styleable.PlaybackButtonView_imagePlayResId)?.toBitmap()
                pauseBitmap =
                    getDrawable(R.styleable.PlaybackButtonView_imagePauseResId)?.toBitmap()
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(minViewSize, minViewSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        if (playBitmap != null && pauseBitmap != null) {
            when (playbackState) {
                PlaybackState.PLAY -> {
                    canvas.drawBitmap(pauseBitmap, null, imageRect, null)
                }

                PlaybackState.PAUSE -> {
                    canvas.drawBitmap(playBitmap, null, imageRect, null)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }

            MotionEvent.ACTION_UP -> {
                playbackState =
                    if (playbackState == PlaybackState.PLAY) PlaybackState.PAUSE else PlaybackState.PLAY
                onClickListener()
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}