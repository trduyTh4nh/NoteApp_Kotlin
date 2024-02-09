package com.example.kotlin_jetpackcompose
data class NoteModel(
    var id: Int,
    var title: String,
    var description: String,
    var content: String,
    var timeNote: String,
    var priority: Int
) {
    constructor() : this(0, "", "", "", "", 0)
}