package com.example.yadman.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yadman.NotesViewModel
import com.example.yadman.data.Note
import com.example.yadman.data.NoteInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotesScreen(vm: NotesViewModel, onlyPinned: Boolean, onOpenNote: (Long) -> Unit) {
    var noteToDelete by remember { mutableStateOf<Note?>(null) }
    val notes = vm.visibleNotes(onlyPinned)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = vm.searchText,
            onValueChange = { vm.searchText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("جستجو در یادداشت‌ها...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )

        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf(NoteInfo.ALL) + NoteInfo.categories) { cat ->
                FilterChip(
                    selected = vm.selectedCategory == cat,
                    onClick = { vm.selectedCategory = cat },
                    label = { Text(cat) }
                )
            }
        }

        if (notes.isEmpty()) {
            EmptyState(
                if (onlyPinned) "هنوز یادداشتی نشان نکردی" else "یادداشتی پیدا نشد.\nبا دکمه + اولین یادداشتت رو بساز!"
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        note = note,
                        onClick = { onOpenNote(note.id) },
                        onPin = { vm.togglePin(note.id) },
                        onDelete = { noteToDelete = note }
                    )
                }
            }
        }
    }

    // دیالوگ تایید حذف
    noteToDelete?.let { note ->
        AlertDialog(
            onDismissRequest = { noteToDelete = null },
            title = { Text("حذف یادداشت") },
            text = { Text("یادداشت «${note.title}» حذف بشه؟") },
            confirmButton = {
                TextButton(onClick = {
                    vm.deleteNote(note.id)
                    noteToDelete = null
                }) {
                    Text("حذف")
                }
            },
            dismissButton = {
                TextButton(onClick = { noteToDelete = null }) {
                    Text("انصراف")
                }
            }
        )
    }
}

@Composable
fun NoteCard(note: Note, onClick: () -> Unit, onPin: () -> Unit, onDelete: () -> Unit) {
    val cardColor = noteColors[note.colorIndex % noteColors.size]
    val dateText = SimpleDateFormat("yyyy/MM/dd - HH:mm", Locale.getDefault())
        .format(Date(note.createdAt))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
            contentColor = Color(0xFF222222)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (note.content.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = note.content,
                    fontSize = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = note.category, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Text(text = dateText, fontSize = 11.sp, color = Color(0xFF555555))
                }
                IconButton(onClick = onPin) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "نشان کردن",
                        tint = if (note.pinned) Color(0xFFF59E0B) else Color(0x55000000)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "حذف")
                }
            }
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "📝", fontSize = 56.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
