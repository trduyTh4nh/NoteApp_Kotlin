@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.example.kotlin_jetpackcompose

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlin_jetpackcompose.ui.theme.Kotlin_JetPackComposeTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.kotlin_jetpackcompose.BoxTag as BoxTag


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    var isSearching by remember { mutableStateOf(true) }
    val keyboardController = LocalSoftwareKeyboardController.current;
    var tag = listOf("No Filter", "Low to high", "High to low")
    var isClickTagNoFil by remember { mutableStateOf(false) }
    var isClickTagLtoH by remember { mutableStateOf(false) }
    var isClickTagHtoL by remember { mutableStateOf(false) }
    var isShowTag by remember { mutableStateOf(true) }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(android.graphics.Color.parseColor("#F28585")),
                    titleContentColor = Color.White
                ),
                title = {
                    if (isSearching) {
                        SearchTextField(onSearchClosed = { isSearching = false })
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 80.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),

                ) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
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
                            modifier = Modifier.size(40.dp)
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
        }
        // GridNoteView()
    }

}

@Composable
fun BoxTag(tag: String, isClick: Boolean, onClickTag: () -> Unit) {
    Box(
        modifier = Modifier
            .border(
                2.dp,
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
            fontSize = 12.sp,
            text = "$tag",
            modifier = Modifier.padding(6.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(onSearchClosed: () -> Unit) {
    val context = LocalContext.current;
    var searchText by remember { mutableStateOf("") }

    BasicTextField(
        value = searchText,
        onValueChange = {
            searchText = it
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

@Composable
fun GridNoteView() {
    val numbers = (0..20).toList()
    LazyVerticalGrid(
        columns = GridCells.Fixed(4)
    ) {
        items(numbers.size) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Number")
                Text(text = "  $it")
            }
        }
    }
}

@Composable
fun Note(number: Number) {

}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    GreetingPreview()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Kotlin_JetPackComposeTheme {
        MyNotesApp()
    }
}