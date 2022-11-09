package com.study.leestagram.navigation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.study.leestagram.BaseFragment
import com.study.leestagram.R
import com.study.leestagram.databinding.FragmentDetailBinding
import com.study.leestagram.navigation.model.ContentDTO

class DetailViewFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.detailfragmentRecyclerView.adapter = DetailViewRecyclerViewAdaptor()
        binding.detailfragmentRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

    }

    inner class DetailViewRecyclerViewAdaptor: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var contentDTOList : ArrayList<ContentDTO> = arrayListOf()
        var contentUidList : ArrayList<String> = arrayListOf()

        init {
            firestore?.collection("contents")
                ?.addSnapshotListener { snapshotQuery, error ->
                    contentDTOList.clear()
                    contentUidList.clear()
                    for(snapshot in snapshotQuery!!.documents){
                        var item = snapshot.toObject(ContentDTO::class.java)
                        contentDTOList.add(item!!)
                        contentUidList.add(snapshot.id)
                    }
                    notifyDataSetChanged()
                }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var itemContentTextview = itemView.findViewById<TextView>(R.id.item_content_textview)
            var itemContent = itemView.findViewById<ImageView>(R.id.item_content)
            var itemUsername = itemView.findViewById<TextView>(R.id.item_username)
            var favoriteCountTextview = itemView.findViewById<TextView>(R.id.favorite_count_textview)

            var favoritBtn = itemView.findViewById<ImageView>(R.id.favorite_button)
        }

        override fun getItemCount(): Int {
            return contentDTOList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder)
            var data = contentDTOList[position] ?: ContentDTO()

            viewHolder.itemContentTextview.text = data.description
            viewHolder.itemUsername.text = data.userId
            viewHolder.favoriteCountTextview.text = data.favoriteCount.toString()

            if( data.favorites.containsKey(uid)){
                viewHolder.favoritBtn.setImageResource(R.drawable.ic_favorite)
            }else{
                viewHolder.favoritBtn.setImageResource(R.drawable.ic_favorite_border)
            }

            //glide
            Glide
                .with(viewHolder.itemView)
                .load(data.imageUrl)
                .placeholder(R.color.black)
                .into(viewHolder.itemContent)

            viewHolder.favoritBtn.setOnClickListener {
                likeEvent(position)
            }
        }

        fun likeEvent(position: Int){
            val docRef = firestore?.collection("contents")?.document(contentUidList[position])

            firestore?.runTransaction{ transaction ->
                val content = transaction.get(docRef!!).toObject(ContentDTO::class.java)

                if(content!!.favorites.containsKey(uid)){
                    content.favoriteCount -= 1
                    content.favorites.remove(uid)
                } else {
                    content.favoriteCount += 1
                    content.favorites.put(uid.toString(), true)
                }
                transaction.set(docRef, content)
            }

        }

    }

}

