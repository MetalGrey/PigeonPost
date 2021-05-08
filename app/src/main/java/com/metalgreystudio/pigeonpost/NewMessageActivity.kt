package com.metalgreystudio.pigeonpost

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*


class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = "Select User"



//        val adapter =  GroupAdapter<GroupieViewHolder>()
//        recycler_newmessage.adapter = adapter
//        recycler_newmessage.layoutManager  = LinearLayoutManager(this)


        FetchUsers()
    }

    private fun FetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach() {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }

                }
                recycler_newmessage.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}

class UserItem(val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_new.text = user.username
        val storage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.getReference()
       // val storageRef = storage.reference
        Picasso.get().load(user.profile_image).into(viewHolder.itemView.imageView_new)
        //Picasso.with(context).load(uri.toString()).into(imageView);

    }
    override fun getLayout(): Int {
    return R.layout.user_row_new_message
    }
}

