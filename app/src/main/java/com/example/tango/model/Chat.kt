package com.example.tango.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Chat {
    @DocumentId var id: String? = null
    var chattingUserIDs: List<String> = emptyList()
}

class Message(senderID: String = "",
              senderName: String = "",
              recipientID: String = "",
              text: String = "") {
    @DocumentId var id: String? = null
    @ServerTimestamp var createdTime: Date? = null
    var senderID: String = senderID
    var senderName: String = senderName
    var recipientID: String = recipientID
    var text: String = text
    var isRead: Boolean = false
}