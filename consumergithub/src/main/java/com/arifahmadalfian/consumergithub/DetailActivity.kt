package com.arifahmadalfian.consumergithub

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arifahmadalfian.consumergithub.adapter.SectionPagerAdapter
import com.arifahmadalfian.consumergithub.database.DatabaseContract
import com.arifahmadalfian.consumergithub.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.arifahmadalfian.consumergithub.model.Users
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detail_activity_fragment.*
import kotlinx.android.synthetic.main.tab_layout.*


class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USERS = "extra_users"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_DELETE = 301
    }

    private var cursor: Cursor? = null
    private var statusFavorite = false
    private var status = ""
    private var position: Int = 0
    private lateinit var uriWithId: Uri
    private lateinit var users: Users


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity_fragment)

        users = intent.getParcelableExtra(EXTRA_USERS) as Users
        uriWithId = Uri.parse("""$CONTENT_URI/${users.id}""")
        cursor = contentResolver.query(uriWithId, null, null, null, null)?.apply {
            statusFavorite = this.moveToNext()
            setStatusFavorite(statusFavorite)
        }
        position = intent.getIntExtra(EXTRA_POSITION, 0)

        initView()

        btn_favorite.setOnClickListener {
            if (!statusFavorite) {
                addDataToSQLite()
            } else {
                deleteDataInSQLite()
            }
            statusFavorite = !statusFavorite
            setStatusFavorite(statusFavorite)
            Snackbar.make(it, status, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun initView() {
        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        sectionPagerAdapter.login = users.login
        view_pager.adapter = sectionPagerAdapter
        tab_layout.setupWithViewPager(view_pager)

        supportActionBar?.title = "Detail Users"

        Glide.with(this)
            .load(users.avatar_url)
            .apply(RequestOptions().override(110, 110))
            .into(img_detail_profile)
        tv_detail_name.text = users.name
        tv_detail_login.text = users.login
        tv_detail_bio.text = users.bio
        tv_detail_company.text = users.company
        tv_detail_location.text = users.location
        tv_detail_url.text = users.html_url
        tv_detail_repositori.text = users.public_repos.toString()
        tv_detail_gist.text = users.public_gists.toString()
        tv_detail_following.text = users.following.toString()
        tv_detail_follower.text = users.followers.toString()
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        status = if (statusFavorite) {
            btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            getText(R.string.add_favorite).toString()
        } else {
            btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            getText(R.string.delete_favorite).toString()
        }

    }

    private fun addDataToSQLite() {
        val intent = Intent()
        intent.putExtra(EXTRA_USERS, users)
        intent.putExtra(EXTRA_POSITION, position)

        val values = ContentValues()
        values.put(DatabaseContract.FavoriteColumns._ID, users.id)
        values.put(DatabaseContract.FavoriteColumns.LOGIN, users.login)
        values.put(DatabaseContract.FavoriteColumns.NAME, users.name)
        values.put(DatabaseContract.FavoriteColumns.AVATAR_URL, users.avatar_url)
        values.put(DatabaseContract.FavoriteColumns.BIO, users.bio)
        values.put(DatabaseContract.FavoriteColumns.COMPANY, users.company)
        values.put(DatabaseContract.FavoriteColumns.LOCATION, users.location)
        values.put(DatabaseContract.FavoriteColumns.HTML_URL, users.html_url)
        values.put(DatabaseContract.FavoriteColumns.REPOSITORY, users.public_repos)
        values.put(DatabaseContract.FavoriteColumns.GISTS, users.public_gists)
        values.put(DatabaseContract.FavoriteColumns.FOLLOWING, users.following)
        values.put(DatabaseContract.FavoriteColumns.FOLLOWERS, users.followers)

        val result = contentResolver.insert(CONTENT_URI, values)
        if (result != null) {
            setResult(RESULT_ADD, intent)
        } else {
            Toast.makeText(this@DetailActivity, "Gagal Menambah ke favorite", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteDataInSQLite() {
        val result = contentResolver.delete(uriWithId, null, null)
        if (result > 0) {
            val intent = Intent()
            intent.putExtra(EXTRA_POSITION, position)
            setResult(RESULT_DELETE, intent)
        } else {
            Toast.makeText(this@DetailActivity, "Gagal Menghapus dari favorite", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cursor?.close()
    }

}