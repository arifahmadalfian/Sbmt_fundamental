package com.arifahmadalfian.githubuser.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arifahmadalfian.githubuser.R
import com.arifahmadalfian.githubuser.model.Users
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.items_github_users.view.*

class GithubUsersAdapter(
    private var users: List<Users>,
    private var clickListener: IOnUsersItemsClickListener
): RecyclerView.Adapter<GithubUsersAdapter.GithubUsersHolder>() {

    var showShimmer = true

    class GithubUsersHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var shimmerFrameLayout: ShimmerFrameLayout = itemView.findViewById(R.id.shimmer)

        @SuppressLint("SetTextI18n")
        fun bind(users: Users, action: IOnUsersItemsClickListener){
            // mencopot background dari shimmerEffek
            itemView.img_profile.background = null
            itemView.tv_nama.background = null
            itemView.tv_id_github.background = null

            with(itemView) {
                Glide.with(itemView.context)
                    .load(users.avatar_url)
                    .apply(RequestOptions().override(55, 55))
                    .into(img_profile)
                tv_nama.text = users.name
                tv_id_github.text = users.html_url

                itemView.setOnClickListener{
                    action.onUsersItemClickListener(users, adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUsersHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.items_github_users, parent, false)
        return GithubUsersHolder(view)
    }

    override fun onBindViewHolder(holder: GithubUsersHolder, position: Int) {
        // menampilkan shimer effek
        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer()
        } else {
            //menghentikan shimmer efek
            holder.shimmerFrameLayout.stopShimmer()
            holder.shimmerFrameLayout.setShimmer(null)

            holder.bind(users[position], clickListener)
        }
    }

    override fun getItemCount(): Int {
        return if (showShimmer) 10 else users.size
    }
}


interface IOnUsersItemsClickListener {
    fun onUsersItemClickListener(users: Users, position: Int)
}


