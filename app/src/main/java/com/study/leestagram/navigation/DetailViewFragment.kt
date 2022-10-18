package com.study.leestagram.navigation

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
import com.google.firebase.firestore.FirebaseFirestore
import com.study.leestagram.R
import com.study.leestagram.navigation.model.ContentDTO

class DetailViewFragment : Fragment() {
    var firestore: FirebaseFirestore? = null

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail, container, false)

        firestore = FirebaseFirestore.getInstance()

        val detailfragment_rv = view.findViewById<RecyclerView>(R.id.detailfragment_recycler_view)
        detailfragment_rv.adapter = DetailViewRecyclerViewAdaptor()
        detailfragment_rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        return view
    }

    inner class DetailViewRecyclerViewAdaptor: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var contentDTOList : ArrayList<ContentDTO> = arrayListOf()
        var contentUidList : ArrayList<String> = arrayListOf()

        init {
            firestore?.collection("images")
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
            var item_content_textview = itemView.findViewById<TextView>(R.id.item_content_textview)
            var item_content = itemView.findViewById<ImageView>(R.id.item_content)
            var item_username = itemView.findViewById<TextView>(R.id.item_username)
        }

        override fun getItemCount(): Int {
            return contentDTOList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder)
            viewHolder.item_content_textview.text = contentDTOList!![position].description
            viewHolder.item_content.setImageURI(contentDTOList!![position].imageUrl?.toUri())
            viewHolder.item_username.text = contentDTOList!![position].userId
        }

    }

}

