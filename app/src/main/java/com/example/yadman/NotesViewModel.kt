package com.example.yadman

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.yadman.data.Note
import com.example.yadman.data.NoteInfo
import com.example.yadman.data.NoteStorage

class NotesViewModel(app: Application) : AndroidViewModel(app) {

    private val storage = NoteStorage(app)

    var notes by mutableStateOf(storage.loadNotes())
        private set

    var darkMode by mutableStateOf(storage.isDarkMode())
        private set

    var newestFirst by mutableStateOf(storage.isNewestFirst())
        private set

    // این دو تا از بیرون هم تغییر می‌کنن (جستجو و فیلتر دسته)
    var searchText by mutableStateOf("")
    var selectedCategory by mutableStateOf(NoteInfo.ALL)

    fun getNote(id: Long): Note? = notes.find { it.id == id }

    fun visibleNotes(onlyPinned: Boolean): List<Note> {
        var list = notes
        if (onlyPinned) {
            list = list.filter { it.pinned }
        }
        if (selectedCategory != NoteInfo.ALL) {
            list = list.filter { it.category == selectedCategory }
        }
        if (searchText.isNotBlank()) {
            val query = searchText.trim()
            list = list.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.content.contains(query, ignoreCase = true)
            }
        }
        val sorted = if (newestFirst) {
            list.sortedByDescending { it.createdAt }
        } else {
            list.sortedBy { it.createdAt }
        }
        // sortedByDescending پایداره پس ترتیب قبلی حفظ می‌شه
        return sorted.sortedByDescending { it.pinned }
    }

    fun saveNote(note: Note) {
        val exists = notes.any { it.id == note.id }
        notes = if (exists) {
            notes.map { if (it.id == note.id) note else it }
        } else {
            notes + note
        }
        storage.saveNotes(notes)
    }

    fun deleteNote(id: Long) {
        notes = notes.filter { it.id != id }
        storage.saveNotes(notes)
    }

    fun togglePin(id: Long) {
        notes = notes.map { if (it.id == id) it.copy(pinned = !it.pinned) else it }
        storage.saveNotes(notes)
    }

    fun clearAll() {
        notes = emptyList()
        storage.saveNotes(notes)
    }

    fun changeDarkMode(value: Boolean) {
        darkMode = value
        storage.setDarkMode(value)
    }

    fun changeNewestFirst(value: Boolean) {
        newestFirst = value
        storage.setNewestFirst(value)
    }
}
