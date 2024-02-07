package com.example.kotlin_jetpackcompose

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlin_jetpackcompose.ui.theme.Kotlin_JetPackComposeTheme


class NoteAc : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kotlin_JetPackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Note()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MissingColorAlphaChannel")
@Composable
fun Note() {
    var isSearching by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current;

    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    val radioOptions = listOf("Low", "Normal", "High")
    var selectedOptions by remember { mutableStateOf(radioOptions[0]) }

    val textStyle = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray
    )
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(android.graphics.Color.parseColor("#F28585")),
                    titleContentColor = androidx.compose.ui.graphics.Color.White
                ),
                title = {
                    Text(
                        text = "Notes",
                        fontWeight = FontWeight.Bold
                    )

                },
                actions = {
                    IconButton(onClick = {
                        isSearching = true
                        keyboardController?.show()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = null,
                            Modifier.size(30.dp),
                            tint = androidx.compose.ui.graphics.Color.White
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
                contentColor = androidx.compose.ui.graphics.Color.Black,
                modifier = Modifier.padding(16.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
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
                BasicTextField(
                    value = title,
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
            Column(Modifier.padding(10.dp)) {
                Text(text = "Description", style = textStyle)
                Spacer(modifier = Modifier.height(8.dp))
                BasicTextField(
                    value = desc,
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

            Row {
                radioOptions.forEach { priority ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (priority == selectedOptions),
                            onClick = { selectedOptions = priority })
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
                BasicTextField(
                    value = content,
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

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Kotlin_JetPackComposeTheme {
        Note()
    }
}