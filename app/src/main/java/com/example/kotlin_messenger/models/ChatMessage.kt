package com.example.kotlin_messenger.models

class ChatMessage(val id: String, val text: String, val fromID: String, val toId: String, val times: Long){
    constructor(): this("","","","",-1)
}