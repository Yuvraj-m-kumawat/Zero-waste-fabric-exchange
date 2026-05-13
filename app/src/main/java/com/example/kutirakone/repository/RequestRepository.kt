package com.example.kutirakone.repository

import com.example.kutirakone.model.Request
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RequestRepository {
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    fun getRequestsForMe(userId: String): Flow<List<Request>> = callbackFlow {
        val subscription = firestore.collection("requests")
            .whereEqualTo("ownerId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val requests = snapshot.toObjects(Request::class.java)
                    snapshot.documents.forEachIndexed { index, doc ->
                        if (index < requests.size) requests[index].id = doc.id
                    }
                    trySend(requests)
                }
            }
        awaitClose { subscription.remove() }
    }

    fun getRequestsByMe(userId: String): Flow<List<Request>> = callbackFlow {
        val subscription = firestore.collection("requests")
            .whereEqualTo("requesterId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val requests = snapshot.toObjects(Request::class.java)
                    snapshot.documents.forEachIndexed { index, doc ->
                        if (index < requests.size) requests[index].id = doc.id
                    }
                    trySend(requests)
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun sendRequest(request: Request): Boolean {
        return try {
            firestore.collection("requests").add(request).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateRequestStatus(requestId: String, newStatus: String): Boolean {
        return try {
            firestore.collection("requests").document(requestId)
                .update("status", newStatus).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
