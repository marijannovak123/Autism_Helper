package com.marijannovak.autismhelper.data.models

data class ContentWrapper (
        val categories: List<Category>,
        val questions: List<Question>,
        val phrases: List<AacPhrase>,
        val phraseCategories: List<PhraseCategory>
)