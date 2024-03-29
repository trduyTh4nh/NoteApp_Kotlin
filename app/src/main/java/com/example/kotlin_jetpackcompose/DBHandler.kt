package com.example.kotlin_jetpackcompose

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.ui.Modifier

class DBHandler(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_NOTE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_NOTE + " TEXT,"
                + DESC_NOTE + " TEXT,"
                + CONTENT_NOTE + " TEXT,"
                + TIME_NOTE + " TEXT, "
                + PRIORITY_NOTE + " INTEGER)")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }
    companion object {
        private const val DB_NAME = "noteDb"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "myNotes"
        private const val ID_NOTE = "id"
        private const val TITLE_NOTE = "title"
        private const val DESC_NOTE = "description"
        private const val CONTENT_NOTE = "content"
        private const val TIME_NOTE = "dateTime"
        private const val PRIORITY_NOTE = "priority"
    }

    fun addNewNote(
        noteTitle: String?,
        noteDesc: String?,
        noteContent: String?,
        noteTime: String?,
        notePriority: Int?
    ) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TITLE_NOTE, noteTitle)
        values.put(DESC_NOTE, noteDesc)
        values.put(CONTENT_NOTE, noteContent)
        values.put(TIME_NOTE, noteTime)
        values.put(PRIORITY_NOTE, notePriority)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    fun readNotes(): ArrayList<NoteModel>? {
        val db = this.readableDatabase
        val cursorNotes: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val noteModelArrayList: ArrayList<NoteModel> = ArrayList()

        if (cursorNotes.moveToFirst()) {
            do {
                noteModelArrayList.add(
                    NoteModel(
                        cursorNotes.getInt(0),
                        cursorNotes.getString(1),
                        cursorNotes.getString(2),
                        cursorNotes.getString(3),
                        cursorNotes.getString(4),
                        cursorNotes.getInt(5)
                    )
                )
            } while (cursorNotes.moveToNext())
        }
        return noteModelArrayList
    }

    fun searchNote(key: String): ArrayList<NoteModel> {
        val db = this.writableDatabase
        val cursorNotes: Cursor =
            db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $TITLE_NOTE LIKE '%$key%'", null)

        val searchListNote: ArrayList<NoteModel> = ArrayList()
        if (cursorNotes.moveToFirst()) {
            do {
                searchListNote.add(
                    NoteModel(
                        cursorNotes.getInt(0),
                        cursorNotes.getString(1),
                        cursorNotes.getString(2),
                        cursorNotes.getString(3),
                        cursorNotes.getString(4),
                        cursorNotes.getInt(5)
                    )
                )
            } while (cursorNotes.moveToNext())
        }
        cursorNotes.close() // Close the cursor after using it
        return searchListNote
    }


    fun updateNotes(
        idNote: Int,
        titleNote: String,
        descNote: String,
        contentNote: String,
        priorityNote: Int
    ) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(TITLE_NOTE, titleNote)
            put(DESC_NOTE, descNote)
            put(CONTENT_NOTE, contentNote)
            put(PRIORITY_NOTE, priorityNote)
        }
        val whereClause = "$ID_NOTE = ?"
        val whereArgs = arrayOf(idNote.toString())

        db.update(TABLE_NAME, contentValues, whereClause, whereArgs)

        db.close()
    }

    fun deleteNote(idNote: Int) {
        val db = this.writableDatabase

        val whereClause = "$ID_NOTE = ?"
        val whereArgs = arrayOf(idNote.toString())

        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }


}