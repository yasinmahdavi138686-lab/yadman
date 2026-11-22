package com.example.yadman.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

// ذخیره‌سازی ساده با SharedPreferences (یادداشت‌ها به صورت JSON ذخیره می‌شن)
class NoteStorage(context: Context) {

    private val prefs = context.getSharedPreferences("yadman_prefs", Context.MODE_PRIVATE)

    fun loadNotes(): List<Note> {
        val raw = prefs.getString(KEY_NOTES, null) ?: return emptyList()
        val result = mutableListOf<Note>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                result.add(
                    Note(
                        id = obj.getLong("id"),
                        title = obj.getString("title"),
                        content = obj.getString("content"),
                        category = obj.getString("category"),
                        colorIndex = obj.getInt("colorIndex"),
                        pinned = obj.getBoolean("pinned"),
                        createdAt = obj.getLong("createdAt")
                    )
                )
            }
        } catch (e: Exception) {
            // اگه دیتا خراب بود فقط لیست خالی برمی‌گردونیم
            return emptyList()
        }
        return result
    }

    fun saveNotes(notes: List<Note>) {
        val array = JSONArray()
        for (note in notes) {
            val obj = JSONObject()
            obj.put("id", note.id)
            obj.put("title", note.title)
            obj.put("content", note.content)
            obj.put("category", note.category)
            obj.put("colorIndex", note.colorIndex)
            obj.put("pinned", note.pinned)
            obj.put("createdAt", note.createdAt)
            array.put(obj)
        }
        prefs.edit().putString(KEY_NOTES, array.toString()).apply()
    }

    fun isDarkMode(): Boolean = prefs.getBoolean(KEY_DARK, false)

    fun setDarkMode(value: Boolean) {
        prefs.edit().putBoolean(KEY_DARK, value).apply()
    }

    fun isNewestFirst(): Boolean = prefs.getBoolean(KEY_NEWEST, true)

    fun setNewestFirst(value: Boolean) {
        prefs.edit().putBoolean(KEY_NEWEST, value).apply()
    }

    companion object {
        private const val KEY_NOTES = "notes"
        private const val KEY_DARK = "dark_mode"
        private const val KEY_NEWEST = "newest_first"
    }
}
