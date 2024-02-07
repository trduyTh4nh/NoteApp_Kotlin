package com.example.kotlin_jetpackcompose
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class DBHandler  (context: Context?) :  SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION)  {
    override fun onCreate(db: SQLiteDatabase) {
        // on below line we are creating an sqlite query and we are
        // setting our column names along with their data types.
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_NOTE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_NOTE + " TEXT,"
                + DESC_NOTE + " TEXT,"
                + CONTENT_NOTE + " TEXT,"
                + TIME_NOTE + " TEXT)")

        // at last we are calling a exec sql method to execute above sql query
        db.execSQL(query)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // this method is called to check if the table exists already.
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
    }

    fun addNewNote(
        noteTitle:String?,
        noteDesc:String?,
        noteContent:String?,
        noteTime:String?
    ){

    }
}