package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.SongItemBinding

class TrackAdapter: RecyclerView.Adapter<TrackAdapter.TrackHolder>() {
    private val trackList = arrayListOf(
        Track("Smells Like Teen Spirit", "Nirvana","5:01",
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
        ),Track("Billie Jean", "Michael Jackson","4:35",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
        ),Track("Stayin' Alive", "Bee Gees","4:10",
            "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
        ),Track("Whole Lotta Love", "Led Zeppelin","5:33",
            "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
        ),Track("Sweet Child O'Mine", "Guns N' Roses","5:03",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg "
        )
        )
    class TrackHolder(itemSong: View): RecyclerView.ViewHolder(itemSong) {

        private val binding = SongItemBinding.bind(itemSong)
            fun bind(model: Track) =
                with(binding) {
                    trackName.text = model.trackName
                    artistName.text = model.artistName
                    trackTime.text = model.trackTime
                    Glide.with(itemView)
                        .load(model.artworkUrl100)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .centerCrop()
                        .transform(RoundedCorners(4))
                        .into(artworkUrl100)
                }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.song_item,parent,false)
        return TrackHolder(view)
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(trackList[position])
    }
}