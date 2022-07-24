package com.scw.myroompractice

import android.os.Looper
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(
    private val repository: WordRepository
): ViewModel() {

    val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()

    fun insert(word: Word) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("@@@", "isMainThread: ${isMainThread()}")
        repository.insert(word)
    }

    private fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()
}

class WordViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}