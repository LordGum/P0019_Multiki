package com.example.multiki.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAnim(anim: AnimationDbModel)

    @Query("DELETE FROM anims WHERE createAt = :createAt")
    suspend fun deleteAnim(createAt: Long)

    @Query("SELECT * FROM anims WHERE createAt = :createAt")
    suspend fun getAnimInfo(createAt: Long): AnimationDbModel

    @Query("SELECT * FROM anims ORDER BY createAt")
    fun getAnimList(): Flow<List<AnimationDbModel>>
}