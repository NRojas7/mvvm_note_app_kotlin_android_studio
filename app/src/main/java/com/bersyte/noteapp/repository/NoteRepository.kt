package com.bersyte.noteapp.repository

import com.bersyte.noteapp.db.NoteDatabase
import com.bersyte.noteapp.model.Folder
import com.bersyte.noteapp.model.Note

class NoteRepository(private val db: NoteDatabase) {

    suspend fun insertNote(note: Note) = db.getNoteDao().insertNote(note)
    suspend fun deleteNote(note: Note) = db.getNoteDao().deleteNote(note)
    suspend fun updateNote(note: Note) = db.getNoteDao().updateNote(note)
    fun getAllNotes() = db.getNoteDao().getAllNotes()
    fun searchNote(query: String?) = db.getNoteDao().searchNote(query)

    suspend fun insertFolder(folder: Folder) = db.getFolderDao().insertFolder(folder)
    suspend fun deleteFolder(folder: Folder) = db.getFolderDao().deleteFolder(folder)
    suspend fun updateFolder(folder: Folder) = db.getFolderDao().updateFolder(folder)
    fun getAllFolders() = db.getFolderDao().getAllFolders()


}