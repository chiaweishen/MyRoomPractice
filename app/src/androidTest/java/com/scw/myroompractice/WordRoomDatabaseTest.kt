package com.scw.myroompractice

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
        dao.insert(word)
        val queryWord = dao.findWords(text).first().let {
            Assert.assertTrue(it.first().word == text)
            it.first()
        }
        dao.delete(queryWord)
        dao.findWords(text).first().also {
            Assert.assertTrue(it.isEmpty())
        }
    }
}