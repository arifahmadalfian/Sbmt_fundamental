package com.arifahmadalfian.consumergithub

import android.os.AsyncTask
import com.arifahmadalfian.consumergithub.model.Users
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout

class Task(val swipeRefreshLayout: WaveSwipeRefreshLayout, var list: ArrayList<Users>) : AsyncTask<Void?, Void?, ArrayList<Users>>() {

    override fun onPostExecute(result: ArrayList<Users>) {
        swipeRefreshLayout.isRefreshing = false
        super.onPostExecute(result)
    }

    override fun doInBackground(vararg params: Void?): ArrayList<Users>{
        return list
    }

}