package com.example.coffeeloby_basicactivity.shop.components.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.coffeeloby_basicactivity.R
import com.example.coffeeloby_basicactivity.core.Api
import com.example.coffeeloby_basicactivity.core.TAG
import kotlinx.android.synthetic.main.fragment_coffee_item_list.*

class CoffeeItemListFragment : Fragment() {
    private lateinit var itemListAdapter: CoffeeItemListAdapter
    private lateinit var itemsModel: CoffeeItemListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_coffee_item_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupItemList()
        fab.setOnClickListener {
            Log.v(TAG, "add new item")
            findNavController().navigate(R.id.CoffeeItemEditFragment)
        }
        fab_logout.setOnClickListener{
            Log.v(TAG, "Logout")
            Api.tokenInterceptor.token = null;
            findNavController().navigate(R.id.action_ItemListFragment_to_Login)
        }
    }

    private fun setupItemList() {
        itemListAdapter = CoffeeItemListAdapter(this)
        item_list.adapter = itemListAdapter
        itemsModel = ViewModelProvider(this).get(CoffeeItemListViewModel::class.java)
        itemsModel.items.observe(viewLifecycleOwner) { items ->
            Log.v(TAG, "update items")
            itemListAdapter.items = items
        }
        itemsModel.loading.observe(viewLifecycleOwner) { loading ->
            Log.i(TAG, "update loading")
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        }
        itemsModel.loadingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                Log.i(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }
        itemsModel.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy")
    }
}