package com.example.yadman.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.yadman.NotesViewModel
import com.example.yadman.data.Note
import com.example.yadman.data.NoteInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(vm: NotesViewModel, noteId: Long, onBack: () -> Unit) {
    val oldNote = vm.getNote(noteId)
    val context = LocalContext.current

    var title by remember { mutableStateOf(oldNote?.title ?: "") }
    var content by remember { mutableStateOf(oldNote?.content ?: "") }
    var category by remember { mutableStateOf(oldNote?.category ?: NoteInfo.categories.first()) }
    var colorIndex by remember { mutableIntStateOf(oldNote?.colorIndex ?: 0) }
    var pinned by remember { mutableStateOf(oldNote?.pinned ?: false) }

    fun save() {
        if (title.isBlank() && content.isBlank()) {
            Toast.makeText(context, "یادداشت خالیه!", Toast.LENGTH_SHORT).show()
            return
        }
        val now = System.currentTimeMillis()
        val note = Note(
            id = oldNote?.id ?: now,
            title = title.trim().ifEmpty { "بدون عنوان" },
            content = content.trim(),
            category = category,
            colorIndex = colorIndex,
            pinned = pinned,
            createdAt = oldNote?.createdAt ?: now
        )
        vm.saveNote(note)
        onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (oldNote == null) "یادداشت جدید" else "ویرایش یادداشت") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت")
                    }
                },
                actions = {
                    IconButton(onClick = { pinned = !pinned }) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "نشان کردن",
                            tint = if (pinned) Color(0xFFF59E0B)
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                    IconButton(onClick = { save() }) {
                        Icon(Icons.Filled.Check, contentDescription = "ذخیره")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("عنوان") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp),
                label = { Text("متن یادداشت") }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("دسته‌بندی", fontWeight = FontWeight.Bold)
            LazyRow(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(NoteInfo.categories) { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("رنگ کارت", fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                noteColors.forEachIndexed { index, color ->
                    val selectedModifier = if (index == colorIndex) {
                        Modifier.border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    } else {
                        Modifier
                    }
                    Row(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(color)
                            .then(selectedModifier)
                            .clickable { colorIndex = index }
                    ) {}
                }
            }
        }
    }
}
