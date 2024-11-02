package com.example.multiki.domain

import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun addAnim(animation: Animation)

    suspend fun deleteAnim(createAt: Long)

    fun getAnimList(): Flow<List<Animation>>

    suspend fun getAnimInfo(createAt: Long): Animation
}