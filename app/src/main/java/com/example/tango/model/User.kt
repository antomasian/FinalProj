package com.example.tango.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class User {
    @DocumentId var id: String? = null
    @ServerTimestamp var createdTime: Date? = null
    var fullName: String = ""
    var displayName: String = ""
    var birthday: Date? = null
    var email: String = ""
}