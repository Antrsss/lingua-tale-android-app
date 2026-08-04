package com.example.linguatale.domain.dto

data class OccurrenceDto(
    val wordId: String,
    val status: String,
    val partOfSpeech: String,
    val endOffset: Any,
    val startOffset: Any
)