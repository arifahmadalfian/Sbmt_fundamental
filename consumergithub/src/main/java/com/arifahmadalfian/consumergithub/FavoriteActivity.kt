package com.arifahmadalfian.consumergithub

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arifahmadalfian.consumergithub.adapter.GithubFavoriteAdapter
import com.arifahmadalfian.consumergithub.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.arifahmadalfian.consumergithub.database.MappingHelper
import com.arifahmadalfian.consumergithub.model.Users
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity(){

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    private lateinit var favoriteAdapter: GithubFavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        supportActionBar?.title = "Favorite Users"

        initView()
        if (savedInstanceState == null) {
            loadFavoriteAcync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Users>(EXTRA_STATE)
            if (list != null) {
                favoriteAdapter.listFavorite = list
            }
        }

    }

    private fun initView() {
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        favoriteAdapter = GithubFavoriteAdapter(this)
        rv_favorite.adapter = favoriteAdapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object: ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadFavoriteAcync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            when (requestCode) {
                DetailActivity.REQUEST_ADD -> if (resultCode == DetailActivity.RESULT_ADD) {
                    val user = data.getParcelableExtra(DetailActivity.EXTRA_USERS) as Users

                    favoriteAdapter.addItem(user)
                    rv_favorite.smoothScrollToPosition(favoriteAdapter.itemCount - 1)
                }
                DetailActivity.RESULT_DELETE -> {
                    val position = data.getIntExtra(DetailActivity.EXTRA_POSITION,0)
                    favoriteAdapter.removeItem(position)

                }
            }
        }
    }

    private fun loadFavoriteAcync() {
       GlobalScope.launch(Dispatchers.Main) {
           progressbar.visibility = View.VISIBLE
           val defferedFavorites = async(Dispatchers.IO) {
               val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
               MappingHelper.mapCursorToArrayList(cursor)
           }
           progressbar.visibility = View.INVISIBLE
           val favorites = defferedFavorites.await()
           if (favorites.size > 0) {
               favoriteAdapter.listFavorite = favorites
           } else {
               favoriteAdapter.listFavorite = ArrayList()
               showSnackbarMessage(getText(R.string.snack_bar).toString())
           }
       }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, favoriteAdapter.listFavorite)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_favorite, message, Snackbar.LENGTH_SHORT).show()
    }

}