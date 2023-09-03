package com.example.playlistmaker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.databinding.PlaylistItemMediatekaBinding
import java.io.File

class PlaylistAdapter(private val listener: Listener) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder>() {
    private val playlist = ArrayList<Playlist>()

    fun setPlaylist(newPlaylists: List<Playlist>?) {
        playlist.clear()
        if (!newPlaylists.isNullOrEmpty()) {
            playlist.addAll(newPlaylists)
        }
        notifyDataSetChanged()
    }

    class PlaylistHolder(itemPlaylist: View) : RecyclerView.ViewHolder(itemPlaylist) {
        private val binding = PlaylistItemMediatekaBinding.bind(itemPlaylist)

        private fun changeEnding(count: Int): String {
            val lastDigit = count % 10
            val lastTwoDigits = count % 100

            return when {
                lastTwoDigits in 11..19 -> "треков"
                lastDigit == 1 -> "трек"
                lastDigit in 2..4 -> "трека"
                else -> "треков"
            }
        }

        fun bind(model: Playlist, listener: Listener) =
            with(binding) {
                val countTracks =
                    model.countTracks.toString() + " " + changeEnding(model.countTracks)
                textViewTitle.text = model.title
                textViewCountTracks.text = countTracks
                itemView.setOnClickListener {
                    listener.onClick(model)
                }
                if (model.filePath.isEmpty()) {
                    imageView.setImageResource(R.drawable.placeholder)
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                } else {
                    Glide.with(itemView)
                        .load(File(model.filePath))
                        .placeholder(R.drawable.placeholder)
                        .centerCrop()
                        .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.rounded_artworkUrl100_high)))
                        .into(imageView)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_item_mediateka, parent, false)
        return PlaylistHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        holder.bind(playlist[position], listener)
    }

    override fun getItemCount(): Int = playlist.size

    interface Listener {
        fun onClick(playlist: Playlist)
    }
}