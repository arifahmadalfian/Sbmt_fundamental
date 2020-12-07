package com.arifahmadalfian.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arifahmadalfian.githubuser.adapter.GithubUsersAdapter
import com.arifahmadalfian.githubuser.adapter.IOnUsersItemsClickListener
import com.arifahmadalfian.githubuser.model.Users
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(), IOnUsersItemsClickListener {

    companion object {
        private val TAG = MainActivity::class.java.simpleName

        private const val authorization = "Authorization"
        private const val token = "token e433538670ddf2177c7503f28d264d800958228b"
        private const val userAgent = "User-Agent"
        private const val request = "request"
    }

    private lateinit var swipeRefresh: WaveSwipeRefreshLayout

    private var list = ArrayList<Users>()
    private var usersAdapter: GithubUsersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //menampilkan list dengan effek shimmer
        getListShimmer()

        //menampilkan list data users
        getListDataUsers()

        swipeRefresh = findViewById(R.id.swipe_refresh)
        swipeRefresh.setOnRefreshListener { task(swipeRefresh, list) }

    }

    private fun getListShimmer() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_github_users)
        recyclerView.setHasFixedSize(true)
        usersAdapter = GithubUsersAdapter(list, this)
        recyclerView.adapter = usersAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ArrayList<GithubUser> di hapus supaya tidak terjadi penumpukan data/ duplikasi data pada saat pencarian
        list.clear()

        //menjalankan shimmer/merubah shimer di adapter
        usersAdapter?.showShimmer = true
    }

    private fun getListDataUsers() {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"
        client.addHeader(authorization, token)
        client.addHeader(userAgent, request)
        client.get(url, object: AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                //jika koneksi berhasil
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val login = jsonObject.getString("login")
                        //mengambil value=url dari key=login
                        getDataUsers(login)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

                //menutup shimmer
                usersAdapter?.showShimmer = false
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                //jika koneksi gagal
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode: Bad Request"
                    403 -> "$statusCode: Forbidden"
                    404 -> "$statusCode: Not Found"
                    else -> "$statusCode: ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getDataUsers(login: String) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${login}"
        client.addHeader(authorization, token)
        client.addHeader(userAgent, request)
        client.get(url, object: AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                //jika koneksi berhasil
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)

                    val id = jsonObject.getInt("id")
                    val profile = jsonObject.getString("avatar_url")
                    val name = jsonObject.getString("name")
                    val loginName = jsonObject.getString("login")
                    val bio = jsonObject.getString("bio")
                    val company = jsonObject.getString("company")
                    val location = jsonObject.getString("location")
                    val htmlUrl = jsonObject.getString("html_url")
                    val repository = jsonObject.getInt("public_repos")
                    val gist = jsonObject.getInt("public_gists")
                    val followings = jsonObject.getInt("following")
                    val followers = jsonObject.getInt("followers")

                    list.add(
                        Users(
                        id = id,
                        avatar_url = profile,
                        name = name,
                        login = loginName,
                        bio = bio,
                        company = company,
                        location = location,
                        html_url = htmlUrl,
                        public_repos = repository,
                        public_gists = gist,
                        following = followings,
                        followers = followers)
                    )
                    usersAdapter?.notifyDataSetChanged()

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                //jika koneksi gagal
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode: Bad Request"
                    403 -> "$statusCode: Forbidden"
                    404 -> "$statusCode: Not Found"
                    else -> "$statusCode: ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getDataSearch(query: String) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=${query}"
        client.addHeader(authorization, token)
        client.addHeader(userAgent, request)
        client.get(url, object: AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                //jika koneksi berhasil
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseObject = JSONObject(result)

                    for (i in 0 until responseObject.getJSONArray("items").length()) {
                        val jsonObject = responseObject.getJSONArray("items").getJSONObject(i)
                        val login = jsonObject.getString("login")
                        getDataUsers(login)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

                //menutup shimmer
                usersAdapter?.showShimmer = false
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                //jika koneksi gagal
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode: Bad Request"
                    403 -> "$statusCode: Forbidden"
                    404 -> "$statusCode: Not Found"
                    else -> "$statusCode: ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                getDataSearch(query)

                //kembali ke list kosong dan menjadalankan loading shimmer
                getListShimmer()

                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {

                true
            }
            R.id.setting -> {
                val settingIntent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(settingIntent)

                //val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                //startActivity(mIntent)
                true
            }
            R.id.favorite -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {

                true
            }
        }
    }

    override fun onUsersItemClickListener(users: Users, position: Int) {
        val intentObject = Intent(this@MainActivity, DetailActivity::class.java)
        intentObject.putExtra(DetailActivity.EXTRA_USERS, users)
        startActivity(intentObject)
    }

    private fun task(swipeRefreshLayout: WaveSwipeRefreshLayout, list: ArrayList<Users>) {
        Task(swipeRefreshLayout, list).execute()
        getListShimmer()
        getListDataUsers()
    }

}


