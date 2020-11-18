package com.example.coffeeloby_basicactivity.shop.components.edit

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.example.coffeeloby_basicactivity.R
import com.example.coffeeloby_basicactivity.core.TAG
import com.example.coffeeloby_basicactivity.shop.data.CoffeeItem
import kotlinx.android.synthetic.main.fragment_coffee_item_edit.*
import java.time.LocalDate

class CoffeeItemEditFragment: Fragment() {
    companion object {
        const val ITEM_ID = "ITEM_ID"
    }

    private lateinit var viewModel: CoffeeItemEditViewModel
    private var itemId: String? = null
    private var item: CoffeeItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        arguments?.let {
            if (it.containsKey(ITEM_ID)) {
                itemId = it.getString(ITEM_ID).toString()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_coffee_item_edit, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()
        fab.setOnClickListener {
            Log.v(TAG, "save item")
            var i = item
            Log.v(TAG, i.toString())
            if (i != null) {
                i.title = title.text.toString()
                i.date = LocalDate.now().toString()
                i.mark = mark.text.toString().toInt()
                i.recommended = recommended.isChecked
                i.description = " "
            }
            if (i != null) {
                viewModel.saveOrUpdateItem(
                    i
                )
            }
        }

        delete.setOnClickListener{
            Log.v(TAG,"delete item")
            var i = item
            Log.v(TAG, i.toString())
            if(i!=null){
                viewModel.deleteItem(i)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(CoffeeItemEditViewModel::class.java)
        viewModel.fetching.observe(viewLifecycleOwner) { fetching ->
            Log.v(TAG, "update fetching")
            progress.visibility = if (fetching) View.VISIBLE else View.GONE
        }

        viewModel.fetchingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.completed.observe(viewLifecycleOwner, Observer { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().popBackStack()
            }
        })
        val id = itemId
        if (id == null) {
            item = CoffeeItem("", "", "", LocalDate.now().toString(), false, 0)
        } else {
            viewModel.getItemById(id).observe(viewLifecycleOwner) {
                Log.v(TAG, "update items")
                if (it != null) {
                    item = it
                    title.setText(it.title)
                    mark.setText(it.mark.toString())
                    recommended.isChecked = it.recommended
                }
            }
        }
    }
}