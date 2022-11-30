package com.example.tango.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class User(fullName: String = "",
           displayName: String = "",
           birthday: Date? = null,
           email: String = "") {
    @DocumentId var id: String? = null
    @ServerTimestamp var createdTime: Date? = null
    var fullName: String = fullName
    var displayName: String = displayName
    var birthday: Date? = birthday
    var email: String = email
}