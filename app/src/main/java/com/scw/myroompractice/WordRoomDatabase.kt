package com.scw.myroompractice

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Database(entities = [Word::class], version = 1)
abstract class WordRoomDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        private var INSTANCE: WordRoomDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): WordRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_db"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .setQueryCallback(RoomDatabase.QueryCallback { sqlQuery, bindArgs ->
                        Log.d("@@@", "SQL: $sqlQuery, Args: $bindArgs")
                    }, Executors.newSingleThreadExecutor())
//                    .addMigrations()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        @VisibleForTesting
        @Synchronized
        fun getTestingInstance(context: Context): WordRoomDatabase {
            return Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                WordRoomDatabase::class.java
            ).build()
        }

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.wordDao())
                    }
                }
            }

            suspend fun populateDatabase(wordDao: WordDao) {
                // Delete all content here.
                wordDao.deleteAll()

                // Add sample words.
                var word = Word("Hello")
                wordDao.insert(word)
                word = Word("World!")
                wordDao.insert(word)

                // TODO: Add your own words!
                word = Word("TODO!")
                wordDao.insert(word)
            }
        }

    }
}