package com.scw.myroompractice

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class WordRoomDatabaseTest {
    private lateinit var db: WordRoomDatabase
    private lateinit var dao: WordDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = WordRoomDatabase.getTestingInstance(context)
        dao = db.wordDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun test() = runTest {
        val text = "Test"
        val word = Word(text)
        val job = launch {
            dao.insert(word)
            dao.findWords(text).collect {
                Assert.assertTrue(it.firstOrNull()?.word == text)
            }
            dao.delete(word)
            dao.findWords(text).collect {
                Assert.assertTrue(it.isEmpty())
            }
        }
        advanceUntilIdle()
        job.cancel()
    }
}