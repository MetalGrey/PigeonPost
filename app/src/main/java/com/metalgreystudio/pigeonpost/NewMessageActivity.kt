package com.metalgreystudio.pigeonpost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"
        val adapter =  GroupAdapter<GroupieViewHolder>()
        recycler_newmessage.adapter = adapter
        //recycler_newmessage.layoutManager  = LinearLayoutManager(this)

        adapter.add(UserItem())
        adapter.add(UserItem())
        adapter.add(UserItem())


    }
}

class UserItem: Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }
    override fun getLayout(): Int {
    return R.layout.user_row_new_message
    }
}

