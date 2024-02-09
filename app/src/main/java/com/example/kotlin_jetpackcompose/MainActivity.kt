@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.example.kotlin_jetpackcompose

import android.R.attr.value
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.kotlin_jetpackcompose.ui.theme.Kotlin_JetPackComposeTheme


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            Kotlin_JetPackComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyNotesApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MissingColorAlphaChannel")
@Composable
fun MyNotesApp() {
    var isSearching by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current;

    var isClickTagNoFil by remember { mutableStateOf(false) }
    var isClickTagLtoH by remember { mutableStateOf(false) }
    var isClickTagHtoL by remember { mutableStateOf(false) }
    var isShowTag by remember { mutableStateOf(true) }
    var searchText by remember { mutableStateOf("") }

    var searhedList by remember { mutableStateOf(ArrayList<NoteModel>()) }

    val context = LocalContext.current

    val dbHandler: DBHandler = DBHandler(context)
    val arrListNote = dbHandler.readNotes()
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(android.graphics.Color.parseColor("#F28585")),
                    titleContentColor = Color.White
                ),
                title = {
                    if (isSearching) {
                        SearchTextField(
                            onSearchClosed = {
                                isSearching = false
                            },
                            onSearchTextChanged = { text ->
                                searchText = text

                                val notesSearch =
                                    arrListNote?.let { it1 -> searchNote(it1, searchText, context) }

                                if (notesSearch != null) {
                                    searhedList = notesSearch
                                }
                                Log.d("Data đã search", notesSearch.toString())
                            })
                    } else {
                        Text(
                            text = "Notes",
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isSearching = true
                        keyboardController?.show()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            Modifier.size(40.dp),
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
                    context.startActivity(Intent(context, NoteAc::class.java))
                },
                contentColor = Color.Black,
                modifier = Modifier.padding(16.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 80.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            isShowTag = !isShowTag
                        },
                        modifier = Modifier.size(35.dp),
                    ) {
                        Icon(
                            imageVector = if (isShowTag) Icons.Default.Menu else Icons.Default.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = Color(android.graphics.Color.parseColor("#F28585")),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                if (isShowTag) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BoxTag("No Filter", isClick = isClickTagNoFil) {
                            isClickTagNoFil = !isClickTagNoFil
                            isClickTagHtoL = false
                            isClickTagLtoH = false


                        }
                        BoxTag("Low to high", isClick = isClickTagLtoH) {
                            isClickTagLtoH = !isClickTagLtoH
                            isClickTagNoFil = false
                            isClickTagHtoL = false
                        }
                        BoxTag("High to low", isClick = isClickTagHtoL) {
                            isClickTagHtoL = !isClickTagHtoL
                            isClickTagNoFil = false
                            isClickTagLtoH = false
                        }
                    }
                }
            }



            if (searhedList.size > 0) {
                if (searhedList != null) {
                    if (isClickTagHtoL) {
                        GridNoteView(sortNotesByPriorityReverse(searhedList), context)
                    } else if (isClickTagLtoH) {
                        GridNoteView(sortNotesByPriority(searhedList), context)
                    } else {
                        GridNoteView(searhedList, context)
                    }
                }
            } else {
                if (arrListNote != null) {
                    if (isClickTagHtoL) {
                        GridNoteView(sortNotesByPriorityReverse(arrListNote), context)
                    } else if (isClickTagLtoH) {
                        GridNoteView(sortNotesByPriority(arrListNote), context)
                    } else {
                        GridNoteView(arrListNote, context)
                    }
                }
            }


        }
    }

}

private fun sortNotesByPriority(notes: ArrayList<NoteModel>): ArrayList<NoteModel> {
    return ArrayList(notes.sortedBy { it.priority })
}

private fun sortNotesByPriorityReverse(notes: ArrayList<NoteModel>): ArrayList<NoteModel> {
    return ArrayList(notes.sortedByDescending { it.priority })
}

private fun searchNote(
    notes: ArrayList<NoteModel>,
    key: String,
    context: Context
): ArrayList<NoteModel>? {
    val dbHandler: DBHandler = DBHandler(context)
    return dbHandler.searchNote(key)
}


@Composable
fun GridNoteView(arrListNote: ArrayList<NoteModel>, context: Context) {
    val context = LocalContext.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth()
    ) {
        this.items(arrListNote) { note ->

            Note(note) { clickedNote ->
                val intent = Intent(context, NoteEditAndDelete::class.java)
                val bundle = Bundle()
                bundle.putInt("idNote", clickedNote.id)
                bundle.putString("title", clickedNote.title)
                bundle.putString("desc", clickedNote.description)
                bundle.putString("content", clickedNote.content)
                bundle.putInt("priority", clickedNote.priority)
                bundle.putString("time", clickedNote.timeNote)
                intent.putExtras(bundle)
                context.startActivity(intent)
            }

        }
    }
}


@Composable
fun Note(note: NoteModel, onClick: (NoteModel) -> Unit) {
    var colorPriority by remember { mutableStateOf(Color(android.graphics.Color.parseColor("#66E173"))) }
    colorPriority = if (note.priority == 1) {
        Color(android.graphics.Color.parseColor("#66E173"))
    } else if (note.priority == 2) {
        Color(android.graphics.Color.parseColor("#F2DD22"))
    } else {
        Color(android.graphics.Color.parseColor("#EA77A0"))
    }
    Box(
        modifier = Modifier
            .padding(6.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .shadow(1.dp)
            .height(120.dp)
            .clickable {
                onClick(note)
            },

        ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.Transparent)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "${note.title}",
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.width(130.dp)
                )
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(
                            color = colorPriority,
                            shape = RoundedCornerShape(100.dp)
                        )
                        .offset(10.dp, 10.dp)
                )
            }
            Text(
                text = "${note.description}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(android.graphics.Color.parseColor("#8A8A8A")),
                fontFamily = FontFamily.Monospace,
            )
            Text(
                text = "${note.timeNote}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}


@Composable
fun BoxTag(tag: String, isClick: Boolean, onClickTag: () -> Unit) {
    Box(
        modifier = Modifier
            .border(
                1.dp,
                if (isClick) Color.Black else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                Color(android.graphics.Color.parseColor("#D9D9D9")),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                onClickTag()
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            text = "$tag",
            modifier = Modifier.padding(10.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(onSearchClosed: () -> Unit, onSearchTextChanged: (String) -> Unit) {
    val context = LocalContext.current;
    var searchText by remember { mutableStateOf("") }

    BasicTextField(
        value = searchText,
        onValueChange = {
            searchText = it
            onSearchTextChanged(it)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchClosed()
            }
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        textStyle = TextStyle.Default.copy(
            fontSize = 14.sp,
            color = Color.Black
        )
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Kotlin_JetPackComposeTheme {
        MyNotesApp()
    }
}