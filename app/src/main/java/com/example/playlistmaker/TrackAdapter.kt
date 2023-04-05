package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.SongItemBinding
import java.text.SimpleDateFormat
import java.util.*

class TrackAdapter(private val listener: Listener) :
    RecyclerView.Adapter<TrackAdapter.TrackHolder>() {
    private val tracks = ArrayList<Track>()
    fun setTracks(newTracks: List<Track>?) {
        tracks.clear()
        if (!newTracks.isNullOrEmpty()) {
            tracks.addAll(newTracks)
        }
        notifyDataSetChanged()
    }

    class TrackHolder(itemSong: View) : RecyclerView.ViewHolder(itemSong) {

        private val binding = SongItemBinding.bind(itemSong)
        fun bind(model: Track, listener: Listener) =
            with(binding) {
                trackName.text = model.trackName
                artistName.text = model.artistName
                trackTime.text = (SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(model.trackTimeMillis)).toString()
                itemView.setOnClickListener {
                    listener.onClick(model)
                }
                Glide.with(itemView)
                    .load(model.artworkUrl100)
                    .placeholder(R.drawable.placeholder)
                    .centerCrop()
                    .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.rounded_artworkUrl100)))
                    .into(artworkUrl100)
            }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        return TrackHolder(view)
    }
    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(tracks[position], listener)
    }

    interface Listener {
        fun onClick(track: Track)
    }
}