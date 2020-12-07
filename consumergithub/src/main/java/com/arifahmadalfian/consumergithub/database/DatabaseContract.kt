package com.arifahmadalfian.consumergithub.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.arifahmadalfian.githubuser"
    const val SCHEME = "content"

    class FavoriteColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val LOGIN = "login"
            const val AVATAR_URL = "avatar_url"
            const val NAME = "name"
            const val BIO = "bio"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val HTML_URL = "html_url"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val REPOSITORY = "repository"
            const val GISTS = "gists"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()

        }
    }

}