package com.example.coffeeloby_basicactivity.shop.components.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeloby_basicactivity.R
import com.example.coffeeloby_basicactivity.core.TAG
import com.example.coffeeloby_basicactivity.shop.components.edit.CoffeeItemEditFragment
import com.example.coffeeloby_basicactivity.shop.data.CoffeeItem
import kotlinx.android.synthetic.main.view_coffee_item.view.*

class CoffeeItemListAdapter (
    private val fragment: Fragment
) : RecyclerView.Adapter<CoffeeItemListAdapter.ViewHolder>() {

    var items = emptyList<CoffeeItem>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onItemClick: View.OnClickListener;

    init {
        onItemClick = View.OnClickListener { view ->
            val item = view.tag as CoffeeItem
            fragment.findNavController().navigate(R.id.CoffeeItemEditFragment, Bundle().apply {
                putString(CoffeeItemEditFragment.ITEM_ID, item._id)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_coffee_item, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        holder.bind(holder, position)


    }
    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.title
        val mark : TextView = view.mark
        val date : TextView = view.date
        val recommended:TextView = view.recommended

        fun bind(holder : ViewHolder, position:Int){
            val item = items[position]

            with(holder){
                itemView.tag = item
                title.text = item.title
                mark.text = item.mark.toString()
                if(item.date!=null){
                    date.text = item.date
                }
                if(item.recommended){
                    recommended.text = "yes"
                }else{
                    recommended.text = "no"
                }
                itemView.setOnClickListener(onItemClick)
            }
        }
    }
}
