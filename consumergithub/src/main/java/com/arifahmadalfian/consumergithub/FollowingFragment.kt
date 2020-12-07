package com.arifahmadalfian.consumergithub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arifahmadalfian.consumergithub.adapter.GithubUsersAdapter
import com.arifahmadalfian.consumergithub.adapter.IOnUsersItemsClickListener
import com.arifahmadalfian.consumergithub.model.Users
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowingFragment(private val login: String) : Fragment(), IOnUsersItemsClickListener {

    private var list= ArrayList<Users>()
    private var usersAdapter: GithubUsersAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_following, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_github_detail_following_users)
        recyclerView.setHasFixedSize(true)
        usersAdapter = GithubUsersAdapter(list, this)
        recyclerView.adapter = usersAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataUsersFollowing()
    }

    private fun getDataUsersFollowing() {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${login}/following"
        client.addHeader("Authorization","token e433538670ddf2177c7503f28d264d800958228b")
        client.addHeader("User-Agent", "request")
        client.get(url, object: AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                //jika koneksi berhasil
                val result = String(responseBody)
                try {
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)

                        val followingLogin = jsonObject.getString("login")
                        getListFollowing(followingLogin)
                    }
                    //menutup shimmer
                    usersAdapter?.showShimmer = false
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getListFollowing(followingLogin: String) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$followingLogin"
        client.addHeader("Authorization","token e433538670ddf2177c7503f28d264d800958228b")
        client.addHeader("User-Agent", "request")
        client.get(url, object: AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                //jika koneksi berhasil
                val result = String(responseBody)
                try {
                    val jsonObject = JSONObject(result)

                    val profile = jsonObject.getString("avatar_url")
                    val name = jsonObject.getString("name")
                    val htmlUrl = jsonObject.getString("html_url")

                    list.add(
                        Users(
                        avatar_url = profile,
                        name = name,
                        html_url = htmlUrl)
                    )
                    usersAdapter?.notifyDataSetChanged()
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onUsersItemClickListener(users: Users, position: Int) {
        Toast.makeText(context, users.name, Toast.LENGTH_SHORT).show()
    }

}