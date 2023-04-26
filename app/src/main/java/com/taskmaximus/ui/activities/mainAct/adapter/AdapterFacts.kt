package com.taskmaximus.ui.activities.mainAct.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.taskmaximus.R
import com.taskmaximus.data.Model.facts
import com.taskmaximus.data.Model.factsItem

class AdapterFacts : RecyclerView.Adapter<AdapterFacts.factViewHolder>() {

    lateinit var context: Context
    var listFacts = ArrayList<factsItem>()
    inner class factViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.tvFact)

        fun bind(item: factsItem) {
            name.text = item.text
            itemView.setOnClickListener {
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): factViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_fact, parent, false)
        context = parent.context
        return factViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getFacts(mylist: ArrayList<factsItem>) {
        listFacts.clear()
        listFacts.addAll(mylist)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: factViewHolder, position: Int) {
        val pos = listFacts[position]
//      val item = getItem(position)
        holder.bind(pos)
    }

    override fun getItemCount(): Int {
        return listFacts.size
    }

}