/*
package com.example.linguatale.ui.screen.reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguatale.data.remote.api.BookApi
import com.example.linguatale.domain.dto.OccurrenceDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.emptyList

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val bookApi: BookApi
) : ViewModel() {

    private val _content = MutableStateFlow<String?>(null)
    val content: StateFlow<String?> = _content

    private val _occurrences = MutableStateFlow<List<OccurrenceDto>>(emptyList())
    val occurrences: StateFlow<List<OccurrenceDto>> = _occurrences

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadChapter(bookId: String, chapterOrder: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Fire both requests in parallel
                val contentDeferred = async { bookApi.getChapterContent(bookId, chapterOrder) }
                val annotationsDeferred = async { bookApi.getAnnotations(bookId, chapterOrder) }

                _content.value = contentDeferred.await().content
                _occurrences.value = annotationsDeferred.await().occurrences
            } catch (e: Exception) {
                // handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}*/
