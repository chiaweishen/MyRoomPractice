package com.scw.myroompractice

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAlphabetizedWords(): Flow<List<Word>>

    @Query("SELECT * FROM word_table WHERE word LIKE :name")
    fun findWords(name: String): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Update
    suspend fun update(word: Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(word: Word)
}