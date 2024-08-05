package com.bersyte.noteapp.model

import androidx.room.Embedded
import androidx.room.Relation

data class FolderWithNotes (
    @Embedded val folder: Folder,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "folderFiledInId"
    )
    val notes:List<Note>
)
