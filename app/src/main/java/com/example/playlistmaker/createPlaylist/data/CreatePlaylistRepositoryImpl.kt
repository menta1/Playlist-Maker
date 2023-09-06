package com.example.playlistmaker.createPlaylist.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.AppDatabase
import com.example.playlistmaker.createPlaylist.domain.CreatePlaylistRepository
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.utils.PlaylistDbConvertor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

const val ROW_ID = 0

class CreatePlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val context: Context,
    private val playlistDbConvertor: PlaylistDbConvertor
) : CreatePlaylistRepository {

    override fun saveImage(uri: Uri) {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myplaylist")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val fileName = uri.lastPathSegment
        val file = File(filePath, fileName.toString())
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    override suspend fun savePlaylist(
        title: String, desc: String, uriPick: Uri?, isPlaceholder: Boolean
    ): Boolean {
        val insertedId = appDatabase.playlistDao().insertPlaylist(
            playlistDbConvertor.map(
                Playlist(
                    title, desc, isPlaceholder(uriPick, isPlaceholder), "", 0
                )
            )
        )
        return insertedId > ROW_ID
    }

    private suspend fun isPlaceholder(uriPick: Uri?, isPlaceholder: Boolean): String {
        if (!isPlaceholder) {
            val filePath =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myplaylist")
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            val fileName = uriPick!!.lastPathSegment
            val file = File(filePath, fileName)
            val inputStream = context.contentResolver.openInputStream(uriPick)
            val outputStream = withContext(Dispatchers.IO) {
                FileOutputStream(file)
            }
            BitmapFactory.decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            return file.toString()

        } else return ""
    }
}