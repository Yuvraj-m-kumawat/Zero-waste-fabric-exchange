package com.example.kutirakone.repository

import android.net.Uri
import com.example.kutirakone.model.Scrap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ScrapRepository {
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }

    fun getScraps(): Flow<List<Scrap>> = callbackFlow {
        val subscription = firestore.collection("scraps")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e("ScrapRepository", "Firestore error: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    try {
                        val scraps = snapshot.toObjects(Scrap::class.java)
                        snapshot.documents.forEachIndexed { index, doc ->
                            if (index < scraps.size) {
                                scraps[index].id = doc.id
                            }
                        }
                        trySend(scraps)
                    } catch (e: Exception) {
                        android.util.Log.e("ScrapRepository", "Error parsing scraps")
                    }
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun uploadScrapWithImage(scrap: Scrap, imageUri: Uri?): Boolean {
        return try {
            // Try to upload image, but don't fail if Storage is disabled
            if (imageUri != null) {
                try {
                    val fileName = "fabric_${System.currentTimeMillis()}.jpg"
                    val ref = storage.reference.child("fabrics/$fileName")
                    ref.putFile(imageUri).await()
                    scrap.imageUrl = ref.downloadUrl.await().toString()
                } catch (e: Exception) {
                    android.util.Log.w("ScrapRepository", "Storage disabled or failed. Saving listing without photo.")
                    scrap.imageUrl = "" // Use empty string to signal default icon
                }
            }
            
            // Upload the text data to Firestore (This works on the FREE plan!)
            firestore.collection("scraps").add(scrap).await()
            true
        } catch (e: Exception) {
            android.util.Log.e("ScrapRepository", "Firestore Upload FAILED: ${e.message}")
            false
        }
    }
}
