package com.example.tango.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Chat {
    @DocumentId var id: String? = null
    var chattingUserIDs: List<String> = emptyList()
}

class Message {
    @DocumentId var id: String? = null
    @ServerTimestamp var createdTime: Date? = null
    var senderID: String = ""
    var senderName: String = ""
    var recipientID: String = ""
    var text: String = ""
    var isRead: Boolean = false
}