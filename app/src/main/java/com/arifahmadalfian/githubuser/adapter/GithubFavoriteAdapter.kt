package com.arifahmadalfian.githubuser.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arifahmadalfian.githubuser.CustomOnItemClickListener
import com.arifahmadalfian.githubuser.DetailActivity
import com.arifahmadalfian.githubuser.R
import com.arifahmadalfian.githubuser.model.Users
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.items_favorite.view.*

class GithubFavoriteAdapter(
    private var activity: Activity
): RecyclerView.Adapter<GithubFavoriteAdapter.FavoriteViewHolder>() {

    var listFavorite = ArrayList<Users>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    inner class FavoriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(usersFavorite: Users) {
            with(itemView) {

                Glide.with(itemView.context)
                    .load(usersFavorite.avatar_url)
                    .apply(RequestOptions().override(90, 90))
                    .into(img_profile_favorite)
                tv_nama_favorite.text = usersFavorite.name
                tv_html_url_favorite.text = usersFavorite.html_url
                txt_followers_favorite.text = context.getString(R.string.follower)
                tv_followers_favorite.text = usersFavorite.followers.toString()
                txt_followings_favorite.text = context.getString(R.string.following)
                tv_followings_favorite.text = usersFavorite.following.toString()

                cl_item_favorite.setOnClickListener(CustomOnItemClickListener(adapterPosition, object: CustomOnItemClickListener.OnItemClickCallback{
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.EXTRA_POSITION, position)
                        intent.putExtra(DetailActivity.EXTRA_USERS, usersFavorite)
                        activity.startActivityForResult(intent, DetailActivity.REQUEST_UPDATE)
                    }

                }))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.items_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int {
        return this.listFavorite.size
    }

    fun addItem(users: Users) {
        this.listFavorite.add(users)
        notifyItemInserted(this.listFavorite.size - 1)
    }

    fun removeItem(position: Int) {
        this.listFavorite.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavorite.size)
    }

}
