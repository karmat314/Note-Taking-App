package com.example.notetakingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.notetakingapp.ui.theme.NoteTakingAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteTakingAppTheme {
                NoteApp()
            }
        }
    }
}


@Preview
@Composable
fun NoteApp(modifier: Modifier = Modifier) {
    var noteTitle by remember { mutableStateOf("") }
    var noteBody by remember { mutableStateOf("") }
    val noteObjectList = remember { mutableStateListOf<Note>() }
    var showNoteDialog by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        val newAmsterdamRegular = FontFamily(Font(R.font.new_amsterdam_regular))
        Text(
            text = "Notes",
            fontSize = 40.sp,
            fontFamily = newAmsterdamRegular,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp)
        )

        HorizontalDivider(thickness = 2.dp)

        Scaffold(
            floatingActionButton = {
                LargeFloatingActionButton(
                    onClick = {
                        noteBody = ""
                        noteTitle = ""
                        showNoteDialog = !showNoteDialog
                    },
                    shape = CircleShape,
                    containerColor = Color(115, 194, 251),
                    modifier = modifier
                ) {
                    Icon(Icons.Rounded.Add, "Floating action button.")
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { innerPadding ->
            Spacer(modifier = modifier.padding(innerPadding))
            NoteDisplay(NoteList = noteObjectList)
        }

        if(showNoteDialog) {
            NoteDialog(
                noteTitle = noteTitle,
                noteBody = noteBody,
                onNoteTitleChange = { noteTitle = it },
                onNoteBodyChange = { noteBody = it },
                onDismissRequest = { showNoteDialog = !showNoteDialog },
                onConfirmation = {
                    val newNote = Note(noteTitle, noteBody)
                    if (newNote.title.isNotEmpty() || newNote.body.isNotEmpty()) {
                        noteObjectList.add(newNote)
                    }
                    showNoteDialog = false
                }
            )
        }
    }
}

class Note(var title: String, var body: String)

@Composable
fun NoteDisplay(NoteList: List<Note> ,modifier: Modifier = Modifier) {
    var showEditNoteDialog by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<Note?>(null) }

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        for (note in NoteList) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .clickable {
                        editingNote = note
                        showEditNoteDialog = true
                    }
            ) {
                Text(
                    text = note.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight(500),
                    modifier = modifier.padding(20.dp)
                )
                Image(
                    painter = painterResource(R.drawable.chevron_right_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }
        }
            if (showEditNoteDialog) {
                var noteTitle by remember { mutableStateOf(editingNote?.title?:"") }
                var noteBody by remember { mutableStateOf(editingNote?.body?:"") }
                NoteDialog(
                    noteTitle = noteTitle,
                    noteBody = noteBody,
                    onNoteTitleChange = { noteTitle = it },
                    onNoteBodyChange = { noteBody = it },
                    onDismissRequest = { showEditNoteDialog = false },
                    onConfirmation = {
                        editingNote?.title = noteTitle
                        editingNote?.body = noteBody
                        showEditNoteDialog = false
                    }
                )
        }
    }
}
@Composable
fun NoteDialog(
    noteTitle: String,
    noteBody: String,
    onNoteTitleChange: (String) -> Unit,
    onNoteBodyChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                NoteTitleTextField(value = noteTitle, onValueChange = onNoteTitleChange)
                NoteBodyTextField(value = noteBody, onValueChange = onNoteBodyChange)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun NoteTitleTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Title") }
    )
}

@Composable
fun NoteBodyTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Body") }
    )
}


