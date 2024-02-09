package com.example.kotlin_jetpackcompose

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlin_jetpackcompose.ui.theme.Kotlin_JetPackComposeTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object IntentManager {
    var intentData: Intent = Intent()
}

class NoteEditAndDelete : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kotlin_JetPackComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    IntentManager.intentData = intent

                    val bundle = IntentManager.intentData.extras


                    val idNote = bundle?.getInt("idNote", -1) ?: -1
                    val titleTransfer = bundle?.getString("title", "")
                    val descTransfer = bundle?.getString("desc", "")
                    val contentTransfer = bundle?.getString("content", "")
                    val priorityTransfer = bundle?.getInt("priority", -1) ?: -1
                    val timeTransfer = bundle?.getString("time", "")

                    if (descTransfer != null) {
                        if (contentTransfer != null) {
                            if (titleTransfer != null) {
                                if (timeTransfer != null) {
                                    UpdateNote(
                                        idNote,
                                        titleTransfer,
                                        descTransfer,
                                        contentTransfer,
                                        priorityTransfer,
                                        timeTransfer
                                    )
                                }
                            }
                        }
                    }


                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MissingColorAlphaChannel")
@Composable
fun UpdateNote(
    idNote: Int,
    titleNote: String,
    descNote: String,
    contentNote: String,
    priorityNote: Int,
    timeNote: String
) {
    var isSearching by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current;
    val context = LocalContext.current
    Log.d(
        "DATA Transfer: ",
        "idNote: ${idNote.toString()}, title: $titleNote, desc: $descNote, content: $contentNote, priority: ${priorityNote.toString()}, time: $timeNote"
    )
    var title by remember { mutableStateOf(titleNote) }
    var desc by remember { mutableStateOf(descNote) }
    var content by remember { mutableStateOf(contentNote) }

    val radioOptions = listOf("Low", "Normal", "High")
    var selectedOptions by remember { mutableStateOf(radioOptions[priorityNote - 1]) }

    val textStyle = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray
    )

    val openAlertDialog = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(android.graphics.Color.parseColor("#F28585")),
                    titleContentColor = Color.White
                ),
                title = {
                    Text(
                        text = "Notes",
                        fontWeight = FontWeight.Bold
                    )

                },
                actions = {
                    IconButton(onClick = {
                        openAlertDialog.value = true

                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = null,
                            Modifier.size(30.dp),
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = Color(android.graphics.Color.parseColor("#FFDCCB")),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val dbHandler: DBHandler = DBHandler(context);

                    val currentDate = Calendar.getInstance()
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.ENGLISH)
                    val formattedDate = dateFormat.format(currentDate.time)

                    var prio = 1
                    if (selectedOptions == "Low") {
                        prio = 1
                    } else if (selectedOptions == "Normal") {
                        prio = 2
                    } else {
                        prio = 3
                    }
                    val subnote = title?.let {
                        desc?.let { it1 ->
                            content?.let { it2 ->
                                NoteModel(
                                    0,
                                    it, it1, it2, formattedDate, prio
                                )
                            }
                        }
                    }
                    Log.d("Note: ", subnote.toString())

                    dbHandler.updateNotes(idNote = idNote, title, desc, content, prio)
                    context.startActivity(Intent(context, MainActivity::class.java))
                },
                contentColor = Color.Black,
                modifier = Modifier.padding(16.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                    Modifier.size(35.dp)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 80.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)

        ) {

            Column(Modifier.padding(10.dp)) {
                Text(text = "Title", style = textStyle)
                Spacer(modifier = Modifier.height(8.dp))
                title?.let { it1 ->
                    BasicTextField(
                        value = it1,
                        onValueChange = { title = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(
                                Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                2.dp,
                                Color(android.graphics.Color.parseColor("#F28585")),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(10.dp),
                    )
                }
            }
            Column(Modifier.padding(10.dp)) {
                Text(text = "Description", style = textStyle)
                Spacer(modifier = Modifier.height(8.dp))
                desc?.let { it1 ->
                    BasicTextField(
                        value = it1,
                        onValueChange = { desc = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .border(
                                2.dp,
                                Color(android.graphics.Color.parseColor("#F28585")),
                                RoundedCornerShape(4.dp)
                            )
                            .background(Color.White)
                            .padding(10.dp)

                    )
                }
            }

            Row {
                radioOptions.forEach { priority ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (priority == selectedOptions),
                            onClick = {
                                selectedOptions = priority
                            })
                        Text(
                            text = priority,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            }
            Column(Modifier.padding(10.dp)) {
                Text(text = "Content", style = textStyle)
                Spacer(modifier = Modifier.height(8.dp))
                content?.let { it1 ->
                    BasicTextField(
                        value = it1,
                        onValueChange = { content = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .border(
                                2.dp,
                                Color(android.graphics.Color.parseColor("#F28585")),
                                RoundedCornerShape(4.dp)
                            )
                            .background(Color.White)
                            .padding(10.dp),
                    )
                }
            }

            if (openAlertDialog.value) {
                DialogConfirmDelete(
                    onDismissRequest = { openAlertDialog.value = false },
                    onConfirmation = {
                        val dbHandler:DBHandler = DBHandler(context)
                        dbHandler.deleteNote(idNote = idNote)
                        context.startActivity(Intent(context, MainActivity::class.java))
                    },
                    dialogTitle = "Bạn chắc chứ?",
                    dialogText = "Khi nhấn XÁC NHẬN thì ghi chú của bạn sẽ bị xóa vĩnh viễn.",
                    icon = Icons.Default.Info
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogConfirmDelete(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {

        AlertDialog(
            icon = {
                Icon(icon, contentDescription = "Example Icon")
            },
            title = {
                Text(text = dialogTitle)
            },
            text = {
                Text(text = dialogText)
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text("Xác nhận")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Hủy")
                }
            },
            containerColor = Color(android.graphics.Color.parseColor("#FFDCCB")),
        )
    
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    Kotlin_JetPackComposeTheme {
        UpdateNote(
            idNote = 1,
            titleNote = "Sample Title",
            descNote = "Sample Description",
            contentNote = "Sample Content",
            priorityNote = 3,
            timeNote = "Sample Time"
        )

    }
}


