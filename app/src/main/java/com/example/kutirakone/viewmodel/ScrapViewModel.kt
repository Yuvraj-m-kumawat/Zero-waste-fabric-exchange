package com.example.kutirakone.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kutirakone.model.Request
import com.example.kutirakone.model.Scrap
import com.example.kutirakone.repository.RequestRepository
import com.example.kutirakone.repository.ScrapRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScrapViewModel : ViewModel() {
    private val repository = ScrapRepository()
    private val requestRepository = RequestRepository()

    // Default User ID (In a real app, this is set after Login)
    // We'll use a static ID for the demo so both phones can see each other's data
    var currentUserId = "User_Demo"
    var currentUserName = "Meha Sharma"

    private val _scraps = MutableStateFlow<List<Scrap>>(getMockScraps())
    val scraps: StateFlow<List<Scrap>> = _scraps.asStateFlow()

    private val _mySentRequests = MutableStateFlow<List<Request>>(emptyList())
    val mySentRequests: StateFlow<List<Request>> = _mySentRequests.asStateFlow()

    private val _receivedRequests = MutableStateFlow<List<Request>>(emptyList())
    val receivedRequests: StateFlow<List<Request>> = _receivedRequests.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadScraps()
        loadRequests()
    }

    fun setUserId(phone: String) {
        currentUserId = phone
        currentUserName = "User_$phone"
        loadRequests() // Refresh requests for the new user
    }

    private fun loadScraps() {
        viewModelScope.launch {
            try {
                repository.getScraps().collect { scraps ->
                    if (scraps.isNotEmpty()) {
                        _scraps.value = scraps
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("ScrapViewModel", "Error loading scraps", e)
            }
        }
    }

    private fun loadRequests() {
        viewModelScope.launch {
            requestRepository.getRequestsByMe(currentUserId).collect { requests ->
                _mySentRequests.value = requests
            }
        }
        viewModelScope.launch {
            requestRepository.getRequestsForMe(currentUserId).collect { requests ->
                _receivedRequests.value = requests
            }
        }
    }

    fun uploadScrap(scrap: Scrap, imageUri: Uri?, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // IMPORTANT: The uploader is the owner
                scrap.userName = currentUserId 
                val success = repository.uploadScrapWithImage(scrap, imageUri)
                onComplete(success)
            } catch (e: Exception) {
                android.util.Log.e("KutiraKone", "Upload Error", e)
                onComplete(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sendRequest(scrap: Scrap, type: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            // OwnerId is the userName of the person who uploaded it
            val ownerId = if (scrap.userName.isEmpty()) "Meha Sharma" else scrap.userName
            
            val request = Request(
                scrapId = scrap.material,
                requesterId = currentUserId, 
                ownerId = ownerId,
                type = type,
                status = "Pending",
                timestamp = System.currentTimeMillis()
            )
            
            val success = requestRepository.sendRequest(request)
            onComplete(success)
        }
    }

    fun updateRequestStatus(requestId: String, newStatus: String) {
        viewModelScope.launch {
            requestRepository.updateRequestStatus(requestId, newStatus)
        }
    }

    companion object {
        fun getMockScraps() = listOf(
            Scrap("1", "Cotton Fabric", "120", "meter", "2.3 km", "Meha Sharma", "", "Pure cotton floral print", "Good", "2", System.currentTimeMillis(), 28.6139, 77.2090),
            Scrap("2", "Silk Pieces", "500", "kg", "3.1 km", "Karan Tailors", "", "Banarasi silk scraps", "New", "1", System.currentTimeMillis(), 28.6200, 77.2100),
            Scrap("3", "Woolen Strip", "150", "meter", "6.5 km", "Anita Boutique", "", "Red wool for knitting", "Excellent", "5", System.currentTimeMillis(), 28.6300, 77.2200),
            Scrap("4", "Denim Scrap", "200", "kg", "1.2 km", "Ravi Tailor", "", "Blue denim pieces", "Used", "3", System.currentTimeMillis(), 28.6000, 77.1900),
            Scrap("5", "Linen Pieces", "350", "meter", "4.8 km", "Suresh Crafts", "", "White linen for embroidery", "Fair", "2.5", System.currentTimeMillis(), 28.6100, 77.2300)
        )
    }
}
