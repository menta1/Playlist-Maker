package com.example.playlistmaker.currentPlaylist.ui.currentPlaylist

import com.example.playlistmaker.adapter.TrackAdapter
import com.example.playlistmaker.player.domain.model.Track

class TrackAdapterForCurrentPlaylist(
    listener: Listener,
    private val longListener: LongListener
) : TrackAdapter(listener) {

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnLongClickListener {
            longListener.onLongClick(tracks[position])
            true
        }
    }

    interface LongListener {
        fun onLongClick(track: Track)
    }
}