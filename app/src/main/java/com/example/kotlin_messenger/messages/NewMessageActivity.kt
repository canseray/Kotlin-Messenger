package com.example.kotlin_messenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.row_user_new_message.view.*


class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)


        supportActionBar?.title = "Select User"

       /* val adapter = GroupAdapter<ViewHolder>()

        adapter.add(UserItem()) //tekrarlandıgı kadar şerit
        adapter.add(UserItem())
        adapter.add(UserItem())


        recyclerView_newMessage.adapter = adapter */

        getUsers()

    }








    private fun getUsers(){
        val referance = FirebaseDatabase.getInstance().getReference("/users")
        referance.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach{
                    Log.d("new message",it.toString())
                    val user = it.getValue(RegisterActivity.User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }
                recyclerView_newMessage.adapter = adapter

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }


}








//textwiew-button layout
class UserItem(val user: RegisterActivity.User): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_textview_newmessage.text = user.username

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_newmessage)
    }


    override fun getLayout(): Int {
        return R.layout.row_user_new_message
    }
}





/*
class CustomAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>{
    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
        */