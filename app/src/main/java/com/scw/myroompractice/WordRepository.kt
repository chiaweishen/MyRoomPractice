package com.scw.myroompractice

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class WordRepository(private val dao: WordDao) {
    val allWords: Flow<List<Word>> = dao.getAlphabetizedWords()

    @WorkerThread
    suspend fun insert(word: Word) {
        dao.insert(word)
    }
}