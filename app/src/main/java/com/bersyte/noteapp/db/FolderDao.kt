package com.bersyte.noteapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.bersyte.noteapp.model.Folder
import com.bersyte.noteapp.model.FolderWithNotes

@Dao
interface FolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: Folder)

    @Update
    suspend fun updateFolder(folder: Folder)

    @Delete
    suspend fun deleteFolder(folder: Folder)

    @Query("SELECT * FROM FOLDERS ORDER BY folderId DESC")
    fun getAllFolders(): LiveData<List<Folder>>

    @Transaction
    @Query("SELECT * FROM folders")
    fun getFoldersWithNotes():List<FolderWithNotes>

    // ATTN: maybe change this to include the note body as well?
   // @Query("SELECT * FROM FOLDERS WHERE folderTitle LIKE :query")

}