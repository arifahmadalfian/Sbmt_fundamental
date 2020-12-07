package com.arifahmadalfian.consumergithub.database

import android.database.Cursor
import com.arifahmadalfian.consumergithub.database.DatabaseContract.FavoriteColumns.Companion.AVATAR_URL
import com.arifahmadalfian.consumergithub.database.DatabaseContract.FavoriteColumns.Companion.BIO
import com.arifahmadalfian.consumergithub.database.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.arifahmadalfian.consumergithub.database.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.arifahmadalfian.consumergithub.database.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.arifahmadalfian.consumergithub.database.DatabaseContract.FavoriteColumns.Companion.GISTS
import com.arifahmadalfian.consumergithub.database.DatabaseContract.FavoriteColumns.Companion.HTML_URL
import com.arifahmadalfian.consumergithub.database.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.arifahmadalfian.consumergithub.database.DatabaseContract.FavoriteColumns.Companion.LOGIN
import com.arifahmadalfian.consumergithub.database.DatabaseContract.FavoriteColumns.Companion.NAME
import com.arifahmadalfian.consumergithub.database.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.arifahmadalfian.consumergithub.database.DatabaseContract.FavoriteColumns.Companion._ID
import com.arifahmadalfian.consumergithub.model.Users

object MappingHelper {

    fun mapCursorToArrayList(favoritesCursor: Cursor?): ArrayList<Users> {
        val favoriteList = ArrayList<Users>()

        favoritesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val login = getString(getColumnIndexOrThrow(LOGIN))
                val avatarUrl = getString(getColumnIndexOrThrow(AVATAR_URL))
                val name = getString(getColumnIndexOrThrow(NAME))
                val htmlUrl = getString(getColumnIndexOrThrow(HTML_URL))
                val bio = getString(getColumnIndexOrThrow(BIO))
                val company = getString(getColumnIndexOrThrow(COMPANY))
                val location = getString(getColumnIndexOrThrow(LOCATION))
                val following = getInt(getColumnIndexOrThrow(FOLLOWING))
                val followers = getInt(getColumnIndexOrThrow(FOLLOWERS))
                val repo = getInt(getColumnIndexOrThrow(REPOSITORY))
                val gist = getInt(getColumnIndexOrThrow(GISTS))

                favoriteList.add(
                    Users(
                        id = id,
                        login = login,
                        avatar_url = avatarUrl,
                        name = name,
                        html_url = htmlUrl,
                        bio = bio,
                        company = company,
                        location = location,
                        following = following,
                        followers = followers,
                        public_repos = repo,
                        public_gists = gist))
            }
        }
        return favoriteList
    }

    fun mapCursorToObject(favoritesCursor: Cursor?): Users {
        var favorite = Users()
        favoritesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(_ID))
            val login = getString(getColumnIndexOrThrow(LOGIN))
            val avatarUrl = getString(getColumnIndexOrThrow(AVATAR_URL))
            val name = getString(getColumnIndexOrThrow(NAME))
            val htmlUrl = getString(getColumnIndexOrThrow(HTML_URL))
            val bio = getString(getColumnIndexOrThrow(BIO))
            val company = getString(getColumnIndexOrThrow(COMPANY))
            val location = getString(getColumnIndexOrThrow(LOCATION))
            val following = getInt(getColumnIndexOrThrow(FOLLOWING))
            val followers = getInt(getColumnIndexOrThrow(FOLLOWERS))
            val repo = getInt(getColumnIndexOrThrow(REPOSITORY))
            val gist = getInt(getColumnIndexOrThrow(GISTS))

            favorite = Users(
                id = id,
                login = login,
                avatar_url = avatarUrl,
                name = name,
                html_url = htmlUrl,
                bio = bio,
                company = company,
                location = location,
                following = following,
                followers = followers,
                public_repos = repo,
                public_gists = gist)
        }
        return favorite
    }
}