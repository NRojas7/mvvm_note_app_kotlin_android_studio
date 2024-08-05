package com.bersyte.noteapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folders")
data class Folder(
    @PrimaryKey val folderId: Long,
    val folderTitle: String
)