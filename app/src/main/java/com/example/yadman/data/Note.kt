package com.example.yadman.data

// مدل یک یادداشت
data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val category: String,
    val colorIndex: Int,
    val pinned: Boolean,
    val createdAt: Long
)

object NoteInfo {
    const val ALL = "همه"
    val categories = listOf("شخصی", "کار", "درس", "ایده")
}
