package com.example.tango.services

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storageMetadata
import java.io.File

// Store files in firebase storage
class Storage {
    // Create a storage reference from our app
    private val photoStorage: StorageReference =
        FirebaseStorage.getInstance().reference

    // https://firebase.google.com/docs/storage/android/upload-files#upload_from_a_local_file
    fun uploadImage(localFile: File, uuid: String, uploadSuccess:(Long)->Unit) {
        // XXX Write me
        Log.d(javaClass.simpleName, "call to uploadImage")
        var file = Uri.fromFile(localFile)
        var metadata = storageMetadata {
            contentType = "image/jpg"
        }
        val uploadTask = photoStorage.child(uuid).putFile(file, metadata)

        // Register observers to listen for when the download is done or if it fails
        uploadTask
            .addOnFailureListener {
                // Handle unsuccessful uploads
                if(localFile.delete()) {
                    Log.d(javaClass.simpleName, "Upload FAILED $uuid, file deleted")
                } else {
                    Log.d(javaClass.simpleName, "Upload FAILED $uuid, file delete FAILED")
                }
            }
            .addOnSuccessListener {
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                val sizeBytes = it.metadata?.sizeBytes ?: -1
                uploadSuccess(sizeBytes)
                if(localFile.delete()) {
                    Log.d(javaClass.simpleName, "Upload succeeded $uuid, file deleted")
                } else {
                    Log.d(javaClass.simpleName, "Upload succeeded $uuid, file delete FAILED")
                }
            }
    }
    // https://firebase.google.com/docs/storage/android/delete-files#delete_a_file
    fun deleteImage(pictureUUID: String) {
        // Delete the file
        // XXX Write me
        Log.d(javaClass.simpleName, "call to delete photo $pictureUUID from storage")
        photoStorage.child(pictureUUID).delete()
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "success deleting image from storage")
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "failure deleting image from storage", it)
            }
    }

    fun uuid2StorageReference(uuid: String): StorageReference {
        return photoStorage.child(uuid)
    }
}