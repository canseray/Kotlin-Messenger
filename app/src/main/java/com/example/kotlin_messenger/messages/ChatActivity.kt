package com.example.kotlin_messenger.messages

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.kotlin_messenger.R
import com.example.kotlin_messenger.models.ChatMessage
import com.example.kotlin_messenger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.row_chat_from.view.*
import kotlinx.android.synthetic.main.row_chat_to.view.*

class ChatActivity : AppCompatActivity() {

    companion object{
        val TAG = "ChatSc"
    }

    val adapter = GroupAdapter<ViewHolder>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerView_chat.adapter = adapter

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user.username


       // setupPuppetData()
        listenForMessages()
    }















    fun send_message_button(view: View){

        val text = editText_chat.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid

        if (fromId ==  null) return

        val referance = FirebaseDatabase.getInstance().getReference("/messages").push()

        val chatMessage = ChatMessage(referance.key!!, text , fromId, toId, System.currentTimeMillis() / 1000)
        referance.setValue(chatMessage)
    }









    private fun listenForMessages(){
        val reference = FirebaseDatabase.getInstance().getReference("/messages")
        reference.addChildEventListener(object: ChildEventListener{

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if (chatMessage != null) {

                    if (chatMessage!!.fromID == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatFromItem(chatMessage.text))
                    } else {
                        adapter.add(ChatFromItem(chatMessage.text))
                    }
                }
            }
                

            override fun onChildRemoved(p0: DataSnapshot) {
            }


        } )
    }















    private fun setupPuppetData(){
        val adapter = GroupAdapter<ViewHolder>()

        adapter.add(ChatToItem("giden uzun mesaj giden uzun mesaj giden uzun"))
        adapter.add(ChatFromItem("gelen mesaj"))

        recyclerView_chat.adapter = adapter
    }
}










class ChatFromItem(val text: String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_from.text = text

    }
    override fun getLayout(): Int {
        return R.layout.row_chat_from
    }
}










class ChatToItem(val text: String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to.text = text
    }

    override fun getLayout(): Int {
        return R.layout.row_chat_to
    }
}
