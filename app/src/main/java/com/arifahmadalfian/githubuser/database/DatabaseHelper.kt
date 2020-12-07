package com.arifahmadalfian.githubuser.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.arifahmadalfian.githubuser.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbfavorite"

        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.FavoriteColumns._ID} INTEGER PRIMARY KEY, " +
                "${DatabaseContract.FavoriteColumns.LOGIN} TEXT NOT NULL," +
                "${DatabaseContract.FavoriteColumns.AVATAR_URL} TEXT," +
                "${DatabaseContract.FavoriteColumns.NAME} TEXT, " +
                "${DatabaseContract.FavoriteColumns.BIO} TEXT, " +
                "${DatabaseContract.FavoriteColumns.COMPANY} TEXT, " +
                "${DatabaseContract.FavoriteColumns.LOCATION} TEXT, " +
                "${DatabaseContract.FavoriteColumns.HTML_URL} TEXT," +
                "${DatabaseContract.FavoriteColumns.FOLLOWERS} INTEGER," +
                "${DatabaseContract.FavoriteColumns.FOLLOWING} INTEGER, "+
                "${DatabaseContract.FavoriteColumns.REPOSITORY} INTEGER, " +
                "${DatabaseContract.FavoriteColumns.GISTS} INTEGER) "

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}